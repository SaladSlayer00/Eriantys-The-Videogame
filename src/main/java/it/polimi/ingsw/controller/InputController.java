package it.polimi.ingsw.controller;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.EasyGame;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.playerBoard.Row;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.model.enums.modeEnum;
import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//This class verifies that all messages sent by client contain valid information
public class InputController {
    private static final long serialVersionUID = 1L;
    private EasyGame game;
    private transient Map<String , VirtualView> virtualViewMap;
    private GameController gameController;

    /**
     * Constructor of the Input Controller Class.
     *
     * @param virtualViewMap Virtual View Map.
     * @param gameController Game Controller.
     */
    public InputController(Map<String ,VirtualView> virtualViewMap , GameController gameController, EasyGame game){
        this.game = game;
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }
    /**
     * Verify data sent by client to server.
     *
     * @param message Message from Client.
     * @return {code @true} if Message contains valid data {@code false} otherwise.
     */
    public boolean verifyReceivedData(Message message) {

        switch(message.getMessageType()) {
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
                return pickCloudCheck(message);
            case DRAW_ASSISTANT:
                return drawAssistantCheck(message);
            case INIT_TOWERS:
                return false;
            default:
                return false;

        }

    }

    public boolean checkLoginNickname(String nickname, View view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase(EasyGame.SERVER_NICKNAME)) {
            view.showGenericMessage("Forbidden name.");
            view.showLoginResult(false, true, null);
            return false;
        } else if (game.isNicknameTaken(nickname)) {
            view.showGenericMessage("Nickname already taken.");
            view.showLoginResult(false, true, null);
            return false;
        }
        return true;
    }

    public boolean playerNumberReplyCheck(Message message) {
        PlayerNumberReply playerNumberReply = (PlayerNumberReply) message;
        if (playerNumberReply.getPlayerNumber() < 3 && playerNumberReply.getPlayerNumber() > 1){
            return true;
        }else{
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.askPlayersNumber();
            return false;
        }
    }

    public boolean moveOnBoard(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        MoveMessage moveMessage = ((MoveMessage) message );
        Color chosenColor = moveMessage.getColor();
        String activePlayerNickname = gameController.getTurnController().getActivePlayer();
        Dashboard activePlayerDashboard = game.getPlayerByNickname(activePlayerNickname).getDashboard();
        if(!(activePlayerDashboard.getHall().contains(new Student(chosenColor)))){
            virtualView.showGenericMessage("There are no" +chosenColor+"in the hall");
            virtualView.askMovingPaw(activePlayerDashboard.getHall());
            return false;
        } else if(activePlayerDashboard.getRow(chosenColor).getNumOfStudents() == 10){
            virtualView.showGenericMessage("The chosen row is full");
            virtualView.askMovingPaw(activePlayerDashboard.getHall());
            return false;
        }else{
            return true;
        }
    }

    public  boolean moveOnIsland(Message message){
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        MoveMessage moveMessage = ((MoveMessage) message);
        Color chosenColor = moveMessage.getColor();
        String activePlayerNickname = gameController.getTurnController().getActivePlayer();
        Dashboard activePlayerDashboard = game.getPlayerByNickname(activePlayerNickname).getDashboard();
        int chosenIndex = moveMessage.getIndex();
        if(!(activePlayerDashboard.getHall().contains(new Student(chosenColor)))){
            virtualView.showGenericMessage("There are no" +chosenColor+"in the hall");
            virtualView.askMovingPaw(activePlayerDashboard.getHall());
            return false;
        }else if(chosenIndex > (game.getGameBoard().getIslands().size()-1) || chosenIndex < 0 ){
            virtualView.showGenericMessage("Index out Bound ");
            virtualView.askMovingPaw(activePlayerDashboard.getHall());//non sono sicuro di questo metodo in questa posizione
            return false;
        }else {
            return true;
        }
    }

    public boolean gameModeReplyCheck(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        GameModeReply gameModeReply = ((GameModeReply) message);
        modeEnum mode = gameModeReply.getGameMode();
        if(!gameModeReply.equals(modeEnum.EASY) && !gameModeReply.equals(modeEnum.EXPERT)){
            virtualView.showGenericMessage("Wrong mode");
            virtualView.askGameMode();
            return false;
        }else{
            return true;
        }

}

    public boolean pickCloudCheck(Message message){
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        PickCloudMessage pickCloudMessage = ((PickCloudMessage) message);
        int chosenIndex = pickCloudMessage.getCloudIndex();
        if(chosenIndex<0 || chosenIndex>game.getChosenPlayersNumber()){
            virtualView.showGenericMessage("Index out Bound ");
            virtualView.askCloud(game.getGameBoard().getClouds());
            return false;
        }else{
            return true;
        }
    }

    public boolean drawAssistantCheck(Message message){
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        AssistantMessage assistantMessage = ((AssistantMessage) message);
        Assistant chosenAssistant = assistantMessage.getAssistant();
        String activePlayerNickname = gameController.getTurnController().getActivePlayer();
        Deck activePlayerDeck = game.getPlayerByNickname(activePlayerNickname).getDeck();
        if(activePlayerDeck.getCards().contains(chosenAssistant)){
            return true;
        }else{
            virtualView.showGenericMessage("The chosen card is not present in the deck");
            virtualView.askAssistant(activePlayerDeck.getCards());
            return false;
        }
    }

    public boolean moveMotherCheck(Message message){
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        MoveMotherMessage moveMotherMessage = ((MoveMotherMessage) message);
        int chosenMoves = moveMotherMessage.getMoves();
        Assistant chosenAssistant = moveMotherMessage.getChosenAssistant();
        if(chosenMoves > 0 && chosenMoves <= chosenAssistant.getMove()){
            return true;
        }else{
            virtualView.showGenericMessage("move not allowed");
            virtualView.askMotherMoves(chosenAssistant.getMove());
            return false;
        }

    }
    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getNickname().equals(gameController.getTurnController().getActivePlayer());
    }



}
