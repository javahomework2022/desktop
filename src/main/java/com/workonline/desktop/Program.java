package com.workonline.desktop;

import javafx.scene.text.Font;


/**
 * 程序的主入口所在类
 */
public class Program {
    /**
     * 程序的主入口
     * @param args 入口参数
     */
    public static void main(String[] args) {
        Font font = Font.loadFont(HelloApplication.class.getResourceAsStream("fonts/Miui-Regular.ttf"),12);
        System.out.println(font);
        HelloApplication.run();
    }
}
