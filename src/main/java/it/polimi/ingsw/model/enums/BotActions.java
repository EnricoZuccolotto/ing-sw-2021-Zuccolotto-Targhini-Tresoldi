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

    @Override
    public String toString() {
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
                return "Lorenzo the Magnifico moves toward the Pope";
            case BlackCross1Shuffle:
                return "Lorenzo the Magnifico moves toward the Pope and he shuffles his cards";
        }
        return "";
    }
}
