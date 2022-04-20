package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public enum Mage {
    MAGE("mage"),
    ELF("elf"),
    FAIRY("fairy"),
    DRAGON("dragon");

    private static final ArrayList<Mage> available = new ArrayList<>();

    //string that specifies the type of the mage
    private String mageType;

    //constructor
    Mage(String mage){
        this.mageType = mage;
    }

    public static void reset() {
        available.clear();
        available.add(MAGE);
        available.add(ELF);
        available.add(FAIRY);
        available.add(DRAGON);
    }

    public static void choose(Mage mage) {
        available.remove(mage);
    }

    public static boolean isChosen(Mage mage) {
        return !(available.contains(mage));
    }

    public static List<Mage> notChosen() {
        return available;
    }

    public static Mage parseInput(String input) {
        return Enum.valueOf(Mage.class, input.toUpperCase());
    }
}
