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

    /**
     * Transform an integer into a Color.
     *
     * @param i integer to transform.
     * @return the integer into a Color.
     */

    public static Colors transform(int i) {
        for (Colors colors : Colors.values())
            if (colors.ordinal() == i)
                return colors;
        return Colors.BLUE;
    }

    /**
     * Gets the string associated with this array of colors.
     *
     * @param colors Array to transform into a string.
     * @return the string associated with this array of colors.
     */

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

