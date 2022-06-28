package it.polimi.ingsw.controller;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.expertDeck.Character;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.playerBoard.Row;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import javax.lang.model.type.DeclaredType;
import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * This class is used to verify the player's input, and represents the second and most extensive check
 * on it. It signals the gameController with boolena return values of the operation's outcome
 */
public class InputController implements Serializable{
    private static final long serialVersionUID = 1L;
    private Mode game;
    private transient Map<String, VirtualView> virtualViewMap;
    private GameController gameController;

    /**
     * Constructor of the Input Controller Class.
     *
     * @param virtualViewMap Virtual View Map.
     * @param gameController Game Controller.
     */
    public InputController(Map<String, VirtualView> virtualViewMap, GameController gameController, Mode game) {
        this.game = game;
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }

    public void setGame(Mode game) {
        this.game = game;
    }

    /**
     * Verify data sent by client to server.
     *
     * @param message Message from Client.
     * @return {code @true} if Message contains valid data {@code false} otherwise.
     */
    public boolean verifyReceivedData(Message message) {

        switch (message.getMessageType()) {
            case LOGIN_REPLY://server does not receive a LOGIN_REPLY.
                return false;
            case PLAYERNUMBER_REPLY:
                return playerNumberReplyCheck(message);
            case MOVE_ON_BOARD:
                return moveOnBoard(message);
            case MOVE_ON_ISLAND:
                return moveOnIsland(message);
            case MOVE_MOTHER:
                return moveMotherCheck(message);
            case GAMEMODE_REPLY:
                return gameModeReplyCheck(message);
            case PICK_CLOUD:
            case GET_FROM_CLOUD:
                return pickCloudCheck(message);
            case DRAW_ASSISTANT:
                return drawAssistantCheck(message);
            case INIT_TOWERS:
                return checkInitTower(message);
            case INIT_DECK:
                return checkInitDeck(message);
            case INIT_GAMEBOARD:
                return true;
            case USE_EXPERT:
                return checkExpert(message);
            default:
                return false;

        }

    }

    /**
     * validity check for the player's nickname
     * @param nickname the player's nickname
     * @param view the player's view
     * @return boolean value for the operation's outcome
     */
    public boolean checkLoginNickname(String nickname, View view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase(EasyGame.SERVER_NICKNAME)) {
            view.showGenericMessage("Forbidden name.");
            view.showLoginResult(false, true, null);
            return false;
        }
        else if(isNull(game)){
            return true;
        }else if (game.isNicknameTaken(nickname)) {
            view.showGenericMessage("Nickname already taken.");
            view.showLoginResult(false, true, null);
            return false;
        }
        return true;
    }

    /**
     * validity check for the player number
     * @param message message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean playerNumberReplyCheck(Message message) {
        PlayerNumberReply playerNumberReply = (PlayerNumberReply) message;
        if (playerNumberReply.getPlayerNumber() <= 3 && playerNumberReply.getPlayerNumber() > 1) {
            return true;
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.askPlayersNumber();
            return false;
        }
    }

    /**
     * validity check for the board move
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean moveOnBoard(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        MoveMessage moveMessage = ((MoveMessage) message);
        Color chosenColor = moveMessage.getColor();
        String activePlayerNickname = gameController.getTurnController().getActivePlayer();
        Dashboard activePlayerDashboard = game.getPlayerByNickname(activePlayerNickname).getDashboard();
        List<Color> colors=new ArrayList<>();
        for(Student s : game.getPlayerByNickname(activePlayerNickname).getDashboard().getHall()){
            colors.add(s.getColor());
        }
        if (!(colors.contains(chosenColor))) {
            virtualView.showGenericMessage("There are no " + chosenColor + " in the hall");
            virtualView.askMoves(activePlayerDashboard.getHall(), game.getGameBoard().getIslands());
            return false;
        } else if (activePlayerDashboard.getRow(chosenColor).getNumOfStudents() == 10) {
            virtualView.showGenericMessage("The chosen row is full");
            virtualView.askMoves(activePlayerDashboard.getHall(), game.getGameBoard().getIslands());
            return false;
        } else {
            return true;
        }
    }

    /**
     * validity check for the island move
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean moveOnIsland(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        MoveMessage moveMessage = ((MoveMessage) message);
        Color chosenColor = moveMessage.getColor();
        String activePlayerNickname = gameController.getTurnController().getActivePlayer();
        Dashboard activePlayerDashboard = game.getPlayerByNickname(activePlayerNickname).getDashboard();
        int chosenIndex = moveMessage.getIndex();
        if (!(activePlayerDashboard.getHall().contains(new Student(chosenColor)))) {
            virtualView.showGenericMessage("There are no" + chosenColor + "in the hall");
            virtualView.askMoves(activePlayerDashboard.getHall(), game.getGameBoard().getIslands());
            return false;
        } else if (chosenIndex > (game.getGameBoard().getIslands().size() - 1) || chosenIndex < 0) {
            virtualView.showGenericMessage("Index out Bound ");
            virtualView.askMoves(activePlayerDashboard.getHall(), game.getGameBoard().getIslands());//non sono sicuro di questo metodo in questa posizione
            return false;
        } else {
            return true;
        }
    }

    /**
     * validity check for the gameMode
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean gameModeReplyCheck(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        GameModeReply gameModeReply = ((GameModeReply) message);
        modeEnum mode = gameModeReply.getGameMode();
        if (!mode.equals(modeEnum.EASY) && !mode.equals(modeEnum.EXPERT)) {
            virtualView.showGenericMessage("Wrong mode");
            virtualView.askGameMode(message.getNickname() , modeEnum.availableGameModes());
            return false;
        } else {
            return true;
        }

    }

    /**
     * validity check for the cloud's choice
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean pickCloudCheck(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        PickCloudMessage pickCloudMessage = ((PickCloudMessage) message);
        int chosenIndex = pickCloudMessage.getCloudIndex();
        if (gameController.getTurnController().getMainPhase().equals(MainPhase.PLANNING)) {
            if ((chosenIndex < 0 || chosenIndex >= game.getChosenPlayerNumber())) {
                virtualView.showGenericMessage("Index out Bound ");
            } else {
                for (Cloud cloud : game.getEmptyClouds()) {
                    if (cloud.getIndex() == chosenIndex) {
                        return true;
                    }
                }
                virtualView.showGenericMessage("Already Picked");
            }
            virtualView.askCloud(message.getNickname(), game.getGameBoard().getClouds());
            return false;
        } else {
            if ((chosenIndex < 0 || chosenIndex >= game.getChosenPlayerNumber())) {
                virtualView.showGenericMessage("Index out Bound ");
                virtualView.askCloud(message.getNickname(), game.getGameBoard().getClouds());
                return false;
            } else {
                for (Cloud cloud : game.getEmptyClouds()) {
                    if (cloud.getIndex() == chosenIndex) {
                        virtualView.showGenericMessage("Empty cloud");
                        virtualView.askCloud(message.getNickname(), game.getGameBoard().getClouds());
                        return false;
                    }
                }
                return true;
            }
        }
    }

    /**
     * validity check for the assistant card
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean drawAssistantCheck(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        AssistantMessage assistantMessage = ((AssistantMessage) message);
        Assistant chosenAssistant = assistantMessage.getAssistant();
        String activePlayerNickname = gameController.getTurnController().getActivePlayer();
        Deck activePlayerDeck = game.getPlayerByNickname(activePlayerNickname).getDeck();
        for(Assistant assistant : activePlayerDeck.getCards()){
            if(assistant.getNumOrder() == chosenAssistant.getNumOrder())
                return true;
        }
        virtualView.showGenericMessage("The chosen card is not present in the deck");
        virtualView.askAssistant(message.getNickname(),gameController.getTurnController().getChosen());
        return false;


    }

    /**
     * validity check for the mother moves
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean moveMotherCheck(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        MoveMotherMessage moveMotherMessage = ((MoveMotherMessage) message);
        int chosenMoves = moveMotherMessage.getMoves();
        Assistant chosenAssistant = moveMotherMessage.getChosenAssistant();
        int extra = 0;
        for(Character c : gameController.getTurnController().getToReset())
        {
            if(c.getName().equals(ExpertDeck.GAMBLER)){
                extra = 2;

            }
        }
        if (chosenMoves > 0 && chosenMoves <= chosenAssistant.getMove()+extra) {
                return true;
            } else {
                virtualView.showGenericMessage("move not allowed");
                virtualView.askMotherMoves(message.getNickname(),chosenAssistant.getMove()+extra);
                return false;
            }
        }


    /**
     * validity check for the user
     * @param receivedMessage the message received from client
     * @return boolean value for the operation's outcome
     */
    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getNickname().equals(gameController.getTurnController().getActivePlayer());
    }

    /**
     * validity check for expert card
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean checkExpert(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        ExpertMessage expertMessage = ((ExpertMessage) message);
        ExpertDeck chosen = expertMessage.getCard();
        Dashboard activePlayerDashboard = game.getPlayerByNickname(message.getNickname()).getDashboard();
        if (game.getGameBoard().getExperts().contains(chosen)) {
            return true;
        } else if(chosen.equals(ExpertDeck.NULL)){
            virtualView.askMoves(activePlayerDashboard.getHall(), game.getGameBoard().getIslands());
            return false;
        }
        else{
            virtualView.showGenericMessage("Expert not available");
            virtualView.askMoves(activePlayerDashboard.getHall(), game.getGameBoard().getIslands());
            return false;
        }

    }

    /**
     * validity check for the tower
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean checkInitTower(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        TowerMessage towerMessage = ((TowerMessage) message);
        Type chosenTower = towerMessage.getType();
        if (chosenTower.equals(Type.BLACK) || chosenTower.equals(Type.GREY) || chosenTower.equals(Type.WHITE)) {
            if (!Type.isChosen(chosenTower)) {
                return true;
            } else {
                virtualView.showGenericMessage("Tower not available");
                virtualView.askInitType(message.getNickname() , Type.notChosen());
                return false;
            }
        } else {
            virtualView.showGenericMessage("There's no such tower");
            virtualView.askInitType(message.getNickname() , Type.notChosen());
            return false;
        }

    }

    /**
     * validity check for the chosen deck
     * @param message the message sent from client
     * @return boolean value for the operation's outcome
     */
    public boolean checkInitDeck(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        DeckMessage deckMessage = ((DeckMessage) message);
        Mage chosenDeck = deckMessage.getMage();
        if (chosenDeck.equals(Mage.MAGE) || chosenDeck.equals(Mage.FAIRY) || chosenDeck.equals(Mage.ELF) || chosenDeck.equals(Mage.DRAGON)) {
            if (!Mage.isChosen(chosenDeck)){
                return true;
            }else{
                virtualView.showGenericMessage(("Deck not available"));
                virtualView.askInitDeck(message.getNickname() ,Mage.notChosen());
                return false;
            }

            }else{
            virtualView.showGenericMessage("There's no such deck");
            virtualView.askInitDeck(message.getNickname() ,Mage.notChosen());
            return false;
        }

    }

}