package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.enums.Resources;

import java.util.Arrays;

public class Warehouse {
    private final Resources[] warehouse;
    private final Resources[] var= {Resources.WHITE, Resources.WHITE, Resources.WHITE};
    private int Resnum;

    public Warehouse() {
        warehouse = new Resources[6];
        Resnum = 0;
        int i;
        for (i = 0; i < 6; i++) {
            warehouse[i] = Resources.WHITE;
        }
    }
    public int getResource(Resources r){
      int c=0;

        if(var[0].equals(r))
            c+=1;

        if(var[1].equals(r))
        {
           c+=1;
           if(var[1].equals(warehouse[2]))
               c+=1;
        }

        if(var[2].equals(r))
        {
            c+=1;
            if(var[2].equals(warehouse[4]))
                c+=1;
            if(var[2].equals(warehouse[5]))
               c+=1;
        }

        return c;

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
                    Resnum+=1;
                    return true;
                }
                else return false;  //riga piena
            case 2:
                if(warehouse[1]==Resources.WHITE){
                    warehouse[1]=resource;
                    var[1]=resource;
                    Resnum+=1;
                    return true;
                }
                else if(warehouse[1]==resource && warehouse[2]==Resources.WHITE){
                    warehouse[2]=resource;
                    Resnum+=1;
                    return true;
                }
                else return false; // riga piena o altre risorse nella riga
            case 3:
                if(warehouse[3]==Resources.WHITE) {
                    warehouse[3] = resource;
                    var[2]=resource;
                    Resnum+=1;
                    return true;
                }
                else if(warehouse[3]==resource){
                    if(warehouse[4]==Resources.WHITE){
                        warehouse[4]=resource;
                        Resnum+=1;
                        return true;
                    }
                    else if(warehouse[5]==Resources.WHITE){
                        warehouse[5]=resource;
                        Resnum+=1;
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
                        temp=var[0];
                        var[0]=var[1];
                        var[1]=temp;
                        return true;
                    }
                    else return false;
                }
                if(end==3){
                    if(warehouse[4]==Resources.WHITE){
                        temp=warehouse[3];
                        warehouse[3]=warehouse[0];
                        warehouse[0]=temp;
                        temp=var[0];
                        var[0]=var[2];
                        var[2]=temp;
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
                    temp=var[1];
                    var[1]=var[2];
                    var[2]=temp;
                    return true;
                }
                break;
            default: return false;
        }
        return false;
    }


// per chiamare il metodo tostring System.out.println(w);
    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouse=" + Arrays.toString(warehouse) +
                ", var=" + Arrays.toString(var) +
                ", Resnum=" + Resnum +
                ", ResRow=" + Arrays.toString(var) +
                '}';
    }

    public boolean popResources(Resources resource){
        int j, i=0;
        while(var[i]!=resource){
            i++;
            if(i==3){
                return false;
            }
        }
        j=i;
        if(i==2){
            i++;
        }
        while(warehouse[i+1]==resource){
            i++;
            if(i==5){
                break;
            }
        }
        if(j==i || j==2 && i==3){
            var[j]=Resources.WHITE;
        }
        warehouse[i]=Resources.WHITE;
        Resnum--;
        return true;
    }
    public int Resourcesnumb(){
        return Resnum;
    }
    public Resources getR(int p){
        return warehouse[p];
    }
}
