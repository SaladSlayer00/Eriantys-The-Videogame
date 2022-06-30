package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * TowerChoiceSceneController class handles the scene that displays the scene that shows the towers when the player has to
 * choose their team
 */
public class TowerChoiceSceneController extends ViewObservable implements BasicSceneController {

        private List<Type> availableColors;

        @FXML
        private ImageView blackTower;
        @FXML
        private ImageView whiteTower;
        @FXML
        private ImageView greyTower;
        @FXML
        private Button backToMenuBtn;

        /**
         * Default constructor
         */
        public TowerChoiceSceneController() {
            this.availableColors = new ArrayList<>();
        }

        @FXML
        public void initialize() {
            blackTower.setDisable(!availableColors.contains(Type.BLACK));
            whiteTower.setDisable(!availableColors.contains(Type.WHITE));
            greyTower.setDisable(!availableColors.contains(Type.GREY));

            blackTower.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onTowerClick(Type.BLACK));
            whiteTower.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onTowerClick(Type.WHITE));
            greyTower.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onTowerClick(Type.GREY));

            backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
        }

        /**
         * Handle the click on the worker.
         *
         * @param color color picked by user.
         */
        private void onTowerClick(Type color) {
            blackTower.setDisable(true);
            whiteTower.setDisable(true);
            greyTower.setDisable(true);
            new Thread(() -> notifyObserver(obs -> obs.OnUpdateInitTower(color))).start();
        }

        /**
         * Handle the click on the back to menu button.
         */
        private void onBackToMenuBtnClick(Event event) {
            backToMenuBtn.setDisable(true);
            new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
            SceneController.changeRootPane(observers, event, "menu_scene.fxml");
        }

        /**
         * Set the colors which are pickable by user.
         *
         * @param availableColors available colors.
         */
        public void setAvailableColors(List<Type> availableColors) {
            this.availableColors = availableColors;

        }
    }
