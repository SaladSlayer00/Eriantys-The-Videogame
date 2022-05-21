package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.observer.ViewObservable;
import javafx.fxml.FXML;

import java.awt.event.MouseEvent;


/* here the first player logged chooses the number of players that are going to play the match
*
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

    //this is to handle the click on the confirm button
    //the parameter of the method is the event of the clicked mouse
    private void onConfirmButtonClick(Event event){
        confirmButton.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) tG.getSelectedToggle();
        int playersNum = Character.getNumericValue(selectedRadioButton.getText().charAt(0));

        new Thread(() -> notifyObserver(observer -> observer.onUpdatePlayersNumber(playersNum))).start();
    }



}
