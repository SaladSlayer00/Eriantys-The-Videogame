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

/**
 * GetStudentFromCardController class to handle the scene that display
 * the choice to get the paws that are on the various expert cards
 * that have some paws on them
 * this scene is displayed just in case of a match played on expert mode
 */
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
    @FXML
    private Label islandIndex;

    /**
     * class constructor
     * @param playerNickname is the nickname of the player that have to make the choice
     * @param gBSC is the gameboardscenecontroller of the actual match
     */
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

    /**
     * this method initializes the class setting all the various parameter to display the scene
     * on the player's screen in the proper way
     */
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

    /**
     * this method updates all the various elements of the gameboard when something happened
     * (such a paw moved during a turn)
     * this metod just calls more specific methods
     * @throws noTowerException in case there are no more towers (end of the match)
     */
    public void updateAll() throws noTowerException {
        updateDashBoard();
        updateExpertStudents();
        setDisabledItems();
    }


    /**
     * setter of the gameboard as it is during that moment in the match
     * @param gameboard is the gameboard that has to be displayed
     * @param dashboards is the list of dashboards that have to be displayed
     * @param players is the list of the players that are playing the match
     */
    public void setGameBoard(Gameboard gameboard, List<Dashboard> dashboards, List<Player> players) {
        reducedGameBoard = gameboard;
        reducedDashboards = dashboards;
        reducedDashBoard = getYourDashBoard();
        listOfPlayer = players;

    }

    /**
     * setter of the expert deck with the cards that are available during that match (which are three randomly chosen from the deck)
     * @param selectedExpert is the deck chosen for the match
     */
    public void setExpertDeck(ExpertDeck selectedExpert){
        expertDeck = selectedExpert;
    }

    /**
     * getter for the dashboard of the player that is playing the turn
     * @return the dashboard of the active player
     */
    public Dashboard getYourDashBoard() {
        for (Dashboard dashboard : reducedDashboards) {
            if (dashboard.getOwner().equals(playerNickname))
                return dashboard;
        }
        return null;
    }

    /**
     * setter for the images of the expert deck
     */
    public void setExpertImage(){
        Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front_" +expertDeck.getText()+".jpg"));
        chosenExpert.setImage(image);

    }

    /**
     * setter for the phase of the turn
     * @param newPhase is the new phase that has to be set as the actual one
     */
    public void setPhase(ExpertDeckPhaseType newPhase){
        phase = newPhase;
    }

    /**
     * this method allows the player to switch dashboards so to look at the dashboards of the other players
     * @param mouseEvent is the input given by the player's mouse
     */
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

    /**
     * this method allows the player to switch dashboards so to look at the dashboards of the other players
     * @param mouseEvent is the input given by the player's mouse
     */
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

    /**
     * this method checks whether a dashboard can be disabled
     * @param button is the button that has to be checked
     * @param index is the index of the actual dashboard displayed
     * @return a boolean variable that tells whether the button may be disabled
     */
    private boolean couldItBeDisabledDashBoard(Button button, int index) {
        if (currentDashboard == index) {
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }


    /**
     * this method checks whether a button can be disabled
     * @param button is the button that has to be checked
     * @param index is the index of the actual dashboard displayed
     * @return a boolean variable that tells whether the button may be disabled
     */
    private boolean couldItBeDisabled(Button button, int index){
        if(currentIslandIndex== index){
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    /**
     * this method allows the player to switch islands so to look at the various islands that can be chosen for
     * applying the effect of the expert card they are playing
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onPreviousIslandButtonClicked(Event mouseEvent) {
        if(currentIslandIndex > 0){
            currentIslandIndex--;
            nextIslandButton.setDisable(false);
        }
        couldItBeDisabled(previousIslandButton, 0);
        updateIsland();
    }

    /**
     * this method handles the clicks on the button of the next mage
     * @param mouseEvent is the input given by the player's mouse
     */
    private void onNextIslandButtonClicked(Event mouseEvent) {
        if(currentIslandIndex < reducedGameBoard.getIslands().size() - 1){
            currentIslandIndex++;
            previousIslandButton.setDisable(false);
        }
        couldItBeDisabled(nextIslandButton, reducedGameBoard.getIslands().size() - 1);
        updateIsland();
    }

    /**
     * this method handles the update of the dashboard after something has happened
     */
    private void updateDashBoard() {
        clearDashBoard();
        Dashboard selectedDashBoard = reducedDashBoard;
        int numberOfTowers = selectedDashBoard.getNumTowers();
        //hall
        for (Student student : selectedDashBoard.getHall()) {
            GuiStudent studentImage = addGuiStudent(student);
            hallList.add(studentImage);
            studentImage.setFitWidth(25);
            studentImage.setFitHeight(25);
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
        for (Student student : selectedDashBoard.getRow(Color.GREEN).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            studentImage.setFitWidth(25);
            studentImage.setFitHeight(25);
            greenRow.getChildren().add(studentImage);
        }

        for (Student student : selectedDashBoard.getRow(Color.RED).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            studentImage.setFitWidth(25);
            studentImage.setFitHeight(25);
            redRow.getChildren().add(studentImage);
        }
        for (Student student : selectedDashBoard.getRow(Color.YELLOW).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            studentImage.setFitWidth(25);
            studentImage.setFitHeight(25);
            yellowRow.getChildren().add(studentImage);
        }
        for (Student student : selectedDashBoard.getRow(Color.PINK).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            studentImage.setFitWidth(25);
            studentImage.setFitHeight(25);
            pinkRow.getChildren().add(studentImage);
        }
        for (Student student : selectedDashBoard.getRow(Color.BLUE).getStudents()) {
            GuiStudent studentImage = addGuiStudent(student);
            studentImage.setFitWidth(25);
            studentImage.setFitHeight(25);
            blueRow.getChildren().add(studentImage);
        }
        //professors
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.GREEN)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.GREEN.toString() + ".png"));
            greenProfessor.setFitHeight(33);
            greenProfessor.setFitHeight(33);
            greenProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.RED)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.RED.toString() + ".png"));
            redProfessor.setFitHeight(33);
            redProfessor.setFitHeight(33);
            redProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.YELLOW)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.YELLOW.toString() + ".png"));
            yellowProfessor.setFitHeight(33);
            yellowProfessor.setFitHeight(33);
            yellowProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.PINK)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.PINK.toString() + ".png"));
            pinkProfessor.setFitHeight(33);
            pinkProfessor.setFitHeight(33);
            pinkProfessor.setImage(image);

        }
        if (getPlayer(selectedDashBoard).getProfessors().contains(Color.BLUE)) {
            Image image = new Image(getClass().getResourceAsStream("/images/pawn/professors/teacher_" + Color.BLUE.toString() + ".png"));
            blueProfessor.setFitHeight(33);
            blueProfessor.setFitHeight(33);
            blueProfessor.setImage(image);

        }

        checkOwnership(selectedDashBoard);
    }

    /**
     * this method handles the update of the islands after something has happened
     */
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
            motherNature.setFitWidth(30);
            motherNature.setFitHeight(30);
            currentIsland.getChildren().add(motherNature);
        }
        if(reducedGameBoard.getIslands().get(currentIslandIndex).getTower()){
            try {
                Image image = new Image(getClass().getResourceAsStream("/images/towers/"+reducedGameBoard.getIslands().get(currentIslandIndex).getTeam().toString()+"_tower.png"));
                ImageView tower = new ImageView(image);
                tower.setFitWidth(30);
                tower.setFitHeight(30);
                currentIsland.getChildren().add(tower);
            }catch (noTowerException e){}
        }
        if(reducedGameBoard.getIslands().get(currentIslandIndex).isBlocked()){
            Image image = new Image(getClass().getResourceAsStream("/images/gameboard/deny_island_icon.png"));
            ImageView denyIslandIcon = new ImageView(image);
            denyIslandIcon.setFitWidth(30);
            denyIslandIcon.setFitHeight(30);
            currentIsland.getChildren().add(denyIslandIcon);
        }

        islandIndex.setText("   Island :" +currentIslandIndex);
    }

    /**
     * this method handles the update of the expert card used after a paw on it has been moved
     */
    private void updateExpertStudents() {
        expertStudents.getChildren().clear();
        switch (expertDeck) {
            case TAVERNER:
                ToIslandCard taverner = (ToIslandCard) getCharacter(expertDeck);
                for (Student student : taverner.getStudents()) {
                    GuiStudent studentImage = addGuiStudent(student);
                    studentImage.setFitHeight(34);
                    studentImage.setFitWidth(34);
                    expertStudents.getChildren().add(studentImage);
                }
                    break;
            case JOKER:
                ExchangeStudentsCard joker = (ExchangeStudentsCard) getCharacter(expertDeck);
                for (Student student : joker.getStudents()) {
                    GuiStudent studentImage = addGuiStudent(student);
                    studentImage.setFitHeight(34);
                    studentImage.setFitWidth(34);
                    expertStudents.getChildren().add(studentImage);
                }
                    break;
            case BARBARIAN:
                OneMoreStudentCard barbarian = (OneMoreStudentCard) getCharacter(expertDeck);
                for (Student student : barbarian.getStudents()) {
                    GuiStudent studentImage = addGuiStudent(student);
                    studentImage.setFitHeight(34);
                    studentImage.setFitWidth(34);
                    expertStudents.getChildren().add(studentImage);
                }
                    break;
                }
}

    /**
     * this method add a gui student (the image) on the screen
     * @param student is the student that has to be added
     * @return the gui student (the image on the screen)
     */
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

    /**
     * this method check whom students own the dashboard that is displayed on the screen at the moment
     * @param dashboard is the dashboard that is displayed
     */
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


    /**
     * this method disabled the rows of a dashboard so that they cannot be modified by the player
     */
    private void disabledRows(){
        blueRow.setDisable(true);
        greenRow.setDisable(true);
        redRow.setDisable(true);
        yellowRow.setDisable(true);
        pinkRow.setDisable(true);
    }

    /**
     * this method enables the various rows of a dashboard so that the player can interact with them
     * during thier turn
     */
    private void enabledRows(){
        blueRow.setDisable(false);
        greenRow.setDisable(false);
        redRow.setDisable(false);
        yellowRow.setDisable(false);
        pinkRow.setDisable(false);
    }

    /**
     * this method clears the dashboard removing the various items that were on it
     */
    private void clearDashBoard(){
        theChosenOne.getChildren().clear();
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

    /**
     * this method handles the clicks on a student's paw when the player clicks it during their turn
     * @param event is the input given by the player's mouse
     */
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

    /**
     * this method handles the clicks on a student's paw on an expert card when the player clicks it
     * @param event is the input given by the player's mouse
     */
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

    /**
     * setter method for the question to ask the player
     * this message is different from each card of the expert deck
     */
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

    /**
     * method that handles the clicks on the row for the green paws
     * if the row is the right one the method works properly,
     * else it shows a message to retry the operation in the right place
     */
    private void onGreenRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.GREEN)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(greenRow);
        }
    }

    /**
     * method that handles the clicks on the row for the red paws
     * if the row is the right one the method works properly,
     * else it shows a message to retry the operation in the right place
     */
    private void onRedRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.RED)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(redRow);
        }

    }

    /**
     * method that handles the clicks on the row for the yellow paws
     *if the row is the right one the method works properly,
     *else it shows a message to retry the operation in the right place
     */
    private void onYellowRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.YELLOW)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(yellowRow);
        }
    }

    /**
     * method that handles the clicks on the row for the pink paws
     * if the row is the right one the method works properly,
     *else it shows a message to retry the operation in the right place
     */
    private void onPinkRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.PINK)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(pinkRow);
        }

    }

    /**
     * method that handles the clicks on the row for the blue paws
     * if the row is the right one the method works properly,
     *else it shows a message to retry the operation in the right place
     */
    private void onBlueRowClicked(){
        if(!chosenStudent.getStudent().getColor().equals(Color.BLUE)&&expertDeck.equals(ExpertDeck.BARBARIAN))
        {
            Platform.runLater(() -> SceneController.alertShown("Message:", "Select the right row"));
        }else{
            notifyGameController(blueRow);
        }

    }


    /**
     * this method notifies the game controller of the various operation made on the dashboard's rows
     * @param selectedRow is the row that has been selected during the turn
     */
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
                Platform.runLater(()->SceneController.changeRootPane(currentGameBoardSceneController,"gameboard_scene.fxml"));
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

    /**
     * this method handles the clicks on an island of the gameboard when the player clicks it during their turn
     * @param event is the input given by the player's mouse
     */
    private void onIslandClicked(MouseEvent event){
        setDisabledItems();
        switch(expertDeck){
            case HERALD:
                new Thread(()->notifyObserver(obs -> obs.OnUpdateEffectHerald(currentIslandIndex))).start();
                Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard_scene.fxml"));
                break;
            case HERBALIST:
                new Thread(() -> notifyObserver(obs->obs.OnUpdateEffectHerbalist(currentIslandIndex))).start();
                Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard_scene.fxml"));
                break;
            case TAVERNER:
                new Thread(()-> notifyObserver(obs->obs.OnUpdateEffectTaverner(currentIslandIndex))).start();
                Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard_scene.fxml"));
                break;
        }

    }

    /**
     * setter that disabled the possible operations on the gameboard so that the player cannot do them
     */
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
