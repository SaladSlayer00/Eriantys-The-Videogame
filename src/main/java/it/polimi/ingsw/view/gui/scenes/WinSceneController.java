package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WinSceneController implements BasicSceneController{

    private Stage actualStage;

    private double offsetX;
    private double offsetY;

    @FXML
    private BorderPane borderRPane;

    @FXML
    private Label labelForTheTitle;

    @FXML
    private Label labelForTheNick;

    @FXML
    private Button confirmButton;

    //the constructor for the scene (the basic one)
    public WinSceneController(){

        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
    }

    @FXML
    public void initialization(){
        borderRPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onBorderRPanePressed);
        borderRPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onBorderRPaneDragged);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClicked);
    }

    //handles the pressing of the pane
    private void onBorderRPanePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    //handles the dragging of the pane
    private void onBorderRPaneDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    //handles the clicking on the confirm button
    private void onConfirmButtonClicked(MouseEvent event){
        actualStage.close();
    }

    //it sets the nickname of the winner of the match
    public void setWinnerNick(String nickOfTheWinner){
        labelForTheNick.setText(nickOfTheWinner);
    }

    //it sets the win scene on the display (theoretically???)
    public void winnerOnDisplay(){
        actualStage.showAndWait();
    }

    //it actually really sets the scene on the stage
    public void setScene(Scene theScene){
        actualStage.setScene(theScene);
    }

}
