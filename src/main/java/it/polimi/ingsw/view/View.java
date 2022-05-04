package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.Cloud;

import javax.swing.text.Position;
import java.util.List;

public interface View {

    //asks the player to choose a nickname
    void askNickname();

    //asks the player to choose a deck from the ones available
    //the parameter of the method is the list of the deck which the player can choose from
    void askDeck(List<Deck> availableDecks);

    //asks the player to choose an assistant's card from the ones that are still in the deck
    //the parameter of the method is the list of the cards in the deck which the player can choose from
    void askAssistantCard(List<Assistant> availableAssistants);

    //asks the player which paw (student) they want to move
    //the parameter of the method is the list of the students that the player can actually move
    void askMovingPaw(List<Student> availableStudents);

    //asks where the player wants to move the paw they have chosen
    //the parameter is the list of admitted positions
    void askPawMove(List<Position> availablePosition);

    //asks the player how many steps they want Mother Nature to do
    //the parameter of the method is the number of admitted steps (???)
    void askMotherNatureMove(int possibleSteps);

    //asks the player which cloud they want to pick the students from
    //the parameter of the method is the list of the remaining cloud
    void askCloud(List<Cloud> availableClouds);

    //asks for the number of players that are going to play
    void askPlayersNumber();

    //asks for the game mode
    void askGameMode();

    /* then we should implement all the various things that have to do with the exceptions, connections and these
    * stuffs.
    * These stuffs include
    *  - generic messages
    *  - successful login
    *  - disconnection message
    *  - error and exit
    *  - lobby (?)
    *  - enable effects (for the expert mode????)
    *  - match infos
    *  - win message
     */
}
