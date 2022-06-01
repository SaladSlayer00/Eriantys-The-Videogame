package it.polimi.ingsw.view.gui.scenes;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class DeckChoiceSceneController extends ViewObservable implements BasicSceneController {

    private List<Mage> mageList;

    private int howManyDecks;

    private int mageIndex;

    private Mage theChosenOne;

    private List<Mage> magesChosen;

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
    //should there be a thing like an imageview for the selevted mage???

    public DeckChoiceSceneController(){
        mageIndex = 0;
    }

    /*
    //THIS SHOULD BE DELATE AFTER THE TESTING OF THE GAME CONTROLLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @FXML
    public void initialization(){
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

    //handling the clicks on the image
    //we can add an info box maybe????
    //TODO
    private void onMageImageClicked(Event mouseEven){
        Mage mage = mageList.get(mageIndex);
        //dunno if we want to add an info popup or similia
    }

    private boolean couldItBeDisabled(Button button, int index){
        if(mageIndex == index){
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    private void setMageImage(String nameOfTheMage){
        Image image = new Image(getClass().getResourceAsStream("/image/cards/" + nameOfTheMage.toLowerCase() + ".png"));
        mageImage.setImage(image);
    }

    private void setMageImage(){
        setMageImage(mageList.get(mageIndex).getText());
    }

    //handling the clicks on the button of the previous mage
    private void onPreviousMageButtonClicked(Event mouseEvent){
        if(mageIndex > 0){
            mageIndex--;
            nextMageButton.setDisable(false);
        }
        couldItBeDisabled(previousMageButton, 0);
        checkWhichButtonHasBeenSelected();
        Platform.runLater(this::setMageImage);
    }

    //handling the clicks on the button of the next mage
    private void onNextMageButtonClicked(Event mouseEvent){
        if(mageIndex < mageList.size() - 1){
            mageIndex++;
            previousMageButton.setDisable(false);
        }
        couldItBeDisabled(nextMageButton, mageList.size() - 1);
        checkWhitchButtonHasBeenSelected();
        Platform.runLater(this::setMageImage);
    }

    //handles the clicks on the chosen mage button
    private void onTheChosenOneButtonClicked(Event mouseEvent){
        //here we have to add the thing for the choice
        checkWhitchButtonHasBeenSelected();
        updateMageChosenListView();
    }

    //handles the clicks on the mage that it has not been chosen
    private void onThisIsNotTheOneButtonClicked(Event mouseEvent){
        //here we have to add the things for the choice
        checkWhitchButtonHasBeenSelected();
        updateMageChosenListView();
    }

    //handles the clicks on the button that confirm the choise
    public void onOkayLetsGoButtonClicked(Event mouseEvent){

    }
    */
    //THIS IS THE END OF THE COMMENT THAT I PUT TO TEST THE GAME CONTROLLER!!!!!!!
}