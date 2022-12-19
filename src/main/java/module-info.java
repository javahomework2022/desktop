/**
 * 模块外显
 */
module com.workonline.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.workonline.util;
    requires java.desktop;


    opens com.workonline.desktop to javafx.fxml;
    exports com.workonline.desktop;
}