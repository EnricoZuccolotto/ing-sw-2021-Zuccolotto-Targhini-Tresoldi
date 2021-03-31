package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.enums.Resources;

public class Warehouse {
    private final Resources[] warehouse;
    private Resources[] var= {Resources.WHITE, Resources.WHITE, Resources.WHITE};

    public Warehouse() {
        warehouse= new Resources[6];
        int i;
        for(i=0; i<6; i++){
            warehouse[i]=Resources.WHITE;
        }
    }

    public boolean AddResources(Resources resource, int row) {
        for(int count=0; count<3; count++){
            if(var[count]==resource && row!=count+1){
                return false;
            }
        }
        switch(row){
            case 1:
                if(warehouse[0]==Resources.WHITE){
                    warehouse[0]=resource;
                    var[0]=resource;
                    return true;
                }
                else return false;  //riga piena
            case 2:
                if(warehouse[1]==Resources.WHITE){
                    warehouse[1]=resource;
                    var[1]=resource;
                    return true;
                }
                else if(warehouse[1]==resource && warehouse[2]==Resources.WHITE){
                    warehouse[2]=resource;
                    return true;
                }
                else return false; // riga piena o altre risorse nella riga
            case 3:
                if(warehouse[3]==Resources.WHITE) {
                    warehouse[3] = resource;
                    var[2]=resource;
                    return true;
                }
                else if(warehouse[3]==resource){
                    if(warehouse[4]==Resources.WHITE){
                        warehouse[4]=resource;
                        return true;
                    }
                    else if(warehouse[5]==Resources.WHITE){
                        warehouse[5]=resource;
                        return true;
                    }
                else return false;
                }
                break;
            default: return false;
        }
        return false;
    }
    public boolean MoveRow(int start, int end) {    //considero sempre che start<end
        Resources temp;
        switch(start){
            case 1:
                if(end==2) {
                    if (warehouse[2] == Resources.WHITE) {
                        temp = warehouse[1];
                        warehouse[1] = warehouse[0];
                        warehouse[0] = temp;
                        return true;
                    }
                    else return false;
                }
                if(end==3){
                    if(warehouse[4]==Resources.WHITE){
                        temp=warehouse[3];
                        warehouse[3]=warehouse[0];
                        warehouse[0]=temp;
                        return true;
                    }
                    else return false;
                    }
                break;
            case 2:
                if(warehouse[5]==Resources.WHITE){
                    temp=warehouse[3];
                    warehouse[3]=warehouse[1];
                    warehouse[1]=temp;
                    temp=warehouse[4];
                    warehouse[4]=warehouse[2];
                    warehouse[2]=temp;
                    return true;
                }
                break;
            default: return false;
        }
        return false;
    }

    /*private void checkConsistency() {
    }*/
    public void printWarehouse(){
        System.out.println(""+warehouse[0]);
        System.out.print(""+warehouse[1]);
        System.out.println("  "+warehouse[2]);
        System.out.print(""+warehouse[3]);
        System.out.print("  "+warehouse[4]);
        System.out.println("  "+warehouse[5]);
    }
}
