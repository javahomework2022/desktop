package com.workonline.desktop;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

import static com.workonline.desktop.StageUtils.getStage;

/**
 * 编辑页面的主容器
 */
public class EditContainerController implements IController {


    /**
     * 本Controller对应的stage
     */
    @FXML
    Stage stage;

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
        this.stage = stage;
    }
}
