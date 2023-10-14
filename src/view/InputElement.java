package view;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * InputElement-luokka, joka sisältää Label- ja TextField-oliot.
 */
public class InputElement{
    /**
     * Label-olion teksti.
     * TextField-olion oletusteksti.
     * TextField-olion prompt-teksti.
     */
    private String labelText, defaultText, promptText;
    /**
     * Label- olio.
     */
    private Label label = null;
    /**
     * TextField-olio.
     */
    private TextField textField = null;
    /**
     * Id, joka määrittää input elementin asetus-avaimen.
     */
    private String id = null;

    /**
     * @param labelText Label-olion teksti.
     * @param defaultText TextField-olion oletusteksti.
     * @param promptText TextField-olion prompt-teksti.
     * Konstruktori myös luo Label- ja TextField-oliot ja asettaa textField-olion leveyden sekä prompt-tekstin.
     */
    public InputElement(String labelText, String defaultText, String promptText) {
        this.labelText = labelText;
        this.defaultText = defaultText;
        this.promptText = promptText;
        label = new Label(labelText);
        textField = new TextField(defaultText);
        textField.setPromptText(promptText);
        textField.setPrefWidth(150);
    }

    /**
     * @param labelText Label-olion teksti.
     * @param defaultText TextField-olion oletusteksti.
     * @param promptText TextField-olion prompt-teksti.
     * @param id Id, joka määrittää input elementin asetus-avaimen.
     * Konstruktori myös luo Label- ja TextField-oliot ja asettaa textField-olion leveyden sekä prompt-tekstin.
     */
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

    /**
     * @param visible Asettaa Label- ja TextField-oliot näkyviksi tai näkymättömiksi.
     */
    public void setVisible(boolean visible) {
        label.setVisible(visible);
        textField.setVisible(visible);
    }

    public String getId() {
        return id;
    }
}