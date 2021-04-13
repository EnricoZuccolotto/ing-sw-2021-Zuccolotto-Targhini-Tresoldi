package it.polimi.ingsw.controller;

import java.util.ArrayList;

public enum TurnState {
    FIRST_TURN,
    FIRST_LEADER_ACTION,
    NORMAL_ACTION,
    WAREHOUSE_ACTION,
    LAST_LEADER_ACTION,
    PRODUCTION_ACTIONS,
    END;
    public static boolean isPossible(TurnState turnState,Action action){
        ArrayList<Action> possibleActions=new ArrayList<>();
       switch (turnState){
           case FIRST_TURN:
           {
               possibleActions.add( Action.LD_FOLD);
           }
           case FIRST_LEADER_ACTION: {
               possibleActions.add( Action.LD_LEADERACTION);
               possibleActions.add( Action.LD_FOLD);
               possibleActions.add( Action.SHIFT_WAREHOUSE);
               possibleActions.add( Action.STD_GETMARKET);
               possibleActions.add( Action.STD_GETPRODUCTION);
               possibleActions.add( Action.STD_USEPRODUCTION);
           }
           case NORMAL_ACTION:
           {
               possibleActions.add( Action.LD_LEADERACTION);
               possibleActions.add( Action.LD_FOLD);
               possibleActions.add( Action.SHIFT_WAREHOUSE);
               possibleActions.add( Action.STD_GETMARKET);
               possibleActions.add( Action.STD_GETPRODUCTION);
               possibleActions.add( Action.STD_USEPRODUCTION);
           }
           case WAREHOUSE_ACTION:
           {
               possibleActions.add( Action.LD_LEADERACTION);
               possibleActions.add( Action.LD_FOLD);
               possibleActions.add( Action.SORTING_WAREHOUSE);
               possibleActions.add( Action.SHIFT_WAREHOUSE);
               possibleActions.add( Action.END_TURN);

           }
           case LAST_LEADER_ACTION:
           {
               possibleActions.add( Action.LD_LEADERACTION);
               possibleActions.add( Action.LD_FOLD);
               possibleActions.add( Action.SHIFT_WAREHOUSE);
               possibleActions.add( Action.END_TURN);
           }
           case PRODUCTION_ACTIONS:
           {
               possibleActions.add( Action.STD_USEPRODUCTION);
               possibleActions.add( Action.LD_LEADERACTION);
               possibleActions.add( Action.LD_FOLD);
               possibleActions.add( Action.SHIFT_WAREHOUSE);
               possibleActions.add( Action.END_TURN);
           }
           case END:
               break;

           default:
               throw new IllegalStateException("Unexpected value: " + turnState);
       }

        return possibleActions.contains(action);
    }


}
