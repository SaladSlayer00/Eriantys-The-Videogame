package it.polimi.ingsw.model.enums;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * modeEnum class for the game mode
 * @authors Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */

public enum modeEnum {
    EASY("easy"),
    EXPERT("expert");
    private static final ArrayList<modeEnum> available = new ArrayList<modeEnum>(Arrays.asList(modeEnum.EASY,modeEnum.EXPERT)) ;
    private String mode;

    /**
     * class constructor
     * @param mode is the chosen mode for the match
     */
    modeEnum(String mode){this.mode = mode;}

    /**
     * availableGameModes method tells the available game modes
     * @return a list containing all the available game mode a player can choose for the match
     */
    public static List<modeEnum> availableGameModes() {return  available;}
    public static Type parseInput(String input) {
        return Enum.valueOf(Type.class, input.toUpperCase());
    }

    /**
     * getText method is a getter of the name of the game mode
     * @return a string indicating the name of the mode
     */
    public String getText() {
        return this.mode;
    }
}


