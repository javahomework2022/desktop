package com.workonline.desktop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * 关于页面的Controller
 */
public class AboutController implements IController{


    /**
     * 运行时信息的label
     */
    @FXML
    Label lb_runtime_info;

    /**
     * 本Controller对应的窗口
     */
    @FXML
    Stage stage;

    /**
     * scene的根页面
     */
    @FXML
    HBox root;

    /**
     * 确定按钮点击事件
     */
    @FXML
    private void btn_ok_click(){
        stage.hide();
    }

    /**
     * 实现IController里的方法
     * @param stage 本Controller里对应的stage
     */
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setOnShown((e)->{
            lb_runtime_info.setText(
                    String.format(" Java version: %s \n Java vendor: %s \n Java vm version: %s \n Java vm vendor: %s",
                            System.getProperty("java.version"),
                            System.getProperty("java.vendor"),
                            System.getProperty("java.vm.version"),
                            System.getProperty("java.vm.vendor")));
        });
    }
}
