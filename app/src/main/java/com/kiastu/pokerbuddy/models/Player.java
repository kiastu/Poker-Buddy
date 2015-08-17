package com.kiastu.pokerbuddy.models;

import com.kiastu.pokerbuddy.Exceptions.NotEnoughChipsException;

/**
 * Created by dakong on 8/17/15.
 */
public class Player {
    private int chips;
    private String name;
    private int bet;

    public Player() {

    }

    public Player(String name, int chips) {
        this.chips = chips;
        this.name = name;
    }

    public float getBet() {
        return bet;
    }

    public void setBet(int currentBet) {
        this.bet = currentBet;
    }

    public float getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canBet(int amount){
        if(amount>chips){
            return false;
        }
        return true;
    }
    public void bet(int amount)throws NotEnoughChipsException {
        if(chips<amount){
            throw new NotEnoughChipsException("Player's bet surpasses chips. ");
        }
        bet -= amount;
        chips-=amount;
    }
    public int allIn() throws NotEnoughChipsException{
        bet(chips);
        return chips;
    }
}
