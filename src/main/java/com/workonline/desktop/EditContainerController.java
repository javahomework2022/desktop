package com.workonline.desktop;

import com.workonline.util.Message;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 编辑页面的主容器
 */
public class EditContainerController implements IController {

    /**
     *
     */
    ObjectOutputStream objectOutputStream;

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
     * 构造函数
     * @throws IOException 抛出IO异常
     */
    public EditContainerController() throws IOException {
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
        Socket socket = ((Socket) ((Map) root.getUserData()).get("socket"));
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.stage = stage;
    }
}
