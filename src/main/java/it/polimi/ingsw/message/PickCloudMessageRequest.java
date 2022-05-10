package it.polimi.ingsw.message;

import it.polimi.ingsw.model.board.Cloud;

import java.util.List;

public class PickCloudMessageRequest extends Message{
    //ID?
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
