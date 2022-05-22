package it.polimi.ingsw.model.enums;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum modeEnum {
    EASY("easy"),
    EXPERT("expert");
    private static final ArrayList<modeEnum> available = new ArrayList<modeEnum>(Arrays.asList(EASY,EXPERT)) ;
    private String mode;

    modeEnum(String mode){this.mode = mode;}

    public static List<modeEnum> availableGameModes() {return  available;}
    public static Type parseInput(String input) {
        return Enum.valueOf(Type.class, input.toUpperCase());
    }

    public String getText() {
        return this.mode;
    }
}


