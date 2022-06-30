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


/**
 * AlertSceneController class for the popup message that are displayed on the screen of the players during the match
 */
public class AlertSceneController implements BasicSceneController {

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

    /**
     * class constructor that sets the fundamental parameter for the class
     */
    public AlertSceneController(){
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
    }

    /**
     * initialize method initializes the class so to allow the players to interact (which means clicks on the button
     * to close the popup or move it on the screen)
     */
    @FXML
    public void initialize(){
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);
        buttonForOkay.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onButtonForOkayClicked);
    }

    /**
     * this method handles the pressing of the mouse on the popup message
     * @param mouseEvent is the input of the mose given by the player
     */
    private void onRootBPaneMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    /**
     * this method handles the dragging of the mouse of the popup
     * @param mouseEvent is the input of the mouse given by the player
     */
    private void onRootBPaneMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    /**
     * this method is the one that handles the clicks on the button to close the popup message
     * @param mouseEvent is the input given by the player when they clicks on the button
     */
    public void onButtonForOkayClicked(MouseEvent mouseEvent){
        actualStage.close();
    }

    /**
     * this method settles the title of the message it is going to ble displayed on the screen
     * @param string is the string containing the title of the popup
     */
    public void setAlertTitle(String string){
        titleLabel.setText(string);
    }

    /**
     * this method sets the message that is going to be displayed in the popup
     * @param string is the string of the message
     */
    public void setAlertMessage(String string){
        messageLabel.setText(string);
    }

    /**
     * this method allow the popup message to be displayed on the player's screen
     */
    public void displayAlert(){
        actualStage.showAndWait();
    }

    /**
     * this method is the setter of the message
     * @param scene is the actual scene that it has been set
     */
    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }


}
