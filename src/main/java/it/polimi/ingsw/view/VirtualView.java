package it.polimi.ingsw.view;

import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

public class VirtualView implements View{
    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    @Override
    public void askNickname() {
        clientHandler.sendMessage(new LoginReply(false, true));
    }

    @Override
    public void askStart() {

    }

    //this is for the choice of the deck at the beginning of the game
    //JUST A QUESTION what should go instead of gamefactory??? I'm a bit lost...
    @Override
    public void askInitDeck(List<Mage> availableDecks) {
        clientHandler.sendMessage(new DeckMessage(GameFactory.SERVER_NICKNAME, MessageType.INIT_DECK, availableDecks));
    }

    @Override
    public void askAssistant(List<Assistant> availableAssistants){
        clientHandler.sendMessage(new AssistantMessage(GameFactory.SERVER_NICKNAME, MessageType.DRAW_ASSISTANT, availableAssistants));
    }

    @Override
    public void askMovingPaw(List<Student> availableStudents) {
        clientHandler.sendMessage(new MoveMessage(GameFactory.SERVER_NICKNAME, MessageType.MOVE, availableStudents));
    }

    //guys i think we should have TWO METHODS for moving the students around
    //one for moving the students on the islands and the other one to move the students on the proper row
    //TODO
    @Override
    public void askMoves(List<Position> availablePosition) {
        clientHandler.sendMessage(new MoveMessage(GameFactory.SERVER_NICKNAME, MessageType.MOVE_ON_BOARD, availablePosition));
    }

    @Override
    public void askMotherMoves(int possibleSteps) {
        clientHandler.sendMessage(new MoveMotherMessage(GameFactory.SERVER_NICKNAME, MessageType.MOVE_MOTHER, possibleSteps));
    }

    @Override
    public void askCloud(List<Cloud> availableClouds) {
        clientHandler.sendMessage(new PickCloudMessage(GameFactory.SERVER_NICKNAME, MessageType.GET_FROM_CLOUD, availableClouds));
    }

    //TODO
    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberReply(GameFactory.SERVER_NICKNAME, MessageType.PLAYERNUMBER_REPLY, ???));
    }

    //TODO
    @Override
    public void askGameMode() {
        clientHandler.sendMessage(new GameModeReply(GameFactory.SERVER_NICKNAME, MessageType.GAMEMODE_REPLY, ???));
    }

    //TODO
    @Override
    public void askInitType(List<Type> teams) {
        clientHandler.sendMessage()
    }

    @Override
    //TODO ??? is this thing right?????????
    public void showGenericMessage(String genericMessage) {
           System.out.println(genericMessage);
    }

    //TODO
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionResult, String nickname) {
    }

    //TODO
    @Override
    public void showDisconnectionMessage(String playerDisconnected) {
    }

    @Override
    public void errorCommunicationAndExit(String nickname) {

    }

    @Override
    public void effectEnabled(String summoner) {

    }

    @Override
    public void showMatchInfo() {

    }

    @Override
    public void winCommunication(Message winMessage, String winner) {

    }
}
