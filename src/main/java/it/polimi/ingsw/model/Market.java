package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Resources;

public class Market {
    private Resources[][] market;
    private Resources slide;

    public Market(){}

    //public int pushColumn(int columnIndex){}
    //public int pushRow(int rowIndex){}

    public Resources[][] getMarket() {
        return market;
    }

    public Resources getSlide() {
        return slide;
    }
}
