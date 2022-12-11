package com.workonline.desktop;

import com.workonline.util.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 */
public class MessageSender {
 public static ObjectOutputStream objectOutputStream;
 public static void sendMessage(Message message) throws IOException {
  if(objectOutputStream != null) {
   objectOutputStream.writeObject(message);
   objectOutputStream.flush();
  }
 }
}