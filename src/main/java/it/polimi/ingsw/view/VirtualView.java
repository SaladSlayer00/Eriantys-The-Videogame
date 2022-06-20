package it.polimi.ingsw.view;

import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.observation.BoardMessage;
import it.polimi.ingsw.message.observation.showAssistantMessage;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.EasyGame;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class VirtualView implements View, Observer {
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
    public void askStart(String nickname, String answer) {
        clientHandler.sendMessage(new StartMessage(nickname, answer));
    }

    //this is for the choice of the deck at the beginning of the game
    //JUST A QUESTION what should go instead of gamefactory??? I'm a bit lost...
    @Override
    public void askInitDeck(String nickname, List<Mage> availableDecks) {
        clientHandler.sendMessage(new DeckMessageRequest(nickname, availableDecks));
    }

    @Override
    public void askAssistant(String nickname, List<Assistant> availableAssistants) {
        clientHandler.sendMessage(new AssistantMessageRequest(nickname, availableAssistants));
    }



    @Override
    public void askMoves(List<Student> students, List<Island> islands) {
        clientHandler.sendMessage(new AskMoveMessage(EasyGame.SERVER_NICKNAME, students, islands));
    }

    @Override
    public void askIslandMoves(Color student, List<Island> islands){}
    @Override
    public void askMotherMoves(String nickname, int possibleSteps) {
        clientHandler.sendMessage(new MoveMotherMessage(nickname, possibleSteps, new Assistant(0, possibleSteps)));
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
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }


    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionResult, String nickname) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionResult));
    }

    //TODO
    @Override
    public void showDisconnectionMessage(String playerDisconnected, String text) {
    }

    @Override
    public void errorCommunicationAndExit(String nickname) {
        clientHandler.sendMessage(new ErrorMessage(nickname, "Error comunication"));

    }

    @Override
    public void effectEnabled(String summoner) {

    }

    @Override
    public void showMatchInfo(int chosen, int actual) {
        clientHandler.sendMessage(new MatchInfoMessage(chosen,actual));
    }

    public void showMatchInfo(List<String> activePlayers , String activePlayerNickname) {
        clientHandler.sendMessage(new MatchInfoMessage(EasyGame.SERVER_NICKNAME , MessageType.MATCH_INFO,activePlayers , activePlayerNickname));

    }

    @Override
    public void winCommunication(String winner) {

    }

    @Override
    public void update(Message message){
        clientHandler.sendMessage(message);
    }

    @Override
    public void showWinMessage(String winner) {
        clientHandler.sendMessage(new WinMessage(winner));
    }


    @Override
    public void updateTable(Gameboard gameBoard, List<Dashboard> dashboards,List<Player> players){
        clientHandler.sendMessage(new BoardMessage(gameBoard,dashboards,players));
    }

    public void showAssistant(int index){
        clientHandler.sendMessage(new showAssistantMessage(index));
    }

    @Override
    public void showLobby(List<String> nicknameList, int maxPlayers) {
        clientHandler.sendMessage(new LobbyMessage(nicknameList,maxPlayers));
    }

    public void askExpert(){
        clientHandler.sendMessage(new ExpertMessage(EasyGame.SERVER_NICKNAME, null));
    }

    public void askColor(){
        clientHandler.sendMessage((new ColorMessage(EasyGame.SERVER_NICKNAME, null)));
    }
}
