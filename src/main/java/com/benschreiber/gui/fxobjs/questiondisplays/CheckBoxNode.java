package com.benschreiber.gui.fxobjs.questiondisplays;

import javafx.scene.control.CheckBox;

import java.util.LinkedList;
import java.util.List;

class CheckBoxNode extends TypeNode {

    CheckBoxNode(List<String> options, String directions, String prompt) {
        super(directions, prompt);

        //Get box amount from options size
        int boxAmount = options.size();

        //Create an array of checkboxes
        CheckBox[] boxes = new CheckBox[boxAmount];

        //Iterate through the array and init each button
        for (int i = 0; i < boxAmount; i++) {

            CheckBox checkBox = new CheckBox(options.get(i));
            checkBox.setStyle(fontSize);

            boxes[i] = checkBox;

            //On mouse clicked, record response to responses
            checkBox.setOnMouseClicked(e -> {

                //Make a new linked list everytime so we don't need to track removing.
                LinkedList<String> list = new LinkedList<>();
                for (CheckBox box : boxes) {
                    if (box.isSelected())
                        list.add(box.getText());
                }
                this.response = list;
            });
        }

        this.node.getChildren().addAll(boxes);
    }

}
