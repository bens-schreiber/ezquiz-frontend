package layouts;

import Questions.Question;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;



public class BaseLayout {
    private BorderPane layout;

    public BaseLayout(Question question) {
        layout = new BorderPane();

        Label label = new Label(question.getQuestion());

        Button nextButton = new Button("Next");
        Button backButton = new Button("Back");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(backButton, nextButton);

        layout.setTop(label);
        layout.setBottom(hbox);


    }

    public BorderPane getBaseLayout() {
        return this.layout;
    }

}
