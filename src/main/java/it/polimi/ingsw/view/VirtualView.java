package it.polimi.ingsw.view;

import it.polimi.ingsw.message.Message;
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

    @Override
    public void askInitDeck(ArrayList<Mage> availableDecks) {

    }

    @Override
    public void askAssistant(List<Assistant> availableAssistants) {

    }

    @Override
    public void askMovingPaw(List<Student> availableStudents) {

    }

    @Override
    public void askMoves(List<Position> availablePosition) {

    }

    @Override
    public void askMotherMoves(int possibleSteps) {

    }

    @Override
    public void askCloud(List<Cloud> availableClouds) {

    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void askGameMode() {

    }

    @Override
    public void askInitType(ArrayList<Type> teams) {

    }

    @Override
    //TODO
    public void showGenericMessage(String genericMessage) {
           System.out.println(genericMessage);
    }

    @Override
    public void showLoginResult(boolean connectionResult, boolean nicknameAccepted, String nickname) {

    }

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
