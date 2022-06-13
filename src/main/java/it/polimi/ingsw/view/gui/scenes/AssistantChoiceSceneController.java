package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class AssistantChoiceSceneController extends ViewObservable implements BasicSceneController {
    private List<Assistant> notAvailableAssistants;
    private int chosenAssistantIndex;
    @FXML
    private List<ImageView> assistant;
    @FXML
    private Button confirmButton;
    @FXML
    private Button deselectButton;
    @FXML
    private ImageView chosenAssistant;
    @FXML
    private List<Label> labelList ;


    public AssistantChoiceSceneController(){
        notAvailableAssistants = new ArrayList<>();
        chosenAssistant = new ImageView();

    }
    @FXML
    public void initialize(){
        confirmButton.setDisable(true);
        deselectButton.setDisable(true);
        disabledAssistant();
        for(ImageView image : this.assistant){

        }
    }

    public void disabledAssistant(){
        for(Assistant assistant: notAvailableAssistants){
            this.assistant.get(assistant.getNumOrder()).setOpacity(0.5);
            this.assistant.get(assistant.getNumOrder()).setDisable(true);

        }
    }


    public void setAssistantDeck(List<Assistant> notAvailableAssistants) {
        this.notAvailableAssistants = notAvailableAssistants;
    }
}
/*
<
 */