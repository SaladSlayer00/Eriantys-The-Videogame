package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PhaseType;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import it.polimi.ingsw.view.gui.guiElements.GuiStudent;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.util.*;

/* this is the class that controls the scene of the gameboard
* the paws moves on the gameboard, so this class is pretty important for all the various method
* that are in charge of the moves of the paws/ professors/ these things...
 */
public class GameBoardSceneController extends ViewObservable implements BasicSceneController {

   private List<Dashboard> reducedDashboards;
   private List<GuiStudent> hallList;
   private List<TilePane> rows;
   private int currentDashboard;
   private Gameboard reducedGameBoard;
   private Dashboard reducedDashBoard;
   private String playerNickname;
   private GuiStudent chosenStudent;
   private PhaseType mainPhase;
   private PhaseType secondaryPhase;

    @FXML
    private GridPane gridPaneIslands;
    @FXML
    private TilePane reducedHall;
    @FXML
    private TilePane greenRow;
    @FXML
    private TilePane redRow;
    @FXML
    private TilePane yellowRow;
    @FXML
    private TilePane pinkRow;
    @FXML
    private TilePane blueRow;
    @FXML
    private TilePane towersSpot;
    @FXML
    private TilePane theChosenOne;
   @FXML
   private Button previousDashBoardButton;
   @FXML
   private Button nextDashBoardButton;


    public GameBoardSceneController(String playerNickname){
        currentDashboard = 0;
        this.playerNickname = playerNickname;
        mainPhase = PhaseType.WAITING;
        secondaryPhase = PhaseType.WAITING;
        reducedDashboards = new ArrayList<>();
        rows = new ArrayList<>();
        hallList = new ArrayList<>();
        greenRow = new TilePane();
        redRow = new TilePane();
        yellowRow = new TilePane();
        pinkRow = new TilePane();
        blueRow = new TilePane();
        theChosenOne = new TilePane();
        towersSpot = new TilePane();
        reducedHall = new TilePane();
        rows.add(greenRow);
        rows.add(redRow);
        rows.add(yellowRow);
        rows.add(pinkRow);
        rows.add(blueRow);

    }


    public void initialize() {
      updateDashBoard();
      previousDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousDashBoardButtonClicked);
      nextDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextDashBoardButtonClicked);
      reducedHall.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onStudentClicked);
      greenRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onGreenRowClicked());
      redRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onRedRowClicked());
      yellowRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onYellowRowClicked());
      pinkRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onPinkRowClicked());
      blueRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onBlueRowClicked());


    }
    public void setGameBoard(Gameboard gameboard) {
        reducedGameBoard= gameboard;

    }

    public void setDashboards(List<Dashboard> dashboards) {
        reducedDashboards = dashboards;
        reducedDashBoard = getYourDashBoard();
    }


    public void updateDashBoard(){
        clearDashBoard();
      Dashboard selectedDashBoard = reducedDashBoard;
      int numberOfTowers = selectedDashBoard.getNumTowers();
      //hall
      for (Student student : selectedDashBoard.getHall()) {
       GuiStudent studentImage = addGuiStudent(student);
       hallList.add(studentImage);
       reducedHall.getChildren().add(studentImage);
      }
      //towers
      Type colorOfTower = selectedDashBoard.getTeam();
      for (int i = 0; i < numberOfTowers; i++) {
       Image tower = new Image(getClass().getResourceAsStream("/images/towers/" + colorOfTower.toString() + "_tower.png"));
       ImageView addedTower = new ImageView(tower);
       addedTower.setFitWidth(46);
       addedTower.setFitHeight(41);
       towersSpot.getChildren().add(addedTower);
      }
      //Row
      for(Student student: selectedDashBoard.getRow(Color.GREEN).getStudents()){
          GuiStudent studentImage = addGuiStudent(student);
          greenRow.getChildren().add(studentImage);
      }
        for(Student student: selectedDashBoard.getRow(Color.RED).getStudents()){
            GuiStudent studentImage = addGuiStudent(student);
            redRow.getChildren().add(studentImage);
        }
        for(Student student: selectedDashBoard.getRow(Color.YELLOW).getStudents()){
            GuiStudent studentImage = addGuiStudent(student);
            yellowRow.getChildren().add(studentImage);
        }
        for(Student student: selectedDashBoard.getRow(Color.PINK).getStudents()){
            GuiStudent studentImage = addGuiStudent(student);
            pinkRow.getChildren().add(studentImage);
        }
        for(Student student: selectedDashBoard.getRow(Color.BLUE).getStudents()){
            GuiStudent studentImage = addGuiStudent(student);
            blueRow.getChildren().add(studentImage);
        }
      checkOwnership(selectedDashBoard);
     }


    public void updateGameBoard(){

    }

    private void  createIsland(Gameboard gameboard){

    }

 private void onPreviousDashBoardButtonClicked(Event mouseEvent)  {
  if(currentDashboard > 0){
   currentDashboard--;
   nextDashBoardButton.setDisable(false);
  }
  couldItBeDisabled(previousDashBoardButton, 0);
   reducedDashBoard = reducedDashboards.get(currentDashboard);
   Platform.runLater(()-> {
    updateDashBoard ();
   });

 }

 //handling the clicks on the button of the next mage
 private void onNextDashBoardButtonClicked(Event mouseEvent){
  if(currentDashboard < reducedDashboards.size() - 1){
   currentDashboard++;
   previousDashBoardButton.setDisable(false);
  }
  couldItBeDisabled(nextDashBoardButton, reducedDashboards.size() - 1);
  reducedDashBoard = reducedDashboards.get(currentDashboard);
  Platform.runLater(()-> {
   updateDashBoard ();
  });

 }

 private boolean couldItBeDisabled(Button button, int index){
  if(currentDashboard == index){
   button.setDisable(true);
   return true;
  }
  button.setDisable(false);
  return false;
 }


 private void checkOwnership(Dashboard dashboard){
        if(!dashboard.getOwner().equals(playerNickname)){
            for(GuiStudent guiStudent: hallList){
                guiStudent.setDisable(true);
                guiStudent.setOpacity(0.5);
            }
            reducedHall.setDisable(true);
            disabledRows();


        }else{
            for(GuiStudent guiStudent : hallList){
                guiStudent.setDisable(false);
            }
            reducedHall.setDisable(false);
            enabledRows();

        }
     setDisabledItems();
 }
private void onStudentClicked(MouseEvent event){
    Dashboard playerDashBoard = getYourDashBoard();
    Node clickedNode = event.getPickResult().getIntersectedNode();
    if(clickedNode instanceof  GuiStudent) {
        //clickedNode.setOpacity(0.5);
        chosenStudent = (GuiStudent) clickedNode;
        theChosenOne.getChildren().add(chosenStudent);
        playerDashBoard.getHall().remove(chosenStudent.getStudent());
        secondaryPhase = PhaseType.MOVE_ON_ISLAND_ROW;
        setDisabledItems();
        Platform.runLater(() -> SceneController.alertShown("Message:", "Please, choose where do you want to move your students! "+ chosenStudent.getStudent().getColor().toString()));

    }
}

public Dashboard getYourDashBoard(){
    for(Dashboard dashboard:reducedDashboards){
        if(dashboard.getOwner().equals(playerNickname))
            return dashboard;
    }
    return null;
}

private void onGreenRowClicked(){
    if(!chosenStudent.getStudent().getColor().equals(Color.GREEN))
    {
        Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
    }else{
        notifyGameController(greenRow);
    }
}

private void onRedRowClicked(){
    if(!chosenStudent.getStudent().getColor().equals(Color.RED))
    {
        Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
    }else{
        notifyGameController(redRow);
    }

}

private void onYellowRowClicked(){
    if(!chosenStudent.getStudent().getColor().equals(Color.YELLOW))
    {
        Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
    }else{
        notifyGameController(yellowRow);
    }
}

private void onPinkRowClicked(){
    if(!chosenStudent.getStudent().getColor().equals(Color.PINK))
    {
        Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
    }else{
        notifyGameController(pinkRow);
    }

}

private void onBlueRowClicked(){
    if(!chosenStudent.getStudent().getColor().equals(Color.BLUE))
    {
        Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
    }else{
        notifyGameController(blueRow);
    }


}

private void notifyGameController(TilePane selectedRow){
        try{
            Color studentColor = chosenStudent.getStudent().getColor();
            chosenStudent.setOpacity(1);
            selectedRow.getChildren().add(chosenStudent);
            theChosenOne.getChildren().remove(chosenStudent);
            setDisabledItems();
            chosenStudent = null;
            new Thread(() -> notifyObserver(obs -> obs.OnUpdateMoveOnBoard(studentColor,studentColor))).start();
        }catch (NullPointerException e){
            Platform.runLater(() -> SceneController.alertShown("Message:", "Please select a student"));
        }

}

private GuiStudent addGuiStudent(Student student){
    Image studentInTheHall = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + student.getColor().toString() + ".png"));
    GuiStudent studentImage = new GuiStudent(student);
    studentImage.setImage(studentInTheHall);
    studentImage.setFitWidth(30);
    studentImage.setFitHeight(36);
    return studentImage;
}

public void setDisabledItems(){
  if(mainPhase.equals(PhaseType.WAITING)){
      for(GuiStudent guiStudent: hallList){
          guiStudent.setDisable(true);
      }
      reducedHall.setDisable(true);
      disabledRows();
  }else if(mainPhase.equals(PhaseType.YOUR_MOVE)&&secondaryPhase.equals(PhaseType.MOVE_ON_ISLAND_ROW)){
      for(GuiStudent guiStudent: hallList){
          guiStudent.setDisable(true);
      }
      reducedHall.setDisable(true);
      enabledRows();
  }else if(mainPhase.equals(PhaseType.YOUR_MOVE)&&secondaryPhase.equals(PhaseType.MOVE_STUDENT)){
      for(GuiStudent guiStudent: hallList){
          guiStudent.setDisable(false);
      }
      reducedHall.setDisable(false);
      disabledRows();


      }else if(mainPhase.equals(PhaseType.YOUR_MOVE)&&secondaryPhase.equals(PhaseType.MOVE_MOTHER)){
          for(GuiStudent guiStudent: hallList){
              guiStudent.setDisable(true);
         }
         reducedHall.setDisable(true);
         disabledRows();
  }
}

public void setSecondaryPhase(PhaseType phaseType){
        secondaryPhase = phaseType;
}

public void setMainPhase(PhaseType phaseType){
        mainPhase = phaseType;
}

private void clearDashBoard(){
    reducedHall.getChildren().clear();
    towersSpot.getChildren().clear();
    hallList.clear();
    greenRow.getChildren().clear();
    redRow.getChildren().clear();
    yellowRow.getChildren().clear();
    pinkRow.getChildren().clear();
    blueRow.getChildren().clear();
}

private void disabledRows(){
    blueRow.setDisable(true);
    greenRow.setDisable(true);
    redRow.setDisable(true);
    yellowRow.setDisable(true);
    pinkRow.setDisable(true);
}

private void enabledRows(){
    blueRow.setDisable(false);
    greenRow.setDisable(false);
    redRow.setDisable(false);
    yellowRow.setDisable(false);
    pinkRow.setDisable(false);
}



}
