package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
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
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import it.polimi.ingsw.model.Player;

import java.util.*;

import static java.lang.Math.abs;

/* this is the class that controls the scene of the gameboard
 * the paws moves on the gameboard, so this class is pretty important for all the various method
 * that are in charge of the moves of the paws/ professors/ these things...
 */
public class GameBoardSceneController extends ViewObservable implements BasicSceneController {

    private List<Dashboard> reducedDashboards;
    private List<GuiStudent> hallList;
    private List<Player> listOfPlayer;
    private List<TilePane> listOfIslands;
    private int currentDashboard;
    private int possibleMotherNatureMoves;
    private Gameboard reducedGameBoard;
    private Dashboard reducedDashBoard;
    private String playerNickname;
    private GuiStudent chosenStudent;
    private PhaseType mainPhase;
    private PhaseType secondaryPhase;
    //dashBoard
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
    //gameBoard
    @FXML
    private AnchorPane archipelago;
    @FXML
    private TilePane islandOne;
    @FXML
    private TilePane islandTwo;
    @FXML
    private TilePane islandThree;
    @FXML
    private TilePane islandFour;
    @FXML
    private TilePane islandFive;
    @FXML
    private TilePane islandSix;
    @FXML
    private TilePane islandSeven;
    @FXML
    private TilePane islandEight;
    @FXML
    private TilePane islandNine;
    @FXML
    private TilePane islandTen;
    @FXML
    private TilePane islandEleven;
    @FXML
    private TilePane islandTwelve;
    //professors
    @FXML
    private ImageView redProfessor;
    @FXML
    private ImageView greenProfessor;
    @FXML
    private ImageView yellowProfessor;
    @FXML
    private ImageView blueProfessor;
    @FXML
    private ImageView pinkProfessor;
    //buttons
    @FXML
    private Button previousDashBoardButton;
    @FXML
    private Button nextDashBoardButton;
    @FXML
    private Button useExpert;
    @FXML
    private Label numberOfCoin;
    @FXML
    private AnchorPane expertSection;
    //Labels
    @FXML
    private Label currentPlayer;



    public GameBoardSceneController(String playerNickname){
        currentDashboard = 0;
        this.playerNickname = playerNickname;
        mainPhase = PhaseType.WAITING;
        secondaryPhase = PhaseType.WAITING;
        reducedDashboards = new ArrayList<>();
        hallList = new ArrayList<>();
        listOfPlayer = new ArrayList<>();
        listOfIslands = new ArrayList<>();
        greenRow = new TilePane();
        redRow = new TilePane();
        yellowRow = new TilePane();
        pinkRow = new TilePane();
        blueRow = new TilePane();
        theChosenOne = new TilePane();
        towersSpot = new TilePane();
        reducedHall = new TilePane();
        archipelago = new AnchorPane();
        islandOne = new TilePane();
        islandTwo = new TilePane();
        islandThree = new TilePane();
        islandFour = new TilePane();
        islandFive = new TilePane();
        islandSix = new TilePane();
        islandSeven = new TilePane();
        islandEight = new TilePane();
        islandNine = new TilePane();
        islandTen = new TilePane();
        islandEleven = new TilePane();
        islandTwelve = new TilePane();
        greenProfessor = new ImageView();
        yellowProfessor = new ImageView();
        redProfessor = new ImageView();
        blueProfessor = new ImageView();
        greenProfessor = new ImageView();
        pinkProfessor = new ImageView();
        numberOfCoin = new Label();
        expertSection = new AnchorPane();
        currentPlayer = new Label();
    }


    public void initialize() throws noTowerException {
        updateAll();
        displayExpertSection();
        previousDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousDashBoardButtonClicked);
        nextDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextDashBoardButtonClicked);
        reducedHall.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onStudentClicked);
        greenRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onGreenRowClicked());
        redRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onRedRowClicked());
        yellowRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onYellowRowClicked());
        pinkRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onPinkRowClicked());
        blueRow.addEventHandler(MouseEvent.MOUSE_CLICKED,event->onBlueRowClicked());
        archipelago.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onIslandClicked);
        useExpert.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onUseExpertClicked);
        couldItBeDisabled(previousDashBoardButton, 0);
        couldItBeDisabled(nextDashBoardButton, reducedDashboards.size() - 1);



    }
    public void setGameBoard(Gameboard gameboard,List<Dashboard> dashboards,List<Player> players) {
        reducedGameBoard= gameboard;
        reducedDashboards = dashboards;
        reducedDashBoard = getYourDashBoard();
        listOfPlayer = players;


    }
    public void updateAll() throws noTowerException {
        updateDashBoard();
        updateGameBoard();
        setDisabledItems();
    }


    private void updateDashBoard(){
        clearDashBoard();
        Dashboard selectedDashBoard = reducedDashBoard;
        int numberOfTowers = selectedDashBoard.getNumTowers();
        //hall
        for (Student student : selectedDashBoard.getHall()) {
            GuiStudent studentImage = addGuiStudent(student);
            hallList.add(studentImage);
            studentImage.setFitHeight(24);
            studentImage.setFitWidth(24);
            reducedHall.getChildren().add(studentImage);
        }
        //towers
        Type colorOfTower = selectedDashBoard.getTeam();
        for (int i = 0; i < numberOfTowers; i++) {
            Image tower = new Image(getClass().getResourceAsStream("/images/towers/" + colorOfTower.toString() + "_tower.png"));
            ImageView addedTower = new ImageView(tower);
            addedTower.setFitWidth(30);
            addedTower.setFitHeight(30);
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
        //professors
        if(getPlayer(selectedDashBoard).getProfessors().contains(Color.GREEN)){
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.GREEN.toString() + ".png"));
             greenProfessor.setFitHeight(36);
             greenProfessor.setFitHeight(36);
             greenProfessor.setImage(image);

        }
        if(getPlayer(selectedDashBoard).getProfessors().contains(Color.RED)){
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.RED.toString() + ".png"));
            redProfessor.setFitHeight(36);
            redProfessor.setFitHeight(36);
            redProfessor.setImage(image);

        }
        if(getPlayer(selectedDashBoard).getProfessors().contains(Color.YELLOW)){
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.YELLOW.toString() + ".png"));
            yellowProfessor.setFitHeight(36);
            yellowProfessor.setFitHeight(36);
            yellowProfessor.setImage(image);

        }
        if(getPlayer(selectedDashBoard).getProfessors().contains(Color.PINK)){
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.PINK.toString() + ".png"));
            pinkProfessor.setFitHeight(36);
            pinkProfessor.setFitHeight(36);
            pinkProfessor.setImage(image);

        }
        if(getPlayer(selectedDashBoard).getProfessors().contains(Color.BLUE)){
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.BLUE.toString() + ".png"));
            blueProfessor.setFitHeight(36);
            blueProfessor.setFitHeight(36);
            blueProfessor.setImage(image);

        }
        currentPlayer.setText(selectedDashBoard.getOwner());
        checkOwnership(selectedDashBoard);
    }



    public void updateGameBoard() throws noTowerException {
        clearArchipelago();
        int index = 0;
        for (Island island : reducedGameBoard.getIslands()) {
            TilePane selectedIsland = (TilePane) archipelago.getChildren().get(index);
            for (Color color : island.getStudents().keySet()) {
                for (Student student : island.getStudents().get(color)) {
                    GuiStudent studentImage = addGuiStudent(student);
                    selectedIsland.getChildren().add(studentImage);
                }
            }
            if(island.isMotherNature()){
                Image image = new Image(getClass().getResourceAsStream("/images/pawn/mother_nature.png"));
                ImageView motherNature = new ImageView(image);
                motherNature.setFitWidth(20);
                motherNature.setFitHeight(20);
                selectedIsland.getChildren().add(motherNature);
            }
            if(island.getTower()){
                Image image = new Image(getClass().getResourceAsStream("/images/towers/"+island.getTeam().toString()+"_tower.png"));
                ImageView tower = new ImageView(image);
                tower.setFitWidth(20);
                tower.setFitHeight(20);
                selectedIsland.getChildren().add(tower);
            }

            if (island.isBlocked()){
                Image image = new Image(getClass().getResourceAsStream("/images/gameboard/deny_island_icon.png"));
                ImageView denyIslandIcon = new ImageView(image);
                denyIslandIcon.setFitHeight(20);
                denyIslandIcon.setFitWidth(20);
                selectedIsland.getChildren().add(denyIslandIcon);
            }

            index++;
        }
        mergeIslands();
        numberOfCoin.setText("coins :" +getPlayer(getYourDashBoard()).getCoins());


    }

    private void onPreviousDashBoardButtonClicked(Event mouseEvent)  {
        if(currentDashboard > 0){
            currentDashboard--;
            nextDashBoardButton.setDisable(false);
        }
        couldItBeDisabled(previousDashBoardButton, 0);
        reducedDashBoard = reducedDashboards.get(currentDashboard);
        Platform.runLater(()-> {
            try {
                updateAll();
            } catch (noTowerException e) {
                e.printStackTrace();
            }
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
            try {
                updateAll();
            } catch (noTowerException e) {
                e.printStackTrace();
            }
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
        setDisabledItems();
        if(!dashboard.getOwner().equals(playerNickname)){
            for(GuiStudent guiStudent: hallList){
                guiStudent.setDisable(true);
                guiStudent.setOpacity(0.75);
            }
            reducedHall.setDisable(true);
            disabledRows();
            towersSpot.setOpacity(0.75);
            redRow.setOpacity(0.75);
            greenRow.setOpacity(0.75);
            blueRow.setOpacity(0.75);
            yellowRow.setOpacity(0.75);
            pinkRow.setOpacity(0.75);
            redProfessor.setOpacity(0.75);
            greenProfessor.setOpacity(0.75);
            yellowProfessor.setOpacity(0.75);
            pinkProfessor.setOpacity(0.75);
            blueProfessor.setOpacity(0.75);
        }else{
            for(GuiStudent guiStudent : hallList){
                guiStudent.setDisable(false);
            }
            reducedHall.setDisable(false);
            enabledRows();
            towersSpot.setOpacity(1);
            redRow.setOpacity(1);
            greenRow.setOpacity(1);
            blueRow.setOpacity(1);
            yellowRow.setOpacity(1);
            pinkRow.setOpacity(1);
            redProfessor.setOpacity(1);
            greenProfessor.setOpacity(1);
            yellowProfessor.setOpacity(1);
            pinkProfessor.setOpacity(1);
            blueProfessor.setOpacity(1);

        }

    }
    private void onStudentClicked(MouseEvent event){
        Dashboard playerDashBoard = getYourDashBoard();
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if(clickedNode instanceof  GuiStudent) {
            chosenStudent = (GuiStudent) clickedNode;
            theChosenOne.getChildren().add(chosenStudent);
            playerDashBoard.getHall().remove(chosenStudent.getStudent());
            secondaryPhase = PhaseType.MOVE_ON_ISLAND_ROW;
            setDisabledItems();
            Platform.runLater(() -> SceneController.alertShown("Message:", "Please, choose where do you want to move your students! "+ chosenStudent.getStudent().getColor().toString()));

        }
    }

    private void onIslandClicked(MouseEvent event){
        Node clickedNode = event.getPickResult().getIntersectedNode();
        TilePane chosenIsland;
        int index = 0;
        int indexOfIsland;
        switch (secondaryPhase) {
            case MOVE_ON_ISLAND_ROW:
                if(clickedNode instanceof TilePane) {
                    chosenIsland = (TilePane) clickedNode;
                    indexOfIsland = getIslandIndex(chosenIsland);
                    Color studentColor = chosenStudent.getStudent().getColor();
                    chosenIsland.getChildren().add(chosenStudent);
                    theChosenOne.getChildren().remove(chosenStudent);
                    setDisabledItems();
                    chosenStudent = null;
                    new Thread(() -> notifyObserver(obs -> obs.OnUpdateMoveOnIsland(studentColor, indexOfIsland, reducedGameBoard.getIslands()))).start();}
                break;
            case MOVE_MOTHER:
                int possibleMoves = getPlayer(getYourDashBoard()).getCardChosen().getMove();
                if(clickedNode instanceof TilePane) {
                    chosenIsland = (TilePane) clickedNode;
                    if((possibleMotherNatureMoves+reducedGameBoard.getMotherNature())> (reducedGameBoard.getIslands().size()-1)){
                        if(getIslandIndex(chosenIsland)<reducedGameBoard.getMotherNature()){
                            index = (reducedGameBoard.getIslands().size()-reducedGameBoard.getMotherNature())+getIslandIndex(chosenIsland);
                        }else{
                            index = getIslandIndex(chosenIsland) - reducedGameBoard.getMotherNature();
                    }
                    }else{
                        index = getIslandIndex(chosenIsland) - reducedGameBoard.getMotherNature();
                    }

                    if (index < 1 || index > possibleMotherNatureMoves) {
                        Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right island"));
                    } else {
                        mainPhase = PhaseType.WAITING;
                        setDisabledItems();
                        disableGlowEffectIsland(possibleMotherNatureMoves);
                        int finalIndex = index;
                        new Thread(() ->notifyObserver(obs -> obs.OnUpdateMoveMother(finalIndex,new Assistant(0,possibleMoves)))).start();
                    }
                }
                break;
        }
    }

    private void onUseExpertClicked(MouseEvent event){
        ExpertCardsSceneController eCSController = new ExpertCardsSceneController(reducedGameBoard,this);
        eCSController.addAllObservers(observers);
        Platform.runLater(()->SceneController.changeRootPane(eCSController,"expert_choice.fxml"));

    }
    public Dashboard getYourDashBoard(){
        for(Dashboard dashboard:reducedDashboards){
            if(dashboard.getOwner().equals(playerNickname))
                return dashboard;
        }
        return null;
    }

    public Gameboard getReducedGameBoard(){
        return reducedGameBoard;
    }

    private int getIslandIndex(TilePane island){
        for(int i = 0; i<archipelago.getChildren().size();i++)
        {
            if(archipelago.getChildren().get(i).equals(island))
                return i;
        }
        System.out.println("non trovata");
        return 0;
    }

    public Player getPlayer(Dashboard dashboard){
        for(Player player :listOfPlayer){
            if(player.getName().equals(dashboard.getOwner()))
                return player;
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
        studentImage.setFitWidth(23);
        studentImage.setFitHeight(23);
        return studentImage;
    }

    public void setDisabledItems(){
        if(mainPhase.equals(PhaseType.WAITING)){
            for(GuiStudent guiStudent: hallList){
                guiStudent.setDisable(true);
            }
            reducedHall.setDisable(true);
            archipelago.setDisable(true);
            useExpert.setDisable(true);
            disabledRows();
        }else if(mainPhase.equals(PhaseType.YOUR_MOVE)&&secondaryPhase.equals(PhaseType.MOVE_ON_ISLAND_ROW)){
            for(GuiStudent guiStudent: hallList){
                guiStudent.setDisable(true);
            }
            reducedHall.setDisable(true);
            enabledRows();
            archipelago.setDisable(false);
            useExpert.setDisable(true);
        }else if(mainPhase.equals(PhaseType.YOUR_MOVE)&&secondaryPhase.equals(PhaseType.MOVE_STUDENT)){
            for(GuiStudent guiStudent: hallList){
                guiStudent.setDisable(false);
            }
            reducedHall.setDisable(false);
            disabledRows();
            archipelago.setDisable(true);
            useExpert.setDisable(false);
        }else if(mainPhase.equals(PhaseType.YOUR_MOVE)&&secondaryPhase.equals(PhaseType.MOVE_MOTHER)){
            for(GuiStudent guiStudent: hallList){
                guiStudent.setDisable(true);
            }
            reducedHall.setDisable(true);
            disabledRows();
            archipelago.setDisable(false);
            useExpert.setDisable(true);
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
        greenProfessor.setImage(null);
        redProfessor.setImage(null);
        yellowProfessor.setImage(null);
        pinkProfessor.setImage(null);
        blueProfessor.setImage(null);
    }

    private void clearArchipelago(){
        islandOne.getChildren().clear();
        islandTwo.getChildren().clear();
        islandThree.getChildren().clear();
        islandFour.getChildren().clear();
        islandFive.getChildren().clear();
        islandSix.getChildren().clear();
        islandSeven.getChildren().clear();
        islandEight.getChildren().clear();
        islandNine.getChildren().clear();
        islandTen.getChildren().clear();
        islandEleven.getChildren().clear();
        islandTwelve.getChildren().clear();

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

    private void mergeIslands(){
        int numberOfIsland = reducedGameBoard.getIslands().size();
        if(numberOfIsland<12){
            for(int i = numberOfIsland; i<archipelago.getChildren().size();i++){
                archipelago.getChildren().get(numberOfIsland).setVisible(false);
                archipelago.getChildren().get(numberOfIsland).setDisable(true);
            }
        }
    }

    public void enabledGlowEffectIsland(int possibleMoves) {
        Glow glow = new Glow();
        possibleMotherNatureMoves = possibleMoves;
        for(int i = 0; i < possibleMoves;i++){
                int index = (reducedGameBoard.getMotherNature()+i+1) % reducedGameBoard.getIslands().size();
                archipelago.getChildren().get(index).setEffect(glow);


        }
    }

    private void disableGlowEffectIsland(int possibleMoves) {
        for (int i = 0; i < possibleMoves; i++) {
            int index = (reducedGameBoard.getMotherNature()+i+1) % reducedGameBoard.getIslands().size();
            archipelago.getChildren().get(index).setEffect(null);
        }
    }
    public List<Player> getListOfPlayer(){
        return listOfPlayer;
    }

    private void displayExpertSection(){
        if(!(reducedGameBoard.getExperts().size()>0)){
            expertSection.setDisable(true);
            expertSection.setVisible(false);
        }

    }

    public List<Dashboard> getReducedDashboards(){
        return reducedDashboards;
    }

}

