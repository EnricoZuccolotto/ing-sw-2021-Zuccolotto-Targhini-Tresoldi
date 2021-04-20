package it.polimi.ingsw.model.tools;

public class ExchangeResources {
    private final int[] Warehouse;
    private final int[] SpecialWarehouse;
    private final int[] Strongbox;

    public ExchangeResources(int[] warehouse, int[] strongbox,int[] specialWarehouse) {
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
