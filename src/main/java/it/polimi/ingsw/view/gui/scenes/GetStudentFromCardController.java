package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.model.expertDeck.ExchangeStudentsCard;
import it.polimi.ingsw.model.expertDeck.OneMoreStudentCard;
import it.polimi.ingsw.model.expertDeck.ToIslandCard;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.List;

public class GetStudentFromCardController extends ViewObservable implements BasicSceneController {

    private List<Dashboard> reducedDashboards;
    private List<GuiStudent> hallList;
    private List<Player> listOfPlayer;
    private List<TilePane> listOfIslands;
    private int currentDashboard;
    private int currentIslandIndex;
    private Gameboard reducedGameBoard;
    private Dashboard reducedDashBoard;
    private String playerNickname;
    private GuiStudent chosenStudent;
    private ExpertDeck expertDeck;
    private ExpertDeckPhaseType phase;
    private GameBoardSceneController currentGameBoardSceneController;
    //dashBoard
    @FXML
    private AnchorPane dashBoard;
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
    //archipelago
    @FXML
    private TilePane currentIsland;
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
    private Button nextIslandButton;
    @FXML
    private Button previousIslandButton;
    @FXML
    private Button nextDashBoardButton;
    @FXML
    private Button previousDashBoardButton;
    //expert card
    @FXML
    private TilePane expertStudents;
    @FXML
    private Label title;
    @FXML
    private ImageView chosenExpert;

    public GetStudentFromCardController(String playerNickname,GameBoardSceneController gBSC) {
        currentDashboard = 0;
        currentIslandIndex = 0;
        this.playerNickname = playerNickname;
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
        dashBoard = new AnchorPane();
        greenProfessor = new ImageView();
        yellowProfessor = new ImageView();
        redProfessor = new ImageView();
        blueProfessor = new ImageView();
        greenProfessor = new ImageView();
        pinkProfessor = new ImageView();
        currentIsland = new TilePane();
        expertStudents = new TilePane();
        chosenExpert = new ImageView();
        title = new Label();
        phase = ExpertDeckPhaseType.IDLE;
        currentGameBoardSceneController = gBSC;


    }

    public void initialize() {
        updateDashBoard();
        updateIsland();
        updateExpertStudents();
        setQuestion();
        setExpertImage();
        setDisabledItems();
        previousDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousDashBoardButtonClicked);
        nextDashBoardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextDashBoardButtonClicked);
        expertStudents.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onExpertStudentClicked);
        reducedHall.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStudentClicked);
        greenRow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onGreenRowClicked());
        redRow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onRedRowClicked());
        yellowRow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onYellowRowClicked());
        pinkRow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onPinkRowClicked());
        blueRow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onBlueRowClicked());
        couldItBeDisabled(previousIslandButton, 0);
        couldItBeDisabled(nextIslandButton, reducedGameBoard.getIslands().size() - 1);
        couldItBeDisabled(previousDashBoardButton, 0);
        couldItBeDisabled(nextDashBoardButton, reducedDashboards.size() - 1);
        previousIslandButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPreviousIslandButtonClicked);
        nextIslandButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextIslandButtonClicked);
        currentIsland.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onIslandClicked);
    }

    public void updateAll() throws noTowerException {
        updateDashBoard();
        updateExpertStudents();
        setDisabledItems();
    }


    public void setGameBoard(Gameboard gameboard, List<Dashboard> dashboards, List<Player> players) {
        reducedGameBoard = gameboard;
        reducedDashboards = dashboards;
        reducedDashBoard = getYourDashBoard();
        listOfPlayer = players;

    }
    public void setExpertDeck(ExpertDeck selectedExpert){
        expertDeck = selectedExpert;
    }

    public Dashboard getYourDashBoard() {
        for (Dashboard dashboard : reducedDashboards) {
            if (dashboard.getOwner().equals(playerNickname))
                return dashboard;
        }
        return null;
    }

    public void setExpertImage(){
        Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front_" +expertDeck.getText()+".jpg"));
        chosenExpert.setImage(image);
    }

    public void setPhase(ExpertDeckPhaseType newPhase){
        phase = newPhase;
    }

    private void onPreviousDashBoardButtonClicked(Event mouseEvent) {
        if (currentDashboard > 0) {
            currentDashboard--;
            nextDashBoardButton.setDisable(false);
        }
        couldItBeDisabledDashBoard(previousDashBoardButton, 0);
        reducedDashBoard = reducedDashboards.get(currentDashboard);
        Platform.runLater(() -> {
            updateDashBoard();
        });

    }

    private void onNextDashBoardButtonClicked(Event mouseEvent) {
        if (currentDashboard < reducedDashboards.size() - 1) {
            currentDashboard++;
            previousDashBoardButton.setDisable(false);
        }
        couldItBeDisabledDashBoard(nextDashBoardButton, reducedDashboards.size() - 1);
        reducedDashBoard = reducedDashboards.get(currentDashboard);
        Platform.runLater(() -> {
            updateDashBoard();
        });


    }

    private boolean couldItBeDisabledDashBoard(Button button, int index) {
        if (currentDashboard == index) {
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }


    private boolean couldItBeDisabled(Button button, int index){
        if(currentIslandIndex== index){
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    private void onPreviousIslandButtonClicked(Event mouseEvent) {
        if(currentIslandIndex > 0){
            currentIslandIndex--;
            nextIslandButton.setDisable(false);
        }
        couldItBeDisabled(previousIslandButton, 0);
        updateIsland();
    }

    //handling the clicks on the button of the next mage
    private void onNextIslandButtonClicked(Event mouseEvent) {
        if(currentIslandIndex < reducedGameBoard.getIslands().size() - 1){
            currentIslandIndex++;
            previousIslandButton.setDisable(false);
        }
        couldItBeDisabled(nextIslandButton, reducedGameBoard.getIslands().size() - 1);
        updateIsland();
    }

    private void updateDashBoard() {
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
        for (Student student : selectedDashBoard.getRow(Color.GREEN).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            greenRow.getChildren().add(studentImage);
        }

        for (Student student : selectedDashBoard.getRow(Color.RED).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            redRow.getChildren().add(studentImage);
        }
        for (Student student : selectedDashBoard.getRow(Color.YELLOW).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            yellowRow.getChildren().add(studentImage);
        }
        for (Student student : selectedDashBoard.getRow(Color.PINK).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            pinkRow.getChildren().add(studentImage);
        }
        for (Student student : selectedDashBoard.getRow(Color.BLUE).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            blueRow.getChildren().add(studentImage);
        }
        //professors
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.GREEN)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.GREEN.toString() + ".png"));
            greenProfessor.setFitHeight(36);
            greenProfessor.setFitHeight(40);
            greenProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.RED)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.RED.toString() + ".png"));
            redProfessor.setFitHeight(36);
            redProfessor.setFitHeight(40);
            redProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.YELLOW)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.YELLOW.toString() + ".png"));
            yellowProfessor.setFitHeight(36);
            yellowProfessor.setFitHeight(40);
            yellowProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.PINK)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.PINK.toString() + ".png"));
            pinkProfessor.setFitHeight(36);
            pinkProfessor.setFitHeight(40);
            pinkProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.BLUE)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.BLUE.toString() + ".png"));
            blueProfessor.setFitHeight(36);
            blueProfessor.setFitHeight(40);
            blueProfessor.setImage(image);

        }

        checkOwnership(selectedDashBoard);
    }

    private void updateIsland()  {
        currentIsland.getChildren().clear();
        for (Color color : reducedGameBoard.getIslands().get(currentIslandIndex).getStudents().keySet()) {
            for (Student student : reducedGameBoard.getIslands().get(currentIslandIndex).getStudents().get(color)) {
                GuiStudent studentImage = addGuiStudent(student);
                currentIsland.getChildren().add(studentImage);
            }
        }
        if(reducedGameBoard.getIslands().get(currentIslandIndex).isMotherNature()){
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/mother_nature.png"));
            ImageView motherNature = new ImageView(image);
            motherNature.setFitWidth(45);
            motherNature.setFitHeight(45);
            currentIsland.getChildren().add(motherNature);
        }
        if(reducedGameBoard.getIslands().get(currentIslandIndex).getTower()){
            try {
                Image image = new Image(getClass().getResourceAsStream("/images/towers/"+reducedGameBoard.getIslands().get(currentIslandIndex).getTeam().toString()+"_tower.png"));
                ImageView tower = new ImageView(image);
                tower.setFitWidth(45);
                tower.setFitHeight(45);
                currentIsland.getChildren().add(tower);
            }catch (noTowerException e){}
        }
        if(reducedGameBoard.getIslands().get(currentIslandIndex).isBlocked()){
            Image image = new Image(getClass().getResourceAsStream("/images/gameboard/deny_island_icon.png"));
            ImageView denyIslandIcon = new ImageView(image);
            denyIslandIcon.setFitWidth(45);
            denyIslandIcon.setFitHeight(45);
            currentIsland.getChildren().add(denyIslandIcon);
        }
    }

    private void updateExpertStudents() {
        expertStudents.getChildren().clear();
        switch (expertDeck) {
            case TAVERNER:
                ToIslandCard taverner = (ToIslandCard) getCharacter(expertDeck);
                for (Student student : taverner.getStudents()) {
                    GuiStudent studentImage = addGuiStudent(student);
                    studentImage.setFitHeight(68);
                    studentImage.setFitWidth(68);
                    expertStudents.getChildren().add(studentImage);
                }
                    break;
            case JOKER:
                ExchangeStudentsCard joker = (ExchangeStudentsCard) getCharacter(expertDeck);
                for (Student student : joker.getStudents()) {
                    GuiStudent studentImage = addGuiStudent(student);
                    studentImage.setFitHeight(68);
                    studentImage.setFitWidth(68);
                    expertStudents.getChildren().add(studentImage);
                }
                    break;
            case BARBARIAN:
                OneMoreStudentCard barbarian = (OneMoreStudentCard) getCharacter(expertDeck);
                for (Student student : barbarian.getStudents()) {
                    GuiStudent studentImage = addGuiStudent(student);
                    studentImage.setFitHeight(68);
                    studentImage.setFitWidth(68);
                    expertStudents.getChildren().add(studentImage);
                }
                    break;
                }
}


    private GuiStudent addGuiStudent(Student student) {
        Image studentInTheHall = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + student.getColor().toString() + ".png"));
        GuiStudent studentImage = new GuiStudent(student);
        studentImage.setImage(studentInTheHall);
        studentImage.setFitWidth(30);
        studentImage.setFitHeight(36);
        return studentImage;
    }

    private Player getPlayer(Dashboard dashboard) {
        for (Player player : listOfPlayer) {
            if (player.getName().equals(dashboard.getOwner()))
                return player;
        }
        return null;
    }

    private void checkOwnership(Dashboard dashboard) {
        setDisabledItems();
        if (!dashboard.getOwner().equals(playerNickname)) {
            for (GuiStudent guiStudent : hallList) {
                guiStudent.setDisable(true);
                guiStudent.setOpacity(0.5);
            }
            reducedHall.setDisable(true);
            disabledRows();


        } else {
            for (GuiStudent guiStudent : hallList) {
                guiStudent.setDisable(false);
            }
            reducedHall.setDisable(false);
            enabledRows();

        }


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

    private void onStudentClicked(MouseEvent event){
        Dashboard playerDashBoard = getYourDashBoard();
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if(clickedNode instanceof  GuiStudent) {
            chosenStudent = (GuiStudent) clickedNode;
            theChosenOne.getChildren().add(chosenStudent);
            playerDashBoard.getHall().remove(chosenStudent.getStudent());
            if(expertDeck.equals(ExpertDeck.JOKER)){
                new Thread(()->notifyObserver(obs->obs.OnUpdateEffectJoker(chosenStudent.getStudent().getColor()))).start();
            }else if(expertDeck.equals(ExpertDeck.MUSICIAN)){
                new Thread(()->notifyObserver(obs->obs.OnUpdateEffectMusician(chosenStudent.getStudent().getColor()))).start();
            }
        }

    }
    private void onExpertStudentClicked(MouseEvent event){
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if(clickedNode instanceof  GuiStudent) {
            chosenStudent = (GuiStudent) clickedNode;
            theChosenOne.getChildren().add(chosenStudent);
            if(expertDeck.equals(ExpertDeck.JOKER)){
                new Thread(()->notifyObserver(obs->obs.OnUpdateEffectJoker(chosenStudent.getStudent().getColor()))).start();
            }else if(expertDeck.equals(ExpertDeck.TAVERNER)){
                new Thread(()->notifyObserver(obs-> obs.OnUpdateEffectTaverner(chosenStudent.getStudent().getColor()))).start();
            }else if(expertDeck.equals(ExpertDeck.BARBARIAN)){
                setPhase(ExpertDeckPhaseType.SELECT_ROW);
                setDisabledItems();
                setQuestion();
                Platform.runLater(() -> SceneController.alertShown("Message:", "Place the student in the dining room"));
            }

        }
    }

    private Character getCharacter(ExpertDeck expertDeck){
        for(Character character : reducedGameBoard.getActiveCards()){
            if(character.getName().equals(expertDeck)){
                return character;
            }
        }
        return null;
    }

    public void setQuestion(){
        if(expertDeck.equals(ExpertDeck.HERALD)){
            title.setText("Please choose the island to calculate influence on");
        }else if(expertDeck.equals(ExpertDeck.HERBALIST)){
            title.setText("Please choose the island to ban");
        }else if(expertDeck.equals(ExpertDeck.TAVERNER)){
            if(phase.equals(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT)) {
                title.setText("Please choose a student from the card ");
            }else{
                title.setText("Please choose the island to put the student on");
            }
        }else if(expertDeck.equals(ExpertDeck.JOKER)){
            if(phase.equals(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT)) {
                title.setText("Please choose a student from the card ");
            }else{
                title.setText("Please choose a student from the hall");
            }
        }else if(expertDeck.equals(ExpertDeck.BARBARIAN)){
                title.setText("Please choose a student from the card ");
        }else if(expertDeck.equals(ExpertDeck.MUSICIAN)){
            if(phase.equals(ExpertDeckPhaseType.SELECT_ROW)) {
                title.setText("Please choose a student from the dining room");
            }else{
                title.setText("Please choose a student from the hall");
            }
        }
    }

    private void onGreenRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.GREEN)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(greenRow);
        }
    }

    private void onRedRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.RED)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(redRow);
        }

    }

    private void onYellowRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.YELLOW)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(yellowRow);
        }
    }

    private void onPinkRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.PINK)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(pinkRow);
        }

    }

    private void onBlueRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.BLUE)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(blueRow);
        }

    }
    private void notifyGameController(TilePane selectedRow){
        if(expertDeck.equals(ExpertDeck.BARBARIAN)){
            try{
                /*
                Color studentColor = chosenStudent.getStudent().getColor();
                chosenStudent.setFitWidth(40);
                chosenStudent.setFitHeight(40);
                selectedRow.getChildren().add(chosenStudent);
                theChosenOne.getChildren().remove(chosenStudent);
                setDisabledItems();
                chosenStudent = null;
                new Thread(() -> notifyObserver(obs -> obs.OnUpdateEffectBarbarian(studentColor))).start();
                 */
                if(selectedRow.equals(redRow)){
                    new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectBarbarian(Color.RED))).start();
                }else if(selectedRow.equals(greenRow)){
                    new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectBarbarian(Color.GREEN))).start();
                }else if(selectedRow.equals(blueRow)){
                    new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectBarbarian(Color.BLUE))).start();
                }else if(selectedRow.equals(yellowRow)){
                    new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectBarbarian(Color.YELLOW))).start();
                }else if(selectedRow.equals(pinkRow)){
                    new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectBarbarian(Color.PINK))).start();
                }
                Platform.runLater(()->SceneController.changeRootPane(currentGameBoardSceneController,"gameboard2_scene.fxml"));
            }catch (NullPointerException e){
                Platform.runLater(() -> SceneController.alertShown("Message:", "Please select a student"));
            }
        }else if(expertDeck.equals(ExpertDeck.MUSICIAN)){
            if(selectedRow.equals(redRow)){
                new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectMusician(Color.RED))).start();
            }else if(selectedRow.equals(greenRow)){
                new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectMusician(Color.GREEN))).start();
            }else if(selectedRow.equals(blueRow)){
                new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectMusician(Color.BLUE))).start();
            }else if(selectedRow.equals(yellowRow)){
                new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectMusician(Color.YELLOW))).start();
            }else if(selectedRow.equals(pinkRow)){
                new Thread(() -> notifyObserver(obs-> obs.OnUpdateEffectMusician(Color.PINK))).start();
            }
        }


    }

    private void onIslandClicked(MouseEvent event){
        setDisabledItems();
        switch(expertDeck){
            case HERALD:
                new Thread(()->notifyObserver(obs -> obs.OnUpdateEffectHerald(currentIslandIndex))).start();
                Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard2_scene.fxml"));
                break;
            case HERBALIST:
                new Thread(() -> notifyObserver(obs->obs.OnUpdateEffectHerbalist(currentIslandIndex))).start();
                Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard2_scene.fxml"));
                break;
            case TAVERNER:
                new Thread(()-> notifyObserver(obs->obs.OnUpdateEffectTaverner(currentIslandIndex))).start();
                Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard2_scene.fxml"));
                break;
        }

    }

   public void setDisabledItems(){
        if(phase.equals(ExpertDeckPhaseType.IDLE)){
            reducedHall.setDisable(true);
            currentIsland.setDisable(true);
            expertStudents.setDisable(true);
            disabledRows();
        }else if(phase.equals(ExpertDeckPhaseType.SELECT_STUDENT_FROM_EXPERT)){
            reducedHall.setDisable(true);
            currentIsland.setDisable(true);
            expertStudents.setDisable(false);
            disabledRows();
        } else if(phase.equals(ExpertDeckPhaseType.SELECT_ISLAND)) {
            reducedHall.setDisable(true);
            currentIsland.setDisable(false);
            expertStudents.setDisable(true);
            disabledRows();
        }else if(phase.equals(ExpertDeckPhaseType.SELECT_STUDENT_FROM_THE_HALL)) {
            reducedHall.setDisable(false);
            currentIsland.setDisable(true);
            expertStudents.setDisable(true);
            disabledRows();
        }else if(phase.equals(ExpertDeckPhaseType.SELECT_ROW)){
            reducedHall.setDisable(true);
            currentIsland.setDisable(true);
            expertStudents.setDisable(true);
            enabledRows();
        }



   }












}
