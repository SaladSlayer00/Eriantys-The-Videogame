package it.polimi.ingsw.controller;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.exceptions.noMoreStudentsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.*;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//si occupa della fase iniziale per settare l'ordine e richiama le azioni per la
//fase azione

public class TurnController implements Serializable {
    private static final long serialVersionUID = -5987205913389392005L; //non so che cazzo è
    private final Mode game;
    private final List<String> nicknameQueue;
    private String activePlayer;
    transient Map<String, VirtualView> virtualViewMap;
    private MainPhase mainPhase;
    private PhaseType phaseType;
    private final GameController gameController;

    public TurnController(Map<String, VirtualView> virtualViewMap, GameController gameController, Mode game) {
        this.game = game;
        this.nicknameQueue = new ArrayList<>(game.getPlayersNicknames());

        this.activePlayer = nicknameQueue.get(0); // set first active player
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Set the active player.
     *
     * @param activePlayer the active Player to be set.
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Set next active player.
     */
    public void next() {

        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
        phaseType = PhaseType.YOUR_MOVE;
    }


    public void setMainPhase(MainPhase turnMainPhase) {
        this.mainPhase = turnMainPhase;
    }


    /**
     * @return the current Phase Type.
     */
    public MainPhase getMainPhase() {
        return mainPhase;
    }

    public void setPhaseType(PhaseType turnPhaseType) {
        this.phaseType = turnPhaseType;
    }


    /**
     * @return the current Phase Type.
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }

    public void newTurn() {
        setActivePlayer(nicknameQueue.get(0));
        turnControllerNotify("Turn of " + activePlayer, activePlayer);
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("Initiate the game! Pick your clouds. . .");

        StorageData storageData = new StorageData();
        storageData.store(gameController);

        setMainPhase(MainPhase.PLANNING);
        setPhaseType(PhaseType.ADD_ON_CLOUD);
        pickCloud();
    }


    //il player SCEGLIE LE CAZZO DI NUVOLE era pickpositions mi pare
    //i controlli sul valore valido penso li farà inputController...???
    public void pickCloud(){
        ArrayList<Cloud> cloudList;
        Player player = game.getPlayerByNickname(getActivePlayer());
        //lista che si passava come parametro per fare scegliere il player
        cloudList = game.getEmptyClouds();
        VirtualView virtualView = virtualViewMap.get(player);
        virtualView.askCloud(cloudList); //da chiedere sugli indici spacchettando?? non so sto metodo che fa
        //manderà un messaggio al player con la lista di disponibili booh poi vedremo
    }

    public void cloudInitializer(int cloudIndex) throws noMoreStudentsException {
        Cloud cloud = game.getGameBoard().getClouds()[cloudIndex];
        Sack sack = game.getGameBoard().getSack();
        int var;
        if(game.getChosenPlayerNumber()==2 || game.getChosenPlayerNumber() == 4){
            var = 3;
        }
        else{
            var = 4;
        }
        for(int i = 0; i < var; var++){
            cloud.addStudent(sack.drawStudent());
        }
    }
    public void pickDeck(){
        ArrayList<Cloud> cloudList;
        Player player = game.getPlayerByNickname(getActivePlayer());
        //lista che si passava come parametro per fare scegliere il player
        cloudList = game.getEmptyClouds();
        VirtualView virtualView = virtualViewMap.get(player);
        virtualView.askCloud(cloudList); //da chiedere sugli indici spacchettando?? non so sto metodo che fa
        //manderà un messaggio al player con la lista di disponibili booh poi vedremo
    }

}
