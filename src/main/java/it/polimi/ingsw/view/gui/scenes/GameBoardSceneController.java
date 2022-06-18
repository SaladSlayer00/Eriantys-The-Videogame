package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.swing.text.Position;
import java.util.*;

/* this is the class that controls the scene of the gameboard
* the paws moves on the gameboard, so this class is pretty important for all the various method
* that are in charge of the moves of the paws/ professors/ these things...
 */
public class GameBoardSceneController extends ViewObservable implements BasicSceneController {
   private Gameboard reducedGameBoard;
   private List<Dashboard> reducedDashboards;
   private List<ImageView> movableStudents;
   private int currentDashboard;
   private List<ImageView> hallList;
   private List<TilePane> rows;
   private String playerNickname;
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
   private Button previousDashBoardButton;
   @FXML
   private Button nextDashBoardButton;


    public GameBoardSceneController(String playerNickname){
     reducedDashboards = new ArrayList<>();
     movableStudents = new ArrayList<>();
     rows = new ArrayList<>();
     currentDashboard = 0;
     this.playerNickname = playerNickname;
     hallList = new ArrayList<>();
     greenRow = new TilePane();
     redRow = new TilePane();
     yellowRow = new TilePane();
     pinkRow = new TilePane();
     blueRow = new TilePane();
     rows.add(greenRow);
     rows.add(redRow);
     rows.add(yellowRow);
     rows.add(pinkRow);
     rows.add(blueRow);

    }


    public void initialize() {
      updateDashBoard(reducedDashboards.get(0));
      previousDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousDashBoardButtonClicked);
      nextDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextDashBoardButtonClicked);

    }
    public void setGameBoard(Gameboard gameboard) {
        reducedGameBoard= gameboard;

    }

    public void setDashboards(List<Dashboard> dashboards) {
        reducedDashboards = dashboards;
    }

    public void updateDashBoard(Dashboard dashboard){
      reducedHall.getChildren().clear();
      towersSpot.getChildren().clear();
      hallList.clear();
      Dashboard selectedDashBoard = dashboard;
      int numberOfTowers = selectedDashBoard.getNumTowers();
      //hall
      for (Student student : selectedDashBoard.getHall()) {
       Color studentColor = student.getColor();
       Image studentInTheHall = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + studentColor.toString() + ".png"));
       ImageView studentImage = new ImageView(studentInTheHall);
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
   Platform.runLater(()-> {
    updateDashBoard (reducedDashboards.get(currentDashboard));
   });

 }

 //handling the clicks on the button of the next mage
 private void onNextDashBoardButtonClicked(Event mouseEvent){
  if(currentDashboard < reducedDashboards.size() - 1){
   currentDashboard++;
   previousDashBoardButton.setDisable(false);
  }
  couldItBeDisabled(nextDashBoardButton, reducedDashboards.size() - 1);
  Platform.runLater(()-> {
   updateDashBoard (reducedDashboards.get(currentDashboard));
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
            for(ImageView imageView : hallList){
                imageView.setDisable(true);
                imageView.setOpacity(0.5);
            }
            for(TilePane tilePane :rows){
                tilePane.setDisable(true);
            }


        }else{
            for(ImageView imageView : hallList){
                imageView.setDisable(false);
            }
            for(TilePane tilePane :rows){
                tilePane.setDisable(false);
            }

        }
 }


}
