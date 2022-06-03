package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class TowerChoiceSceneController extends ViewObservable implements BasicSceneController {


    private List<Type> availableTowers;

    private int TowerIndex;

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
        setTowerImage(availableTowers.get(0).getText());
        iHaveChangedMyMindButton.setDisable(true);

        couldItBeDisabled(previousTowerButton, 0);
        couldItBeDisabled(nextTowerButton, availableTowers.size() - 1);

        previousTowerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousTowerButtonClicked);
        nextTowerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextTowerButtonClicked);
    }
}
