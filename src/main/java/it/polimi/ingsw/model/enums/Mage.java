package it.polimi.ingsw.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mage class for the possible type of mage that the player can choose at the beginning of the match
 * @author Beatrice Insalata, Teka Kimbi, Alice Maccarini
 */
public enum Mage {
    MAGE("mage"),
    ELF("elf"),
    FAIRY("fairy"),
    DRAGON("dragon");

    private static final ArrayList<Mage> available = new ArrayList<>(Arrays.asList(Mage.MAGE, Mage.FAIRY ,Mage.ELF,Mage.DRAGON));
    //string that specifies the type of the mage
    private String mageType;

    /**
     * class constructor
     * @param mage is the string corresponding to the name of the mage indicated by the enum types
     */
    Mage(String mage){
        this.mageType = mage;
    }

    /**
     * reset method resets the arraylist of the available mages
     */
    public static void reset() {
        available.clear();
        available.add(MAGE);
        available.add(ELF);
        available.add(FAIRY);
        available.add(DRAGON);
    }

    /**
     * choose method allows the player to choose a mage between the ones still available
     * @param mage is the mage chosen that will be removed from the possible choices
     */
    public static void choose(Mage mage) {
        available.remove(mage);
    }

    /**
     * isChosen method tells whether the mage selected has already been chosen
     * @param mage is the mage of which cit is wanted to know the status
     * @return a boolean variable indicating the status of the chosen mage
     */
    public static boolean isChosen(Mage mage) {
        return !(available.contains(mage));
    }

    /**
     * notChosen method tells the mages that are still availbales
     * @return a list containing all the available mages
     */
    public static List<Mage> notChosen() {
        return available;
    }

    /**
     * parseInput method normalises for the game logic the name of the mage given as input
     * @param input is the player's input
     * @return the enum type of the input typed
     */
    public static Mage parseInput(String input) {
        return Enum.valueOf(Mage.class, input.toUpperCase());
    }

    /**
     * isEmpty method tells whether there are no more mages available
     * @return a boolean variable indicating the emptiness of the mages' list
     */
    public static boolean isEmpty(){
        return(available.size()==0);
    }

    /**
     * gettext method is a getter of the name of the enum types
     * @return a string indicating a specific mage
     */
    public String getText(){
        return mageType;
    }
}
