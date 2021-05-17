package it.polimi.ingsw.model.enums;
/**
 * This enum contains all possible type of resource.
 */
public enum Resources {
    SERVANT,
    COIN,
    STONE,
    SHIELD,
    FAITH,
    WHITE,
    WHATEVER;

    /**
     * Transform an integer into a Resource
     *
     * @param i integer to transform
     * @return the integer into a Resource
     */
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

    public static String toString(int[] resources) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < resources.length; i++)
            if (resources[i] != 0) {
                string.append(resources[i]);
                string.append(" ");
                string.append(Resources.transform(i));
                string.append(",");
            }
        if (string.length() != 0)
            string.deleteCharAt(string.lastIndexOf(","));
        return string.toString();
    }

    public static String getResources(int[] effect) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < 4; i++)
            if (effect[i] != 0) {
                if (effect[i] > 1) {
                    string.append(effect[i]);
                    string.append(" ");
                }
                string.append(Resources.transform(i));
            }
        return string.toString();
    }

}
