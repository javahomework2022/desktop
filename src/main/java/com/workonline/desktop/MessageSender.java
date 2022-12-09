package com.workonline.desktop;

import com.workonline.util.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 */
public class MessageSender {
 public static void sendMessage(Message message, ObjectOutputStream objectOutputStream) throws IOException {
  objectOutputStream.writeObject(message);
 }
}