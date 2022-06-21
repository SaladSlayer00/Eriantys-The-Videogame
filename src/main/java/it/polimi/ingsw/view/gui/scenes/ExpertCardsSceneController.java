package it.polimi.ingsw.view.gui.scenes;
/*
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
        cards = new ImageView[12];
        tilePane = new TilePane();
        for(int i = 0; i < 12; i++){
            int j = i + 1;
            Image image = new Image(getClass().getResourceAsStream("images/cards/characters/CarteTOT_front" + j + ".jpg"));
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
                onExpertCardsClick("ToIslandCard", 1, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[1].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("ImproperInfluenceCard", 2, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[2].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("TwoMoreMovesCard", 3, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[3].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("InfluenceBansCard", 4, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[4].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("NoTowerCard", 5, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[5].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("ExchangeStudentsCard",6, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[6].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("TwoMorePointsCard", 7, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[7].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("NullColorCard", 8, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[8].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("SwapTwoStudentsCard", 9, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[9].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("OneMoreStudentCard", 10, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[10].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("RemoveAColorCard", 11, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        cards[11].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                onExpertCardsClick("ProfessorControllerCard", 12, actualGameController, actualTurnController);
            } catch (noMoreStudentsException e) {
                e.printStackTrace();
            }
        });
        lessGoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onLessGoButtonClick());
        rethoughtAboutItButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onRethoughtAboutItButtonClick());
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootPaneMousePressed);
        rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootPaneMouseDragged);
    }

    /*don't think that we need to disable cards to be honest...
    private void disabledCards(){

    }


    private void onExpertCardsClick(String nameOfTheCard, int easyIndex, GameController gc, TurnController tc) throws noMoreStudentsException {
        setExpertCardImage(easyIndex);
        chosenOneName = nameOfTheCard;
        if(nameOfTheCard.equals("ToIslandCard")){
            chosenOne = new ToIslandCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("ImproperInfluenceCard")){
            chosenOne = new ImproperInfluenceCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("TwoMoreMovesCard")){
            chosenOne = new TwoMoreMovesCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("InfluenceBansCard")){
            chosenOne = new InfluenceBansCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("NoTowerCard")){
            chosenOne = new NoTowerCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("ExchangeStudentsCard")){
            chosenOne = new ExchangeStudentsCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("TwoMorePointsCard")){
            chosenOne = new TwoMorePointsCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("NullColorCard")){
            chosenOne = new NullColorCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("SwapTwoStudentsCard")){
            chosenOne = new SwapTwoStudentsCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("OneMoreStudentCard")){
            chosenOne = new OneMoreStudentCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("RemoveAColorCard")){
            chosenOne = new RemoveAColorCard(actualGameController, actualTurnController);
        }
        else if(nameOfTheCard.equals("ProfessorControllerCard")){
            chosenOne = new ProfessorControllerCard(actualGameController, actualTurnController);
        }
    }

    private void onLessGoButtonClick(){
       //TODO
    }

    private void onRethoughtAboutItButtonClick(){
        for(int i = 0; i < 12; i++){
            cards[i].setDisable(false);
            cards[i].setOpacity(1);
        }
        chosenOne = null;
        selectedCard.setImage(null);
        lessGoButton.setDisable(true);
        rethoughtAboutItButton.setDisable(true);
    }

    private void setExpertCardImage(int easyIndex){
        Image image = new Image(getClass().getResourceAsStream("/images/cards/characters/CarteTOT_front" + easyIndex + ".png"));
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
