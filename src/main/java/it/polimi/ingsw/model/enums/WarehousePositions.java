package it.polimi.ingsw.model.enums;


public enum WarehousePositions {
    SPECIAL_WAREHOUSE,
    WAREHOUSE_FIRST_ROW,
    WAREHOUSE_SECOND_ROW,
    WAREHOUSE_THIRD_ROW;

    public static WarehousePositions transform(int i) {
        switch (i) {
            case 0:
                return SPECIAL_WAREHOUSE;
            case 1:
                return WAREHOUSE_FIRST_ROW;
            case 2:
                return WAREHOUSE_SECOND_ROW;
            case 3:
                return WAREHOUSE_THIRD_ROW;
            default:
                return null;
        }
    }
}