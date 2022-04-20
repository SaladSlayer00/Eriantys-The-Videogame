package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.invalidNumberException;
import it.polimi.ingsw.model.GameFactory;
import it.polimi.ingsw.model.Mode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.modeEnum;

import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class GameHandler {
    private static final String PLAYER = "Player";
    private final Server server;
    private final GameController controller;
    private Mode game;
    private final GameFactory gameFactory;
    private final PropertyChangeSupport controllerListener = new PropertyChangeSupport(this);
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Random rnd = new Random();
    private int started;
    private int playersNumber;
    private modeEnum gameMode;

    public GameHandler(Server server) {
        this.server = server;
        this.gameFactory = new GameFactory();
        started = 0;
        controller = new GameController((this));
        controllerListener.addPropertyChangeListener(controller);
    }

    //il socket setta questo parametro
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    //il socket setta questo parametro
    public void setGameMode(modeEnum gameMode) {
        this.gameMode = gameMode;
    }

    public void createGame() throws invalidNumberException {
        game = gameFactory.getMode(this.gameMode, this.playersNumber);
    }

    public void createGameController(){
        controller.setGame(this.game, this.gameMode);
    }

    public void createPlayer(String nickname, int playerID){
        game.initializePlayer(new Player(nickname, playerID));
    }

    public void setup() {
        if(started==0) started=1;
        DeckTypeRequest req = new DeckMessage("Please choose your Deck.");
        req.addRemaining(PlayerColors.notChosen());
        if(playersNumber==2 && PlayerColors.notChosen().size()>1) {
            String nickname = game.getActivePlayers().get(playersNumber - PlayerColors.notChosen().size() + 1).
                    getNickname();
            singleSend(req, server.getIDByNickname(nickname));
            sendAllExcept(new CustomMessage("User " + nickname + " is choosing his color!", false),
                    server.getIDByNickname(nickname));
            return;
        }
        else if(playersNumber==3 && !PlayerColors.notChosen().isEmpty()) {
            String nickname = game.getActivePlayers().get(playersNumber - PlayerColors.notChosen().size()).
                    getNickname();
            if(PlayerColors.notChosen().size()==1) {
                game.getPlayerByNickname(nickname).setColor(PlayerColors.notChosen().get(0));
                singleSend(new CustomMessage("\nThe society decides for you! You have the " +
                        PlayerColors.notChosen().get(0) + " color!\n", false), server.getIDByNickname(nickname));
                singleSend(new ColorMessage(null, PlayerColors.notChosen().get(0).toString()),
                        server.getIDByNickname(nickname));
                PlayerColors.choose(PlayerColors.notChosen().get(0));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
            else {
                server.getClientByID(server.getIDByNickname(nickname)).send(req);
                sendAllExcept(new CustomMessage("User " + nickname + " is choosing his color!", false),
                        server.getIDByNickname(nickname));
                return;
            }
        }






}
