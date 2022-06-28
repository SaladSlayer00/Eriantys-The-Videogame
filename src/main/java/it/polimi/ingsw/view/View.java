package it.polimi.ingsw.view;

import it.polimi.ingsw.exceptions.noTowersException;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Gameboard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.playerBoard.Dashboard;
import it.polimi.ingsw.model.Player;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** The general view interface that contains (it should at least) all the methods that are going to
* be used by the various view types (such as cli and gui)
* in general these are all the various stuffs needed for user/game communications.
 */

public interface View {

    /** Asks the player to choose a nickname
     *
     */
    void askNickname();

    /** Player reply and request from server to start game
     *
     * @param nickname player's nickname
     * @param answer player's answer
     */
    void askStart(String nickname, String answer);

    /**
     * Asks the player to choose a deck from the ones available
     * the parameter of the method is the list of the deck which the player can choose from.
     *
     * @param nickname the player's nickname
     * @param availableDecks the decks available for choosing
     */
    void askInitDeck(String nickname, List<Mage> availableDecks);

    /**
     * Asks the player to choose an assistant's card from the ones that are still in the deck
     * the parameter of the method is the list of the cards in the
     * deck which the player can choose from.
     *
     * @param nickname the player's nickname
     * @param availableAssistants the assistants available for choosing
     */

     void askAssistant(String nickname, List<Assistant> availableAssistants);


    /**
     * Asks where the player wants to move the paw they have chosen
     * the parameter is the list of admitted positions
     *
     * @param students the list of available students
     * @param islands the list of islands
     */
    void askMoves(List<Student> students, List<Island> islands);

    /**
     * Asks the player where on the islands they want to move the students
     *
     * @param student the color of the selected student
     * @param islands the list of islands
     */
    void askIslandMoves(Color student, List<Island> islands);


    /**
     * Asks the player where on the islands they want to move the students
     *
     * @param nickname the player's nickname
     * @param possibleSteps the possible steps mother nature can do
     */
    void askMotherMoves(String nickname, int possibleSteps);

    /**
     * Asks the player which cloud they want to pick the students from
     * the parameter of the method is the list of the remaining cloud
     *
     * @param nickname the player's nickname
     * @param availableClouds the list of available clouds
     */
    void askCloud(String nickname, List<Cloud> availableClouds);

    /**
     * Asks for the number of players that are going to play
     */
    void askPlayersNumber();

    /**
     * Asks for the game mode
     *
     * @param nickname the player's nickname
     * @param gameModes the modes of the game
     */
    void askGameMode(String nickname, List<modeEnum> gameModes);

    /**
     * Asks the player toselect a team
     *
     * @param nickname the player's nickname
     * @param teams the list of available teams
     */
    void askInitType(String nickname, List<Type> teams);


     /**
      * Sends a generic message
      *
     * @param genericMessage
     */
    void showGenericMessage(String genericMessage);

    /**
     * Communicates the successful login of the player (should check the order of the parameters!!!)
     * the parameters are two boolean and the nickname of the player
     *
     * @param nicknameAccepter if the nickname's accepted
     * @param connectionResult if the connection's successful
     * @param nickname player's nickname
     */
    void showLoginResult(boolean nicknameAccepter, boolean connectionResult, String nickname);

    /**Communicates the disconnection of the player
     *
     * @param playerDisconnected disconnected player
     * @param text message to send
     */
    default void showDisconnectionMessage(String playerDisconnected, String text) {
    }

    /** Communicates that an error has occurred and exits form the game
     *
     * @param nickname the player's nickname
     */
    void errorCommunicationAndExit(String nickname);

    /**Shows the information of the match played
     *
     * @param chosen players chosen
     * @param actual active players
     */
    void showMatchInfo(int chosen, int actual);

    /**
     * Second verson with every player present
     * @param activePlayers list of active player
     * @param activePlayerNickname list of nickname of active player
     */
    void showMatchInfo(List<String> activePlayers , String activePlayerNickname);

    /** Tells the player which player has won
     *
     * @param winner nickname of winner
     */
    void showWinMessage(String winner);

    /**
     * Method to update the needed parameters to show the gameboard and dashboard status from the cli
     *
     * @param gameboard the instance of the gameoard
     * @param dashboards the instance of the dashboards
     * @param players the player list
     * @throws noTowersException if there's no towers on the dashboard
     */
    void updateTable(Gameboard gameboard, List<Dashboard> dashboards,List<Player> players) throws noTowersException;

    /**
     * Shows the players in the lobby before the game starts
     *
     * @param nicknameList list of nicknames for players
     * @param maxPlayers max number of players available
     */
    void showLobby(List<String> nicknameList, int maxPlayers);

    /**
     * Asks to input an expert card
     */
    void askExpert();

    /**
     * Asks to input a color
     */
    void askColor();

}
