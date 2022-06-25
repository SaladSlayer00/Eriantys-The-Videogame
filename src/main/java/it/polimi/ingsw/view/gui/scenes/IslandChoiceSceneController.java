package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import it.polimi.ingsw.view.gui.guiElements.GuiStudent;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class IslandChoiceSceneController extends ViewObservable implements BasicSceneController {
    private List<Island> islandList;
    private int currentIndex;
    private ExpertDeck expertDeck;
    private final Stage actualStage;
    private double offsetX;
    private double offsetY;
    @FXML
    private TilePane currentIsland;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button confirmButton;
    @FXML
    private Label islandIndex;
    @FXML
    private Label question;

    public IslandChoiceSceneController(){
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.toFront();
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        currentIndex = 0;
        currentIsland = new TilePane();

    }
    @FXML
    public void initialize(){
        couldItBeDisabled(previousButton, 0);
        couldItBeDisabled(nextButton, islandList.size() - 1);
        previousButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousButtonClicked);
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextButtonClicked);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButtonClicked);

    }
    private boolean couldItBeDisabled(Button button, int index){
        if(currentIndex == index){
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    private void onPreviousButtonClicked(Event mouseEvent) {
        if(currentIndex > 0){
            currentIndex--;
            nextButton.setDisable(false);
        }
        couldItBeDisabled(previousButton, 0);
        updateIsland();
    }

    //handling the clicks on the button of the next mage
    private void onNextButtonClicked(Event mouseEvent) {
        if(currentIndex < islandList.size() - 1){
            currentIndex++;
            previousButton.setDisable(false);
        }
        couldItBeDisabled(nextButton, islandList.size() - 1);
        updateIsland();
    }

    private void onConfirmButtonClicked(Event mouseEvent){
        if(expertDeck.equals(ExpertDeck.HERALD)){
            new Thread(()->notifyObserver(observers->observers.OnUpdateEffectHerald(currentIndex))).start();
        }else if(expertDeck.equals(ExpertDeck.HERBALIST)){
            new Thread(()->notifyObserver(observers->observers.OnUpdateEffectHerbalist(currentIndex))).start();
        }else if(expertDeck.equals(ExpertDeck.TAVERNER)){
            new Thread(()->notifyObserver(observers->observers.OnUpdateEffectTaverner(currentIndex))).start();
        }
    }

    public void setListOfIsland(List<Island> listOfIsland){
        this.islandList = listOfIsland;
    }

    public void setExpert(ExpertDeck expertDeck){
        this.expertDeck = expertDeck;

    }

    private void updateIsland()  {
        currentIsland.getChildren().clear();
        for (Color color : islandList.get(currentIndex).getStudents().keySet()) {
            for (Student student : islandList.get(currentIndex).getStudents().get(color)) {
                GuiStudent studentImage = addGuiStudent(student);
                currentIsland.getChildren().add(studentImage);
            }
        }
            if(islandList.get(currentIndex).isMotherNature()){
                Image image = new Image(getClass().getResourceAsStream("/images/pawn/mother_nature.png"));
                ImageView motherNature = new ImageView(image);
                motherNature.setFitWidth(30);
                motherNature.setFitHeight(30);
                currentIsland.getChildren().add(motherNature);
            }
            if(islandList.get(currentIndex).getTower()){
                try {
                    Image image = new Image(getClass().getResourceAsStream("/images/towers/"+islandList.get(currentIndex).getTeam().toString()+"_tower.png"));
                    ImageView tower = new ImageView(image);
                    tower.setFitWidth(30);
                    tower.setFitHeight(30);
                    currentIsland.getChildren().add(tower);
                }catch (noTowerException e){}
            }
            islandIndex.setText("Index : " +currentIndex);
        }

    public void setQuestion(){
        if(expertDeck.equals(ExpertDeck.HERALD)){
            question.setText("Please choose the island to calculate influence on");
        }else if(expertDeck.equals(ExpertDeck.HERBALIST)){
            question.setText("Please choose the island to ban");
        }else if(expertDeck.equals(ExpertDeck.TAVERNER)){
            question.setText("Please choose the island to put the student on");
        }
    }

    private GuiStudent addGuiStudent(Student student){
        Image studentOnIsland = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + student.getColor().toString() + ".png"));
        GuiStudent studentImage = new GuiStudent(student);
        studentImage.setImage(studentOnIsland);
        studentImage.setFitWidth(30);
        studentImage.setFitHeight(36);
        return studentImage;
    }

    public void displayAlert(){
        actualStage.showAndWait();
    }

    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }

}
