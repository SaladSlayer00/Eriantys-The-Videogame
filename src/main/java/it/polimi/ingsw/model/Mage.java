package it.polimi.ingsw.model;

public enum Mage {
    MAGE("mage"),
    ELF("elf"),
    FAIRY("fairy"),
    DRAGON("dragon");

    //string that specifies the type of the mage
    private String mageType;

    //contructor
    Mage(String mage){
        this.mageType = mage;
    }
}
