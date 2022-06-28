package it.polimi.ingsw.view.gui.scenes;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * DeckChoiceSceneController class for the handling of the scene that is displayed when the player
 * has to choose the deck for the match
 */
public class DeckChoiceSceneController extends ViewObservable implements BasicSceneController {

    private List<Mage> mageList;

    private Mage thisIsTheChoice;

    private int mageIndex;

    @FXML
    private Button previousMageButton;
    @FXML
    private Button nextMageButton;
    @FXML
    private Button theChosenOneButton;
    @FXML
    private Button thisIsNotTheOneButton;
    @FXML
    private Button okayLetsGoButton;
    @FXML
    private ImageView mageImage;
    @FXML
    private ListView<String> selectedMageListView;
    @FXML
    private ImageView chosenMage;


    /**
     * class constructor
     */
    public DeckChoiceSceneController(){
        mageIndex = 0;
        thisIsTheChoice = null;
        chosenMage = new ImageView();
    }

    /**
     * this method initializes the class setting all the various parameter to display the scene
     * on the player's screen in the proper way
     */
    @FXML
    public void initialize(){
        setMageImage(mageList.get(0).getText());
        thisIsNotTheOneButton.setDisable(true);

        couldItBeDisabled(previousMageButton, 0);
        couldItBeDisabled(nextMageButton, mageList.size() - 1);

        previousMageButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousMageButtonClicked);
        nextMageButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextMageButtonClicked);
        theChosenOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTheChosenOneButtonClicked);
        thisIsNotTheOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onThisIsNotTheOneButtonClicked);
        okayLetsGoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkayLetsGoButtonClicked);
        mageImage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMageImageClicked);
    }

    /**
     * this method handles the clicks on the image of the mage
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onMageImageClicked(Event mouseEvent){
        Mage mage = mageList.get(mageIndex);
    }

    /**
     * this method disables the buttons
     * @param button is the button that it has to be checked if it could be disabled
     * @param index is the index of the mage
     * @return a boolean variable that tells if the button can be disabled
     */
    private boolean couldItBeDisabled(Button button, int index){
        if(mageIndex == index){
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    /**
     * setter for the mage's image in the proper place so to let the player understand that that is the mage
     * that is set as chosen at the moment
     * @param nameOfTheMage is the string indicating the name of the mage that represents the deck chosen
     */
    private void setMageImage(String nameOfTheMage){
        Image image = new Image(getClass().getResourceAsStream("/images/cards/mage/" + nameOfTheMage.toLowerCase() + ".png"));
        mageImage.setImage(image);
    }

    /**
     * setter of the mage
     */
    private void setMageImage(){
        setMageImage(mageList.get(mageIndex).getText());
    }

    /**
     * this method handles the clicks on the button of the previous mage
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onPreviousMageButtonClicked(Event mouseEvent){
        if(mageIndex > 0){
            mageIndex--;
            nextMageButton.setDisable(false);
        }
        couldItBeDisabled(previousMageButton, 0);
        checkWhichButtonHasBeenSelected();
        Platform.runLater(this::setMageImage);
    }

    /**
     * method that handles the clicks on the button of the next mage
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onNextMageButtonClicked(Event mouseEvent){
        if(mageIndex < mageList.size() - 1){
            mageIndex++;
            previousMageButton.setDisable(false);
        }
        couldItBeDisabled(nextMageButton, mageList.size() - 1);
        checkWhichButtonHasBeenSelected();
        Platform.runLater(this::setMageImage);
    }


    /**
     * method that handles the clicks on the chosen mage button
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onTheChosenOneButtonClicked(Event mouseEvent){
        //here we have to add the thing for the choice
        thisIsTheChoice = mageList.get(mageIndex);
        checkWhichButtonHasBeenSelected();
        Image image = new Image(getClass().getResourceAsStream("/images/cards/mage/" + thisIsTheChoice.getText().toLowerCase() + ".png"));
        chosenMage.setImage(image);
        //updateMageChosenListView();

    }

    /**
     * handles the clicks on the button that deselect the mage chosen
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onThisIsNotTheOneButtonClicked(Event mouseEvent){
        //here we have to add the things for the choice
        thisIsTheChoice = null;
        checkWhichButtonHasBeenSelected();
        chosenMage.setImage(null);
        //updateMageChosenListView();
    }

    /**
     * method that handles the clicks on the button that confirm the choice
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onOkayLetsGoButtonClicked(Event mouseEvent) {
        if (thisIsTheChoice == null) {
            SceneController.alertShown("Error", "Please select your deck");
        } else {
            disableAllTheButtons();
            new Thread(() -> notifyObserver(observer -> observer.OnUpdateInitDeck(thisIsTheChoice))).start();
        }
    }

    /**
     * this method disables all the button of the scene
     */
    private void disableAllTheButtons(){
        previousMageButton.setDisable(true);
        nextMageButton.setDisable(true);
        thisIsNotTheOneButton.setDisable(true);
        theChosenOneButton.setDisable(true);
        okayLetsGoButton.setDisable(true);
    }

    /**
     * this method checks and switches the status of the buttons
     */
    private void checkWhichButtonHasBeenSelected(){
        if (!theChosenOneButton.isDisable() || thisIsTheChoice != null) {
            theChosenOneButton.setDisable(false);
        }

        if(thisIsTheChoice!=null && thisIsTheChoice.equals(mageList.get(mageIndex))){
            theChosenOneButton.setDisable(true);
            thisIsNotTheOneButton.setDisable(false);
        } else {
            theChosenOneButton.setDisable(thisIsTheChoice != null);
            thisIsNotTheOneButton.setDisable(true);
        }
    }

    /**
     * setter method that sets the available decks
     * @param availableMages is the list of available mages that indicates the player the decks that they can choose from
     */
    public void setAvailableDecks(List<Mage> availableMages){
        this.mageList = availableMages;
    }

}
