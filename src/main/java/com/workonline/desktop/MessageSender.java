package com.workonline.desktop;

import com.workonline.util.Message;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 这个类是个静态类，负责和服务器沟通。
 */
public class MessageSender {
    /**
     * 输出流
     */
    public static ObjectOutputStream objectOutputStream;
    /**
     * socket对象
     */
    public static Socket socket;

    /**
     * 是否和服务器连接
     */
    public static boolean connected = false;


    /**
     * @return 是否连接成功
     */
    public static boolean connect() {
        if (!connected) {
            try {
                socket = new Socket(InetAddress.getByName("43.138.44.240"), 10099);
                MessageSender.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                new Thread(new MessageReceiver(socket)).start();
                connected = true;
                System.out.println("connected with server and no need to connect again");
                return true;
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("网络错误");
                alert.showAndWait();
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * @param message 消息
     */
    public static void sendMessage(Message message) {

        if (objectOutputStream != null) {
            try {
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
                System.out.printf("成功发送：%s %s %s%n", message.command, message.document, message.operation);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("网络错误，和服务器断开连接。\n请及时保存文件，然后重新登录。");
                alert.show();
                e.printStackTrace();
                System.out.printf("发送失败：%s %s %s%n", message.command, message.document, message.operation);
                throw new RuntimeException(e);
            }
        } else {
            System.out.printf("测试发送：%s %s %s%n", message.command, message.document, message.operation);
        }
    }
}