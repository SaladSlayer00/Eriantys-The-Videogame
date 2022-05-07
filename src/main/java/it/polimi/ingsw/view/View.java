package it.polimi.ingsw.view;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enums.Mage;
import it.polimi.ingsw.model.enums.Type;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

/* this is the general view interface that contains (it should at least) all the methods that are going to
* be used by the various view types (such as cli and friends)
* in general these are all the various stuffs needed for user/game communications (kinda)
 */

public interface View {

    //asks the player to choose a nickname
    void askNickname();

    void askStart();

    //asks the player to choose a deck from the ones available
    //the parameter of the method is the list of the deck which the player can choose from
    void askInitDeck(ArrayList<Mage> availableDecks);

    //asks the player to choose an assistant's card from the ones that are still in the deck
    //the parameter of the method is the list of the cards in the deck which the player can choose from
    void askAssistant(List<Assistant> availableAssistants);

    //asks the player which paw (student) they want to move
    //the parameter of the method is the list of the students that the player can actually move
    void askMovingPaw(List<Student> availableStudents);

    //asks where the player wants to move the paw they have chosen
    //the parameter is the list of admitted positions
    void askMoves(List<Position> availablePosition);

    //asks the player how many steps they want Mother Nature to do
    //the parameter of the method is the number of admitted steps (???)
    void askMotherMoves(int possibleSteps);

    //asks the player which cloud they want to pick the students from
    //the parameter of the method is the list of the remaining cloud
    void askCloud(List<Cloud> availableClouds);

    //asks for the number of players that are going to play
    void askPlayersNumber();

    //asks for the game mode
    void askGameMode();

    void askInitType(ArrayList<Type> teams);

    /* THESE THINGS NEED TO BE DISCUSS A BIT I THINK!!!
    * TODO
    * then we should implement all the various things that have to do with the exceptions, connections and these
    * stuffs.
    * These stuffs include
    *  - successful login                                            ((okay???))
    *  - disconnection message                                       ((okay???))
    *  - error and exit                                              TO BE CHECKED
    *  - lobby (?)                                                   TO BE WRITTEN
    *  - enable effects (for the expert mode????)                    TO BE DISCUSSED
    *  - match infos                                                 TO BE CHECKED
    *  - win message                                                 TO BE CHECKED
     */

    // here the beginning of the implementation of the methods listed above
    //sends a generic message
    void showGenericMessage(String genericMessage);

    //communicates the successful login of the player (should check the order of the parameters!!!)
    //the parameters are two boolean and the nickname of the player
    void showLoginResult(boolean nicknameAccepter, boolean connectionResult, String nickname);

    //TODO
    //communicates the disconnection of the player
    //the parameters are the nickname of the player who has been disconnected and a boolean (???)
    void showDisconnectionMessage(String playerDisconnected);

    //TODO
    //communicates that an error has occurred and exits form the game
    //the parameter is the nick of the player who's going to be kicked out (???)
    void errorCommunicationAndExit(String nickname);

    //TODO
    //communicates that the wanted effect has been activated
    //tbh i'm not 100% sure of this method because i don't really know if this is going to work out for our code...
    //also a thing to pass the card that has been summoned???????????
    void effectEnabled(String summoner);

    //TODO
    //shows the information of the match played
    //the parameters should be a list of players and things like these... i think???
    void showMatchInfo();

    //TODO
    //communicates to the winner they have won
    //should we pass the nick of the player or the REAL player???
    void winCommunication(Message winMessage, String winner);

}
