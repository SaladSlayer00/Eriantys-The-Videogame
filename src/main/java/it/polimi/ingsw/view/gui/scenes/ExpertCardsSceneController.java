package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.enums.PhaseType;
import it.polimi.ingsw.model.expertDeck.*;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
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
    private ExpertDeck chosenOne;
    private Gameboard actualGameBoard;
    private GameBoardSceneController currentGameBoardSceneController;
    @FXML
    private TilePane tilePane;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button lessGoButton;
    @FXML
    private Button exit;
    @FXML
    private ImageView selectedCard;

    public ExpertCardsSceneController(Gameboard gameboard,GameBoardSceneController gBSC) {
        selectedCard = new ImageView();
        cards = new ImageView[3];
        tilePane = new TilePane();
        actualGameBoard = gameboard;
        currentGameBoardSceneController = gBSC;
        createExpertCards();

    }

    @FXML
    public void initialize() {
        createExpertCards();
        lessGoButton.setDisable(true);
        //Platform.runLater(() -> disabledCards());
        cards[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            onExpertCardsClick(actualGameBoard.getExperts().get(0).getText());
        });
        cards[1].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            onExpertCardsClick(actualGameBoard.getExperts().get(1).getText());
        });
        cards[2].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            onExpertCardsClick(actualGameBoard.getExperts().get(2).getText());
        });
        lessGoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLessGoButtonClick);
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onExitButtonClick);
    }

    /*don't think that we need to disable cards to be honest...
    private void disabledCards(){

    }
 */

    private void onExpertCardsClick(String nameOfTheCard) {
        lessGoButton.setDisable(false);
        setExpertCardImage(nameOfTheCard);
        if (nameOfTheCard.equals("taverner")) {
            chosenOne = ExpertDeck.TAVERNER;
            SceneController.alertShown("Taverner", "The player that summons this card can pick one of the four students that are on this cardS");
        } else if (nameOfTheCard.equals("herald")) {
            chosenOne = ExpertDeck.HERALD;
            SceneController.alertShown("Herald", "This card allows the player who summons it to decide an island where they can calculate the\n" +
                    "influence even if Mother Nature has not finished there her movement");
        } else if (nameOfTheCard.equals("gambler")) {
            chosenOne = ExpertDeck.GAMBLER;
            SceneController.alertShown("Gambler", "This card allows the summoner to move Mother Nature of two more islands than the number that is\n" +
                    "written on the Assistant card they have played");
        } else if (nameOfTheCard.equals("herbalist")) {
            chosenOne = ExpertDeck.HERBALIST;
            SceneController.alertShown("Herbalist", "When a player summon the card they can put one of ban paw on an island of their choice\n" +
                    " when Mother Nature ends her journey on that island, the paw is put again on the card and\n" +
                    " the influence it is not calculated!");
        } else if (nameOfTheCard.equals("customer")) {
            chosenOne = ExpertDeck.CUSTOMER;
            SceneController.alertShown("Customer", "When a player summons this card at the moment of the calculation of the influence the towers on the island\n" +
                    "are not to be taken in consideration");
        } else if (nameOfTheCard.equals("joker")) {
            chosenOne = ExpertDeck.JOKER;
            SceneController.alertShown("Joker", "The summoner can exchange as much as three students in their hall with three students on the card");
        } else if (nameOfTheCard.equals("knight")) {
            chosenOne = ExpertDeck.KNIGHT;
            SceneController.alertShown("Knight", "This card gives two more influence points to the summoner");
        } else if (nameOfTheCard.equals("seller")) {
            chosenOne = ExpertDeck.SELLER;
            SceneController.alertShown("Seller", "When a player summons this card they can choose a color that will have no influence in the calculation\n" +
                    "of the influence");
        } else if (nameOfTheCard.equals("musician")) {
            chosenOne = ExpertDeck.MUSICIAN;
            SceneController.alertShown("Musician", "The player that summons this card can swap two of their students from the row to the dashboard");
        } else if (nameOfTheCard.equals("barbarian")) {
            chosenOne = ExpertDeck.BARBARIAN;
            SceneController.alertShown("Barbarian", "The summoner can choose a student from this card\n" +
                    "and move it on their hall");
        } else if (nameOfTheCard.equals("banker")) {
            chosenOne = ExpertDeck.BANKER;
            SceneController.alertShown("Banker", "The player that summons this card can choose a color and every player (themselves included) has to take\n" +
                    "three students from the row of the chosen color and put them back in the sack");
        } else if (nameOfTheCard.equals("cook")) {
            chosenOne = ExpertDeck.COOK;
            SceneController.alertShown("Cook", "This card allows the player who summons it to control the professor even if they have the same number\n" +
                    "students of the player who has it in that very moment");
        }
    }

    private void onLessGoButtonClick(Event event) {
        new Thread(() -> notifyObserver((observers -> observers.OnUpdateExpert(chosenOne)))).start();

    }
    private void onExitButtonClick(Event event){
        Platform.runLater(() -> SceneController.alertShown("Message:", "Please, choose a student to move!"));
        currentGameBoardSceneController.setMainPhase(PhaseType.YOUR_MOVE);
        currentGameBoardSceneController.setSecondaryPhase(PhaseType.MOVE_STUDENT);
        Platform.runLater(()-> {
            try {
                currentGameBoardSceneController.updateAll();
            } catch (noTowerException e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard2_scene.fxml"));

    }

    public void createExpertCards() {
        for (int i = 0; i < 3; i++) {
            System.out.println("");
            Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front_" + actualGameBoard.getExperts().get(i).getText()+ ".jpg"));
            ImageView imageVw = new ImageView(image);
            imageVw.setFitWidth(350d);
            imageVw.setFitHeight(540d);
            Platform.runLater(() -> tilePane.getChildren().add(imageVw));
            cards[i] = imageVw;
        }
    }

    private void setExpertCardImage(String nameOfTheCard) {
        Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front_" + nameOfTheCard + ".jpg"));
        selectedCard.setImage(image);
    }

}


