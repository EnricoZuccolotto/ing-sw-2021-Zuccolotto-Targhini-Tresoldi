package it.polimi.ingsw.model.board;

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
        strongbox.setResources(Resources.COIN,10);
        strongbox.setResources(Resources.SHIELD,10);
        strongbox.setResources(Resources.SERVANT,10);
        strongbox.setResources(Resources.STONE,10);
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

        return VP;
    }

    @Override
    public void addWarehouseSpace(Resources resource) {
        // throw new IllegalDecoratorException()
    }

    @Override
    public ArrayList<Integer> getExtraResources() {
        return new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0));
    }

    @Override
    public void addExtraResources(Resources resources, int quantity) {
        // throw new IllegalDecoratorException()
    }

    @Override
    public void takeExtraResources(Resources resource, int quantity) {
        // throw new IllegalDecoratorException()
    }

    @Override
    public void addDiscount(Resources resource, int amount) {
        // throw new IllegalDecoratorException()
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
        // throw new IllegalDecoratorException()
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



        // throw new IllegalDecoratorException()
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
    public boolean addProductionCard(DevelopmentCard c){


        ArrayList<Integer> possibilities=new ArrayList<>();
        if(checkResources(c.getCostCard()))
            if(checkLevel(c))
            {
               if(c.getLevel()==1) {
                   if (productionSpaces.size() < 3) {
                       System.out.println("aggiungo nuovo spazio");
                       productionSpaces.add(new SpaceProd(c));
                       payResources(c.getCostCard());
                       return true;
                   }
               }
               else
                   {
                       for (SpaceProd sp: productionSpaces) {
                           if (sp.getTop().getLevel() == c.getLevel()-1)
                               possibilities.add(productionSpaces.indexOf(sp));
                       }

                       if(possibilities.size()==1) {
                           productionSpaces.get(possibilities.get(0)).addCard(c);
                           payResources(c.getCostCard());
                           System.out.println("1 possibilità");
                           return true;
                       }
                       else
                           {
                               System.out.println("Possibili soluzioni"+possibilities);
                               //make a choice
                               //productionSpaces.get(possibilities.get(choice)).addCard(c);
                               // payResources(c.getCostCard());
                               return false;//ho un array con le possibili soluzioni possibilities
                    }}
                return false;
            }
               return false;
            }

    private void payResources(int [] r)
    {
        int tmp;
        ArrayList<Integer> Er=getExtraResources();
        for(int  i=0;i<4;i++){

            if(r[i]!=0)
            {
                tmp=r[i]- Er.get(i);
                if(tmp>0)
                    takeExtraResources(Resources.transform(i), Er.get(i));
                else
                    takeExtraResources(Resources.transform(i),r[i]);
                tmp=tmp-warehouse.getResource(Resources.transform(i));
                // se >0 devo prendere le risorse sia dalla strongobox che dalla warehouse
                if(tmp>0) {
                    for (int j = 0; j < warehouse.getResource(Resources.transform(i)); j++)
                        warehouse.popResources(Resources.transform(i));
                    strongbox.setResources(Resources.transform(i),strongbox.getResources(Resources.transform(i))-tmp);
                }
                // se <=0 devo prendere le risorse dalla warehouse
                else
                    for (int j = 0; j < r[i]; j++)
                        warehouse.popResources(Resources.transform(i));

            }
            }


    }
   public boolean checkLevel(DevelopmentCard c){
        if(c.getLevel()==1) {
            if (productionSpaces.size() < 3)
                return true;
        }
        else {
                    for (SpaceProd sp : productionSpaces)
                        if (sp.getTop().getLevel() == c.getLevel() - 1)
                            return true;
                }
        //LowResourcesException
       System.out.println("Livello insuff");
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
                    System.out.println("Risorse insuff");
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
                    System.out.println("colori insuff");
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
