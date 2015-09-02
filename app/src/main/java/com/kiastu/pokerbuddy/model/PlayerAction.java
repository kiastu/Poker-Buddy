package com.kiastu.pokerbuddy.model;

/**
 * Created by kiastu on 01/09/15.
 */
public class PlayerAction {
    public enum Action {CALL, FOLD, RAISE, ALLIN}
    private Action action;
    private int amount;

    public PlayerAction(Action action, int amount){
        this.action = action;
        this.amount = amount;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
