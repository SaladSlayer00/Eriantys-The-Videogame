package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.scenes.AlertSceneController;
import it.polimi.ingsw.view.gui.scenes.BasicSceneController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;

public class SceneController extends ViewObservable {

    //should there be a String for the prefix???

    private static Scene activeScene;
    public static BasicSceneController activeSceneController;

    //getter for the active scene
    public static Scene getActiveScene(){
        return activeScene;
    }

    //getter for the active scene controller
    public BasicSceneController getSceneController(){
        return activeSceneController;
    }

    //this is the method that changes the root panel
    //it returns the new scene controller
    public static <T> T changeRootPane(List<ViewObserver> listOfObserver, Scene actualScene, String fxml){
        T controller = null;

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml ));
            System loader;
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

    //another one
    public static <T> T changeRootPane(List<ViewObserver> listOfObserver, Event event, String fxml){
        Scene actualScene = ((Node) event.getSource()).getScene();
        return changeRootPane(listOfObserver, actualScene, fxml);
    }


    public static <T> T changeRootPane(List<ViewObserver> observerList, String fxml){
        return changeRootPane(observerList, activeScene, fxml);
    }

    //another one
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

    public static void changeRootPane(BasicSceneController bsController, String fxml){
        changeRootPane(bsController, activeScene, fxml);
    }

    //this should show the client message in a popup
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
    }
}
