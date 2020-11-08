package layouts.nodes;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import questions.Question;

public class Matching {
    public static VBox getNode(Question question) {
        ComboBox options = new ComboBox();

        for (String opt : question.getOptions()) {
            options.getItems().add(opt);
        }

        VBox vbox = new VBox();
        vbox.getChildren().add(options);
        return vbox;

    }
}
