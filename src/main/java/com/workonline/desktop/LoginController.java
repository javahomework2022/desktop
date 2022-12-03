package com.workonline.desktop;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class LoginController {
    @FXML
    VBox vbox_login,vbox_register;
    @FXML
    private void btn_login_clicked(){
    }

    @FXML
    private void btn_go_register_clicked(){
        vbox_login.setVisible(false);
        vbox_register.setVisible(true);
    }

    @FXML
    private void btn_register_clicked(){

    }

    @FXML
    private void btn_go_back_clicked(){
        vbox_login.setVisible(true);
        vbox_register.setVisible(false);
    }
}
