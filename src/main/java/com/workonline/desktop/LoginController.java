package com.workonline.desktop;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import java.io.IOException;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 登录界面的Controll
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
    Stage stage;

    /**
     * 登录按钮点击事件
     * @throws IOException 抛出异常
     */
    @FXML
    private void btnLoginClicked() throws IOException {
        Stage stage = getStage(1024,600,"edit_container_view.fxml","协同办公",800,480);
        stage.show();
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
    private void btnRegisterClicked(){

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
