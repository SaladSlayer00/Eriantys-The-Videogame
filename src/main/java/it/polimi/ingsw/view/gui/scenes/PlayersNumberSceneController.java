package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;


/**
 * PlayersNumberSceneController class that handles the scene that displays the window for the choice of the
 * number of players
 */
public class PlayersNumberSceneController extends ViewObservable implements BasicSceneController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button backToMainButton;

    @FXML
    private RadioButton radioButtonROne;

    @FXML
    private RadioButton radioButtonRTwo;

    @FXML
    private ToggleGroup tG;

    private int minimumPlayers;
    private int maximumPlayers;

    //basic constructor
    public void PlayerNumberSceneController(){
        minimumPlayers = 0;
        maximumPlayers = 0;
    }

    @FXML
    public void initialize(){
        radioButtonROne.setText(minimumPlayers + " players");
        radioButtonRTwo.setText(maximumPlayers + " players");

        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClick);
        backToMainButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMainButtonClick);
    }


    /**
     * this method handles the clicks on the button that confirms the choice
     * @param event is the input given by the player with the mouse
     */
    @FXML
    private void onConfirmButtonClick(Event event){
        confirmButton.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) tG.getSelectedToggle();
        if(selectedRadioButton!=null) {
            int playersNum = Character.getNumericValue(selectedRadioButton.getText().charAt(0));
            new Thread(() -> notifyObserver(observer -> observer.onUpdatePlayersNumber(playersNum))).start();
        }else{
            Platform.runLater(()->SceneController.alertShown("Message:", "Please select the number of players "));
            confirmButton.setDisable(false);
        }
    }

    /**
     * this method handles the clicks on the back to menu button
     * @param event is the input given by the player's mouse
     */

    @FXML
    private void onBackToMainButtonClick(Event event){
        backToMainButton.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    /**
     * initialization of the buttons that specified the number of players available
     * @param minimumPlayers is the minimum number of players available fot the choice
     * @param maximumPlayers is the maximum number of players available fot the choice
     */
    public void setRangeForPlayers(int minimumPlayers, int maximumPlayers){
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
    }

}
