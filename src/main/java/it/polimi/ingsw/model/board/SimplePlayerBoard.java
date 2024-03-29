package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.IllegalDecoratorException;
import it.polimi.ingsw.exceptions.InsufficientLevelException;
import it.polimi.ingsw.exceptions.WinnerException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.enums.WarehousePositions;
import it.polimi.ingsw.model.player.SpaceProd;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.tools.ExchangeResources;
import it.polimi.ingsw.view.cli.ColorsCLI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents a non-decorated Player Board.
 */
public class SimplePlayerBoard implements PlayerBoard, Serializable {
    private final Strongbox strongbox;
    private final boolean inkWell;
    private final ArrayList<LeaderCard> leaderCards;
    private final ArrayList<SpaceProd> productionSpaces;
    private final Warehouse warehouse;
    private int VP;

    public SimplePlayerBoard(boolean inkWell) {
        this.inkWell = inkWell;
        strongbox = new Strongbox();
        leaderCards = new ArrayList<>(2);
        productionSpaces = new ArrayList<>();
        productionSpaces.add(new SpaceProd());
        productionSpaces.add(new SpaceProd());
        productionSpaces.add(new SpaceProd());
        warehouse = new Warehouse();
    }

    @Override
    public void setVP(int VP) {
        this.VP = VP;
    }

    @Override
    public int getVP() {
        return VP;
    }

    @Override
    public int getProductionNumber() {
        int cont = 0;
        for (SpaceProd spaceProd : productionSpaces)
            if (!(spaceProd.getTop().getLevel() == 0))
                cont += 1;
        cont++;
        return cont;
    }

    @Override
    public ArrayList<SpaceProd> getProductionSpaces() {
        return productionSpaces;
    }

    @Override
    public int getLeaderCardsNumber() {
        int cont=0;
        for(LeaderCard c:leaderCards)
            if(!c.getUncovered())
                cont++;
            return cont;
    }

    @Override
    public boolean getInkwell() {
        return inkWell;
    }

    @Override
    public LeaderCard getLeaderCard(int index ){
        return leaderCards.get(index);
    }

    @Override
    public void addLeaderCard(LeaderCard leaderCard) {
                leaderCards.add(leaderCard);
    }

    @Override
    public void removeLeaderCard(LeaderCard c){
        leaderCards.remove(c);
    }

    @Override
    public int getVictoryPointsCards() {
        int VP = 0;
        // Get leader cards victory points
        for(LeaderCard card : leaderCards){
            if(card.getUncovered())
            VP += card.getVP();
        }
        // Get production cards victory points
        for(SpaceProd sp : productionSpaces){
            VP += sp.getVictoryPoints();
        }
        return VP;
    }

    @Override
    public int getNumberResources() {
        int n;
        // Get resources number victory points
        n = (strongbox.getNumResources() + warehouse.ResourceNumber());
        return n;
    }


    @Override
    public void addWarehouseSpace(Resources resource, int maxQuantity) {
        throw new IllegalDecoratorException();
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        return new ArrayList<>(Arrays.asList(0, 0, 0, 0));
    }

    @Override
    public boolean addExtraResources(Resources resources, int quantity) {
        throw new IllegalDecoratorException();
    }

    @Override
    public boolean takeExtraResources(Resources resource, int quantity) {
        throw new IllegalDecoratorException();
    }

    @Override
    public void addDiscount(Resources resource, int amount) {
        throw new IllegalDecoratorException();
    }

    @Override
    public boolean isResourceDiscounted(Resources resource) {
        return false;
    }

    @Override
    public int getResourceDiscount(Resources resource) {
        return 0;
    }

    @Override
    public void addSubstitute(Resources resource){
        throw new IllegalDecoratorException();
    }

    @Override
    public ArrayList<Boolean> getSubstitutes(){
        return new ArrayList<>(Arrays.asList(false, false, false, false));
    }

    @Override
    public ArrayList<Resources> getSubstitutableResources(){return null;}

    @Override
    public boolean isResourceSubstitutable(Resources resource){
        return false;
    }

    @Override
    public void addProduction(Resources resource) {
        throw new IllegalDecoratorException();
    }

    @Override
    public ArrayList<Resources> getProductions(Resources resource) {
        return null;
    }

    @Override
    public boolean addWarehouseResource(Resources r, WarehousePositions row) {
        return warehouse.AddResources(r, row);
    }

    @Override
    public boolean shiftWarehouseRows(WarehousePositions startingRow, WarehousePositions newRowPosition) {
        return warehouse.MoveRow(startingRow, newRowPosition);
    }


    @Override
    public boolean addProductionCard(DevelopmentCard c,int index){
            if(productionSpaces.get(index).getTop().getLevel()==c.getLevel()-1)
            {
                productionSpaces.get(index).addCard(c);
                checkWinnerNumCards();
                return true;
            }
        throw new InsufficientLevelException();
    }

    @Override
    public int[] getProductionCost(int index){
        return productionSpaces.get(index).getTop().getCostProduction();
    }

    @Override
    public int[] getProductionResult(int index){
        return productionSpaces.get(index).getTop().getProductionResult();
    }

    /**
     * Check if the player has 7 cards (the game ends).
     */
    private void checkWinnerNumCards() {
        if (productionSpaces.size() == 3) {
            if (productionSpaces.get(0).getNumbCard() + productionSpaces.get(1).getNumbCard() + productionSpaces.get(2).getNumbCard() == 7)
                throw new WinnerException();
        }
    }


    @Override
    public boolean payResourcesWarehouse(int [] r)
    {
        for(int  i=0;i<4;i++){
            if(r[i]!=0)
            {
                if (r[i] - warehouse.getNumberResource(Resources.transform(i)) <= 0) {
                    for (int j = 0; j < r[i]; j++)
                        warehouse.popResources(Resources.transform(i));
                } else return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkResourcesWarehouse(int [] r)
    {
        for(int  i=0;i<4;i++) {
            if (r[i] != 0) {
                if (warehouse.getNumberResource(Resources.transform(i)) < r[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkResourcesSpecialWarehouse(int [] r) {
        for (int i = 0; i < 4; i++) {
            if (r[i] != 0)
                return false;
        }
        return true;
    }

    @Override
    public boolean payResourcesSpecialWarehouse(int [] r){return true;}

    @Override
    public boolean payResourcesStrongbox(int [] r){
        for(int  i=0;i<4;i++){
            if(r[i]!=0)
            {
                if(r[i]- strongbox.getResources(Resources.transform(i))<=0)
                    strongbox.removeResources(Resources.transform(i),r[i]);
                else return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkResourcesStrongbox(int[] r) {
        for (int i = 0; i < 4; i++) {
            if (r[i] != 0) {
                if (strongbox.getResources(Resources.transform(i)) < r[i])
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkLevel(DevelopmentCard c) {

        for (SpaceProd sp : productionSpaces)
                if (sp.getTop().getLevel() == c.getLevel() - 1)
                    return true;
        throw new InsufficientLevelException();
    }

    @Override
    public boolean checkResources(int [] resources){
        int  tmp;
        for (int i = 0; i < 4; i++)
            if (resources[i] != 0) {
                tmp = resources[i] - warehouse.getNumberResource(Resources.transform(i));
                tmp = tmp - strongbox.getResources(Resources.transform(i));
                if (tmp > 0) {
                    return false;
                }
            }
        return true;
    }

    @Override
    public boolean checkColors(int [] colors){
        int tmp;
        for(Colors c: Colors.values()){
            tmp = 0;
            if (colors[c.ordinal()] != 0) {
                for (SpaceProd sp : productionSpaces)
                    tmp += sp.checkColor(c);
                if (colors[c.ordinal()] - tmp > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int getNumberResourceStrongbox(Resources resources) {
        return strongbox.getResources(resources);
    }

    @Override
    public Resources getResourceWarehouse(int position) {
        return warehouse.getR(position);
    }

    @Override
    public void addStrongboxResource(Resources r, int quantities) {
        strongbox.addResources(r, quantities);
    }

    @Override
    public ArrayList<Resources> getResources(int choice, int temp){
        ArrayList<Resources> list= new ArrayList<>();
        int previous;
        int[] a={0, 0, 0, 0};
        if (choice == 0) {
            for (int i = 0; i < 4; i++) {
                previous = a[i];
                a[i]=temp+1;
                if (checkResourcesWarehouse(a)) {
                    list.add(Resources.transform(i));
                }
                a[i] = previous;
            }
        } else if (choice == 1) {
            for (int i = 0; i < 4; i++) {
                previous = a[i];
                a[i]=temp+1;
                if (checkResourcesStrongbox(a)) {
                    list.add(Resources.transform(i));
                }
                a[i] = previous;
            }
        }
        return list;
    }


    @Override
    public boolean checkColorsAndLevel(int[] colors, int level) {
        int tmp;
        for (Colors c : Colors.values()) {
            tmp = 0;
            if (colors[c.ordinal()] != 0) {
                for (SpaceProd sp : productionSpaces)
                    tmp += sp.checkColor(c, level);
                if (colors[c.ordinal()] - tmp > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public Strongbox getStrongbox() {
        return strongbox;
    }

    @Override
    public ExchangeResources getExchangeResources() {
        int[] fromWarehouse = new int[4];
        for(int i = 0; i < 4; i++){
            fromWarehouse[i] = warehouse.getNumberResource(Resources.transform(i));
        }
        int[] fromStrongbox = new int[4];
        for(int i = 0; i < 4; i++){
            fromStrongbox[i] = strongbox.getResources(Resources.transform(i));
        }
        int[] fromSpecial = new int[4];
        for(int i = 0; i < 4; i++){
            fromSpecial[i] = 0;
        }
        return new ExchangeResources(fromWarehouse, fromStrongbox, fromSpecial);
    }

    @Override
    public String toString(boolean mine) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Strongbox: ");
        stringBuilder.append(strongbox);
        stringBuilder.append("\n");
        if (getLeaderCardsNumber() != 0)
            stringBuilder.append("Leader cards:\n");
        for (LeaderCard card : leaderCards) {
            if (mine && !card.getUncovered()) {
                stringBuilder.append(leaderCards.indexOf(card));
                stringBuilder.append(". ");
                stringBuilder.append(card);
                stringBuilder.append("\n");
            } else if (mine && card.getUncovered()) {
                stringBuilder.append(ColorsCLI.YELLOW_BOLD);
                stringBuilder.append(leaderCards.indexOf(card));
                stringBuilder.append(". ");
                stringBuilder.append(ColorsCLI.CLEAR);
                stringBuilder.append(card);
                stringBuilder.append("\n");
            }
        }
        if (productionSpaces.size() != 0)
            stringBuilder.append("Development cards:\n");
        for (SpaceProd prod : productionSpaces) {
            if (prod.getTop().getLevel() != 0) {
                stringBuilder.append(productionSpaces.indexOf(prod));
                stringBuilder.append(". ");
                stringBuilder.append(prod.getTop());
                for (int i = 1; i < prod.getNumbCard(); i++) {
                    stringBuilder.append("  |");
                    stringBuilder.append(prod.getCards().get(i).getColor());
                    stringBuilder.append("  VP:");
                    stringBuilder.append(prod.getCards().get(i).getVP());
                }
                stringBuilder.append("\n");
            }
        }
        stringBuilder.append(warehouse);

        return stringBuilder.toString();
    }
}
