package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Resources;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Market {
    private final Resources[][] market;
    private Resources slide;

    public Market() {
        market= new Resources[3][4];
        int temp;
        double[] a= {2, 2, 2, 2, 1, 4};
        temp= (int) Math.floor(Math.random()*6);
        a[temp]--;
        if(temp>=4){
            temp=temp+1;
        }
        slide=Resources.transform(temp);
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                temp= (int) Math.floor(Math.random()*6);
                while(a[temp]==0){
                    temp= (int) Math.floor(Math.random()*6);
                }
                a[temp]--;
                if(temp>=4){
                    temp=temp+1;
                }
                market[i][j]=Resources.transform(temp);
            }
        }
    }

    public ArrayList<Resources> pushColumn(int columnIndex){
        Resources temp;
        ArrayList<Resources> ret= new ArrayList<Resources>();
        temp=market[0][columnIndex];
        ret.add(0, temp);
        for(int i=1; i<3; i++){
            market[i-1][columnIndex]=market[i][columnIndex];
            ret.add(i, market[i-1][columnIndex]);
        }
        market[2][columnIndex]=slide;
        slide=temp;
        return ret;
    }

    public ArrayList<Resources> pushRow(int rowIndex){
        Resources temp;
        ArrayList<Resources> ret= new ArrayList<Resources>();
        temp=market[rowIndex][0];
        ret.add(0, temp);
        for(int i=1; i<4; i++){
            market[rowIndex][i-1]=market[rowIndex][i];
            ret.add(i, market[rowIndex][i-1]);
        }
        market[rowIndex][3]=slide;
        slide=temp;
        return ret;
    }

    public Resources[][] getMarket() {
        return market;
    }

    public Resources getSlide() {
        return slide;
    }

    @Override
    public String toString() {
        return
                "market=" + Arrays.toString(market) +
                ", slide=" + slide
                ;
    }

    public void printMarket(){
        System.out.println("slide is " +slide);
        System.out.print(""+market[0][0]);
        System.out.print("  "+market[0][1]);
        System.out.print("  "+market[0][2]);
        System.out.println("  "+market[0][3]);
        System.out.print(""+market[1][0]);
        System.out.print("  "+market[1][1]);
        System.out.print("  "+market[1][2]);
        System.out.println("  "+market[1][3]);
        System.out.print(""+market[2][0]);
        System.out.print("  "+market[2][1]);
        System.out.print("  "+market[2][2]);
        System.out.println("  "+market[2][3]);
    }
}
