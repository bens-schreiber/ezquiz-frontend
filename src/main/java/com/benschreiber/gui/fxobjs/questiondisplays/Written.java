package com.benschreiber.gui.fxobjs.questiondisplays;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Collections;

class Written extends TypeNode {

    Written(String directions, String prompt) {
        super(directions, prompt);

        TextField textField = new TextField();
        textField.setMaxSize(115, 10);
        Label label = new Label("Answer:");

        //Set functionality
        textField.setOnKeyTyped(e -> this.response = Collections.singletonList(textField.getText()));
        this.node.getChildren().addAll(label, textField);
    }
}
