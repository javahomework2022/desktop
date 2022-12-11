package com.workonline.desktop;


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
}