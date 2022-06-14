package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class AssistantChoiceSceneController extends ViewObservable implements BasicSceneController {
    private List<Assistant> notAvailableAssistants;
    private List<ImageView> assistant;
    private Assistant chosenOne;
    private final Stage actualStage;
    private double offsetX;
    private double offsetY;
    @FXML
    private BorderPane rootBPane;
    @FXML
    private Button confirmButton;
    @FXML
    private Button deselectButton;
    @FXML
    private ImageView chosenAssistant;
    @FXML
    private ImageView assistant0ne;
    @FXML
    private ImageView assistantTwo;
    @FXML
    private ImageView assistantThree;
    @FXML
    private ImageView assistantFour ;
    @FXML
    private ImageView assistantFive;
    @FXML
    private ImageView assistantSix ;
    @FXML
    private ImageView assistantSeven ;
    @FXML
    private ImageView assistantEight;
    @FXML
    private ImageView assistantNine  ;
    @FXML
    private ImageView assistantTen;

    public AssistantChoiceSceneController(){
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.toFront();
        //actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        notAvailableAssistants = new ArrayList<>();
        chosenAssistant = new ImageView();
        assistant = new ArrayList<>();
        assistant.add(assistant0ne);
        assistant.add(assistantTwo);
        assistant.add(assistantThree);
        assistant.add(assistantFour);
        assistant.add(assistantFive);
        assistant.add(assistantSix);
        assistant.add(assistantSeven);
        assistant.add(assistantEight);
        assistant.add(assistantNine);
        assistant.add(assistantTen);
        /*
        for(int i = 0; i<11;i++){
            Image image = new Image(getClass().getResourceAsStream("/images/cards/assistant/Assistant(" +i + ").png"));
            assistant.get(i).setImage(image);
        }
*/
    }
    @FXML
    public void initialize(){
        confirmButton.setDisable(true);
        deselectButton.setDisable(true);
        disabledAssistant();
        assistant0ne.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(1,1));
        assistantTwo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(2,1));
        assistantThree.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(3,1));
        assistantFour.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(4,2));
        assistantFive.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(5,2));
        assistantSix.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(6,3));
        assistantSeven.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(7,3));
        assistantEight.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(8,4));
        assistantNine.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(9,4));
        assistantTen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(10,5));
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event->onConfirmButton());
        deselectButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onDeselectButton());
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);

    }

    private void disabledAssistant(){
        for(Assistant assistant: notAvailableAssistants){
            this.assistant.get(assistant.getNumOrder()-1).setOpacity(0.3);
            this.assistant.get(assistant.getNumOrder()-1).setDisable(true);

        }
    }

    private void onAssistantClick(int choosenAssistantIndex , int moves){
        for(ImageView image : this.assistant){
            image.setDisable(true);
            image.setOpacity(0.5);
        }
        setAssistantImage(choosenAssistantIndex);
        chosenOne = new Assistant(choosenAssistantIndex,moves);
        confirmButton.setDisable(false);
        deselectButton.setDisable(false);

    }

    private void onConfirmButton(){
        new Thread(() -> notifyObserver(obs -> obs.OnUpdateAssistant(chosenOne))).start();
        actualStage.close();
    }

    private void onDeselectButton(){
        for(ImageView image : this.assistant){
            image.setDisable(true);
            image.setOpacity(1);
        }
        chosenOne = null;
        confirmButton.setDisable(true);
        deselectButton.setDisable(true);
    }

    private void setAssistantImage(int index){
        Image image = new Image(getClass().getResourceAsStream("/images/cards/assistant/Assistant(" +index + ").png"));
        chosenAssistant.setImage(image);
    }

    private void onRootBPaneMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    //this method handles the event of the dragging of the mouse
    private void onRootBPaneMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    public void displayAlert(){
        actualStage.showAndWait();
    }

    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }

    public void setAssistantDeck(List<Assistant> notAvailableAssistants) {
        this.notAvailableAssistants = notAvailableAssistants;
    }

}
