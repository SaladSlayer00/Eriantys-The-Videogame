package it.polimi.ingsw.observer;

import it.polimi.ingsw.message.Message;

public interface Observer {
    void update(Message message);
}
