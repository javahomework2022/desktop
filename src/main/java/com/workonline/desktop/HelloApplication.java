package com.workonline.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 程序的Application,负责窗口的事件循环
 */
public class HelloApplication extends Application {
    /**
     * launch 后就进入窗口循环了。
     */
    public static void run() {
        launch();
        System.out.println("application exited");
        System.exit(0);
    }

    /**
     * 重写start方法，已经进入Application的事件循环中了
     *
     * @param stage 默认窗口
     * @throws IOException 抛出异常
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 480);
        ((IController) fxmlLoader.getController()).setStage(stage);
        scene.getStylesheets().add(HelloApplication.class.getResource("mainstyle.css")
                .toExternalForm());
        stage.setTitle("登录");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}