package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import javax.swing.text.Position;
import java.util.*;

/* this is the class that controls the scene of the gameboard
* the paws moves on the gameboard, so this class is pretty important for all the various method
* that are in charge of the moves of the paws/ professors/ these things...
 */
public class GameBoardSceneController extends ViewObservable implements BasicSceneController {
    private Gameboard gameboard;
    private List<Dashboard> dashboards;
    private List<AnchorPane> listOfIsland;
    private List<HashMap> reducedGameBoard;
    private Map<AnchorPane,List<ImageView>> islandAndStudents;
    @FXML
    private AnchorPane anchorPaneIslands;

    public GameBoardSceneController(){
        this.dashboards = new ArrayList<>();
        listOfIsland = new ArrayList<>();
        reducedGameBoard = new ArrayList<>();
        anchorPaneIslands = new AnchorPane();
        islandAndStudents = new HashMap<>();
        for(Node node :anchorPaneIslands.getChildren()){
            listOfIsland.add((AnchorPane) node);
        }
        for(Island island :gameboard.getIslands()){
            HashMap<Color, Integer> tempIsland = new HashMap<Color,Integer>(){{
                put(Color.RED,0);
                put(Color.YELLOW,0);
                put(Color.BLUE,0);
                put(Color.GREEN,0);
                put(Color.PINK,0);
            }};
            reducedGameBoard.add(tempIsland);
        }
        for(AnchorPane anchorPane:listOfIsland){
            List<ImageView> tempImageView = new ArrayList<>();
            for(Node node:anchorPane.getChildren()){
                tempImageView.add((ImageView) node);
            }
            islandAndStudents.put(anchorPane,tempImageView);

        }

    }

    public void initialize(){

    }
    public void setGameBoard(Gameboard gameboard) {
        this.gameboard = gameboard;
    }

    public void setDashboards(List<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }
}
