package com.benschreiber.gui.fxobjs.qbuilderdisplays;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Collections;
import java.util.List;


class TrueOrFalse extends OptionNode {

    //Dropdown menu with only two answers of true or false. Width set according to width of other Nodes text fields
    public ComboBox<String> answers = new ComboBox<>();
    {
        answers.setItems(FXCollections.observableArrayList("true", "false"));

        answers.setPrefWidth(150);

        //Select true by default
        answers.getSelectionModel().selectFirst();
    }

    /**
     * Constructor
     */
    TrueOrFalse() {

        //Create a horizontal placement box and put the comboBox inside.
        HBox hBox = new HBox();
        hBox.getChildren().setAll(new Label("Answer: "), answers);

        this.node = hBox;

    }

    @Override
    public List<String> getOptions() {
        return null;
    }

    @Override
    public List<String> getAnswer() {
        return Collections.singletonList(answers.getSelectionModel().getSelectedItem());
    }

    @Override
    public boolean isAnswered() {
        //ComboBoxes will always be answered
        return true;
    }

}
