package com.workonline.desktop;


import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class EditorTabController {

    public boolean is_owner = false;

    @FXML
    public Tab root;

    @FXML
    public TextArea textArea_editor;

    @FXML
    public Label label_room_id,label_room_people;
    public ChangeListener<? super String> textChanged = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

            System.out.println("Text Changed");
            System.out.println(s);
            System.out.println(t1);
        }
    };
}