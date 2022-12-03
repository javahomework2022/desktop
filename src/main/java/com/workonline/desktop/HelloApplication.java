package com.workonline.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login_view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 480);
        scene.getStylesheets().add(HelloApplication.class.getResource("mainstyle.css")
                .toExternalForm());
        stage.setTitle("登录");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void run(){
        launch();
    }

}