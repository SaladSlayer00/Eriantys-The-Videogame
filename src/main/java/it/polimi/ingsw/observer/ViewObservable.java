package it.polimi.ingsw.observer;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enums.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Abstract class that allows for the model to be observed by various agents that
 * will react according to the notified changes
 */
public abstract class ViewObservable {
    protected final List<ViewObserver> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param obs the observer to be added.
     */
    public void addObserver(ViewObserver obs) {
        observers.add(obs);
    }

    /**
     * Adds a list of observers.
     *
     * @param observerList the list of observers to be added.
     */
    public void addAllObservers(List<ViewObserver> observerList) {
        observers.addAll(observerList);
    }

    /**
     * Removes an observer.
     *
     * @param obs the observer to be removed.
     */
    public void removeObserver(ViewObserver obs) {
        observers.remove(obs);
    }

    /**
     * Removes a list of observers.
     *
     * @param observerList the list of observers to be removed.
     */
    public void removeAllObservers(List<ViewObserver> observerList) {
        observers.removeAll(observerList);
    }

    /**
     * Notifies all the current observers through the lambda argument.
     *
     * @param lambda the lambda to be called on the observers.
     */
    protected void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver observer : observers) {
            lambda.accept(observer);
        }
    }

}
