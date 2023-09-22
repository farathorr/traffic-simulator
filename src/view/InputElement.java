package view;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InputElement{
    private String labelText, defaultText, promptText;
    private Label label = null;
    private TextField textField = null;

    public InputElement(String labelText, String defaultText, String promptText) {
        this.labelText = labelText;
        this.defaultText = defaultText;
        this.promptText = promptText;
        label = new Label(labelText);
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        textField = new TextField(defaultText);
        textField.setPromptText(promptText);
        textField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        textField.setPrefWidth(150);

    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }
}