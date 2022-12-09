package com.workonline.desktop;

import com.workonline.util.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 登录界面的Controller
 */
public class LoginController implements IController {
    /**
     * login和register页面的VBox
     */
    @FXML
    VBox vbox_login,vbox_register;
    /**
     * root
     */
    @FXML
    HBox hbox_root;

    /**
     * stage
     */
    @FXML
    public static Stage stage;

    /**
     *
     */
    @FXML
    TextField tf_username;

    /**
     *
     */
    @FXML
    PasswordField pf_password1,pf_password2;

    /**
     * 登录按钮点击事件
     * @throws IOException 抛出异常
     */
    @FXML
    private void btnLoginClicked() throws IOException {
        Stage stage1 = getStage(1024,600,"edit_container_view.fxml","协同办公",800,480);
        stage1.show();
        var mainStage = (Stage) hbox_root.getScene().getWindow();
        if(mainStage != null)  mainStage.close();
    }

    /**
     * 去注册按钮点击事件
     */
    @FXML
    private void btnGoRegisterClicked(){
        vbox_login.setVisible(false);
        vbox_register.setVisible(true);
    }

    /**
     * 注册按钮点击事件
     */
    @FXML
    private void btnRegisterClicked() throws IOException {
        String username = tf_username.getText(),
                p1 = pf_password1.getText(),
                p2 = pf_password2.getText();

        if(!p1.equals(p2)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("密码不一致");
            alert.showAndWait();
            return;
        }

        Socket socket = null;
        PrintStream printStream = null;
        BufferedWriter bw = null;
        try {
            socket = new Socket(InetAddress.getByName("43.138.44.240"),10099);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("网络错误");
            alert.showAndWait();
            return;
        }
        new Thread(new MessageReceiver(socket)).start();
        Message message = new Message();
        message.command=String.format("register %s %s",username,p1);
        MessageSender.sendMessage(message,new ObjectOutputStream( socket.getOutputStream()));
        Map<String,Object> map = new HashMap<>();
        map.put("socket",socket);
        Stage stage1 = getStage(1024,600,"edit_container_view.fxml","协同办公",800,480,map);
        stage1.show();
        var mainStage = (Stage) hbox_root.getScene().getWindow();
        if(mainStage != null)  mainStage.close();
    }

    /**
     * 返回登录页面点击事件
     */
    @FXML
    private void btnGoBackClicked(){
        vbox_login.setVisible(true);
        vbox_register.setVisible(false);
    }

    /**
     * 设置stage
     * @param stage stage
     */
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
