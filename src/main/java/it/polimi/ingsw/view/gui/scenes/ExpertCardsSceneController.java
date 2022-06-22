package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.expertDeck.*;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ExpertCardsSceneController extends ViewObservable implements BasicSceneController {

    String chosenOneName;
    private ImageView[] cards;
    private Character chosenOne;
    private final Stage actualStage;
    private double offsetX;
    private double offsetY;
    private GameController actualGameController;
    private TurnController actualTurnController;
    @FXML
    private TilePane tilePane;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button lessGoButton;
    @FXML
    private Button rethoughtAboutItButton;
    @FXML
    private  Button exit;
    @FXML
    private ImageView selectedCard;

    public ExpertCardsSceneController(TurnController turnController, GameController gameController){
        actualGameController = gameController;
        actualTurnController = turnController;
        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.toFront();
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        selectedCard = new ImageView();
        cards = new ImageView[3];
        tilePane = new TilePane();
        for(int i = 0; i < 3; i++){
            Image image = new Image(getClass().getResourceAsStream("images/cards/characters/CarteTOT_front_" +gameController.getGame().getExperts().get(i).getText() + ".jpg"));
            ImageView imageVw = new ImageView(image);
            imageVw.setFitWidth(100d);
            imageVw.setFitHeight(130d);
            Platform.runLater(() -> tilePane.getChildren().add(imageVw));
            cards[i] = imageVw;
        }
    }

    @FXML
    public void initialize(){
        lessGoButton.setDisable(true);
        rethoughtAboutItButton.setDisable(true);
        //Platform.runLater(() -> disabledCards());
        cards[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick(actualGameController.getGame().getGameBoard().getExperts().get(0).getText(), actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[1].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick(actualGameController.getGame().getGameBoard().getExperts().get(1).getText(), actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[2].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick(actualGameController.getGame().getGameBoard().getExperts().get(2).getText(), actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        lessGoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLessGoButtonClick);
        rethoughtAboutItButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onRethoughtAboutItButtonClick());
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->actualStage.close());
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootPaneMousePressed);
        rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootPaneMouseDragged);
    }

    /*don't think that we need to disable cards to be honest...
    private void disabledCards(){

    }
 */

    private void onExpertCardsClick(String nameOfTheCard,  GameController gc, TurnController tc) throws noMoreStudentsException {
        setExpertCardImage(nameOfTheCard);
        chosenOneName = nameOfTheCard;
        if(nameOfTheCard.equals("taverner")){
            chosenOne = new ToIslandCard(actualGameController, actualTurnController);
            SceneController.alertShown("Taverner", "The player that summons this card can pick one of the four students that are on this cardS");
        }
        else if(nameOfTheCard.equals("herald")){
            chosenOne = new ImproperInfluenceCard(actualGameController, actualTurnController);
            SceneController.alertShown("Herald", "This card allows the player who summons it to decide an island where they can calculate the\n" +
                    "influence even if Mother Nature has not finished there her movement");
        }
        else if(nameOfTheCard.equals("gambler")){
            chosenOne = new TwoMoreMovesCard(actualGameController, actualTurnController);
            SceneController.alertShown("Gambler", "This card allows the summoner to move Mother Nature of two more islands than the number that is\n" +
                    "written on the Assistant card they have played");
        }
        else if(nameOfTheCard.equals("herbalist")){
            chosenOne = new InfluenceBansCard(actualGameController, actualTurnController);
            SceneController.alertShown("Herbalist", "When a player summon the card they can put one of ban paw on an island of their choice\n" +
                    "* when Mother Nature ends her journey on that island, the paw is put again on the card and\n" +
                    "* the influence it is not calculated!");
        }
        else if(nameOfTheCard.equals("customer")){
            chosenOne = new NoTowerCard(actualGameController, actualTurnController);
            SceneController.alertShown("Customer", "When a player summons this card at the moment of the calculation of the influence the towers on the island\n" +
                    "are not to be taken in consideration");
        }
        else if(nameOfTheCard.equals("joker")){
            chosenOne = new ExchangeStudentsCard(actualGameController, actualTurnController);
            SceneController.alertShown("Joker", "The summoner can exchange as much as three students in their hall with three students on the card");
        }
        else if(nameOfTheCard.equals("knight")){
            chosenOne = new TwoMorePointsCard(actualGameController, actualTurnController);
            SceneController.alertShown("Knight", "This card gives two more influence points to the summoner");
        }
        else if(nameOfTheCard.equals("seller")){
            chosenOne = new NullColorCard(actualGameController, actualTurnController);
            SceneController.alertShown("Seller", "When a player summons this card they can choose a color that will have no influence in the calculation\n" +
                    "of the influence");
        }
        else if(nameOfTheCard.equals("musician")){
            chosenOne = new SwapTwoStudentsCard(actualGameController, actualTurnController);
            SceneController.alertShown("Musician", "The player that summons this card can swap two of their students from the row to the dashboard");
        }
        else if(nameOfTheCard.equals("barbarian")){
            chosenOne = new OneMoreStudentCard(actualGameController, actualTurnController);
            SceneController.alertShown("Barbarian", "The summoner can choose a student from this card\n" +
                    "and move it on their hall");
        }
        else if(nameOfTheCard.equals("banker")){
            chosenOne = new RemoveAColorCard(actualGameController, actualTurnController);
            SceneController.alertShown("Banker", "The player that summons this card can choose a color and every player (themselves included) has to take\n" +
                    "three students from the row of the chosen color and put them back in the sack");
        }
        else if(nameOfTheCard.equals("cook")){
            chosenOne = new ProfessorControllerCard(actualGameController, actualTurnController);
            SceneController.alertShown("Cook", "This card allows the player who summons it to control the professor even if they have the same number\n" +
                    "students of the player who has it in that very moment");
        }
    }

    private void onLessGoButtonClick(Event event){
       if(chosenOneName.equals("taverner")){
           SceneController.changeRootPane(observers, event, "toIslandCard_scene.fxml");
       }
       else if(chosenOneName.equals("herald")){
           SceneController.changeRootPane(observers, event, "improperInfluenceCard_scene.fxml");
       }
       else if(chosenOneName.equals("gambler")){
           SceneController.changeRootPane(observers, event, "twoMoreMovesCard_scene.fxml");
       }
       else if(chosenOneName.equals("herbalist")){
           SceneController.changeRootPane(observers, event, "influenceBansCard_scene.fxml");
       }
       else if(chosenOneName.equals("customer")){
           SceneController.changeRootPane(observers, event, "noTowerCard_scene.fxml");
       }
       else if(chosenOneName.equals("joker")){
           SceneController.changeRootPane(observers, event, "exchangeStudentCard_scene.fxml");
       }
       else if(chosenOneName.equals("knight")){
           SceneController.changeRootPane(observers, event, "twoMorePointsCard_scene.fxml");
       }
       else if(chosenOneName.equals("seller")){
           SceneController.changeRootPane(observers, event, "nullColorCard_scene.fxml");
       }
       else if(chosenOneName.equals("musician")){
           SceneController.changeRootPane(observers, event, "swapTwoStudentCard_scene.fxml");
       }
       else if(chosenOneName.equals("barbarian")){
           SceneController.changeRootPane(observers, event, "oneMoreStudentCard_scene.fxml");
       }
       else if(chosenOneName.equals("banker")){
           SceneController.changeRootPane(observers, event, "removeAColorCard_scene.fxml");
       }
       else if(chosenOneName.equals(("cook"))){
           SceneController.changeRootPane(observers, event, "professorControllerCard_scene.fxml");
       }
    }

    private void onRethoughtAboutItButtonClick(){
        for(int i = 0; i < 3; i++){
            cards[i].setDisable(false);
            cards[i].setOpacity(1);
        }
        chosenOne = null;
        selectedCard.setImage(null);
        lessGoButton.setDisable(true);
        rethoughtAboutItButton.setDisable(true);
    }

    private void setExpertCardImage(String nameOfTheCard){
        Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front" +nameOfTheCard+ ".png"));
        selectedCard.setImage(image);
    }

    private void onRootPaneMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    private void onRootPaneMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }

    public void displayAlert(){
        actualStage.showAndWait();
    }
}
