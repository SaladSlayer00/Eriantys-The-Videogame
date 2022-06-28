package it.polimi.ingsw.view.gui.scenes;

import com.sun.scenario.effect.impl.prism.PrRenderInfo;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.enums.PhaseType;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import it.polimi.ingsw.view.gui.guiElements.GuiStudent;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ColorChoiceSceneController is the controller that sets the scene for the moment of the game when the player have to choose
 * a color for certain effects of the expert mode
 * the two cards that need this choice are banker and seller that use the chosen color to give advantages or disadvantages
 * to the players
 */

public class ColorChoiceSceneController extends ViewObservable implements BasicSceneController{

    private ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.PINK, Color.YELLOW, Color.BLUE));
    private GuiStudent chosenColor;
    private ExpertDeck expertChosen;
    private GameBoardSceneController gBSC;
    private final Stage actualStage;
    private double offsetX;
    private double offsetY;

    @FXML
    private AnchorPane rootAP;
    @FXML
    private TilePane tP;
    @FXML
    private Button okayButton;
    @FXML
    private Button notOkayButton;

    /**
     * class constructor
     */
    public ColorChoiceSceneController(){

        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.toFront();
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        tP = new TilePane();
        for(Color c : colors){
            GuiStudent colorImage = addGuiStudent(c);
            Platform.runLater(()-> tP.getChildren().add(colorImage));
        }
    }

    /**
     * this method initializes the class setting all the various parameter to display the scene on the player's
     * screen in the proper way
     */
    @FXML
    public void initialize(){
        okayButton.setDisable(true);
        notOkayButton.setDisable(true);
        tP.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onColorClicked);
        okayButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkayButtonClicked);
        notOkayButton.addEventFilter(MouseEvent.MOUSE_CLICKED, this::onNotOkayButtonClicked);
        rootAP.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootAPMousePressed);
        rootAP.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootAPMouseDragged);
    }

    /**
     * setter for the gameboard
     * @param gBSC is the gameboard of the match that it is played at the moment of the selection
     */
    public void setgBSC(GameBoardSceneController gBSC) {
        this.gBSC = gBSC;
    }

    /**
     * setter that tells which expert card has been played
     * @param ec is the expert card chosen by the player
     */
    public void setExpert(ExpertDeck ec){
        expertChosen = ec;
    }

    /**
     * this method handles the clicks on the various images of the paws
     * @param event is the input given by the player's mouse
     */
    private void onColorClicked(MouseEvent event){
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if(clickedNode instanceof  GuiStudent) {
            chosenColor = (GuiStudent) clickedNode;
            okayButton.setDisable(false);
            notOkayButton.setDisable(false);
            for(Node node: tP.getChildren()){
                GuiStudent currentGuiStudent = (GuiStudent) node;
                if(!currentGuiStudent.equals(chosenColor)){
                    currentGuiStudent.setOpacity(0.5);
                }
            }
            tP.setDisable(true);
        }
    }

    /**
     * this method add the gui student to the scene
     * @param color is the color of the student
     * @return a gui student to be added to the scene
     */
    private GuiStudent addGuiStudent(Color color) {
        Image colorImage = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + color.getText() + ".png"));
        Student student = new Student(color);
        GuiStudent studentImage = new GuiStudent(student);
        studentImage.setImage(colorImage);
        studentImage.setFitWidth(85d);
        studentImage.setFitHeight(85d);
        return studentImage;
    }

    /**
     * this method handles the clicks on the button that gives the confirmation of the choice of the color
     * the controller is notified with the proper card chosen so that it knows how to react
     * @param event is the input given by the player's mouse
     */
    private void onOkayButtonClicked(MouseEvent event){
        if(expertChosen.equals(ExpertDeck.BANKER)) {
            new Thread(() -> notifyObserver(observers -> observers.OnUpdateEffectBanker(chosenColor.getStudent().getColor()))).start();
        }
        else if(expertChosen.equals(ExpertDeck.SELLER)){
            new Thread(() -> notifyObserver(observers -> observers.OnUpdateEffectSeller(chosenColor.getStudent().getColor()))).start();
        }
        Platform.runLater(() -> SceneController.alertShown("Message:", "Please, choose a student to move!"));
        Platform.runLater(()->SceneController.changeRootPane(gBSC,"gameboard_scene.fxml"));
        gBSC.setMainPhase(PhaseType.YOUR_MOVE);
        gBSC.setSecondaryPhase(PhaseType.MOVE_STUDENT);
        actualStage.close();
    }

    /**
     * this is the method that the handles the clicks on the button that deselect the choice of the color
     * that was previously clicked
     * @param event is the input given by the player's mouse
     */
    private void onNotOkayButtonClicked(MouseEvent event){
        for(Node node: tP.getChildren()){
            GuiStudent currentGuiStudent = (GuiStudent) node;
            currentGuiStudent.setOpacity(1);
        }
        tP.setDisable(false);
        chosenColor = null;
        okayButton.setDisable(true);
        notOkayButton.setDisable(true);
    }

    /**
     * this method handles the pressing on the window that display the scene
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onRootAPMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    /**
     * this method handles the dragging of the scene
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onRootAPMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    /**
     * setter of the scene
     * @param scene is the proper scene
     */
    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }


    /**
     * setter of the expert card chosen by the player
     * @param expertChosen
     */
    public void setExpertChosen(ExpertDeck expertChosen){
        this.expertChosen = expertChosen;
    }

    /**
     * method that displays the message for the choice so that the player can read it
     */
    public void displayAlert(){
        actualStage.showAndWait();
    }
}
