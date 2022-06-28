package it.polimi.ingsw.observer;

import it.polimi.ingsw.message.Message;

/**
 * Interface Observer is the one that implements the observation mechanics, updating when there's
 * a relevant change.
 */
public interface Observer {
    void update(Message message);
}
