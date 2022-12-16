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
import java.util.HashMap;
import java.util.Map;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 登录界面的Controller
 */
public class LoginController implements IController {


    public String username;
    public boolean logged = false;
    /**
     * 构造方法，添加本类对应的一些接收命令
     */
    public LoginController(){
        addCommand();
    }
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
    TextField tf_username,tf_login_username;

    /**
     *
     */
    @FXML
    PasswordField pf_password1,pf_password2,pf_login_password;

    /**
     * 登录按钮点击事件
     * @throws IOException 抛出异常
     */
    @FXML
    private void btnLoginClicked() throws IOException {
        boolean test = false;
        username = tf_login_username.getText();
        String p1 = pf_login_password.getText();
        if(test){
            if(! MessageSender.connect()) return;
            Message message = new Message();
            message.command=String.format("login %s %s",username,p1);
            MessageSender.sendMessage(message);
        }else {
            HashMap<String,Object> map = new HashMap<>();
            map.put("username",username);
            Stage stage1 = getStage(1024, 600, "edit_container_view.fxml", "协同办公", 800, 480,map);
            stage1.show();
            var mainStage = (Stage) hbox_root.getScene().getWindow();
            if (mainStage != null) mainStage.hide();
        }
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
        username = tf_username.getText();
                String p1 = pf_password1.getText(),
                p2 = pf_password2.getText();
        if(!p1.equals(p2)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("密码不一致");
            alert.showAndWait();
            return;
        }
        if(! MessageSender.connect()) return;
        Message message = new Message();
        message.command=String.format("register %s %s",username,p1);
        MessageSender.sendMessage(message);

    }

    private void addCommand() {
        MessageReceiver.r_commands.put("register_success",(commands,message)->{
            Map<String,Object> map = new HashMap<>();
            map.put("username", username);
            Stage stage1;
            try {
                stage1 = getStage(1024,600,"edit_container_view.fxml","协同办公",800,480,map);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            stage1.show();
            Stage mainStage = LoginController.stage;
            if(mainStage != null)  mainStage.hide();
        });
        MessageReceiver.r_commands.put("login_success",(commands,message)->{
            Map<String,Object> map = new HashMap<>();
            map.put("username", username);
            Stage stage1;
            try {
                stage1 = getStage(1024,600,"edit_container_view.fxml","协同办公",800,480,map);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            stage1.show();
            Stage mainStage = LoginController.stage;
            if(mainStage != null)  mainStage.hide();
        });
        MessageReceiver.r_commands.put("register_fail_id_used",(commands,message)->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("注册失败");
            alert.setHeaderText(null);
            alert.setContentText("用户名被使用，请更换用户名");
            alert.showAndWait();
        });
        MessageReceiver.r_commands.put("login_fail",(commands,message)->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("登录失败");
            alert.setHeaderText(null);
            alert.setContentText("用户名不存在，或密码错误，请重试");
            alert.showAndWait();
        });
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
        LoginController.stage = stage;
    }
}
