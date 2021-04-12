package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.playerboard.IllegalDecoratorException;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.SpaceProd;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;

import java.util.ArrayList;
import java.util.Arrays;

public class SimplePlayerBoard implements PlayerBoard {
    private Strongbox strongbox;
    private boolean inkWell;
    private ArrayList<LeaderCard> leaderCards;
    private ArrayList<SpaceProd> productionSpaces;
    public Warehouse warehouse;


    public SimplePlayerBoard(boolean inkWell){
        this.inkWell = inkWell;
        strongbox = new Strongbox();
        leaderCards = new ArrayList<LeaderCard>(2);
        productionSpaces = new ArrayList<SpaceProd>();
        warehouse = new Warehouse();

    }
    @Override
    public int getLeaderCardsNumber() {
        return leaderCards.size();
    }
    @Override
    public boolean getInkwell() {
        return inkWell;
    }

    @Override
    public void addLeaderCard(LeaderCard leaderCard) {
        if(checkResources(leaderCard.getCostResources()))
            if(checkColors(leaderCard.getCostColor()))
                leaderCards.add(leaderCard);
    }
    @Override
    public void removeLeaderCard(LeaderCard c){
        leaderCards.remove(c);
    }

    @Override
    public int getVictoryPoints() {
        int VP = 0;

        // Get leadercards victory points
        for(LeaderCard card : leaderCards){
            VP += card.getVP();
        }

        // Get production cards victory points
        for(SpaceProd sp : productionSpaces){
            VP += sp.getVictoryPoints();
        }
        // Get resources number victory points
        VP+=(strongbox.getNumResources()+warehouse.Resourcesnumb()+getExtraResources().stream().reduce(0, Integer::sum))/5;

        return VP;
    }

    @Override
    public void addWarehouseSpace(Resources resource, int maxQuantity) {
        throw new IllegalDecoratorException();
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        return new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0));
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
        return new ArrayList<Boolean>(Arrays.asList(false, false, false, false));
    }

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
    public boolean addWarehouseResource(Resources r, int row){
        return warehouse.AddResources(r,row);
    }

    @Override
    public boolean shiftWarehouseRows(int startingRow, int newRowPosition) {
        return warehouse.MoveRow(startingRow, newRowPosition);
    }

    @Override
    // Add a DevelopementCard to the production Spaces with only 1 choice possible
    public boolean addProductionCard(DevelopmentCard c){
        if(checkResources(c.getCostCard()))
            if(checkLevel(c))
            {
               if(c.getLevel()==1) {
                   if (productionSpaces.size() < 3) {
                       productionSpaces.add(new SpaceProd(c));
                       checkWinnerNumCards();
                       return true;
                   }
               }
               else
                       for (SpaceProd sp: productionSpaces) {
                           if (sp.getTop().getLevel() == c.getLevel() - 1) {
                               sp.addCard(c);
                               checkWinnerNumCards();
                               return true;
                           }
                       }
            }return false;
    }
    // Add a DevelopementCard to the production Space[index]
    public boolean addProductionCard(DevelopmentCard c,int index){
        if(checkResources(c.getCostCard()))
            if(productionSpaces.get(index).getTop().getLevel()==c.getLevel()-1)
            {
                productionSpaces.get(index).addCard(c);
                checkWinnerNumCards();
                return true;
            }
        return false;
    }

public void checkWinnerNumCards(){
    if(productionSpaces.get(0).getNumbCard()+productionSpaces.get(0).getNumbCard()+productionSpaces.get(0).getNumbCard()==7)
        throw new WinnerException();
}



    public boolean payResourcesWarehouse(int [] r)
    {


        for(int  i=0;i<4;i++){
            if(r[i]!=0)
            {
                if(r[i]-warehouse.getResource(Resources.transform(i))<=0) {
                    for (int j = 0; j < warehouse.getResource(Resources.transform(i)); j++)
                        warehouse.popResources(Resources.transform(i));
                }
                else return false;
            }
        }
return true;
    }
    public boolean payResourcesSpecialWarehouse(int [] r){
        ArrayList<Integer> Er=getExtraResources();
        for(int  i=0;i<4;i++){

            if(r[i]!=0)
            {
              if(r[i]- Er.get(i)<=0)
                  takeExtraResources(Resources.transform(i), r[i]);
              else return false;
    }
        }
        return true;
    }
    public boolean payResourcesSpecialStrongbox(int [] r){
        for(int  i=0;i<4;i++){

            if(r[i]!=0)
            {
                if(r[i]- strongbox.getResources(Resources.transform(i))<=0)
                    strongbox.setResources(Resources.transform(i),r[i]);
                else return false;
            }
        }
        return true;
    }

   public boolean checkLevel(DevelopmentCard c){
        if(c.getLevel()==1) {
            return productionSpaces.size() < 3;
        }
        else {
                    for (SpaceProd sp : productionSpaces)
                        if (sp.getTop().getLevel() == c.getLevel() - 1)
                            return true;
                }
        //LowResourcesException
      // System.out.println("Livello insuff");
      return false;
    }

    public boolean checkResources(int [] resources){
        int  tmp;
        ArrayList<Integer> Er=getExtraResources();
        for(int  i=0;i<4;i++)
            if(resources[i]!=0)
            {
                tmp=resources[i]-Er.get(i);
                tmp=tmp-warehouse.getResource(Resources.transform(i));
                tmp=tmp-strongbox.getResources(Resources.transform(i));
                if(tmp>0) {
                    //LowLevelException
                  //  System.out.println("Risorse insuff");
                    return false;
                }

            }
        return true;

    }
    public boolean checkColors(int [] colors){
        int tmp;
        for(Colors c:Colors.values()){
            tmp=0;
            if(colors[c.ordinal()]!=0){
                for(SpaceProd sp:productionSpaces)
                    tmp+=sp.checkColor(c);
                if(colors[c.ordinal()]-tmp>0) {
                //LowColorsException
                   // System.out.println("colori insuff");
                    return false;
                 }
            }
            }
        return true;
    }

    @Override
    public String toString() {
        return "SimplePlayerBoard{" +
                "\n" + strongbox +
                ", inkWell=" + inkWell +
                ", leaderCards=" + leaderCards +
                "\n" + productionSpaces +
                "\n" + warehouse +
                '}';
    }
}
