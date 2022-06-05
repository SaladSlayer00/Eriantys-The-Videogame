package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class AssistantChoiceSceneController extends ViewObservable implements BasicSceneController {

    private List<Assistant> availableAssistants;

    private Assistant thisIsTheChoice;

    private int assistantIndex;

    @FXML
    private Button previousAssistantButton;
    @FXML
    private Button nextAssistantButton;
    @FXML
    private Button chosenAssistantButton;
    @FXML
    private Button notThisOneButton;
    @FXML
    private Button confirmButton;
    @FXML
    private ImageView assistantImage;

    public AssistantChoiceSceneController(){
        assistantIndex = 0;
        thisIsTheChoice = null;
    }

    @FXML
    public void initialize(){
        setAssistantImage(availableAssistants.get(0).getNumOrder());
        notThisOneButton.setDisable(true);

        couldItBeDisable(previousAssistantButton, 0);
        couldItBeDisable(nextAssistantButton, availableAssistants.size() - 1);

        previousAssistantButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousAssistantButtonClicked);
        nextAssistantButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextAssistantButtonClicked);
        chosenAssistantButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenAssistantButtonClicked);
        notThisOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNotThisOneButtonClicked);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClicked);
        assistantImage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAssistantImageClicked);
    }

    //handles the clicks on the image of the assiatnt
    private void onAssistantImageClicked(Event mouseEvent){
        Assistant assistant = availableAssistants.get(assistantIndex);
        //???
    }

    private boolean couldItBeDisable(Button button, int index){
        if(assistantIndex == index){
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    private void setAssistantImage(int assistantOrder){
        Image image = new Image(getClass().getResourceAsStream("/image/assistantcard/" + assistantOrder + ".png"));
        assistantImage.setImage(image);
    }

    private void setAssistantImage(){
        setAssistantImage(availableAssistants.get(assistantIndex).getNumOrder());
    }

    //handles the clicks on the button for the previous card
    private void onPreviousAssistantButtonClicked(Event mouseEvent){
        if(assistantIndex > 0){
            assistantIndex--;
            nextAssistantButton.setDisable(false);
        }
        couldItBeDisable(previousAssistantButton, 0);
        checkWhichButtonHasBeenSelected();
        Platform.runLater(this::setAssistantImage);
    }

    //handles the clicks on the button for the next card
    private void onNextAssistantButtonClicked(Event mouseEvent){
        if(assistantIndex < availableAssistants.size() - 1){
            assistantIndex++;
            previousAssistantButton.setDisable(false);
        }
        couldItBeDisable(nextAssistantButton, availableAssistants.size() - 1);
        checkWhichButtonHasBeenSelected();
        Platform.runLater(this::setAssistantImage);
    }

    //handles the clicks on the button for select an assistant
    private void onChosenAssistantButtonClicked(Event mouseEvent){
        thisIsTheChoice = availableAssistants.get(assistantIndex);
        checkWhichButtonHasBeenSelected();
    }

    //handles the clicks on the button to deselect the card
    private void onNotThisOneButtonClicked(Event mouseEvent){
        thisIsTheChoice = null;
        checkWhichButtonHasBeenSelected();
    }

    //handles the clicks on the button to confirm the choice
    private void onConfirmButtonClicked(Event mouseEvent){
        disableAllTheButtons();
        new Thread(() -> notifyObserver(observer -> observer.OnUpdateAssistant(thisIsTheChoice))).start();
    }

    //this disables all the button
    private void disableAllTheButtons(){
        previousAssistantButton.setDisable(true);
        nextAssistantButton.setDisable(true);
        chosenAssistantButton.setDisable(true);
        notThisOneButton.setDisable(true);
        confirmButton.setDisable(true);
    }

    //check and switches the status of the buttons
    private void checkWhichButtonHasBeenSelected(){
        if(!chosenAssistantButton.isDisable() || thisIsTheChoice != null){
            chosenAssistantButton.setDisable(false);
        }
        if(thisIsTheChoice.equals(availableAssistants.get(assistantIndex))){
            chosenAssistantButton.setDisable(true);
            notThisOneButton.setDisable(false);
        } else {
            chosenAssistantButton.setDisable(thisIsTheChoice != null);
            notThisOneButton.setDisable(true);
        }
    }

    //setter for the list of available assistant
    public void setAssistantDeck(List<Assistant> availableChoice){
        this.availableAssistants = availableChoice;
    }
}
