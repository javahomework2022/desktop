package com.workonline.desktop;


import com.workonline.util.Operation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class EditorTabController {

    public boolean is_owner = false;
    public int roomid = 0;

    @FXML
    public Tab root;

    @FXML
    public TextArea textArea_editor;

    @FXML
    public Label label_room_id,label_room_people;
    public ChangeListener<? super String> textChanged = new ChangeListener<String>() {
        int cnt =0;
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t) {

            cnt++;
            System.out.println("Text Changed"+cnt);
            System.out.println(s);
            System.out.println(t);
            Operation operation = new Operation();
            int s_length = s.length(),t_length = t.length();
            int retain_len=0;
            for(int i=0;i<s_length && i<t_length;i++)
            {
                if(s.charAt(i) == t.charAt(i))
                {
                    //System.out.println(s.charAt(i)+" "+t.charAt(i));
                    retain_len ++;
                }
            }
            String delete_string = s.substring(retain_len-1);
            String insert_string = t.substring(retain_len-1);
            operation.retain(retain_len-1);
            operation.delete(delete_string);
            operation.insert(insert_string);
            System.out.println(operation.getOperations());
        }
    };

}