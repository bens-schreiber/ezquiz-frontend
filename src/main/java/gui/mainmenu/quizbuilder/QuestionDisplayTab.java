package gui.mainmenu.quizbuilder;

import gui.etc.FXHelper;
import gui.mainmenu.quizbuilder.nodes.OptionNode;
import gui.mainmenu.quizbuilder.nodes.OptionNodeBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;
import question.Question;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX tab for the tab wizard
 */
public class QuestionDisplayTab extends Tab implements Initializable {

    @FXML
    VBox optionsAnswerPane;

    @FXML
    TextArea questionText, directionsText;

    @FXML
    ComboBox<Question.Type> typeDropDownMenu;

    private OptionNode optionsNode;

    public QuestionDisplayTab() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXHelper.Window.QUESTION_TAB.getPath()));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Question.Type> dropDownItems = FXCollections.observableArrayList();
        dropDownItems.addAll(Question.Type.MULTIPLECHOICE, Question.Type.CHECKBOX,
                Question.Type.TRUEORFALSE, Question.Type.WRITTEN);

        typeDropDownMenu.setItems(dropDownItems);

        //Default to multiple choice
        optionsNode = new OptionNodeBuilder()
                .type(Question.Type.MULTIPLECHOICE)
                .build();

        optionsAnswerPane.getChildren().setAll(optionsNode.getNode());


        typeDropDownMenu.getSelectionModel().select(Question.Type.MULTIPLECHOICE);

        typeDropDownMenu.setOnAction(event -> {

            optionsNode = new OptionNodeBuilder()
                    .type(typeDropDownMenu.getSelectionModel().getSelectedItem())
                    .build();

            optionsAnswerPane.getChildren().setAll(optionsNode.getNode());

        });

    }


    public boolean isAnswered() {
        return optionsNode.isAnswered() && !questionText.getText().isEmpty();
    }

    JSONObject getJSONQuestion() throws JSONException {

        return new JSONObject()
                .put("question", questionText.getText())
                .put("options", optionsNode.getOptions() != null ? String.join("/", optionsNode.getOptions()) : "")
                .put("answer", String.join("/", optionsNode.getAnswer()))
                .put("directions", directionsText.getText().isEmpty() ? directionsText.getText() : "")
                .put("type", typeDropDownMenu.getSelectionModel().getSelectedItem());
    }
}
