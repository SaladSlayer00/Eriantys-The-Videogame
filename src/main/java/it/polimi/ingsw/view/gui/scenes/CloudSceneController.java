package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;
public class CloudSceneController  extends ViewObservable implements BasicSceneController{

    private List<Cloud> availableClouds;
    private List<Cloud> emptyClouds;
    private final Stage actualStage;
    private double offsetX;
    private double offsetY;
    private int chosenCloud;
    private List<ImageView> studentsImagesZero;
    private List<ImageView> studentsImagesOne;
    private List<ImageView> studentsImagesTwo;
    private String typeOfChoice;

    @FXML
    private AnchorPane rootBPane;
    @FXML
    private AnchorPane anchorPaneCloudZero;
    @FXML
    private AnchorPane anchorPaneCloudOne;
    @FXML
    private AnchorPane anchorPaneCloudTwo;
    @FXML
    private Button cloudZero;
    @FXML
    private Button cloudOne;
    @FXML
    private Button cloudTwo;
    @FXML
    private Button confirmButton;
    public CloudSceneController(){
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.toFront();
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        emptyClouds = new ArrayList<>();
        availableClouds = new ArrayList<>();
        studentsImagesZero = new ArrayList<>();
        studentsImagesOne = new ArrayList<>();
        studentsImagesTwo = new ArrayList<>();
    }
    @FXML
    public void initialize(){
        Platform.runLater(()->createListOfClouds(availableClouds));
        Platform.runLater(()->setListOfClouds(availableClouds));
        Platform.runLater(()->setNumberOfClouds(availableClouds));
        confirmButton.setDisable(true);
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);
        cloudZero.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onCloudZeroClicked(availableClouds));
        cloudOne.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onCloudOneClicked(availableClouds));
        cloudTwo.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onCloudTwoClicked(availableClouds));
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event->OnConfirmButtonClicked());

    }
    private void createListOfClouds(List<Cloud> availableClouds) {
        int numOfStudents;
        if (availableClouds.size() == 2) {
            numOfStudents = 3;
        } else {
            numOfStudents = 4;
        }
        for (int i = 0; i < numOfStudents; i++) {
            studentsImagesZero.add((ImageView) anchorPaneCloudZero.getChildren().get(i));
            studentsImagesTwo.add((ImageView) anchorPaneCloudTwo.getChildren().get(i));
            studentsImagesOne.add((ImageView) anchorPaneCloudOne.getChildren().get(i));
        }
    }

    private void setListOfClouds(List<Cloud> availableClouds){
        int numOfStudents;
        if (availableClouds.size() == 2) {
            numOfStudents = 3;
        } else {
            numOfStudents = 4;
        }
        if(availableClouds.get(0).getStudents().size()!=0){
                for(int i = 0; i< numOfStudents; i++){
                String color = availableClouds.get(0).getStudents().get(i).getColor().toString();
                Image image = new Image(getClass().getResourceAsStream("/images/pawn/students/student_"+color+".png"));
                studentsImagesZero.get(i).setImage(image);
            }

        }
        if(availableClouds.get(1).getStudents().size()!=0) {
            for (int i = 0; i < numOfStudents; i++) {
                String color = availableClouds.get(1).getStudents().get(i).getColor().toString();
                Image image = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + color + ".png"));
                studentsImagesTwo.get(i).setImage(image);
            }
        }

        if(numOfStudents ==4 &&  availableClouds.get(2).getStudents().size()!=0){
            for(int i = 0; i< numOfStudents; i++) {
            String color = availableClouds.get(2).getStudents().get(i).getColor().toString();
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + color + ".png"));
            studentsImagesOne.get(i).setImage(image);

        }
        }

    }

    private void onCloudZeroClicked(List<Cloud> availableClouds){
        chosenCloud = availableClouds.get(0).getIndex();
        cloudZero.setDisable(true);
        cloudTwo.setDisable(false);
        if(availableClouds.size()==3){
            cloudOne.setDisable(false);
        }
        confirmButton.setDisable(false);

    }
    private void onCloudTwoClicked(List<Cloud> availableClouds){
        chosenCloud = availableClouds.get(1).getIndex();
        cloudTwo.setDisable(true);
        cloudZero.setDisable(false);
        if(availableClouds.size()==3){
            cloudOne.setDisable(false);
        }
        confirmButton.setDisable(false);
    }
    private void onCloudOneClicked(List<Cloud> availableClouds){
        chosenCloud = availableClouds.get(2).getIndex();
        cloudOne.setDisable(true);
        cloudTwo.setDisable(false);
        cloudZero.setDisable(false);
        confirmButton.setDisable(false);
    }
    private void OnConfirmButtonClicked(){
        if(typeOfChoice == "firstPick"){
            new Thread(() -> notifyObserver(obs -> obs.OnUpdatePickCloud(chosenCloud))).start();
        }else if(typeOfChoice == "get"){
            new Thread(() -> notifyObserver(obs -> obs.OnUpdateGetFromCloud(chosenCloud))).start();
        }
        actualStage.close();
    }

    public void setScene(Scene cloudScene) {
        actualStage.setScene(cloudScene);
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

    public void setAvailableClouds(List<Cloud> availableClouds) {
        this.availableClouds = availableClouds;
    }

    public void setEmptyClouds(List<Cloud> emptyClouds) {
        this.emptyClouds = emptyClouds;
    }

    public void displayAlert() {
        actualStage.showAndWait();
    }

    public void setTypeOfChoice(String choice) {
        typeOfChoice = choice;
    }

    public void setNumberOfClouds(List<Cloud> availableClouds){
        if(availableClouds.size()==3){
            cloudOne.setDisable(false);
        }else{
            cloudOne.setDisable(true);
        }
    }
}