package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;
import it.polimi.ingsw.model.enums.modeEnum;
import it.polimi.ingsw.server.ClientHandler;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

public class VirtualView implements View{
    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler(){
        return clientHandler;
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
    public void askInitDeck(String nickname, List<Mage> availableDecks) {
        clientHandler.sendMessage(new DeckMessageRequest(nickname, availableDecks));
    }

    @Override
    public void askAssistant(String nickname, List<Assistant> availableAssistants){
        clientHandler.sendMessage(new AssistantMessageRequest(nickname, availableAssistants));
    }

    @Override
    public void askMovingPaw(List<Student> availableStudents) {
    }

    //guys i think we should have TWO METHODS for moving the students around
    //one for moving the students on the islands and the other one to move the students on the proper row
    //TODO
    @Override
    public void askMoves(List<Student> students) {
    }

    @Override
    public void askMotherMoves(int possibleSteps) {
    }

    @Override
    public void askCloud(String nickname, List<Cloud> availableClouds) {
        clientHandler.sendMessage(new PickCloudMessageRequest(nickname, availableClouds));
    }

    //TODO
    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    //TODO
    @Override
    public void askGameMode(String nickname, List<modeEnum> gameModes) {
        clientHandler.sendMessage(new GameModeRequest(nickname, gameModes));
    }

    //TODO
    @Override
    public void askInitType(String nickname, List<Type> teams){
        clientHandler.sendMessage(new TowerMessageRequest(nickname, teams));
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
    public void showDisconnectionMessage(String playerDisconnected, String text) {
    }

    @Override
    public void errorCommunicationAndExit(String nickname) {

    }

    @Override
    public void effectEnabled(String summoner) {

    }

    @Override
    public void showMatchInfo(int chosen, int actual) {
        clientHandler.sendMessage(new MatchInfoMessage(chosen,actual));
    }

    @Override
    public void winCommunication(Message winMessage, String winner) {

    }
}
