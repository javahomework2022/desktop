package com.workonline.desktop;

import com.workonline.util.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 编辑页面的主容器
 */
public class EditContainerController implements IController {

    String username;


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
        MessageReceiver.r_commands.put("create_room_success",(commands,message)->{
            int roomid = Integer.getInteger(commands[1]);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editor_tab.fxml"));
            try {
                Tab tab = fxmlLoader.load();
                EditorTabController controller = fxmlLoader.getController();
                controller.label_room_id.setText("房间ID："+roomid);
                controller.label_room_people.setText("");
                controller.is_owner = true;
                tabPane_container.getTabs().add(tab);
                tab_list.put(roomid,tab);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        MessageReceiver.r_commands.put("enter_room_success",(commands, message) -> {
            int roomid = Integer.getInteger(commands[1]);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editor_tab.fxml"));
            try {
                Tab tab = fxmlLoader.load();
                EditorTabController controller = fxmlLoader.getController();
                controller.label_room_id.setText("房间ID："+roomid);
                controller.label_room_people.setText("");
                tabPane_container.getTabs().add(tab);
                tab_list.put(roomid,tab);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        MessageReceiver.r_commands.put("enter_room_fail",(commands, message) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.titleProperty().set("进入房间失败");
            alert.headerTextProperty().set("房间号不存在，请检查房间号");
            alert.showAndWait();
        });
        MessageReceiver.r_commands.put("room_closed",(commands, message) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("房间已关闭");
            alert.headerTextProperty().set("房间已被房主关闭");
            alert.showAndWait();
            int roomid = Integer.getInteger(commands[1]);
            Tab tab = tab_list.get(roomid);
            tabPane_container.getTabs().remove(tab);
            tab_list.remove(tab);
        });
    }


    public void menuItemCreateRoomClick() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"提示",new ButtonType("新建文件"),new ButtonType("打开现有文件"));
        alert.setHeaderText("通过新建文件还是打开现有文件创建房间？");
        var ret = alert.showAndWait();
        String filepath;
        if(ret.get().getText().equals("新建文件")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("新建文件");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showSaveDialog(stage);
            if(file == null) return;
            filepath = file.getPath();
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

    public void menuItemEnterRoomClick() throws IOException {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("提示");
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText("请输入房间ID");
        var res = textInputDialog.showAndWait();
        if(res.isPresent()){
            String strid = res.get();
            try {
                Integer.getInteger(strid);
            }catch(Exception e) {
                return;
            }
            Message message = new Message();
            message.command = "enter "+strid;
            MessageSender.sendMessage(message);
        }

    }

    public void menuItemQuitRoomClick() throws  IOException{
        Tab tab = tabPane_container.getSelectionModel().getSelectedItem();

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
        this.username = ((String) ((Map<?, ?>) root.getScene().getUserData()).get("username"));
        EditContainerController.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editor_tab.fxml"));

        try {
            Tab tab = fxmlLoader.load();

            tabPane_container.getTabs().add(tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
