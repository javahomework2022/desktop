package com.workonline.desktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * 提供关于Stage的一些静态方法
 */
public class StageUtils {
    /**
     *获取stage
     * @param width stage的宽度
     * @param height 高度
     * @param xmlpath xml文件地址
     * @param title 标题
     * @param minWidth 最小宽度
     * @param minHeight 最小高度
     * @return 生成的stage
     * @throws IOException 抛出异常
     */
    public static Stage getStage (int width,int height,String xmlpath,String title,int minWidth,int minHeight) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(xmlpath));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = new Stage();
        ((IController) fxmlLoader.getController()).setStage(stage);
        scene.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("mainstyle.css")).toExternalForm());

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMinHeight(minHeight);
        stage.setMinWidth(minWidth);
        return  stage;
    }

    /**
     * 生成没有最小高度和宽度的stage
     * @param width 宽度
     * @param height 高度
     * @param xmlpath xml文件地址
     * @param title 标题
     * @return stage
     * @throws IOException 抛出异常
     */
    public static Stage getStage(int width,int height,String xmlpath,String title) throws IOException{
        return getStage(width, height, xmlpath, title,0,0);
    }
}
