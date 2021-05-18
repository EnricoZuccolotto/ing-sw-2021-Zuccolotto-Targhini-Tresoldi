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
    NOT_IN_TURN,
    END;

    /**
     * This function answer to the question 'is the Action action possible in the TurnState turnState?'
     *
     * @param action    Action
     * @param turnState current TurnState
     * @return true if the action is possible in this turnState, else false.
     */
    public static boolean isPossible(TurnState turnState, Action action) {
        ArrayList<Action> possibleActions = possibleActions(turnState);
        return possibleActions.contains(action);
    }

    public static ArrayList<Action> possibleActions(TurnState turnState) {
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
                possibleActions.add(Action.ACTIVE_LEADER);
                possibleActions.add(Action.SHIFT_WAREHOUSE);
                possibleActions.add(Action.GET_RESOURCES_FROM_MARKET);
                possibleActions.add(Action.BUY_DEVELOPMENT_CARD);
                possibleActions.add(Action.USE_PRODUCTIONS);
                break;
            }

            case WAREHOUSE_ACTION: {
                possibleActions.add(Action.ACTIVE_LEADER);
                possibleActions.add(Action.SORTING_WAREHOUSE);
                possibleActions.add(Action.SHIFT_WAREHOUSE);
                possibleActions.add(Action.END_TURN);
                break;
            }
            case LAST_LEADER_ACTION: {
                possibleActions.add(Action.ACTIVE_LEADER);
                possibleActions.add(Action.SHIFT_WAREHOUSE);
                possibleActions.add(Action.END_TURN);
                break;
            }
            case PRODUCTION_ACTIONS: {
                possibleActions.add(Action.USE_PRODUCTIONS);
                possibleActions.add(Action.ACTIVE_LEADER);
                possibleActions.add(Action.SHIFT_WAREHOUSE);
                possibleActions.add(Action.END_TURN);
                break;
            }
            case END:
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + turnState);
        }
        return possibleActions;
    }


}
