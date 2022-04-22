package it.polimi.ingsw.server.answers;

import it.polimi.ingsw.model.Mage;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ColorMessage is an Answer used for sending a color.
 *
 * @author Luca Pirovano
 * @see Answer
 */
public class DeckMessage implements Answer {
    private final String message;
    private final String mage;
    private List<Mage> remaining = new ArrayList<>();

    /**
     * Constructor DeckMessage creates a new DeckMessage instance.
     *
     * @param message of type String - the message to be displayed.
     */
    public DeckMessage(String message) {
        this.message = message;
        this.mage = null;
    }
    /**
     * Constructor DeckMessage creates a new DeckMessage instance.
     *
     * @param message of type String - the message to be displayed.
     * @param mage of type String - the color parsed.
     */
    public DeckMessage(String message, String mage) {
        this.message = message;
        this.mage = mage;
    }

    /**
     * Method getMage returns the color of this ColorMessage object.
     *
     * @return the mage (type String) of this DeckMessage object.
     */
    public String getMage() {
        return mage;
    }

    /**
     * Method addRemaining updates the list of reamining colors.
     *
     * @param mages of type List&lt;Mage&gt; - the list of remaining colors.
     */
    public void addRemaining(List<Mage> mages) {
        remaining = mages;
    }

    /**
     * Method getRemaining returns the remaining of this ColorMessage object.
     *
     * @return the remaining (type List&lt;PlayerColors&gt;) of this ColorMessage object.
     */
    public List<Mage> getRemaining() {
        return remaining;
    }

    /**
     * Method getMessage returns the message of this WorkerPlacement object.
     *
     * @return the message (type Object) of this WorkerPlacement object.
     * @see Answer#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }
}

