package com.workonline.desktop;

import com.workonline.util.Message;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * 存储命令的lambda表达式引用
 */
interface MessageHandle {
    void run(String[] commands, Message message);
}

/**
 * 新建线程用来处理从服务器收到的Message
 */
public class MessageReceiver implements Runnable {
    /**
     * 存储命令的哈希表
     */
    public static HashMap<String, MessageHandle> r_commands = new HashMap<>();
    /**
     * 唯一的socket对象
     */
    Socket socket;
    /**
     * 输入流
     */
    InputStream inputStream;

    /**
     * 接收消息类的构造对象
     * @param socket socket连接对象
     * @throws IOException 抛出异常
     */
    public MessageReceiver(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            while (true) {
                Message message = ((Message) objectInputStream.readObject());
                String command = message.command;
                String[] commands = command.split(" ");
                if (r_commands.containsKey(commands[0])) {
                    System.out.println("收到命令:" + command);
                    Platform.runLater(() -> {
                        r_commands.get(commands[0]).run(commands, message);
                    });
                } else {
                    System.out.println("unknown command:" + commands[0]);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}