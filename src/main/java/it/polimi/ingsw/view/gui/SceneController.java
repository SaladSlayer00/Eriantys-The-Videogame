package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.scenes.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 * SceneController class handles the transitions between the various scenes of the game
 */
public class SceneController extends ViewObservable  {


    private static Scene activeScene;
    private static BasicSceneController activeSceneController;

    //getter for the active scene

    public static Scene getActiveScene(){
        return activeScene;
    }

    //getter for the active scene controller
    public static BasicSceneController getSceneController(){
        return activeSceneController;
    }

    /**
     * this is the method that changes the root panel
     * @param listOfObserver is the list of the observers linked to the match
     * @param actualScene is the scene that is displayed at the moment
     * @param fxml is the fxml file of the scene that has to be displayed after
     * @param <T> is the parameter that changed that influenced the change of the scene
     * @return the controller to the new scene
     */
    public static <T> T changeRootPane(List<ViewObserver> listOfObserver, Scene actualScene, String fxml){
        T controller = null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/"+fxml));
            //System loader;
            Parent root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            ((ViewObservable) controller).addAllObservers(listOfObserver);
            activeSceneController = (BasicSceneController) controller;
            activeScene = actualScene;
            activeScene.setRoot(root);
        } catch(IOException exception){
            Client.LOGGER.severe(exception.getMessage());
        }
        return controller;
    }

    /**
     * this is the method that changes the root panel
     * @param listOfObserver is the list of the observer linked to the match
     * @param event is the event that occurred
     * @param fxml is the file fxml of the new scene
     * @param <T> is the parameter that has changed
     * @return the controller to the new scene
     */
    public static <T> T changeRootPane(List<ViewObserver> listOfObserver, Event event, String fxml){
        Scene actualScene = ((Node) event.getSource()).getScene();
        return changeRootPane(listOfObserver, actualScene, fxml);
    }


    /**
     * this is the method that changes the root panel
     * @param observerList is the list of observer linked to the match
     * @param fxml is the fxml file of the new scene to be displayed
     * @param <T> is the parameter changed
     * @return the controller to the new scene
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, String fxml){
        return changeRootPane(observerList, activeScene, fxml);
    }

    /**
     * this is the method that changes the root panel but it also gives the opportunity to
     * decide a specific scene to be displayed
     * @param bsController is the scene controller to be displayed
     * @param scene is the actual scene
     * @param fxml is the fxml file of the scene to display
     */
    public static void changeRootPane(BasicSceneController bsController, Scene scene, String fxml){
        try{
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            loader.setController(bsController);
            activeSceneController = bsController;
            Parent root = loader.load();
            activeScene = scene;
            activeScene.setRoot(root);
        } catch(IOException exception){
            Client.LOGGER.severe(exception.getMessage());
        }
    }

    /**
     * this is the method that changes the root panel
     * @param bsController is the scene controller of the new scene to display
     * @param fxml is the fxml file
     */
    public static void changeRootPane(BasicSceneController bsController, String fxml){
        changeRootPane(bsController, activeScene, fxml);
    }

    /**
     * it displays a message as a popup
     * @param mainTitle is the title of the message to be displayed
     * @param text is the text of the message to be displayed
     */
    public static void alertShown(String mainTitle, String text){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/alert_scene.fxml" ));
        Parent parent;
        try{
            parent = fxmlLoader.load();

        }catch(IOException exception){
            Client.LOGGER.severe(exception.getMessage());
            return;
        }
        AlertSceneController alertSC = fxmlLoader.getController();
        Scene alertScene = new Scene(parent);
        alertSC.setScene(alertScene);
        alertSC.setAlertTitle(mainTitle);
        alertSC.setAlertMessage(text);
        alertSC.displayAlert();
    }


    /**
     * this method shows the popup of the window for the choice of the assistants' card
     * @param availableAssistants is the list of available assistant from which the player can choose
     * @param obs is the list of observer
     * @param gameBoardSceneController is the gameboard controller for the match
     * @param choice is the choice done by the player
     * @param player is the player who's choosing
     */
    public static void showingAssistantPopup(List<Assistant> availableAssistants, List<ViewObserver> obs,GameBoardSceneController gameBoardSceneController, String choice,String player){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/assistantChoice_scene.fxml"));

        Parent parent;
        try{
            parent = fxmlLoader.load();
        }catch(IOException ioException){
            Client.LOGGER.severe(ioException.getMessage());
            return;
        }
        AssistantChoiceSceneController aSController = fxmlLoader.getController();
        Scene assistantScene = new Scene(parent);
        aSController.setScene(assistantScene);
        aSController.addAllObservers(obs);
        aSController.setAssistantDeck(availableAssistants);
        aSController.setTypeOfChoice(choice);
        aSController.setPlayerNickname(player);
        aSController.setGameBoardSceneController(gameBoardSceneController);
        aSController.displayAlert();
    }

    /**
     * this method shows the popup of the scene of the clouds
     * @param emptyClouds is the list of the empty clouds
     * @param availableClouds is the list of the available clouds
     * @param obs is the list of observer
     * @param choice is the choice done
     * @param gBSC is the gameboard scene controller of the match
     */
    public static void showingCloudsPopup(List<Cloud> emptyClouds, List<Cloud> availableClouds, List<ViewObserver> obs, String choice, GameBoardSceneController gBSC){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/clouds_scene.fxml"));

        Parent parent;
        try{
            parent = fxmlLoader.load();
        }catch(IOException ioException){
            Client.LOGGER.severe(ioException.getMessage());
            return;
        }
        CloudSceneController cSController = fxmlLoader.getController();
        Scene assistantScene = new Scene(parent);
        cSController.setScene(assistantScene);
        cSController.addAllObservers(obs);
        cSController.setAvailableClouds(availableClouds);
        cSController.setEmptyClouds(emptyClouds);
        cSController.setTypeOfChoice(choice);
        cSController.setGameBoardSceneController(gBSC);
        cSController.displayAlert();
    }


    public static void showingTheWinningPopup(String whosTheWinner,String playerNickname){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/win_scene.fxml"));

        Parent parente;
        try{
            parente = fxmlLoader.load();
        }catch(IOException ioException){
            Client.LOGGER.severe(ioException.getMessage());
            return;
        }
        WinSceneController winSController = fxmlLoader.getController();
        Scene winningScene = new Scene(parente);
        winSController.setScene(winningScene);
        winSController.setWinnerNick(whosTheWinner);
        winSController.isWinner(playerNickname);
        winSController.winnerOnDisplay();
    }

    public static void showingContinuePopUp( List<ViewObserver> obs,GameBoardSceneController gameBoardSceneController){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/continue_scene.fxml"));

        Parent parent;
        try{
            parent = fxmlLoader.load();
        }catch(IOException ioException){
            Client.LOGGER.severe(ioException.getMessage());
            return;
        }
        ContinueSceneController cSController = fxmlLoader.getController();
        Scene assistantScene = new Scene(parent);
        cSController.setScene(assistantScene);
        cSController.addAllObservers(obs);
        cSController.setNextScene(gameBoardSceneController);
        cSController.displayAlert();
    }

    /**
     * this method is used in the expert mode only and it is used for the cards that have students' paws on them
     * this method handles the choice of a paw on that cards
     * @param obs is the list for the observers
     * @param gameBoardSceneController is the scene controller of the gameboard of the match
     * @param expertDeck is the expert deck that it is used in te match
     */
    public static void showingColorChoicePopUp(List<ViewObserver> obs , GameBoardSceneController gameBoardSceneController, ExpertDeck expertDeck){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/colorChoice_scene.fxml"));
        Parent parent;
        try{
            parent = fxmlLoader.load();
        }catch(IOException ioException){
            Client.LOGGER.severe(ioException.getMessage());
            return;
        }
        ColorChoiceSceneController cCSController = fxmlLoader.getController();
        Scene colorChoiceScene = new Scene(parent);
        cCSController.addAllObservers(obs);
        cCSController.setScene(colorChoiceScene);
        cCSController.setgBSC(gameBoardSceneController);
        cCSController.setExpertChosen(expertDeck);
        cCSController.displayAlert();
    }


}
