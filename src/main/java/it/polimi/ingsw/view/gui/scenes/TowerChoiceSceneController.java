package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
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

        private List<Type> availableColors;

        @FXML
        private ImageView blackTower;
        @FXML
        private ImageView whiteTower;
        @FXML
        private ImageView greyTower;
        @FXML
        private Button backToMenuBtn;

        /**
         * Default constructor.
         */
        public TowerChoiceSceneController() {
            this.availableColors = new ArrayList<>();
        }

        @FXML
        public void initialize() {
            blackTower.setDisable(!availableColors.contains(Type.BLACK));
            whiteTower.setDisable(!availableColors.contains(Type.WHITE));
            greyTower.setDisable(!availableColors.contains(Type.GREY));

            blackTower.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onTowerClick(Type.BLACK));
            whiteTower.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onTowerClick(Type.WHITE));
            greyTower.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onTowerClick(Type.GREY));

            backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
        }

        /**
         * Handle the click on the worker.
         *
         * @param color color picked by user.
         */
        private void onTowerClick(Type color) {
            blackTower.setDisable(true);
            whiteTower.setDisable(true);
            greyTower.setDisable(true);
            new Thread(() -> notifyObserver(obs -> obs.OnUpdateInitTower(color))).start();
        }

        /**
         * Handle the click on the back to menu button.
         */
        private void onBackToMenuBtnClick(Event event) {
            backToMenuBtn.setDisable(true);
            new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
            SceneController.changeRootPane(observers, event, "menu_scene.fxml");
        }

        /**
         * Set the colors which are pickable by user.
         *
         * @param availableColors available colors.
         */
        public void setAvailableColors(List<Type> availableColors) {
            this.availableColors = availableColors;

        }
    }

/*
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

    public TowerChoiceSceneController(){
        towerIndex = 0;
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

    //setter fot the available towers
    public void setAvailableColors(List<Type> availableTowers){
        this.availableTowers = availableTowers;
    }
}
 */
