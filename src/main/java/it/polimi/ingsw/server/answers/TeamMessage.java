package it.polimi.ingsw.server.answers;

import it.polimi.ingsw.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ColorMessage is an Answer used for sending a color.
 *
 * @author Luca Pirovano
 * @see Answer
 */
public class TeamMessage implements Answer {
    private final String message;
    private final String team;
    private List<Type> remaining = new ArrayList<>();

    /**
     * Constructor TeamMessage creates a new TeamMessage instance.
     *
     * @param message of type String - the message to be displayed.
     */
    public TeamMessage(String message) {
        this.message = message;
        this.team = null;
    }
    /**
     * Constructor TeamMessage creates a new TeamMessage instance.
     *
     * @param message of type String - the message to be displayed.
     * @param team of type String - the color parsed.
     */
    public TeamMessage(String message, String team) {
        this.message = message;
        this.team = team;
    }

    /**
     * Method getTeam returns the color of this TeamMessage object.
     *
     * @return the team (type String) of this TeamMessage object.
     */
    public String getTeam() {
        return team;
    }

    /**
     * Method addRemaining updates the list of reamining colors.
     *
     * @param teams of type List&lt;Type&gt; - the list of remaining colors.
     */
    public void addRemaining(List<Type> teams) {
        remaining = teams;
    }

    /**
     * Method getRemaining returns the remaining of this TeamMessage object.
     *
     * @return the remaining (type List&lt;Type&gt;) of this TeamMessage object.
     */
    public List<Type> getRemaining() {
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
