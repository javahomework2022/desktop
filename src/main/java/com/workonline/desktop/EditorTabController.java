package com.workonline.desktop;


import com.workonline.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.AbstractMap;
import java.util.Map;


public class EditorTabController {

    int version;
    boolean listen = true;
    Status status = Synchronized.getInstance();

    public boolean is_owner = false;
    public int roomid = 0;

    @FXML
    public Tab root;

    @FXML
    public TextArea textArea_editor,textArea_chatArea;

    @FXML
    public Label label_room_id,label_room_people;

    @FXML
    public TextField textField_msg;
    public void onTabClosed(Event e){
        if(is_owner){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("房主别想溜");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.consume();
            return;
        }
        Tab tab = this.root;
        Message message = new Message();
        message.command = "quit_room " + roomid;
        MessageSender.sendMessage(message);
        EditContainerController controller = EditContainerController.getInstance();
        controller. tabPane_container.getTabs().remove(tab);
        controller. tab_list.remove(roomid);
    }

    public void btnSendMsgClick(){
        String msg = textField_msg.getText();
        if(msg.isBlank() || msg.isEmpty()){
            return;
        }
        String nmsg = EditContainerController.username+":"+msg;
        Message message = new Message();
        message.document = nmsg;
        message.command = "send_msg "+roomid;
        MessageSender.sendMessage(message);
        textField_msg.setText("");
    }

    public void textField_msg_keyDown(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            btnSendMsgClick();
        }
    }
    public ChangeListener<? super String> textChanged = new ChangeListener<>() {
        int cnt =0;
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t){
            if(listen) {
                cnt++;
                System.out.println("Text Changed" + cnt);
                System.out.println(s);
                System.out.println(t);
                Operation operation = new Operation();
                int head_retain_len = 0;
                int tail_retain_len = 0;
                int s_length = s.length(), t_length = t.length();
                for (int i = 0; i < s_length && i < t_length; i++) {
                    if (s.charAt(i) == t.charAt(i)) {
                        head_retain_len++;
                    }else{
                        break;
                    }
                }
                operation.retain(head_retain_len);
                for(int i = s_length-1,j = t_length-1; i>=head_retain_len&&j>=head_retain_len; i--,j--){
                    if(s.charAt(i) == t.charAt(j)){
                        tail_retain_len++;
                    }
                    else{
                        break;
                    }
                }
                if(head_retain_len+tail_retain_len<s_length){
                    String delete_string = s.substring(head_retain_len,s_length-tail_retain_len);
                    operation.delete(delete_string);
                }
                if(head_retain_len+tail_retain_len<t_length){
                    String insert_string = t.substring(head_retain_len,t_length-tail_retain_len);
                    operation.insert(insert_string);
                }
                operation.retain(tail_retain_len);
//            System.out.println(operation.getOperations());
                try {
                    clientEdit(operation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void setStatus(Status status){
        this.status = status;
    }
    /*
     * 当用户对文档做出了改变，编辑了操作时调用
     */
    public void clientEdit(Operation operation)throws Exception{
        this.setStatus(this.status.clientEdit(this,operation));
    }
    /*
    当从服务器接收到一个新的操作,但是不是自己等待确认的操作，是别的用户的操作时调用该方法
     */
    public void applyServer(Operation operation) throws Exception{
        this.version++;
        this.setStatus(this.status.applyServer(this,operation));
    }
    /*
    当客户等待确认的操作被服务器确认后调用
     */
    public void serverAcknowledged() throws Exception{
        this.version++;
        this.setStatus(this.status.serverAcknowledged(this));
    }
    /*
    向服务器发送操作
     */
    public void sendOperation(int version,Operation operation) {
        EditContainerController editContainerController = EditContainerController.getInstance();
        Tab tab = editContainerController.tabPane_container.getSelectionModel().getSelectedItem();
        int roomId = ((EditorTabController) ((Map<?, ?>) tab.getUserData()).get("controller")).roomid;
        Text_Operation textOperation = new Text_Operation(version,EditContainerController.username,operation);
        Message message = new Message();
        message.command = String.format("operation %d",roomId);
        message.operation = textOperation;
        System.out.println("发送operation:version "+textOperation.version);
        System.out.println(textOperation.operation.toString());
        MessageSender.sendMessage(message);
    }


    /**
     * 将操作应用到文档中
     */
//    public void applyOperation(Operation operation)
//    {
//        int index = 0;
//        String str = textArea_editor.getText();
//        StringBuilder newStr = new StringBuilder();
////        str[0] = "hello";
//        for(AtomicOperation i:operation.getOperations())
//        {
//            if(i.isInsert())
//            {
//                newStr.append(i.getInsertString());
//            }
//            else if(i.isDelete())
//            {
//                int len = newStr.length();
//                newStr.delete(len-i.getDeleteLength(),len);
//            }
//            else if(i.isRetain())
//            {
//                newStr.append(str, index, i.getRetainLength());
//                index += i.getRetainLength();
//            }
//        }
////        System.out.println(newStr);
////        EditContainerController editContainerController = EditContainerController.getInstance();
////        Tab tab1 = editContainerController.tab_list.get(this.roomid);
//        listen = false;
//        textArea_editor.setEditable(false);
//        int current = textArea_editor.getCaretPosition();
//        textArea_editor.setText(newStr.toString());
//        int now = index(operation,current);
//        textArea_editor.positionCaret(now);
//        textArea_editor.setEditable(true);
//        listen = true;
//    }
    public void applyOperation(Operation operation)
    {
        int index = 0;
        String str = textArea_editor.getText();
        StringBuilder newStr = new StringBuilder();
        for(AtomicOperation i:operation.getOperations())
        {
            if(i.isInsert())
            {
                newStr.append(i.getInsertString());
            }
            else if(i.isDelete())
            {
                index+=i.getDeleteLength();
            }
            else if(i.isRetain())
            {
                newStr.append(str, index, index+i.getRetainLength());
                index += i.getRetainLength();
            }
        }
        listen = false;
        int current = textArea_editor.getCaretPosition();
        textArea_editor.setEditable(false);
        textArea_editor.setText(newStr.toString());
        int now = getIndex(operation,current);
        textArea_editor.setEditable(true);
        textArea_editor.positionCaret(now);
        listen = true;
    }
    public static int getIndex(Operation operation, int index){
        int newIndex = index;
        for(AtomicOperation atomicOperation:operation.getOperations()){
            if(atomicOperation.isRetain()){
                index-=atomicOperation.getRetainLength();
            }
            else if(atomicOperation.isInsert()){
                newIndex+=atomicOperation.getInsertString().length();
            }
            else{
                newIndex-=atomicOperation.getDeleteLength();
                index-=atomicOperation.getDeleteLength();
            }
            if(index<0)
                break;
        }
        return newIndex;
    }
}

abstract class Status{
    public abstract Status clientEdit(EditorTabController editorTabController,Operation operation)throws Exception;
    public abstract Status applyServer(EditorTabController editorTabController,Operation operation) throws Exception;
    public abstract Status serverAcknowledged(EditorTabController editorTabController) throws Exception;

}

/*
在Synchronized状态时，用户没有要发给服务器的操作
 */
class Synchronized extends Status{
    //使用单例模式
    private static Synchronized instance;
    private Synchronized(){}
    public static Synchronized getInstance()
    {
        if (instance == null)
        {
            instance = new Synchronized();
        }
        return instance;
    }
    /*
    当用户做了编辑时，使用该函数给服务器发送操作，并切换到AwaitingConfirm状态
     */
    public AwaitingConfirm clientEdit(EditorTabController editorTabController,Operation operation) {
        editorTabController.sendOperation(editorTabController.version,operation);
        return new AwaitingConfirm(operation);
    }
    /*
    从服务器接受到了一个新的操作，这个操作能够直接应用于当前版本的客户文件
     */
    public Synchronized applyServer(EditorTabController editorTabController,Operation operation){
        editorTabController.applyOperation(operation);
        return this;
    }
    /*
    由于没有需要确认的操作，抛出异常
     */
    public Synchronized serverAcknowledged(EditorTabController editorTabController) throws Exception{
        throw new Exception("There is no pending operation");
    }
}

class AwaitingConfirm extends Status{
    Operation pendingOperation;
    public AwaitingConfirm(Operation operation){
        this.pendingOperation = operation;
    }
    /*
    当用户在此状态下做了编辑时，不立刻发送操作，而是并切换到AwaitingWithBuffer状态
     */
    public AwaitingWithBuffer clientEdit(EditorTabController editorTabController,Operation operation){
        return new AwaitingWithBuffer(this.pendingOperation,operation);
    }
    /*
    在接收到服务器发来的一个操作，并且不是自己待确认的那个，是别的用户的操作时调用
     */
    public AwaitingConfirm applyServer(EditorTabController editorTabController,Operation operation) throws Exception{
        AbstractMap.SimpleEntry<Operation,Operation> entry = OT.transform(this.pendingOperation,operation);
        editorTabController.applyOperation(entry.getValue());
        return new AwaitingConfirm(entry.getKey());
    }
    /*
    在用户的操作被确认后调用，返回Synchronized状态
     */
    public Synchronized serverAcknowledged(EditorTabController editorTabController){
        return Synchronized.getInstance();
    }
}

class AwaitingWithBuffer extends Status{
    Operation pendingOperation;
    Operation bufferOperation;
    public AwaitingWithBuffer(Operation pendingOperation,Operation bufferOperation){
        this.pendingOperation = pendingOperation;
        this.bufferOperation = bufferOperation;
    }
    /*
    当用户在此状态下做了编辑时，将新的编辑内容合并到buffer中
     */
    public AwaitingWithBuffer clientEdit(EditorTabController editorTabController,Operation operation)throws Exception{
        Operation newBuffer = OT.compose(this.bufferOperation,operation);
        return new AwaitingWithBuffer(this.pendingOperation,newBuffer);
    }
    /*
    在接收到服务器发来的一个操作，并且不是自己待确认的那个，是别的用户的操作时调用
     */
    public AwaitingWithBuffer applyServer(EditorTabController editorTabController,Operation operation) throws Exception{
        AbstractMap.SimpleEntry<Operation,Operation> entry1 = OT.transform(this.pendingOperation,operation);
        AbstractMap.SimpleEntry<Operation,Operation> entry2 = OT.transform(this.bufferOperation,entry1.getValue());
        editorTabController.applyOperation(entry2.getValue());
        return new AwaitingWithBuffer(entry1.getKey(),entry2.getKey());
    }
    /*
    在用户等待确认的操作被确认后调用，返回AwaitingConfirm状态，并发送缓冲内容
     */
    public AwaitingConfirm serverAcknowledged(EditorTabController editorTabController){
        editorTabController.sendOperation(editorTabController.version,this.bufferOperation);
        return new AwaitingConfirm(this.bufferOperation);
    }
}