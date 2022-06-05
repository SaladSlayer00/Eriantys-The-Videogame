package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class TowerChoiceSceneController extends ViewObservable implements BasicSceneController {


    private List<Type> availableTowers;

    private int towerIndex;

    private Type chosenTower;

    @FXML
    private Button previousTowerButton;
    @FXML
    private Button nextTowerButton;
    @FXML
    private Button thisCouldDoButton;
    @FXML
    private Button iHaveChangedMyMindButton;
    @FXML
    private Button thisIsTheUltimateChoiceButton;
    @FXML
    private ImageView towerImage;

    public TowerChoiceSceneController(int numberOfPlayers){
        if(numberOfPlayers == 2 || numberOfPlayers == 4){
            availableTowers.add(Type.BLACK);
            availableTowers.add(Type.WHITE);
        }
        else if(numberOfPlayers == 3){
            availableTowers.add(Type.BLACK);
            availableTowers.add(Type.WHITE);
            availableTowers.add(Type.GREY);
        }
        chosenTower = null;
    }

    @FXML
    public void initialization(){
        //setTowerImage(availableTowers.get(0).getText());
        iHaveChangedMyMindButton.setDisable(true);

        //couldItBeDisabled(previousTowerButton, 0);
        //couldItBeDisabled(nextTowerButton, availableTowers.size() - 1);

        //previousTowerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousTowerButtonClicked);
        //nextTowerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextTowerButtonClicked);
        thisCouldDoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onThisCouldDoButtonClicked);
        iHaveChangedMyMindButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onIHaveChangedMyMindButtonClicked);
        thisIsTheUltimateChoiceButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onThisIsTheUltimateChoiceButtonClicked);
        towerImage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTowerImageClicked);
    }

    private void onTowerImageClicked(Event mouseEvent){
        Type tower = availableTowers.get(towerIndex);
    }

    private boolean couldItBeDisabled(Button button, int index){
        if(towerIndex == index){
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    private void setTowerImage(String colorOfTheTower){
        Image image = new Image(getClass().getResourceAsStream("/image/towers/" + colorOfTheTower.toLowerCase() + ".png"));
        towerImage.setImage(image);
    }

    private void setTowerImage(){
        setTowerImage(availableTowers.get(towerIndex).getText());
    }

    //this is for the handling on the clicks on the button for the previous tower (???)
    private void onPreviousTowerButtonClicked(Event mouseEvent){
        if(towerIndex > 0){
            towerIndex--;
            nextTowerButton.setDisable(false);
        }
        couldItBeDisabled(previousTowerButton, 0);
        checkWhichButtonHasBeenSelected();
        Platform.runLater(this::setTowerImage);
    }

    //this is for the handling on the clicks on the button for the next tower (???)
    private void onNextTowerButtonClicked(Event mouseEvent){
        if(towerIndex < availableTowers.size() - 1){
            towerIndex++;
            previousTowerButton.setDisable(false);
        }
        couldItBeDisabled(nextTowerButton, availableTowers.size() - 1);
        checkWhichButtonHasBeenSelected();
        Platform.runLater(this::setTowerImage);
    }

    //handles the clicks on the button that makes the player choose the color of the towers
    private void onThisCouldDoButtonClicked(Event mouseEvent){
        chosenTower = availableTowers.get(towerIndex);
        checkWhichButtonHasBeenSelected();
       // updateTowerChosenListView();
    }

    //handles the clicks on the button that deselects the color of the towers chosen by the player
    private void onIHaveChangedMyMindButtonClicked(Event mouseEvent){
        chosenTower = null;
        checkWhichButtonHasBeenSelected();
        //updateTowerChosenView();
    }

    //handles the clicks on the button that confirm the player's choice
    private void onThisIsTheUltimateChoiceButtonClicked(Event mouseEvent){
        disableAllTheButton();
        new Thread(() -> notifyObserver(observer -> observer.OnUpdateInitTower(chosenTower))).start();
    }

    //this disables ALL the button
    private void disableAllTheButton(){
        previousTowerButton.setDisable(true);
        nextTowerButton.setDisable(true);
        thisCouldDoButton.setDisable(true);
        iHaveChangedMyMindButton.setDisable(true);
        thisIsTheUltimateChoiceButton.setDisable(true);
    }

    private void checkWhichButtonHasBeenSelected(){
        if(thisCouldDoButton.isDisable() && chosenTower == null){
            thisCouldDoButton.setDisable(false);
        }
        if(chosenTower.equals(availableTowers.get(towerIndex))){
            thisCouldDoButton.setDisable(true);
            iHaveChangedMyMindButton.setDisable(false);
        } else{
            thisCouldDoButton.setDisable(chosenTower != null);
            iHaveChangedMyMindButton.setDisable(true);
        }
    }

}
