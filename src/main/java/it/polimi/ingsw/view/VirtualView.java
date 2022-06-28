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

/**
 * Instance of virtual view associated to the player that the server side
 * uses to simulate the actions it has on the client. Implements the main methods to act on
 * the real view.
 */

public class VirtualView implements View, Observer {
    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }


    /**
     * Asks the player for a nickname.
     */
    @Override
    public void askNickname() {
        clientHandler.sendMessage(new LoginReply(false, true));
    }

    /**
     * Asks the player for a start answer.
     *
     * @param nickname player's nickname
     * @param answer player's answer
     */
    @Override
    public void askStart(String nickname, String answer) {
        clientHandler.sendMessage(new StartMessage(nickname, answer));
    }

    /**
     * Asks the player for the deck choice.
     *
     * @param nickname the player's nickname
     * @param availableDecks the decks available for choosing
     */
    @Override
    public void askInitDeck(String nickname, List<Mage> availableDecks) {
        clientHandler.sendMessage(new DeckMessageRequest(nickname, availableDecks));
    }

    /**
     * Asks the player to choose an assistant from those available.
     *
     * @param nickname the player's nickname
     * @param availableAssistants the assistants available for choosing
     */
    @Override
    public void askAssistant(String nickname, List<Assistant> availableAssistants) {
        clientHandler.sendMessage(new AssistantMessageRequest(nickname, availableAssistants));
    }


    /**
     * Asks the player for the student moves.
     *
     * @param students the list of available students
     * @param islands the list of islands
     */
    @Override
    public void askMoves(List<Student> students, List<Island> islands) {
        clientHandler.sendMessage(new AskMoveMessage(EasyGame.SERVER_NICKNAME, students, islands));
    }

    /**
     * Asks the player for the island moves.
     *
     * @param student the color of the selected student
     * @param islands the list of islands
     */
    @Override
    public void askIslandMoves(Color student, List<Island> islands){}

    /**
     * Asks the player for the mother nature moves.
     *
     * @param nickname the player's nickname
     * @param possibleSteps the possible steps mother nature can do
     */
    @Override
    public void askMotherMoves(String nickname, int possibleSteps) {
        clientHandler.sendMessage(new MoveMotherMessage(nickname, possibleSteps, new Assistant(0, possibleSteps)));
    }

    /**
     * Asks the player for the cloud selection.
     *
     * @param nickname the player's nickname
     * @param availableClouds the list of available clouds
     */
    @Override
    public void askCloud(String nickname, List<Cloud> availableClouds) {
        clientHandler.sendMessage(new PickCloudMessageRequest(nickname, availableClouds));
    }

    /**
     * Asks the player for the number of clients they want to have connected.
     */
    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    /**
     * Asks the player to input the preferred gameMode.
     *
     * @param nickname the player's nickname
     * @param gameModes the modes of the game
     */
    @Override
    public void askGameMode(String nickname, List<modeEnum> gameModes) {
        clientHandler.sendMessage(new GameModeRequest(nickname, gameModes));
    }

    /**
     * Asks the player for the tower color.
     *
     * @param nickname the player's nickname
     * @param teams the list of available teams
     */
    @Override
    public void askInitType(String nickname, List<Type> teams){
        clientHandler.sendMessage(new TowerMessageRequest(nickname, teams));
    }

    /**
     * Sends the player a generic message in text format.
     *
     * @param genericMessage
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    /**
     * Shows the login outcome to the player.
     *
     * @param nicknameAccepted
     * @param connectionResult if the connection's successful
     * @param nickname player's nickname
     */
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionResult, String nickname) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionResult));
    }


    @Override
    public void showDisconnectionMessage(String playerDisconnected, String text) {
    }

    /**
     * Ends the comunication when an error occurs.
     *
     * @param nickname the player's nickname
     */
    @Override
    public void errorCommunicationAndExit(String nickname) {
        clientHandler.sendMessage(new ErrorMessage(nickname, "Error comunication"));

    }


    /**
     * Shows the info for the match.
     *
     * @param chosen players chosen
     * @param actual active players
     */
    @Override
    public void showMatchInfo(int chosen, int actual) {
        clientHandler.sendMessage(new MatchInfoMessage(chosen,actual));
    }

    /**
     * Shows the info for the match (second version).
     *
     * @param activePlayers list of active player.
     *
     * @param activePlayerNickname list of nickname of active player
     */
    @Override
    public void showMatchInfo(List<String> activePlayers , String activePlayerNickname) {
        clientHandler.sendMessage(new MatchInfoMessage(EasyGame.SERVER_NICKNAME , MessageType.MATCH_INFO,activePlayers , activePlayerNickname));

    }


    /**
     * Sends update to the view as observer.
     *
     *
     * @param message the message sent from the server
     */
    @Override
    public void update(Message message){
        clientHandler.sendMessage(message);
    }

    /**
     * Shows the winner.
     *
     * @param winner nickname of winner
     */
    @Override
    public void showWinMessage(String winner) {
        clientHandler.sendMessage(new WinMessage(winner));
    }


    /**
     * Updates the table allowing the view to show it correctly.
     *
     * @param gameBoard the gameboard
     * @param dashboards the instance of the dashboards
     * @param players the player list
     */
    @Override
    public void updateTable(Gameboard gameBoard, List<Dashboard> dashboards,List<Player> players){
        clientHandler.sendMessage(new BoardMessage(gameBoard,dashboards,players));
    }

    /**
     * Shows the players connected before the game starts.
     *
     * @param nicknameList list of nicknames for players
     * @param maxPlayers max number of players available
     */
    @Override
    public void showLobby(List<String> nicknameList, int maxPlayers) {
        clientHandler.sendMessage(new LobbyMessage(nicknameList,maxPlayers));
    }

    /**
     * Asks the player for an expert card.
     *
     */
    public void askExpert(){
        clientHandler.sendMessage(new ExpertMessage(EasyGame.SERVER_NICKNAME, null));
    }

    /**
     * Asks the player to input a color.
     */
    public void askColor(){
        clientHandler.sendMessage((new ColorMessage(EasyGame.SERVER_NICKNAME, null)));
    }
}
