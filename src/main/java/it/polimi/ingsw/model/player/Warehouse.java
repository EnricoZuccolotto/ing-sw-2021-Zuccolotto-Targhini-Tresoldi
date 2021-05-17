package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;

import java.io.Serializable;
/**
 * This class represents the warehouse.
 * warehouse is an array of 6 resource the represents the warehouse.
 * resources is an array of 3 resource the contains the type of resource contained in each row
 * ResourceNumber represents the number of resources contained in the warehouse
 */
public class Warehouse implements Serializable {
    private final Resources[] warehouse;
    private final Resources[] resources = {Resources.WHITE, Resources.WHITE, Resources.WHITE};
    private int ResourceNumber;

    /**
     * Build a new warehouse and initialize each position to the resource white.
     */
    public Warehouse() {
        warehouse = new Resources[6];
        ResourceNumber = 0;
        int i;
        for (i = 0; i < 6; i++) {
            warehouse[i] = Resources.WHITE;
        }
    }

    /**
     * Gets the number of resources of type r contained in the warehouse
     *
     * @return the number of resources of type r contained in the warehouse
     */
    public int getNumberResource(Resources r) {
        int c = 0;

        if (resources[0].equals(r))
            c += 1;

        if (resources[1].equals(r)) {
            c += 1;
            if (resources[1].equals(warehouse[2]))
                c += 1;
        }

        if (resources[2].equals(r)) {
            c += 1;
            if (resources[2].equals(warehouse[4]))
                c += 1;
            if (resources[2].equals(warehouse[5]))
                c += 1;
        }

        return c;

    }

    /**
     * Try to add a resource to the row specified.
     *
     * @return true if the resource was added, else false
     */
    public boolean AddResources(Resources resource, WarehousePositions row) {
        for (int count = 0; count < 3; count++) {
            if (resources[count] == resource && row.ordinal() != count + 1) {
                return false;
            }
        }
        switch (row) {
            case WAREHOUSE_FIRST_ROW:
                if (warehouse[0] == Resources.WHITE) {
                    warehouse[0] = resource;
                    resources[0] = resource;
                    ResourceNumber += 1;
                    return true;
                } else return false;  //riga piena
            case WAREHOUSE_SECOND_ROW:
                if (warehouse[1] == Resources.WHITE) {
                    warehouse[1] = resource;
                    resources[1] = resource;
                    ResourceNumber += 1;
                    return true;
                } else if (warehouse[1] == resource && warehouse[2] == Resources.WHITE) {
                    warehouse[2] = resource;
                    ResourceNumber += 1;
                    return true;
                } else return false; // riga piena o altre risorse nella riga
            case WAREHOUSE_THIRD_ROW:
                if (warehouse[3] == Resources.WHITE) {
                    warehouse[3] = resource;
                    resources[2] = resource;
                    ResourceNumber += 1;
                    return true;
                } else if (warehouse[3] == resource) {
                    if (warehouse[4] == Resources.WHITE) {
                        warehouse[4] = resource;
                        ResourceNumber += 1;
                        return true;
                    } else if (warehouse[5] == Resources.WHITE) {
                        warehouse[5] = resource;
                        ResourceNumber += 1;
                        return true;
                    } else return false;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    /**
     * Switch row start with row end
     *
     * @return true if the operation succeeded, else false.
     */
    public boolean MoveRow(WarehousePositions start, WarehousePositions end) {
        Resources temp;
        switch (start) {
            case WAREHOUSE_FIRST_ROW:
                if (end.equals(WarehousePositions.WAREHOUSE_SECOND_ROW)) {
                    if (warehouse[2] == Resources.WHITE) {
                        temp = warehouse[1];
                        warehouse[1] = warehouse[0];
                        warehouse[0] = temp;
                        temp = resources[0];
                        resources[0] = resources[1];
                        resources[1] = temp;
                        return true;
                    } else return false;
                }
                if (end.equals(WarehousePositions.WAREHOUSE_THIRD_ROW)) {
                    if (warehouse[4] == Resources.WHITE) {
                        temp = warehouse[3];
                        warehouse[3] = warehouse[0];
                        warehouse[0] = temp;
                        temp = resources[0];
                        resources[0] = resources[2];
                        resources[2] = temp;
                        return true;
                    } else return false;
                }
                break;
            case WAREHOUSE_SECOND_ROW:
                if (warehouse[5] == Resources.WHITE) {
                    temp = warehouse[3];
                    warehouse[3] = warehouse[1];
                    warehouse[1] = temp;
                    temp = warehouse[4];
                    warehouse[4] = warehouse[2];
                    warehouse[2] = temp;
                    temp = resources[1];
                    resources[1] = resources[2];
                    resources[2] = temp;
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Warehouse: " +
                "\n" + "     " + t(warehouse[0]) +
                "\n" + "  " + t(warehouse[1]) + " " + t(warehouse[2]) +
                "\n" + t(warehouse[3]) + " " + t(warehouse[4]) + " " + t(warehouse[5]);
    }

    private String t(Resources r) {
        if (r.equals(Resources.WHITE))
            return "VOID";
        else return r.toString();
    }

    /**
     * Try to remove a the resource from the warehouse
     *
     * @return true if it succeeded, else false.
     */
    public boolean popResources(Resources resource) {
        int j, i = 0;
        while (resources[i] != resource) {
            i++;
            if (i == 3) {
                return false;
            }
        }
        j = i;
        if (i == 2) {
            i++;
        }
        while (warehouse[i + 1] == resource) {
            i++;
            if (i == 5) {
                break;
            }
        }
        if (j == i || j == 2 && i == 3) {
            resources[j] = Resources.WHITE;
        }
        warehouse[i] = Resources.WHITE;
        ResourceNumber--;
        return true;
    }

    /**
     * Gets the number of resources contained in the warehouse
     *
     * @return the number of resources  contained in the warehouse
     */
    public int ResourceNumber() {
        return ResourceNumber;
    }

    /**
     * Gets the warehouse
     *
     * @return the  warehouse
     */
    public Resources getR(int p) {
        return warehouse[p];
    }
}
