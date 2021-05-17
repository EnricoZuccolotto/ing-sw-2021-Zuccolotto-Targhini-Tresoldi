package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.network.messages.MarketUpdateMessage;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the market.
 * market is a matrix of resources 3x4
 * slide represents the resource in the slide.
 */
public class Market extends Observable implements Serializable {
    private final Resources[][] market;
    private Resources slide;

    /**
     * Build a new market with the correct number of resources in random position.
     */
    public Market() {
        market = new Resources[3][4];
        int temp;
        double[] a = {2, 2, 2, 2, 1, 4};
        temp = (int) Math.floor(Math.random() * 6);
        a[temp]--;

        slide = Resources.transform(temp);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                temp = (int) Math.floor(Math.random() * 6);
                while (a[temp] == 0) {
                    temp = (int) Math.floor(Math.random() * 6);
                }
                a[temp]--;
                market[i][j] = Resources.transform(temp);
            }
        }
    }

    /**
     * Gets the column index of resources from the market.
     *
     * @param columnIndex number of the column
     * @return the column of resources from the market.
     */
    public ArrayList<Resources> pushColumn(int columnIndex) {
        Resources temp;
        ArrayList<Resources> ret = new ArrayList<>();
        temp = market[0][columnIndex];
        ret.add(0, temp);
        for (int i = 1; i < 3; i++) {
            market[i - 1][columnIndex] = market[i][columnIndex];
            ret.add(i, market[i - 1][columnIndex]);
        }
        market[2][columnIndex] = slide;
        slide = temp;
        notifyObserver(new MarketUpdateMessage("server", this));
        return ret;
    }

    /**
     * Gets the row index of resources from the market.
     *
     * @param rowIndex number of the row
     * @return the row of resources from the market.
     */
    public ArrayList<Resources> pushRow(int rowIndex) {
        Resources temp;
        ArrayList<Resources> ret = new ArrayList<>();
        temp = market[rowIndex][0];
        ret.add(0, temp);
        for (int i = 1; i < 4; i++) {
            market[rowIndex][i - 1] = market[rowIndex][i];
            ret.add(i, market[rowIndex][i - 1]);
        }
        market[rowIndex][3] = slide;
        slide = temp;
        notifyObserver(new MarketUpdateMessage("server", this));
        return ret;
    }

    /**
     * Gets the resource in the slide
     *
     * @return the resource in the slide
     */
    public Resources getSlide() {
        return slide;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Market:\n");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                stringBuilder.append(market[i][j]);
                stringBuilder.append(" | ");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("Slide is:");
        stringBuilder.append(slide);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }


}
