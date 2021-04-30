package com.benschreiber.gui.fxobjs.qbuilderdisplays;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.List;

/**
 * CheckBox node for building a CheckBox classes.question in the QuizBuilder.
 */
class CheckBoxNode extends OptionNode {

    //Options inputted.
    private final List<TextField> options = new LinkedList<>();

    //List of CheckBoxes by the options, checked checkboxes represent an answer to the classes.question.
    private final List<CheckBox> answers = new LinkedList<>();

    /**
     * Constructor
     */
    CheckBoxNode() {

        //Begin with a vertical placement box with 15 spacing between each node.
        VBox vBox = new VBox();
        vBox.setSpacing(15);

        //Fill vBox with horizontal boxes
        for (int i = 0; i < 4; i++) {

            //Create hBox, set spacing between nodes to 15 and center
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(15);

            //Add a new TextField to options, which is the user input.
            options.add(new TextField());
            options.get(i).setPromptText("Option #" + (i + 1));

            //Add a CheckBox to answers, which determines if the classes.question is the answer.
            answers.add(new CheckBox());

            //Add nodes to hBox
            hBox.getChildren().addAll(answers.get(i), options.get(i));

            //Add hBox to vbox
            vBox.getChildren().add(hBox);
        }

        this.node = vBox;

    }

    @Override
    public List<String> getOptions() {

        //Establish a linked list of options from the user Text Fields text.
        LinkedList<String> list = new LinkedList<>();
        options.forEach(option -> list.add(option.getText()));
        return list;
    }

    @Override
    public List<String> getAnswer() {

        //Check if a box is checked, and find corresponding node, add to answer list.
        LinkedList<String> answer = new LinkedList<>();
        for (CheckBox checkBox : answers) {
            if (checkBox.isSelected()) {
                answer.add(options.get(answers.indexOf(checkBox)).getText());
            }
        }

        return answer;
    }

    @Override
    public boolean isAnswered() {
        return (options.stream().noneMatch(option -> option.getText().isEmpty())
                && (answers.stream().anyMatch(CheckBox::isSelected)));
    }

}
