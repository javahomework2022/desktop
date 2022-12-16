package com.workonline.desktop;

import com.workonline.util.Message;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 */
public class MessageSender {
 public static ObjectOutputStream objectOutputStream;
 public static void sendMessage(Message message) {

  if(objectOutputStream != null) {
   try {
    objectOutputStream.writeObject(message);
    objectOutputStream.flush();
   } catch (IOException e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("错误");
    alert.setContentText("网络错误，请重启APP");
    alert.show();
    e.printStackTrace();
    throw new RuntimeException(e);
   }
  }else{
   System.out.printf("测试发送：%s\n%s\n%s%n",message.command,message.document,message.operation);
  }
 }
}