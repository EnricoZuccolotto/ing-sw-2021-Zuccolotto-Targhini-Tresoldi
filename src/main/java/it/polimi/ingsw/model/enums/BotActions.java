package it.polimi.ingsw.model.enums;
/**
 * This enum contains all possible bot actions.
 */
public enum BotActions {
    DiscardYellow,
    DiscardBlue,
    DiscardPurple,
    DiscardGreen,
    BlackCross2,
    BlackCross1Shuffle;

    /**
     * Gets the string associated with this bot action.
     *
     * @return the string associated with this bot action.
     */
    public String toSentence() {
        switch (this) {
            case DiscardBlue:
                return "Lorenzo the Magnifico discarded two " + Colors.BLUE + " development cards from the decks";
            case DiscardGreen:
                return "Lorenzo the Magnifico discarded two " + Colors.GREEN + " development cards from the decks";
            case DiscardPurple:
                return "Lorenzo the Magnifico discarded two " + Colors.PURPLE + " development cards from the decks";
            case DiscardYellow:
                return "Lorenzo the Magnifico discarded two " + Colors.YELLOW + " development cards from the decks";
            case BlackCross2:
                return "Lorenzo the Magnifico moves toward the Pope (+2)";
            case BlackCross1Shuffle:
                return "Lorenzo the Magnifico moves toward the Pope (+1) and he shuffles his cards ";
        }
        return "";
    }

    /**
     * Gets the image path of this bot action.
     *
     * @return the image path of this bot action.
     */

    public String getImagePath() {
        switch (this) {
            case DiscardBlue:
                return "Image/Singleplayer/cerchio1.png";
            case DiscardGreen:
                return "Image/Singleplayer/cerchio2.png";
            case DiscardPurple:
                return "Image/Singleplayer/cerchio3.png";
            case DiscardYellow:
                return "Image/Singleplayer/cerchio4.png";
            case BlackCross2:
                return "Image/Singleplayer/cerchio5.png";
            case BlackCross1Shuffle:
                return "Image/Singleplayer/cerchio7.png";
        }
        return "Image/Resources/white.png";
    }
}
