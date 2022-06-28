package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.enums.Color;

/**
 * Class that contains the symbolic constants for the island builder.
 */
public class Constants {

    private static final String VERTICAL_LINE = "|";
    private static final String PLUS = "+";

    public static final String DASHBOARD =
            PLUS
                    + "-"
                    + PLUS
                    + "\n"
                    + VERTICAL_LINE
                    + ColorCli.ANSI_YELLOW
                    + "-"
                    + "ID"
                    + "-"
                    + ColorCli.RESET
                    + VERTICAL_LINE
                    + "\n"
                    + PLUS
                    + "-"
                    + PLUS
                    + "\n"
                    + VERTICAL_LINE
                    + ColorCli.ANSI_YELLOW
                    + "-"
                    + "PLAYER"
                    + "-"
                    + ColorCli.RESET
                    + VERTICAL_LINE
                    + "\n"
                    + VERTICAL_LINE
                    + "-"
                    + VERTICAL_LINE
                    + "\n"
                    + PLUS
                    + "-"
                    + PLUS
                    + "\n"
                    + VERTICAL_LINE
                    + ColorCli.ANSI_YELLOW
                    + "-"
                    + "MAGE"
                    + "-"
                    + ColorCli.RESET
                    + VERTICAL_LINE
                    + "\n"
                    + VERTICAL_LINE
                    + "-"
                    + VERTICAL_LINE
                    + "\n"
                    + PLUS
                    + "-"
                    + PLUS
                    + "\n"
                    + VERTICAL_LINE
                    + ColorCli.ANSI_YELLOW
                    + "-"
                    + "TEAM"
                    + "-"
                    + ColorCli.RESET
                    + VERTICAL_LINE
                    + "\n"
                    + VERTICAL_LINE
                    + "-"
                    + VERTICAL_LINE
                    + "\n"
                    + PLUS
                    + "-"
                    + PLUS
                    + "\n";



    public static final String ISLAND = "----------"+
            "\n" + "|" + "[" + "x" + "]" +"x" +"[" + "x" + "]" + "|" + "\n"
            + "|" + "[" + "x" + "]" +"x" +"[" + "x" + "]" + "|" +
            "\n" + "|" + "[" + "x" + "]" + "x" +"[" + "x" + "]" + "|" + "\n" + "----------";

}
