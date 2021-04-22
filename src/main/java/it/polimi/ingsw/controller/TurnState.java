package it.polimi.ingsw.controller;

import java.util.ArrayList;
/**
 * This enum contains all possible turn states.
 */
public enum TurnState {
    FIRST_TURN,
    SECOND_TURN,
    FIRST_LEADER_ACTION,
    NORMAL_ACTION,
    WAREHOUSE_ACTION,
    LAST_LEADER_ACTION,
    PRODUCTION_ACTIONS,
    END;

    /**
     * This function answer to the question 'is the Action action possible in the TurnState turnState?'
     *
     * @param action    Action
     * @param turnState current TurnState
     * @return true if the action is possible in this turnState, else false.
     */
    public static boolean isPossible(TurnState turnState, Action action) {
        ArrayList<Action> possibleActions = new ArrayList<>();
        switch (turnState) {
            case FIRST_TURN: {
                possibleActions.add(Action.FIRST_ACTION);
                break;
            }
            case SECOND_TURN: {
                possibleActions.add(Action.SECOND_ACTION);
                break;
            }
            case NORMAL_ACTION:
            case FIRST_LEADER_ACTION: {
                possibleActions.add(Action.LD_ACTION);
                possibleActions.add(Action.SHIFT_WAREHOUSE);
                possibleActions.add(Action.STD_GETMARKET);
                possibleActions.add(Action.STD_GETPRODUCTION);
                possibleActions.add(Action.STD_USEPRODUCTION);
                break;
            }

            case WAREHOUSE_ACTION: {
                possibleActions.add(Action.LD_ACTION);
                possibleActions.add(Action.SORTING_WAREHOUSE);
                possibleActions.add(Action.SHIFT_WAREHOUSE);
                possibleActions.add(Action.END_TURN);
                break;
           }
           case LAST_LEADER_ACTION:
           {
               possibleActions.add( Action.LD_ACTION);
               possibleActions.add( Action.SHIFT_WAREHOUSE);
               possibleActions.add( Action.END_TURN);
               break;
           }
           case PRODUCTION_ACTIONS:
           {
               possibleActions.add( Action.STD_USEPRODUCTION);
               possibleActions.add( Action.LD_ACTION);
               possibleActions.add( Action.SHIFT_WAREHOUSE);
               possibleActions.add( Action.END_TURN);
               break;
           }
           case END:
               break;

           default:
               throw new IllegalStateException("Unexpected value: " + turnState);
       }

        return possibleActions.contains(action);
    }


}
