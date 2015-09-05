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

    private String TAG = "Player";

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


    public float getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }


    public void bet(int amount) {
        if (chips < amount) {
            Log.w(TAG,"Cannot bet the amount "+amount+". The player has "+chips" chips left. No bet placed.");
            amount = 0;
        }
        currentBet += amount;
        chips -= amount;
    }

    public boolean canBet(int amount) {
        if (amount > chips) {
            return false;
        }
        return true;
    }

    public float getCurrentBet() {
        return currentBet;
    }

    public boolean call(int callAmount) {

        if(callAmount<currentBet){
            //TODO: Replace with log
            Log.e(TAG,"Error with attempted call, the call amount is already satisfied.");
            return false; //there's something wrong here...
        }
        if(canBet(callAmount-currentBet)){
            bet(callAmount-currentBet);
            return true;
        }else{
            Log.w(TAG,"Cannot bet the amount "+callAmount+". The player has "+chips+" chips left. No bet placed.");
            return false;
        }
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

    public boolean fold() {
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
