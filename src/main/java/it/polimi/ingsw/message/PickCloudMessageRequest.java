package it.polimi.ingsw.message;

import it.polimi.ingsw.model.board.Cloud;

import java.util.List;

/**
 * Class used to ask the player to choose a cloud from those available
 */
public class PickCloudMessageRequest extends Message{
    private static final long serialVersionUID = -3796309698593755714L;
    private List<Cloud> availableClouds;

    public PickCloudMessageRequest(String nickname, List<Cloud> availableClouds){
        super(nickname, MessageType.PICKCLOUD_REQUEST);
        this.availableClouds = availableClouds;
    }

    public List<Cloud> getClouds(){
        return availableClouds;
    }

    @Override
    public String toString(){
        return "PickCloudMessage{" +
                "nickname = " + getNickname() +
                "availableClouds = " + getClouds() +
                "}";
    }
}
