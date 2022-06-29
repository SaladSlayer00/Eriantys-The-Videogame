package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.ExpertDeck;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.scenes.*;
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
 * SceneController class is the main scene controller that handles all the various transition between the various scenes
 * and allows them to work correctly during the game
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
     *this is the method that changes the parameter of a scene
    *it returns the new scene controller
     * @param listOfObserver is the list of observers that are linked to the game
     * @param actualScene is the scene that it is displayed at the very moment on the player's screen
     * @param fxml is the fxml file of the scene displayed
     * @param <T> is the type of parameter changed
     * @return the controller of the new scene to load
     **/
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
     * changes the root panel of the scene argument
     * @param <T> is the parameter changed
     * @param listOfObserver is the lists of observers linked to the game
     * @param event is the event occurred
     * @param fxml is the fxml of the new scene to load
     * @return the controller of the new scene loaded
     */
    public static <T> T changeRootPane(List<ViewObserver> listOfObserver, Event event, String fxml){
        Scene actualScene = ((Node) event.getSource()).getScene();
        return changeRootPane(listOfObserver, actualScene, fxml);
    }


    /**
     * changes the root panel of the scene argument
     * @param observerList is the lists of observers linked to the game
     * @param fxml is the fxml file of the new scene to load
     * @param <T> is tge parameter changed
     * @return the controller of the new scene loaded
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, String fxml){
        return changeRootPane(observerList, activeScene, fxml);
    }

    /**
     * changes the root panel of the scene argument and it gives the opportunity to
     * decide a controller to pass as a parameter
     * @param bsController is the scene controller that it is wanted to pass as the new one
     * @param scene is the scene it ha sto be set
     * @param fxml is the fxml file of the new scene
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
     * changes the root panel of the scene argument
     * @param bsController is the scene controller that it is wanted to be set
     * @param fxml is the fxml file of the new scene
     */
    public static void changeRootPane(BasicSceneController bsController, String fxml){
        changeRootPane(bsController, activeScene, fxml);
    }

    /**
     * shows a message as a popup to the player
     * @param mainTitle is the title of the message that it has to be displayed
     * @param text is the text of the message that it has to be displayed
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
     * show the assistant choice scene as a popup to the player
     * @param availableAssistants is the list of the available assistant from which the player can choose
     * @param obs is the list of observers linked to the game
     */
    public static void showingAssistantPopup(List<Assistant> availableAssistants, List<ViewObserver> obs){
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
        aSController.displayAlert();
    }

    /**
     * it shows the scene that handles the clouds as a popup
     * @param emptyClouds is the list of empty clouds
     * @param availableClouds is the list of available clouds for the choice
     * @param obs is the list of observers linked to the match
     * @param choice is string that represents the choice
     */
    public static void showingCloudsPopup(List<Cloud> emptyClouds,List<Cloud> availableClouds, List<ViewObserver> obs,String choice){
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
        cSController.displayAlert();
    }

    /**
     * this is the method that controls the popup for the winning message
     */
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

    /**
     * this is the method that shows the popup for the continue scene
     * @param obs is the list of observers
     * @param gameBoardSceneController is the gameboard scene controller linked to the match
     */
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
     * handles the popup window that asks the player to choose a color for the expert cards that requires it
     * @param obs is the list of observers for the match
     * @param gameBoardSceneController is the gameboard scene controller linked to the match
     * @param expertDeck is the expert deck linked to the match
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
