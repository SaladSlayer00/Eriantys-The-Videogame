package it.polimi.ingsw.observer;
//import message
import it.polimi.ingsw.message.Message;

import java.util.ArrayList;
import java.util.List;

/* Observable class that can be observed by implementing the {@link Observer} interface and registering as listener.
 */
public class Observable {

    private final List<Observer> observers = new ArrayList<>();

    /* Adds an observer
     * obs is the observer that has to be added to the list
     */
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    /* this remove an observer from the list
     * obs is the observer that has to be removed
     */
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    /* Notifies all the current observers through the update method and passes to them a {@link Message}.
     * message is the message that has to be passed to the observers.
     */
    protected void notifyObserver(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
