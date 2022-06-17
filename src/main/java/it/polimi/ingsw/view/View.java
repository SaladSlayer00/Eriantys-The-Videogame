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

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/* this is the general view interface that contains (it should at least) all the methods that are going to
* be used by the various view types (such as cli and friends)
* in general these are all the various stuffs needed for user/game communications (kinda)
 */

public interface View {

    //asks the player to choose a nickname
    void askNickname();


    //player reply and request from server to start game
    void askStart(String nickname, String answer);

    //asks the player to choose a deck from the ones available
    //the parameter of the method is the list of the deck which the player can choose from
    void askInitDeck(String nickname, List<Mage> availableDecks);

    //asks the player to choose an assistant's card from the ones that are still in the deck
    //the parameter of the method is the list of the cards in the deck which the player can choose from
    void askAssistant(String nickname, List<Assistant> availableAssistants);


    //asks where the player wants to move the paw they have chosen
    //the parameter is the list of admitted positions
    void askMoves(List<Student> students, List<Island> islands);

    //asks the player where on the islands they want to move the students
    void askIslandMoves(Color student, List<Island> islands);


    //asks the player how many steps they want Mother Nature to do
    //the parameter of the method is the number of admitted steps (???)
    void askMotherMoves(String nickname, int possibleSteps);

    //asks the player which cloud they want to pick the students from
    //the parameter of the method is the list of the remaining cloud
    void askCloud(String nickname, List<Cloud> availableClouds);

    //asks for the number of players that are going to play
    void askPlayersNumber();

    //asks for the game mode
    void askGameMode(String nickname, List<modeEnum> gameModes);

    void askInitType(String nickname, List<Type> teams);

    /* THESE THINGS NEED TO BE DISCUSS A BIT I THINK!!!
    * TODO
    * then we should implement all the various things that have to do with the exceptions, connections and these
    * stuffs.
    * These stuffs include
    *  - successful login                                            TO BE CHECKED ((okay???))
    *  - disconnection message                                       TO BE CHECKED ((okay???))
    *  - error and exit                                              TO BE CHECKED
    *  - lobby (?)                                                   TO BE WRITTEN
    *  - enable effects (for the expert mode????)                    TO BE DISCUSSED THIS THINGGGGG
    *  - match infos                                                 TO BE CHECKED
    *  - win message                                                 TO BE CHECKED
    *
    * we should also consider the idea to do two different methods for the action of moving the students
    * one method for moving the students on an island and another one for moving the students in the right row!!!
    *
    * the main problem in these methods are the parameter 'cuz I don't know if they are right!!!!!
     */

    // here the beginning of the implementation of the methods listed above
    //sends a generic message
    void showGenericMessage(String genericMessage);

    //communicates the successful login of the player (should check the order of the parameters!!!)
    //the parameters are two boolean and the nickname of the player
    void showLoginResult(boolean nicknameAccepter, boolean connectionResult, String nickname);

    //TODO
    //communicates the disconnection of the player
    //the parameters are the nicknames of the player who has been disconnected and a boolean (???)
    //maybe it needs another parameter???
    default void showDisconnectionMessage(String playerDisconnected, String text) {
    }

    //TODO
    //communicates that an error has occurred and exits form the game
    //the parameter is the nick of the player who's going to be kicked out (???)
    //same thing here as for showDisconnectionMessage: should we add a parameter?????
    void errorCommunicationAndExit(String nickname);

    //TODO
    //communicates that the wanted effect has been activated
    //tbh i'm not 100% sure of this method because i don't really know if this is going to work out for our code...
    //also a thing to pass the card that has been summoned???????????
    //should we add a parameter here too????????
    void effectEnabled(String summoner);

    //TODO
    //shows the information of the match played
    //the parameters should be a list of players and things like these... i think???
    void showMatchInfo(int chosen, int actual);

    void showMatchInfo(List<String> activePlayers , String activePlayerNickname);


    //TODO
    //communicates to the winner they have won
    //should we pass the nick of the player or the REAL player???
    //this should be right (((i think)))
    void winCommunication(String winner);

    //TODO
    //tells the player which player has won
    void showWinMessage(String winner);

    void updateTable(Gameboard gameboard, List<Dashboard> dashboards) throws noTowersException;

    //shows the assistants chosen
    void showAssistant(int index);

    void showLobby(List<String> nicknameList, int maxPlayers);

    void askExpert();

}
