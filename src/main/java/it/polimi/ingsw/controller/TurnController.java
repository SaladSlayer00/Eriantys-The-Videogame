package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Mode;

import java.io.Serializable;
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
    private PhaseType phaseType;
    private final GameController gameController;

    public TurnController(GameController controller, GameHandler gameHandler, ActionController actionController) {
        this.controller = controller;
        this.actionController = actionController;
        this.gameHandler = gameHandler;
    }
}
