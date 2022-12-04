package com.workonline.desktop;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.workonline.util.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    VBox vbox_login,vbox_register;
    @FXML
    HBox hbox_root;
    @FXML
    private void btn_login_clicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("edit_container_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 600);
        scene.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("mainstyle.css")).toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("协同办公");
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(800);
        stage.show();
        var mainstage = (Stage) hbox_root.getScene().getWindow();
        if(mainstage != null)  mainstage.close();
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
