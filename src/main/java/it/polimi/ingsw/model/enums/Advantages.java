package it.polimi.ingsw.model.enums;
/**
 * This enum contains all possible type of advantage.
 */
public enum Advantages {
    SALES,
    PROD,
    WAREHOUSE,
    CHANGE;

    /**
     * Gets the string associated with this card.
     *
     * @return the string associated with this card.
     */

    public String toString(int[] effect) {
        StringBuilder string = new StringBuilder();
        switch (this) {
            case PROD: {
                string.append(" You can use a special production: 1 ");
                string.append(Resources.getResources(effect));
                string.append("--> 1 ").append(Resources.WHATEVER).append("+ 1 ").append(Resources.FAITH);
                break;
            }
            case SALES: {
                string.append(" You will pay 1 less ");
                string.append(Resources.getResources(effect));
                string.append(" when you will buy a new production card ");
                break;
            }
            case WAREHOUSE: {
                string.append(" You can store in the special warehouse the resource for that amount: ");
                string.append(Resources.getResources(effect));
                break;
            }
            case CHANGE: {
                string.append(" You will get the ");
                string.append(Resources.getResources(effect));
                string.append(" resource instead of the WHITE resource ");
                break;
            }

        }

        return string.toString();
    }

}
