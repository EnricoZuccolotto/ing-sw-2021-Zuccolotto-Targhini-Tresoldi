package it.polimi.ingsw.model.enums;

public enum Resources {
    SERVANT,
    COIN,
    STONE,
    SHIELD,
    FAITH,
    WHITE,
    WHATEVER;
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
                return FAITH;
            case 5:
                return WHITE;
            case 6:
                return WHATEVER;
            default:
                throw new IllegalStateException("Unexpected value: " + i);
        }
    }
}
