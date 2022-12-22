package com.workonline.desktop;

import javafx.stage.Stage;


/**
 * Controller的接口，负责在创建页面时将stage添加到Controller里面
 */
public interface IController {
    /**
     * 添加Controller对应的stage
     *
     * @param stage 对应的stage
     */
    void setStage(Stage stage);
}
