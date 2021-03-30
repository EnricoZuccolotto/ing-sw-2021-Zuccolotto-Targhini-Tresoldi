package it.polimi.ingsw.model.enums;

public enum Resources {
    SERVANT,
    COIN,
    STONE,
    SHIELD,
    WHATEVER,
    FAITH,
    WHITE;
    public static Resources transform(int i) {
        switch (i) {
            case 0:
                return SERVANT;
            case 1:
                return COIN;
            case 2:
                return STONE;
            case 3:
                return SHIELD;
            case 4:
                return WHATEVER;
            case 5:
                return FAITH;
            case 6:
                return WHITE;
            default:
                throw new IllegalStateException("Unexpected value: " + i);
        }
    }
}
