package it.polimi.ingsw.model.enums;

import it.polimi.ingsw.view.cli.ColorsCLI;

/**
 * This enum contains all possible colors of the cards.
 */
public enum Colors {
    BLUE,
    YELLOW,
    PURPLE,
    GREEN;

    public static Colors transform(int i) {
        switch (i) {
            case 0:
                return BLUE;
            case 1:
                return YELLOW;
            case 2:
                return PURPLE;
            case 3:
                return GREEN;
            default:
                throw new IllegalStateException("Unexpected value: " + i);
        }
    }

    public static String toString(int[] colors) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < 4; i++)
            if (colors[i] != 0) {
                string.append(colors[i]);
                string.append(" ");
                string.append(Colors.transform(i));
                string.append(",");
            }
        if (string.length() != 0)
            string.deleteCharAt(string.lastIndexOf(","));
        return string.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        switch (this) {
            case YELLOW:
                return stringBuilder.append(ColorsCLI.YELLOW).append("YELLOW").append(ColorsCLI.RESET).toString();
            case BLUE:
                return stringBuilder.append(ColorsCLI.BLUE).append("BLUE").append(ColorsCLI.RESET).toString();
            case PURPLE:
                return stringBuilder.append(ColorsCLI.PURPLE).append("PURPLE").append(ColorsCLI.RESET).toString();
            case GREEN:
                return stringBuilder.append(ColorsCLI.GREEN).append("GREEN").append(ColorsCLI.RESET).toString();
        }
        return "";
    }
}

