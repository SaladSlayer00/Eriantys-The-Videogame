package it.polimi.ingsw.view.gui.scenes;


import it.polimi.ingsw.observer.ViewObserver;
import javafx.fxml.FXML;


/* here the first player logged chooses the number of players that are going to play the match
*
 */
public class PlayersNumberSceneController extends ViewObserver implements BasicSceneController {

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
    }



}
