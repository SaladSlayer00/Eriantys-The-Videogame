package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javax.swing.text.Position;
import java.util.List;
import java.util.Timer;

/* this is the class that controls the scene of the gameboard
* the paws moves on the gameboard, so this class is pretty important for all the various method
* that are in charge of the moves of the paws/ professors/ these things...
 */
public class GameboardSceneController extends ViewObservable implements BasicSceneController {

    private static final String WHAT_II_HAS_BEEN_SELECTED = "wantedPane";

    private Node node;
    private Position position;
    private int whatCanBeClicked;
    private final List<Position> listOfClickedPosition;
    private MessageType clicked;

    private Timer timer;

    //niente raga mo bisogna capire come sistemare le cose qua
    @FXML
}
