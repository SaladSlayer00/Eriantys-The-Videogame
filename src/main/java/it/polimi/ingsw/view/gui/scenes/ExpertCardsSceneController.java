package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.exceptions.noTowerException;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.enums.PhaseType;
import it.polimi.ingsw.controller.expertDeck.*;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

/**
 * ExpertCardSceneController class for the handling of the scene that is displayed for the selection of the expert card
 */
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
    private Button back;
    @FXML
    private ImageView selectedCard;

    /**
     * class constructor
     * @param gameboard is the actual gameboard of the match
     * @param gBSC is the gameboard scene controller of the match
     */
    public ExpertCardsSceneController(Gameboard gameboard,GameBoardSceneController gBSC) {
        selectedCard = new ImageView();
        cards = new ImageView[3];
        tilePane = new TilePane();
        actualGameBoard = gameboard;
        currentGameBoardSceneController = gBSC;
        createExpertCards();

    }

    /**
     * this method initializes the class setting all the various parameter to display the scene
     * on the player's screen in the proper way
     */
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
        back.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onBackButtonClick);
    }



    /**
     * this method handles the clicks on the various expert cards and handles all the possible situations given by the
     * differences between the various cards of the expert deck
     * @param nameOfTheCard is the name of the card that has been clicked
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

    /**
     * this method handles the click on the button that confirms the choice of the card
     * @param event is the input given by the player's mouse
     */
    private void onLessGoButtonClick(Event event) {
        new Thread(() -> notifyObserver((observers -> observers.OnUpdateExpert(chosenOne)))).start();
        if(chosenOne.equals(ExpertDeck.COOK) || chosenOne.equals(ExpertDeck.KNIGHT) || chosenOne.equals(ExpertDeck.GAMBLER) || chosenOne.equals(ExpertDeck.CUSTOMER)){
            Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard_scene.fxml"));
        }
    }

    /**
     * this method handles the clicks on the button that exit form the scene of the choice of the card
     * the match keeps going as normal
     * @param event is the input given by the player's mouse
     */
    private void onBackButtonClick(Event event){
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
        Platform.runLater(() -> SceneController.changeRootPane(currentGameBoardSceneController, "gameboard_scene.fxml"));

    }

    /**
     * this method creates the various expert cards (which are three for each match)
     */
    public void createExpertCards() {
        for (int i = 0; i < 3; i++) {
            Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front_" + actualGameBoard.getExperts().get(i).getText().toLowerCase()+ ".jpg"));
            ImageView imageVw = new ImageView(image);
            imageVw.setFitWidth(240);
            imageVw.setFitHeight(356);
            Platform.runLater(() -> tilePane.getChildren().add(imageVw));
            cards[i] = imageVw;
        }
    }

    /**
     * setter for the image of an expert card
     * @param nameOfTheCard is the string indicating the name of the expert card that has to be displayed
     */
    private void setExpertCardImage(String nameOfTheCard) {
        Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front_" + nameOfTheCard.toLowerCase() + ".jpg"));
        selectedCard.setImage(image);
    }

}


