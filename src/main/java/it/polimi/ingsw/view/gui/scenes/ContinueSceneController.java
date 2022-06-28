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

    //this is the constructor
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

    @FXML
    public void initialize(){
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);
        buttonForOkay.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onButtonForOkayClicked);
        quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onQuitButtonClicked);
    }

    //this method handles the event for the pressing of the mouse
    private void onRootBPaneMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    //this method handles the event of the dragging of the mouse
    private void onRootBPaneMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    //this method handles the clicking on the okay button
    public void onButtonForOkayClicked(MouseEvent mouseEvent){
        String inputWord = "YES";
;       notifyObserver(obs -> obs.OnStartAnswer(inputWord));
        actualStage.close();
    }

    public void onQuitButtonClicked(MouseEvent mouseEvent){
        String inputWord = "NO";
        notifyObserver(obs -> obs.OnStartAnswer(inputWord));
        Platform.runLater(()->SceneController.changeRootPane(gBSC,"gameboard_scene.fxml"));
        actualStage.close();
    }

    public void displayAlert(){
        actualStage.showAndWait();
    }

    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }

    public void setNextScene(GameBoardSceneController gameBoardSceneController){
        gBSC = gameBoardSceneController;
    }


}
