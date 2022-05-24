package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


//TODO
public class AlertSceneController extends   BasicSceneController {

    private final Stage actualStage;

    private double offsetX;
    private double offsetY;

    @FXML
    private BorderPane rootBPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button buttonForOkay;

    //this is the constructor
    public AlertSceneController(){
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
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);
        rootBPane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onButtonForOkayClicked);
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
        actualStage.close();
    }

    public void setAlertTitle(String string){
        titleLabel.setText(string);
    }

    public void setAlertMessage(String string){
        messageLabel.setText(string);
    }

    public void displayLabel(){
        actualStage.showAndWait();
    }

    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }
}
