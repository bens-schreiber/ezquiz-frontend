package com.benschreiber.gui.mainmenu.quizbuilder.nodes;

import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * MultipleChoice options node for displaying a Multiple Choice classes.question in the Quiz Builder.
 */
class MultipleChoice extends OptionNode {

    //List of options given by the user in TextFields
    private final List<TextField> options = new LinkedList<>();

    //List of RadioButtons that signify if an hBox contains the answer.
    private final List<RadioButton> answers = new LinkedList<>();


    /**
     * Constructor
     */
    MultipleChoice() {

        //Create a vertical box with spacing between nodes of 15
        VBox vBox = new VBox();
        vBox.setSpacing(15);

        //Create a toggle group that makes all RadioButtons within singly toggleable
        ToggleGroup toggleGroup = new ToggleGroup();

        //Fill vBox with options
        for (int i = 0; i < 4; i++) {

            //horizontal placement box with spacing between nodes of 15 and left-center alignment
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(15);

            //Add a new TextField for the user to input their option into
            options.add(new TextField());
            options.get(i).setPromptText("Option #" + (i + 1));

            //Create a RadioButton and add it to the toggleGroup
            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(toggleGroup);
            answers.add(radioButton);

            //Add nodes to hBox
            hBox.getChildren().addAll(answers.get(i), options.get(i));

            vBox.getChildren().add(hBox);
        }

        this.node = vBox;
    }

    @Override
    public List<String> getAnswer() {
        String answer = "";
        for (RadioButton radioButton : answers) {
            if (radioButton.isSelected()) {
                answer = options.get(answers.indexOf(radioButton)).getText();
                break;
            }
        }
        return Collections.singletonList(answer);
    }

    @Override
    public List<String> getOptions() {
        LinkedList<String> list = new LinkedList<>();
        options.forEach(option -> list.add(option.getText()));
        return list;
    }

    @Override
    public boolean isAnswered() {
        return (options.stream().noneMatch(option -> option.getText().isEmpty())
                && (answers.stream().anyMatch(RadioButton::isSelected)));
    }
}
