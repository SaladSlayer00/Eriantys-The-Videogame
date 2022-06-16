package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private ImageView[] studentsImages;
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
        studentsImages = new ImageView[9];
        anchorPaneCloudZero = new AnchorPane();
        anchorPaneCloudOne = new AnchorPane();
        anchorPaneCloudTwo = new AnchorPane();
        for(int i = 0; i<9;i++){
            ImageView imageView = new ImageView();
            if(i<3){
                Platform.runLater(()->anchorPaneCloudZero.getChildren().add(imageView));
            }else if(i<6){
                Platform.runLater(()->anchorPaneCloudTwo.getChildren().add(imageView));
            }else{
                Platform.runLater(()->anchorPaneCloudOne.getChildren().add(imageView));
            }
            studentsImages[i] = imageView;
        }
        Platform.runLater(()->xyCorrection());


    }
    @FXML
    public void initialize(){
        Platform.runLater(()->createListOfClouds(availableClouds));
        confirmButton.setDisable(true);
        if(availableClouds.size()<3){
            cloudOne.setDisable(true);
        }
        rootBPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootBPaneMousePressed);
        rootBPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootBPaneMouseDragged);
        cloudZero.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onCloudZeroClicked(availableClouds));
        cloudOne.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onCloudOneClicked(availableClouds));
        cloudTwo.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onCloudTwoClicked(availableClouds));
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event->OnConfirmButtonClicked());

    }


    private void createListOfClouds(List<Cloud> availableClouds){
        for(Cloud cloud:availableClouds){
            for(int i = 0; i<cloud.getStudents().size();i++){
                String color = cloud.getStudents().get(i).getColor().toString();
                Image image = new Image(getClass().getResourceAsStream("/images/pawn/students/student_"+color+".png"));
                studentsImages[i].setImage(image);
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
        cloudTwo.setDisable(true);
        cloudZero.setDisable(false);
        confirmButton.setDisable(false);
    }
    private void OnConfirmButtonClicked(){
        if(typeOfChoice == "Pick"){
            new Thread(() -> notifyObserver(obs -> obs.OnUpdatePickCloud(chosenCloud))).start();
        }else{
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

    private void xyCorrection(){
        studentsImages[0].setLayoutX(14);
        studentsImages[0].setLayoutY(63);
        studentsImages[1].setLayoutX(122);
        studentsImages[1].setLayoutY(39);
        studentsImages[2].setLayoutX(84);
        studentsImages[2].setLayoutY(132);
        studentsImages[3].setLayoutX(108);
        studentsImages[3].setLayoutY(29);
        studentsImages[4].setLayoutX(11);
        studentsImages[4].setLayoutY(54);
        studentsImages[5].setLayoutX(99);
        studentsImages[5].setLayoutY(131);
        studentsImages[6].setLayoutX(14);
        studentsImages[6].setLayoutY(66);
        studentsImages[7].setLayoutX(91);
        studentsImages[7].setLayoutY(131);
        studentsImages[8].setLayoutX(120);
        studentsImages[8].setLayoutY(23);
    }
}
