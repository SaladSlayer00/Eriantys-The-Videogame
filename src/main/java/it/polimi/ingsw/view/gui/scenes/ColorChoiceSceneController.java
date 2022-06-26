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

//should decide how to display the choice;

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

    public void setgBSC(GameBoardSceneController gBSC) {
        this.gBSC = gBSC;
    }

    public void setExpert(ExpertDeck ec){
        expertChosen = ec;
    }

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

    private GuiStudent addGuiStudent(Color color) {
        Image colorImage = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + color.getText() + ".png"));
        Student student = new Student(color);
        GuiStudent studentImage = new GuiStudent(student);
        studentImage.setImage(colorImage);
        studentImage.setFitWidth(85d);
        studentImage.setFitHeight(85d);
        return studentImage;
    }

    private void onOkayButtonClicked(MouseEvent event){
        if(expertChosen.equals(ExpertDeck.BANKER)) {
            new Thread(() -> notifyObserver(observers -> observers.OnUpdateEffectBanker(chosenColor.getStudent().getColor()))).start();
        }
        else if(expertChosen.equals(ExpertDeck.SELLER)){
            new Thread(() -> notifyObserver(observers -> observers.OnUpdateEffectSeller(chosenColor.getStudent().getColor()))).start();
        }
        Platform.runLater(() -> SceneController.alertShown("Message:", "Please, choose a student to move!"));
        Platform.runLater(()->SceneController.changeRootPane(gBSC,"gameboard2_scene.fxml"));
        gBSC.setMainPhase(PhaseType.YOUR_MOVE);
        gBSC.setSecondaryPhase(PhaseType.MOVE_STUDENT);
        actualStage.close();
    }

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

    private void onRootAPMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    private void onRootAPMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }


    public void setExpertChosen(ExpertDeck expertChosen){
        this.expertChosen = expertChosen;
    }

    public void displayAlert(){
        actualStage.showAndWait();
    }
}
