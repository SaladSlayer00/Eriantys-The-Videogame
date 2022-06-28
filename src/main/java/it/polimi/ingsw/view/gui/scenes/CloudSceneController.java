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

/**
 * CloudSceneController class handles the scene that is displayed fot the choice of the cloud to fill at the beginning of the turn
 * @authors Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */
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

    /**
     * class constructor
     */
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

    /**
     * this method initializes the class setting all the various parameter to display the scene
     * on the player's screen in the proper way
     */
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

    /**
     * this method create the list of cloud that are available to be filled with the students' paws by the player
     * here the clouds are created to be displayed
     * @param availableClouds is the list of the available clouds that have to be filled
     */
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

    /**
     * this method is a setter for the list of available clouds that here are filled with the students' paws
     * @param availableClouds is the list of available clouds
     */
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

    /**
     * handles the clicks on the first cloud of the list of clouds that are used in the match
     * @param availableClouds is the list of clouds that are available at the moment so to know which cloud occupied
     *                        the first position available
     */
    private void onCloudZeroClicked(List<Cloud> availableClouds){
        chosenCloud = availableClouds.get(0).getIndex();
        cloudZero.setDisable(true);
        cloudTwo.setDisable(false);
        if(availableClouds.size()==3){
            cloudOne.setDisable(false);
        }
        confirmButton.setDisable(false);

    }

    /**
     * handles the clicks on the third cloud of the list of clouds that are used in the match
     * @param availableClouds is the list of clouds that are available at the moment so to know which cloud occupied
     *                        the third position available
     */
    private void onCloudTwoClicked(List<Cloud> availableClouds){
        chosenCloud = availableClouds.get(1).getIndex();
        cloudTwo.setDisable(true);
        cloudZero.setDisable(false);
        if(availableClouds.size()==3){
            cloudOne.setDisable(false);
        }
        confirmButton.setDisable(false);
    }

    /**
     * handles the clicks on the second cloud of the list of clouds that are used in the match
     * @param availableClouds is the list of clouds that are available at the moment so to know which cloud occupied
     *                        the second position available
     */
    private void onCloudOneClicked(List<Cloud> availableClouds){
        chosenCloud = availableClouds.get(2).getIndex();
        cloudOne.setDisable(true);
        cloudTwo.setDisable(false);
        cloudZero.setDisable(false);
        confirmButton.setDisable(false);
    }

    /**
     * method that handles the clicks on the button that gives the confirmation of the choice of the cloud selected
     * by the player
     */
    private void OnConfirmButtonClicked(){
        if(typeOfChoice == "firstPick"){
            new Thread(() -> notifyObserver(obs -> obs.OnUpdatePickCloud(chosenCloud))).start();
        }else if(typeOfChoice == "get"){
            new Thread(() -> notifyObserver(obs -> obs.OnUpdateGetFromCloud(chosenCloud))).start();
        }
        actualStage.close();
    }

    /**
     * this method sets the scene so to display it in the proper way to the player's screen
     * @param cloudScene is the actual scene to display
     */
    public void setScene(Scene cloudScene) {
        actualStage.setScene(cloudScene);
    }

    /**
     * this method handles the mouse pressing on the window that display the clouds
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onRootBPaneMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    /**
     * this method handles the event of the dragging of the mouse
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onRootBPaneMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    /**
     * setter method that tells which cloud are available
     * @param availableClouds is the list of available clouds
     */
    public void setAvailableClouds(List<Cloud> availableClouds) {
        this.availableClouds = availableClouds;
    }

    /**
     * this method gives the list of empty clouds (they don't have students on them at the moment)
     * @param emptyClouds is the list of clouds that are empty at the moment
     */
    public void setEmptyClouds(List<Cloud> emptyClouds) {
        this.emptyClouds = emptyClouds;
    }

    /**
     * method that is used to display a message so that the player can read it
     */
    public void displayAlert() {
        actualStage.showAndWait();
    }

    /**
     * this method is used to set the choice so that the scene controller can understand it
     * @param choice is the string indicating the choice made
     */
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