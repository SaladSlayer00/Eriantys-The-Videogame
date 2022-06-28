package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class WinSceneController implements BasicSceneController{

    private Stage actualStage;
    private double offsetX;
    private double offsetY;
    private String winnerNickName;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label winnerName;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView statusImage;

    //the constructor for the scene (the basic one)
    public WinSceneController(){
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        statusImage = new ImageView();
        winnerName = new Label();

    }

    @FXML
    public void initialize(){
        anchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onBorderRPanePressed);
        anchorPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onBorderRPaneDragged);
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onExitButtonClicked);
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
    private void onExitButtonClicked(MouseEvent event){
        actualStage.close();
        System.exit(0);
    }

    //it sets the nickname of the winner of the match
    public void setWinnerNick(String nickOfTheWinner){
        winnerName.setText(nickOfTheWinner);
        winnerNickName = nickOfTheWinner;
    }

    //it sets the win scene on the display (theoretically???)
    public void winnerOnDisplay(){
        actualStage.showAndWait();
    }

    //it actually really sets the scene on the stage
    public void setScene(Scene theScene){
        actualStage.setScene(theScene);
    }

    public void isWinner(String player){
        if (winnerNickName.equals(player)){
            statusImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/winner.png"))));
        }else{
            statusImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/lose.png"))));
        }
    }

}
