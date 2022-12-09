package com.workonline.desktop;

import com.workonline.util.Message;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 新建线程用来处理从服务器收到的Message
 */
public class MessageReceiver implements Runnable {
    Socket socket;
    InputStream inputStream;
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
                switch (commands[0]){
                    case "register_success":
                        Platform.runLater(()->{
                            Map<String,Object> map = new HashMap<>();
                            map.put("socket",socket);
                            Stage stage1 = null;
                            try {
                                stage1 = getStage(1024,600,"edit_container_view.fxml","协同办公",800,480,map);
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                            stage1.show();
                            Stage mainStage = LoginController.stage;
                            if(mainStage != null)  mainStage.close();
                        });
                        break;

                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}