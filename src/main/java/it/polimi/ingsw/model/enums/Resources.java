package it.polimi.ingsw.model.enums;

import it.polimi.ingsw.view.cli.ColorsCLI;

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
        for (Resources resources : Resources.values())
            if (resources.ordinal() == i)
                return resources;
        return Resources.WHATEVER;
    }

    public static String toString(int[] resources) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < resources.length; i++)
            if (resources[i] != 0) {
                string.append(resources[i]);
                string.append(" ");
                string.append(Resources.transform(i).toString());
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
                string.append(Resources.transform(i).toString());
            }
        return string.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        switch (this) {
            case COIN:
                stringBuilder.append(ColorsCLI.YELLOW).append("COIN");
                break;
            case SHIELD:
                stringBuilder.append(ColorsCLI.BLUE).append("SHIELD");
                break;
            case SERVANT:
                stringBuilder.append(ColorsCLI.PURPLE).append("SERVANT");
                break;
            case STONE:
                stringBuilder.append(ColorsCLI.GRAY).append("STONE");
                break;
            case FAITH:
                stringBuilder.append(ColorsCLI.RED).append("FAITH");
                break;
            case WHITE:
                stringBuilder.append(ColorsCLI.WHITE).append("WHITE");
                break;
            case WHATEVER:
                stringBuilder.append(ColorsCLI.BLACK).append("WHATEVER");
                break;
        }

        return "" + stringBuilder.append(ColorsCLI.RESET);
    }

    public String getBallImagePath() {

        switch (this) {
            case WHITE:
                return "Image/WhiteBall.png";
            case FAITH:
                return "Image/Market/FaithBall.png";
            case STONE:
                return "Image/Market/StoneBall.png";
            case SERVANT:
                return "Image/Market/ServantBall.png";
            case SHIELD:
                return "Image/Market/ShieldBall.png";
            case COIN:
                return "Image/Market/CoinBall.png";
        }
        return "Image/Market/WhiteBall.png";
    }
}
