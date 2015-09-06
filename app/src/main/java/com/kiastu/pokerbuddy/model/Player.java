package com.kiastu.pokerbuddy.model;

import android.util.Log;

/**
 * Created by dakong on 8/17/15.
 */
public class Player {
    private int chips;
    private String name;
    private int currentBet;
    private boolean isAllIn;
    private boolean isFolded;

    private static String TAG = "Player";

    public Player() {
    }

    public Player(String name, int chips) {
        this.chips = chips;
        this.name = name;
        this.isAllIn = false;
        this.isFolded = false;
        this.currentBet = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }


    public void bet(int amount) {
        currentBet += amount;
        chips -= amount;
    }

    public boolean canBet(int amount) {
        if (amount - currentBet > chips) {
            return false;
        }
        return true;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void call(int callAmount) {
            bet(callAmount-currentBet);
    }

    public int addChips(int amount) {
        if (amount != 0) {
            isAllIn = false;
        }
        return chips += amount;
    }

    public int allIn(){
        currentBet += chips;
        chips = 0;
        isAllIn = true;
        return currentBet;
    }

    public boolean isAllIn() {
        return isAllIn;
    }

    public boolean isFolded() {
        return isFolded;
    }

    public void fold() {
        this.isFolded = true;
    }

    public int newRound() {
        int newBet = currentBet;
        currentBet = 0;
        isFolded = false;
        isAllIn = false;
        return newBet;

    }
}
