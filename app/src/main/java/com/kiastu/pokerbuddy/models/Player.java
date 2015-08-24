package com.kiastu.pokerbuddy.models;

import com.kiastu.pokerbuddy.Exceptions.NotEnoughChipsException;

/**
 * Created by dakong on 8/17/15.
 */
public class Player {
    private int chips;
    private String name;
    private int currentBet;
    private boolean isAllIn;
    private boolean isFolded;

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


    public void bet(int amount) throws NotEnoughChipsException {
        if (chips < amount) {
            throw new NotEnoughChipsException("Player's bet surpasses chips. ");
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

    public boolean call(int callAmount) throws NotEnoughChipsException {

        if(callAmount<currentBet){
            //TODO: Replace with log
            System.out.println("Error with call, something's went wrong");
            return false; //there's something wrong here...
        }
        else if(callAmount>currentBet + chips){
            throw new NotEnoughChipsException("Cannot call when you have no chips!");
        }
    }


    public int addChips(int amount) {
        if (amount != 0) {
            isAllIn = false;
        }
        return chips += amount;
    }

    //chose not to use bet() as I feel that this function should not throw an exception
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
