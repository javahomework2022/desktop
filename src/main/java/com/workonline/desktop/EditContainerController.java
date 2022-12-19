package com.workonline.desktop;

import com.workonline.util.Message;
import com.workonline.util.Text_Operation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 编辑页面的主容器
 */
public class EditContainerController implements IController {
    static EditContainerController self;
    public static EditContainerController getInstance(){
        return self;
    }
    public static String username;


    HashMap<Integer,Tab> tab_list = new HashMap<>();

    @FXML
    TabPane tabPane_container;
    @FXML
    AnchorPane root;
    /**
     * 本Controller对应的stage 单例
     */
    @FXML
    public static Stage stage;

    /**
     * 关于页面的stage，是个单例。
     */
    Stage about_stage = getStage(600,400,"about_view.fxml","关于 协同办公",600,400);

    /**
     * 构造函数 这是个单例，所以理论上只可能执行一次，在这里面添加接收到的方法，可以快捷操作这个类内部的变量。
     * @throws IOException 抛出IO异常
     */
    public EditContainerController() throws IOException {
        self = this;
        MessageReceiver.r_commands.put("create_room_success",(commands,message)->{
            int roomid = Integer.parseInt(commands[1]);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editor_tab.fxml"));
            try {
                Tab tab = fxmlLoader.load();
                tab.setText(String.valueOf(roomid));
                EditorTabController controller = fxmlLoader.getController();
                controller.roomid = roomid;
                controller.version = 0;
                controller.label_room_id.setText("房间ID："+roomid);
                controller.label_room_people.setText("");
                controller.is_owner = true;
                controller.textArea_editor.setText(message.document);
                controller.textArea_editor.textProperty().addListener(controller.textChanged);
                var map = new HashMap<String,Object>();
                map.put("controller",controller);
                tab.setUserData(map);
                tabPane_container.getTabs().add(tab);
                tabPane_container.getSelectionModel().select(tab);
                tab_list.put(roomid,tab);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        MessageReceiver.r_commands.put("enter_room_success",(commands, message) -> {
            int roomid = Integer.parseInt(commands[2]);
            int version = Integer.parseInt(commands[1]);
            String doc = message.document;
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editor_tab.fxml"));
            try {
                Tab tab = fxmlLoader.load();
                EditorTabController controller = fxmlLoader.getController();
                tab.setText(String.valueOf(roomid));
                controller.roomid = roomid;
                controller.version = version;
                controller.textArea_editor.setText(doc);
                controller.label_room_id.setText("房间ID："+roomid);
                controller.label_room_people.setText("");
                controller.textArea_editor.textProperty().addListener(controller.textChanged);
                var map = new HashMap<String,Object>();
                map.put("controller",controller);
                tab.setUserData(map);
                tabPane_container.getTabs().add(tab);
                tabPane_container.getSelectionModel().select(tab);
                tab_list.put(roomid,tab);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        MessageReceiver.r_commands.put("enter_room_fail",(commands, message) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("进入房间失败");
            alert.setHeaderText(null);
            alert.setContentText("房间号不存在，请检查房间号");
            alert.showAndWait();
        });
        MessageReceiver.r_commands.put("room_closed",(commands, message) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("房间已关闭");
            alert.setHeaderText(null);
            alert.setContentText("房间已被房主关闭");
            alert.showAndWait();
            int roomid = Integer.parseInt(commands[1]);
            Tab tab = tab_list.get(roomid);
            tabPane_container.getTabs().remove(tab);
            tab_list.remove(roomid);
        });
        MessageReceiver.r_commands.put("broadcast",(commands, message) -> {
            int roomid = Integer.parseInt(commands[1]);
            Text_Operation textOperation = message.operation;
            String username = textOperation.username;
            Tab tab = tab_list.get(roomid);
            EditorTabController controller  = (EditorTabController) ((Map<?,?>) tab.getUserData()).get("controller");
            try {
                if(username.equals(EditContainerController.username)  && roomid == controller.roomid)
                {
                    controller.serverAcknowledged();
                }
                else
                {
                    controller.applyServer(textOperation.operation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        MessageReceiver.r_commands.put("broad_msg",((commands, message) -> {
            AddMsg(message.document, Integer.parseInt(commands[1]));
        }));
        MessageReceiver.r_commands.put("current_user",((commands, message) -> {
            Tab tab = tab_list.get(Integer.parseInt( commands[1]));
            ((EditorTabController) ((Map<?, ?>) tab.getUserData()).get("controller")).label_room_people.setText(message.document);
        }));
    }


    public void menuItemCreateRoomClick() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"",new ButtonType("新建文件"),new ButtonType("打开现有文件"));
        alert.setHeaderText(null);
        alert.setContentText("通过新建文件还是打开现有文件创建房间？");
        var ret = alert.showAndWait();
        String filepath;
        if(ret.isPresent() && ret.get().getText().equals("新建文件")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("新建文件");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showSaveDialog(stage);
            if(file == null) return;
            filepath = file.getPath();
            Files.createFile(Paths.get(filepath));
        }else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择文本文件");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("文本文件","*.txt"),
                    new FileChooser.ExtensionFilter("所有文件","*.*")
            );
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(stage);
            if(file == null) return;
            filepath = file.getPath();
        }
        String doc = Files.readString(Paths.get(filepath));
        Message message = new Message();
        message.command = "create_room";
        message.document = doc;
        MessageSender.sendMessage(message);
    }

    public void menuItemEnterRoomClick() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("提示");
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText("请输入房间ID");
        var res = textInputDialog.showAndWait();
        if(res.isPresent()){
            String strid = res.get();
            try {
                Integer.parseInt(strid);
            }catch(Exception e) {
                return;
            }
            if(tab_list.containsKey(Integer.parseInt(strid))){
                Alert alert = new Alert(Alert.AlertType.ERROR,"");
                alert.setHeaderText(null);
                alert.setContentText("您已经在房间内，请注意");
                alert.setTitle("提示");
                alert.show();
            }
            Message message = new Message();
            message.command = "enter "+strid;
            MessageSender.sendMessage(message);
        }

    }

    public void menuItemQuitRoomClick() {
        Tab tab = tabPane_container.getSelectionModel().getSelectedItem();
        if(tab == null) return;
        EditorTabController controller = (EditorTabController) ((Map<?, ?>) tab.getUserData()).get("controller");
        boolean is_owner = controller.is_owner;
        if(is_owner){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("房主别想溜");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        int roomid = ((EditorTabController) ((Map<?, ?>) tab.getUserData()).get("controller")).roomid;
        Message message = new Message();
        message.command = "quit_room " + roomid;
        MessageSender.sendMessage(message);
        tabPane_container.getTabs().remove(tab);
        tab_list.remove(roomid);

    }

    public void menuItemCloseRoomClick() {
        Tab tab = tabPane_container.getSelectionModel().getSelectedItem();
        if(tab == null) return;
        EditorTabController controller = (EditorTabController) ((Map<?, ?>) tab.getUserData()).get("controller");
        boolean is_owner = controller.is_owner;
        if(!is_owner){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("您不是房主");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        Message message = new Message();
        message.command = "close_room "+controller.roomid;
        MessageSender.sendMessage(message);
    }



    public void menuItemHelpClick(){
    }

    //撤销，复制，剪切，粘贴，查找，替换，字体
    public void menuItemUndoClick(){
        if(tabPane_container.getSelectionModel().getSelectedItem() == null) return;
        TextInputControl textInputControl = ((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).textArea_editor;
        textInputControl.undo();
    }
    public void menuItemCopyClick(){
        if(tabPane_container.getSelectionModel().getSelectedItem() == null) return;
        TextInputControl textInputControl = ((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).textArea_editor;
        textInputControl.copy();
    }
    public void menuItemCutClick(){
        if(tabPane_container.getSelectionModel().getSelectedItem() == null) return;
        TextInputControl textInputControl = ((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).textArea_editor;
        textInputControl.cut();
    }
    public void menuItemPasteClick(){
        if(tabPane_container.getSelectionModel().getSelectedItem() == null) return;;
        TextInputControl textInputControl = ((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).textArea_editor;
        textInputControl.paste();
    }

    public void menuItemSaveFileClick() throws IOException {
        if(tabPane_container.getSelectionModel().getSelectedItem() == null) return;;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("另存为");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).roomid+".txt");
        File file = fileChooser.showSaveDialog(stage);
        if(file == null) return;
        String filepath = file.getPath();
        Path path = Path.of(filepath);
        Files.createFile(path);
        Files.writeString(path,((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).textArea_editor.getText());
    }

    public void menuItemFontClick(){
        if(tabPane_container.getSelectionModel().getSelectedItem() == null) return;
        var dialog = new FontSelectorDialog(((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).textArea_editor.getFont());
        var res = dialog.showAndWait();
        if(res.isPresent()){
            ((EditorTabController) ((Map<?, ?>) tabPane_container.getSelectionModel().getSelectedItem().getUserData()).get("controller")).textArea_editor.setFont(res.get());
        }
    }


    public void AddMsg(String msg,int roomid){
        Tab tab = tab_list.get(roomid);
        TextArea textArea_chatArea = ((EditorTabController) ((Map<?, ?>) tab.getUserData()).get("controller")).textArea_chatArea;
        textArea_chatArea.setText(textArea_chatArea.getText()+"\n"+msg);
        textArea_chatArea.positionCaret(textArea_chatArea.getText().length());
    }
    /**
     * 关于按钮点击事件
     */
    public void menuItemAboutClick(){
        about_stage.show();
    }

    /** 实现接口方法
     * @param stage 本Controller对应的stage
     */
    @Override
    public void setStage(Stage stage) {
        stage.setOnCloseRequest((e)->{

            //检查有没有自己开的房间
            StringBuilder lists= new StringBuilder();
            for (Tab tab : tab_list.values()) {
                var et = ((EditorTabController) ((Map<?, ?>) tab.getUserData()).get("controller"));
                if(et.is_owner){
                    lists.append(et.roomid);
                    lists.append(" ");
                }
            }
            if (!"".equals(lists.toString())) {
                Alert nalert = new Alert(Alert.AlertType.CONFIRMATION, "提示", new ButtonType("确定"));
                nalert.setHeaderText(null);
                nalert.setTitle("提示");
                nalert.setContentText(String.format("您创建的房间[%s]没有关闭，请处理后再退出", lists.toString()));
                nalert.show();
                e.consume();
                return;
            }
            //再次提示确定登出
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"",new ButtonType("退出登录"),new ButtonType("取消"));
            alert.setHeaderText(null);
            alert.setContentText("确定退出登录？将会关闭所有房间。");
            var ret = alert.showAndWait();
            if(ret.isPresent() && ret.get().getText().equals("退出登录")) {
                Message message = new Message();
                message.command = "log_out " + username;
                try {
                    MessageSender.objectOutputStream.writeObject(message);
                    MessageSender.objectOutputStream.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                MessageSender.connected = false;
                LoginController.stage.show();
            }else {
                e.consume();
            }
        });
        username = ((String) ((Map<?, ?>) root.getScene().getUserData()).get("username"));
        EditContainerController.stage = stage;
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editor_tab.fxml"));
//        try {
//            Tab tab = fxmlLoader.load();
//            var controller = (EditorTabController) fxmlLoader.getController();
//            var map = new HashMap<String,Object>();
//            map.put("controller",controller);
//            tab.setUserData(map);
//            controller.textArea_editor.textProperty().addListener(controller.textChanged);
//            tabPane_container.getTabs().add(tab);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
