package com.benschreiber.gui.fxobjs.qbuilderdisplays;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.Collections;
import java.util.List;

class Written extends OptionNode {

    //TextField for user to write answer to classes.question in
    private final TextField answer = new TextField();

    /**
     * Constructor
     */
    Written() {

        answer.setPrefWidth(150);

        //Place text field in hbox with label
        HBox hBox = new HBox();
        Label l = new Label("Answer: ");
        hBox.setMinWidth(Region.USE_COMPUTED_SIZE);
        l.setMinWidth(Region.USE_COMPUTED_SIZE);
        hBox.getChildren().addAll(l, answer);

        this.node = hBox;

    }


    @Override
    public List<String> getOptions() {
        return null;
    }

    @Override
    public List<String> getAnswer() {
        return Collections.singletonList(answer.getText());
    }

    @Override
    public boolean isAnswered() {
        return !answer.getText().isEmpty();
    }

}
