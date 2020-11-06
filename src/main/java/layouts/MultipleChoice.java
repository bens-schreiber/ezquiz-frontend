package layouts;

import Questions.Question;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import layouts.BaseLayout;

public class MultipleChoice{

    public static BorderPane getLayout(Question question) {
        BorderPane layout = new BaseLayout(question).getLayout();

        RadioButton radio1 = new RadioButton(question.getOptions().get(0));
        RadioButton radio2 = new RadioButton(question.getOptions().get(1));
        RadioButton radio3 = new RadioButton(question.getOptions().get(2));
        RadioButton radio4 = new RadioButton(question.getOptions().get(3));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(radio1, radio2, radio3, radio4);
        layout.setCenter(vbox);
        return layout;
    }
}
