package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.ExpertDeck;
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

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//should decide how to display the choice;

public class ColorChoiceSceneController extends ViewObservable implements BasicSceneController{

    private ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.PINK, Color.YELLOW, Color.BLUE));
    private Color choice;
    private ExpertDeck expertChosen;
    private Gameboard actualGameboard;
    private final Stage actualStage;
    private double offsetX;
    private double offsetY;
    private Color chosenColor;
    private ImageView[] paws;

    @FXML
    private AnchorPane rootAP;
    @FXML
    private TilePane tP;
    @FXML
    private Button okayButton;
    @FXML
    private Button notOkayButton;

    public ColorChoiceSceneController(){

        actualStage = new Stage();
        actualStage.initOwner(SceneController.getActiveScene().getWindow());
        actualStage.initModality(Modality.APPLICATION_MODAL);
        actualStage.initStyle(StageStyle.UNDECORATED);
        actualStage.toFront();
        actualStage.setAlwaysOnTop(true);
        offsetX = 0;
        offsetY = 0;
        tP = new TilePane();
        paws = new ImageView[5];
        int i = 0;
        for(Color c : colors){
            Image p = new Image(getClass().getResourceAsStream("/images/pawn/students/student_" + c.getText() +".png"));
            ImageView imageView = new ImageView(p);
            imageView.setFitWidth(100d);
            imageView.setFitHeight(130d);
            Platform.runLater(()-> tP.getChildren().add(imageView));
            paws[i] = imageView;
            i++;
        }
    }

    @FXML
    public void initialize(){

        okayButton.setDisable(true);
        notOkayButton.setDisable(true);
        paws[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onColorClicked("red"));
        paws[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onColorClicked("green"));
        paws[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onColorClicked("pink"));
        paws[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onColorClicked("yellow"));
        paws[0].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onColorClicked("blue"));
        okayButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkayButtonClicked);
        notOkayButton.addEventFilter(MouseEvent.MOUSE_CLICKED, this::onNotOkayButtonClicked);
        rootAP.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootAPMousePressed);
        rootAP.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootAPMouseDragged);
    }

    public void setGameboard(Gameboard gb){
        actualGameboard = gb;
    }

    public void setExpert(ExpertDeck ec){
        expertChosen = ec;
    }

    private void onColorClicked(String color){
        choice.valueOf(color);
        okayButton.setDisable(false);
        notOkayButton.setDisable(false);
        int i = 0;
        for(Color c: colors){
            if(!c.equals(choice)){
                paws[i].setDisable(true);
                paws[i].setOpacity(0.5);
            }
            i++;
        }
    }

    private void onOkayButtonClicked(Event mouseEvent){
        if(expertChosen.equals(ExpertDeck.valueOf("Banker"))) {
            new Thread(() -> notifyObserver(observers -> observers.OnUpdateEffectBanker(choice))).start();
        }
        else if(expertChosen.equals(ExpertDeck.valueOf("Seller"))){
            new Thread(() -> notifyObserver(observers -> observers.OnUpdateEffectSeller(choice)));
        }
        actualStage.close();
    }

    private void onNotOkayButtonClicked(Event mouseEvent){
        for(int i = 0; i < 5; i++) {
            paws[i].setDisable(false);
            paws[i].setOpacity(1);
        }
        choice = null;
        okayButton.setDisable(true);
        notOkayButton.setDisable(true);
    }

    private void onRootAPMousePressed(MouseEvent mouseEvent){
        offsetX = actualStage.getX() - mouseEvent.getScreenX();
        offsetY = actualStage.getY() - mouseEvent.getScreenY();
    }

    private void onRootAPMouseDragged(MouseEvent mouseEvent){
        actualStage.setX(mouseEvent.getScreenX() + offsetX);
        actualStage.setY(mouseEvent.getScreenY() + offsetY);
    }

    public void setScene(Scene scene){
        actualStage.setScene(scene);
    }

}
