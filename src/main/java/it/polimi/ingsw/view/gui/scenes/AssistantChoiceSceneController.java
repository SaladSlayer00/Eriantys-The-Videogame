package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * AssistantChoiceSceneController class for the game phase when the player choose their assistants' deck
 * @authors Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */
public class AssistantChoiceSceneController extends ViewObservable implements BasicSceneController {
    private List<Assistant> notAvailableAssistants;
    private ImageView[] imagesView;
    private Assistant chosenOne;
    private final Stage actualStage;
    private double offsetX;
    private double offsetY;
    @FXML
    private TilePane tilePane;
    @FXML
    private AnchorPane rootBPane;
    @FXML
    private Button confirmButton;
    @FXML
    private Button deselectButton;
    @FXML
    private ImageView chosenAssistant;

    /**
     *  class constructor which sets the various parameter for the scene on the screen
     */
    public AssistantChoiceSceneController(){
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.toFront();
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        notAvailableAssistants = new ArrayList<>();
        chosenAssistant = new ImageView();
        imagesView = new ImageView[10];
        tilePane = new TilePane();
       for(int i = 0; i<10;i++){
           int j = i+1;
           Image image = new Image(getClass().getResourceAsStream("/images/cards/assistant/Assistente(" +j +").png"));
           ImageView imageView = new ImageView(image);
           imageView.setFitWidth(100d);
           imageView.setFitHeight(130d);
           Platform.runLater(()->tilePane.getChildren().add(imageView));
           imagesView[i] = imageView;
       }

    }

    /**
     * this method initializes the class so to allow the player to interact with the scene
     * all the cards are set so the make the scene react in the proper way to the clicks of the players
     */
    @FXML
    public void initialize(){
        confirmButton.setDisable(true);
        deselectButton.setDisable(true);
        Platform.runLater(()->disabledAssistant());
        imagesView[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(1,1));
        imagesView[1].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(2,1));
        imagesView[2].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(3,1));
        imagesView[3].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(4,2));
        imagesView[4].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(5,2));
        imagesView[5].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(6,3));
        imagesView[6].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(7,3));
        imagesView[7].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(8,4));
        imagesView[8].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(9,4));
        imagesView[9].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onAssistantClick(10,5));
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event->onConfirmButton());
        deselectButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onDeselectButton());
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);

    }

    /**
     * this method disables the assistants that cannot be chosen making them faded
     */
    private void disabledAssistant(){
        for(Assistant assistant: notAvailableAssistants){
            System.out.println();
            imagesView[(assistant.getNumOrder()-1)].setOpacity(0.3);
            imagesView[(assistant.getNumOrder()-1)].setDisable(true);

        }
    }

    /**
     * this method handles the clicks on an assistant's image
     * when the player clicks on the assistant it becomes the selected one
     * @param chosenAssistantIndex is the index of the assistant it has been chosen
     * @param moves is the integer that indicates the moves that the assistant allows to Mother Nature
     */
    private void onAssistantClick(int chosenAssistantIndex , int moves){
        for(int i = 0; i <10;i++){
            imagesView[i].setDisable(true);
            imagesView[i].setOpacity(0.5);
        }
        setAssistantImage(chosenAssistantIndex);
        chosenOne = new Assistant(chosenAssistantIndex,moves);
        confirmButton.setDisable(false);
        deselectButton.setDisable(false);

    }

    /**
     * method that handles the clicks on the button to give the confirmation of the choice of the card of the assistant
     */
    private void onConfirmButton(){
        new Thread(() -> notifyObserver(obs -> obs.OnUpdateAssistant(chosenOne))).start();
        actualStage.close();
    }

    /**
     * method that handles the clicks on the button to deselect the card of the assistant that it was clicked
     */
    private void onDeselectButton(){
        for(int i = 0; i<10;i++){
            imagesView[i].setDisable(false);
            imagesView[i].setOpacity(1);
        }
        disabledAssistant();
        chosenOne = null;
        chosenAssistant.setImage(null);
        confirmButton.setDisable(true);
        deselectButton.setDisable(true);
    }

    /**
     * method that sets the image of the card of the assistant selected in the space on the scene thought to make the
     * choice more visible
     * @param index is th integer that indicates the index of the assistant
     */
    private void setAssistantImage(int index){
        Image image = new Image(getClass().getResourceAsStream("/images/cards/assistant/Assistente(" +index + ").png"));
        chosenAssistant.setImage(image);
    }

    /**
     * method that handles the pressing of the mouse on the window that shows the assistants' cards
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onRootBPaneMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    /**
     * this method handles the event of the dragging of the mouse on the window that shows the assistants' cards
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onRootBPaneMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    /**
     * this method displays the message for the choice of the assistant
     */
    public void displayAlert(){
        actualStage.showAndWait();
    }

    /**
     * this method sets the scene so to make the scene visible to the player's display
     * @param scene
     */
    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }

    /**
     * this method sets the deck of the assistants' cards telling which ones are not available to be selected
     * @param notAvailableAssistants
     */
    public void setAssistantDeck(List<Assistant> notAvailableAssistants) {
        this.notAvailableAssistants = notAvailableAssistants;
    }

}
