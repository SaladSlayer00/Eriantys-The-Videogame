package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.PortUnreachableException;

/**
 * ContinueSceneController class to handle the scene that asks the player if they wanted to keep going with their choice
 * @aouthors Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */
public class ContinueSceneController extends ViewObservable implements BasicSceneController {

    private final Stage actualStage;
    private double offsetX;
    private double offsetY;
    private GameBoardSceneController gBSC;

    @FXML
    private BorderPane rootBPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button buttonForOkay;

    @FXML
    private Button quitButton;

    /**
     * class constructor
     */
    public ContinueSceneController(){
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        titleLabel = new Label("MESSAGE :");
        messageLabel = new Label("Would you like to choose another?\n");
    }

    /**
     * this method initializes the class setting all the
     * various parameter to display the scene on the player's screen in the proper way
     */
    @FXML
    public void initialize(){
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);
        buttonForOkay.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onButtonForOkayClicked);
        quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onQuitButtonClicked);
    }

    /**
     * this method handles the event for the pressing of the mouse
     * @param mouseEvent is the input of the player's mouse
     */
    private void onRootBPaneMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    /**
     * this method handles the event of the dragging of the mouse
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onRootBPaneMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    /**
     * this method handles the clicking on the button that sends the confirmation of the choice to the game controller
     * @param mouseEvent is the input given by the player's mouse
     */
    public void onButtonForOkayClicked(MouseEvent mouseEvent){
        String inputWord = "YES";
;       notifyObserver(obs -> obs.OnStartAnswer(inputWord));
        actualStage.close();
    }

    /**
     * this method handles the clicks on the button that deselect the choices made
     * @param mouseEvent is the input given by te player's mouse
     */
    public void onQuitButtonClicked(MouseEvent mouseEvent){
        String inputWord = "NO";
        notifyObserver(obs -> obs.OnStartAnswer(inputWord));
        Platform.runLater(()->SceneController.changeRootPane(gBSC,"gameboard_scene.fxml"));
        actualStage.close();
    }

    /**
     * this method let the massage for the question be displayed so that the player can read it
     */
    public void displayAlert(){
        actualStage.showAndWait();
    }

    /**
     * this method sets the scene properly
     * @param scene
     */
    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }

    /**
     * this method sets the scene that has to be displayed next
     * @param gameBoardSceneController is the scene controller of the gameboard that handles the actual match
     */
    public void setNextScene(GameBoardSceneController gameBoardSceneController){
        gBSC = gameBoardSceneController;
    }


}
