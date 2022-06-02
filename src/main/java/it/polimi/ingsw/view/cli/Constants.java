package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.enums.Color;

public class Constants {
    public static final String LINE_BLOCK =
            "█████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████";
    private static final String TWELVE_LINE_BLOCK = "████████████";
    private static final String ELEVEN_LINE_BLOCK = "███████████";
    private static final String LVL_1_LINE_BLOCK = "║█████████████████████║";
    private static final String UPPER_LVL_1 = "╔═════════════════════╗";
    private static final String BOTTOM_LVL_1 = "╚═════════════════════╝";
    private static final String TEN_LINE_BLOCK = "██████████";
    private static final String UPPER_INSIDE_LVL_2 = "┌───────────────────┐";
    private static final String LOWER_INSIDE_LVL_2 = "└───────────────────┘";
    private static final String THREE_LINE_BLOCK = "███";
    private static final String NINE_LINE_BLOCK = "█████████";
    private static final String VERTICAL_DOUBLE_LINE = "║";
    private static final String VERTICAL_DOUBLE_LINE_RIGHT = "║\n";
    private static final String FIVE_LINE_BLOCK = "█████";
    private static final String SEVEN_LINE_BLOCK = "███████";
    private static final String NINETEEN_LINE_BLOCK = "███████████████████";
    public static final String SINGLE_LINE_BLOCK = "█";
    private static final String SIX_LINE_BLOCK = "██████";
    private static final String EIGHT_LINE_BLOCK = "████████";
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

}
