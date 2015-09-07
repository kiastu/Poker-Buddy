package com.kiastu.pokerbuddy;

import android.util.Log;

import com.kiastu.pokerbuddy.model.CircularIterator;
import com.kiastu.pokerbuddy.model.Phase;
import com.kiastu.pokerbuddy.model.Player;

import java.util.ArrayList;

/**
 * This is the base poker class, played with no restrictions on the raise amount/intervals.
 * In the future, the different games will be inherited classes from PokerGame.
 * <p/>
 * Notes:
 * Alastair: Set it from chips to currency. Poker night boissss
 */

//TODO: Write logic for split pots
//TODO: Write logic for turn/river
//TODO: Write function for ALLIN to take in to account the highestBet.
//TODO: Fix my weird iterator.
public class PokerGame {
    private Phase currentPhase;
    private ArrayList<Player> players;
    private int pot;
    private int sidePot;
    private int dealerIndex;
    private int smallBlind;
    private int bigBlind;
    private int highestBet;
    private boolean betRaised;
    private Player raiser;
    private CircularIterator<Player> playerIterator;
    private Player roundStarter;
    private Player currentPlayer;

    private static String TAG = "PokerGame";

    public PokerGame(){
        this.players = new ArrayList<>();
        this.dealerIndex = 0;
        this.pot = 0;
        this.sidePot = 0;
        this.betRaised = false;
        this.smallBlind = 0;
        this.bigBlind = 0;
        this.currentPhase = Phase.DEAL;
        this.playerIterator = new CircularIterator<>(players, 0);
    }
    public void setupDummyPlayers(int numPlayers, int startMoney) {
        if (numPlayers < 2) {
            Log.e(TAG, "Error, players can't be less than 2");
        }
        for (int i = 0; i < numPlayers; i++) {
            Player dummy = new Player("Player" + i, startMoney);
            players.add(dummy);
        }
        Log.d(TAG, "Dummy players completed");
    }


    public void payBlinds(){
        //pay blinds
        Player smallPlayer = playerIterator.next();
        if (smallPlayer.canBet(smallBlind)) {
            smallPlayer.bet(smallBlind);
        } else if (smallPlayer.getChips() != 0) {
            //we can still afford something at least.
            smallPlayer.allIn();
            //TODO: Handle split pot
        }
        Player bigPlayer = playerIterator.next();
        if (bigPlayer.canBet(bigBlind)) {
            bigPlayer.bet(bigBlind);
        } else if (bigPlayer.getChips() != 0) {
            bigPlayer.allIn();
            //TODO:Handle split pot
        }
        highestBet = bigBlind;
    }

    /**
     * Handles the preparation for the start of a new currentPhase.
     * - Collect pot, distribute chips out to winners, eliminate losers.
     */
    public Phase nextPhase() {
        //prepare the board.
        collectToPot();
        highestBet = 0;
        raiser = null;
        betRaised = false;
        switch(currentPhase){
            case DEAL:
                currentPhase = Phase.FLOP;
                break;
            case FLOP:
                currentPhase = Phase.RIVER;
                break;
            case RIVER:
                currentPhase = Phase.TURN;
                break;
            case TURN:
                currentPhase = Phase.DEAL;
                break;
            case FINISHED:
                break;
        }
        return currentPhase;
    }

    /**
     * Call this method at the end of a round to eliminate any losers without chips.
     */
    public void cleanOutLosers() {
        //TODO: Finish function
        for(Player player:players){
            if(player.getChips()<=0){
                players.remove(player);
            }
        }
    }

    private int collectToPot() {
        int collected = 0;
        for (Player player : players) {
            int toPot = player.newRound();
            pot += toPot;
            collected += toPot;
        }
        return collected;
    }
    public void newRound(){
        //TODO: handle newRound logic.
        currentPhase = Phase.DEAL;
    }

    public void raise(int raiseAmount){
        currentPlayer.bet(raiseAmount);
        setHighestBet(raiseAmount);
        setRaiser(currentPlayer);
        setBetRaised(true);
    }
    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public int getSidePot() {
        return sidePot;
    }

    public void setSidePot(int sidePot) {
        this.sidePot = sidePot;
    }

    public int getDealerIndex() {
        return dealerIndex;
    }

    public void setDealerIndex(int dealerIndex) {
        this.dealerIndex = dealerIndex;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getHighestBet() {
        return highestBet;
    }

    public void setHighestBet(int highestBet) {
        this.highestBet = highestBet;
    }

    public boolean isBetRaised() {
        return betRaised;
    }

    public void setBetRaised(boolean betRaised) {
        this.betRaised = betRaised;
    }

    public Player getRaiser() {
        return raiser;
    }

    public void setRaiser(Player raiser) {
        this.raiser = raiser;
    }

    public CircularIterator<Player> getPlayerIterator() {
        return playerIterator;
    }

    public void setPlayerIterator(CircularIterator<Player> playerIterator) {
        this.playerIterator = playerIterator;
    }

    public Player getNextPlayer(){
        currentPlayer = this.playerIterator.next();
        return currentPlayer;
    }

    public Player getRoundStarter() {
        return roundStarter;
    }

    public void setRoundStarter(Player roundStarter) {
        this.roundStarter = roundStarter;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
