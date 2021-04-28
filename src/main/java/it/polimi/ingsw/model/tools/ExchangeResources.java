package it.polimi.ingsw.model.tools;


import java.io.Serializable;

/**
 * This class is used in the messages to exchange resources.
 */
public class ExchangeResources implements Serializable {
    private final int[] Warehouse;
    private final int[] SpecialWarehouse;
    private final int[] Strongbox;

    /**
     * Default constructor. In all arrays index means resource ordinal, value means number of said resources.
     * @param warehouse Resources obtained from warehouse
     * @param strongbox Resources obtained from strongbox.
     * @param specialWarehouse Resources obtained from special warehouse.
     */
    public ExchangeResources(int[] warehouse, int[] strongbox, int[] specialWarehouse) {
        Warehouse = warehouse;
        SpecialWarehouse = specialWarehouse;
        Strongbox = strongbox;
    }

    public int[] getWarehouse() {
        return Warehouse;
    }

    public int[] getSpecialWarehouse() {
        return SpecialWarehouse;
    }

    public int[] getStrongbox() {
        return Strongbox;
    }
}
