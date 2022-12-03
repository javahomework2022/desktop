module com.workonline.desktop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.workonline.desktop to javafx.fxml;
    exports com.workonline.desktop;
}