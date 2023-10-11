package view;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InputElement{
    private String labelText, defaultText, promptText;
    private Label label = null;
    private TextField textField = null;
    private String id = null;

    public InputElement(String labelText, String defaultText, String promptText) {
        this.labelText = labelText;
        this.defaultText = defaultText;
        this.promptText = promptText;
        label = new Label(labelText);
        textField = new TextField(defaultText);
        textField.setPromptText(promptText);
        textField.setPrefWidth(150);
    }

    public InputElement(String labelText, String defaultText, String promptText, String id) {
        this.labelText = labelText;
        this.defaultText = defaultText;
        this.promptText = promptText;
        label = new Label(labelText);
        textField = new TextField(defaultText);
        textField.setPromptText(promptText);
        textField.setPrefWidth(150);
        this.id = id;
    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }

    public void setVisible(boolean visible) {
        label.setVisible(visible);
        textField.setVisible(visible);
    }

    public String getId() {
        return id;
    }
}