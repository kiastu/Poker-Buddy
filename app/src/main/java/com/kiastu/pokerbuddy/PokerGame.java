package com.kiastu.pokerbuddy;

import android.util.Log;

import com.kiastu.pokerbuddy.exception.NotEnoughChipsException;
import com.kiastu.pokerbuddy.model.CircularIterator;
import com.kiastu.pokerbuddy.model.Phase;
import com.kiastu.pokerbuddy.model.Player;
import com.kiastu.pokerbuddy.model.PlayerAction;

import java.util.ArrayList;

/**
 * Created by dakong on 8/17/15.
 * This is the base poker class, played with no restrictions on the raise amount/intervals.
 * In the future, the different games will be inherited classes from PokerGame.
 * <p/>
 * Notes:
 * Alastair:
 * Set it from chips to currency. Poker night boissss
 */

//TODO: Write unit test for logic
//TODO: Write logic for split pots
//TODO: Write logic for turn/river
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
    CircularIterator<Player> playerIterator;
    private PokerGameInterface viewListener;

    private static String TAG = "PokerGame";

    public PokerGame(PokerGameInterface listener) {
        this.viewListener = listener;
        this.players = new ArrayList<>();
        this.setupDummyPlayers(5, 10000);
        this.dealerIndex = 0;
        this.pot = 0;
        this.sidePot = 0;
        this.betRaised = false;
        this.smallBlind = 0;
        this.bigBlind = 0;
        this.playerIterator = new CircularIterator<>(players, 1);
    }
    public void setupDummyPlayers(int numPlayers, int startMoney) {
        if (numPlayers < 2) {
            Log.e(TAG, "Error, players can't be less than 2");
        }
        for (int i = 0; i < numPlayers; i++) {
            Player dummy = new Player("Player" + i, startMoney);
        }
        Log.d(TAG, "Dummy players completed");
    }

    public void playPhase(Player roundStarter) {

        if(currentPhase == Phase.DEAL){
            payBlinds();
        }
        Player checkPlayer = playerIterator.next();

        do {
            takeTurn(checkPlayer);
            checkPlayer = playerIterator.next();
        } while (!checkPlayer.equals(roundStarter));

        //handle the event that someone has raised.
        while (betRaised) {
            Player currentPlayer = playerIterator.next();
            if (raiser.equals(currentPlayer)) {
                //it's looped back again.
                betRaised = false;
            }
            //take a turn until they make an available action
            while (!takeTurn(currentPlayer)) {
                Log.e(PokerGame.TAG, "Invalid action.");
            }
        }
    }

    public void payBlinds(){
        //pay blinds
        try {
            Player smallPlayer = playerIterator.next();
            Player bigPlayer = playerIterator.next();
            if (smallPlayer.canBet(smallBlind)) {
                smallPlayer.bet(smallBlind);
            } else if (smallPlayer.getChips() != 0) {
                //we can still afford something at least.
                smallPlayer.allIn();
                //TODO: Handle split pot
            }
            if (bigPlayer.canBet(bigBlind)) {
                bigPlayer.bet(bigBlind);
            } else if (bigPlayer.getChips() != 0) {
                bigPlayer.allIn();
                //TODO:Handle split pot
            }

        } catch (NotEnoughChipsException e) {
            Log.e(TAG, "Player does not have enough chips to pay the blind.");
        }

        highestBet = bigBlind;
    }

    public boolean takeTurn(Player player) {
        viewListener.onPlayerTurnBegin();
        if (player.isFolded()) {
            return true;
        }
        PlayerAction playerAction = viewListener.onRequirePlayerAction();
        int betAmount = playerAction.getAmount();
        PlayerAction.Action action = playerAction.getAction();
        switch (action) {
            case CALL: {
                try {
                    player.call(highestBet);
                } catch (NotEnoughChipsException e) {
                    return false;
                }
                viewListener.onPlayerTurnEnd(PlayerAction.Action.CALL);
                return true;
            }

            case FOLD: {
                player.fold();
                viewListener.onPlayerTurnEnd(PlayerAction.Action.FOLD);
                break;
            }
            case RAISE: {
                try {
                    player.bet(betAmount);
                    highestBet += betAmount;
                    betRaised = true;
                    raiser = player;
                } catch (NotEnoughChipsException e) {
                    return false;
                }
                viewListener.onPlayerTurnEnd(PlayerAction.Action.RAISE);
                return true;
            }
            case ALLIN: {
                highestBet = player.allIn();
                viewListener.onPlayerTurnEnd(PlayerAction.Action.ALLIN);
                return true;
            }

        }
        return false;
    }



    /**
     * Handles the preparation for the start of a new currentPhase.
     * - Collect pot, distribute chips out to winners, eliminate losers.
     */
    public Phase nextPhase() {
        //prepare the board.
        collectToPot();
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
                newRound();
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
    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }



}
