package it.polimi.ingsw.model.enums;


public enum WarehousePositions {
    SPECIAL_WAREHOUSE,
    WAREHOUSE_FIRST_ROW,
    WAREHOUSE_SECOND_ROW,
    WAREHOUSE_THIRD_ROW;

    public static WarehousePositions transform(int i) {
        for (WarehousePositions warehousePositions : WarehousePositions.values())
            if (warehousePositions.ordinal() == i)
                return warehousePositions;
        return WAREHOUSE_FIRST_ROW;
    }
}