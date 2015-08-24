package com.kiastu.pokerbuddy;

import android.util.Log;

import com.kiastu.pokerbuddy.Exceptions.NotEnoughChipsException;
import com.kiastu.pokerbuddy.models.CircularIterator;
import com.kiastu.pokerbuddy.models.Player;

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

    public enum Phase {DEAL, FLOP, TURN, RIVER, FINISHED}

    public enum Action {CALL, FOLD, RAISE, ALLIN}

    private Phase phase;
    private ArrayList<Player> players;
    private int pot, sidePot, dealerIndex, smallBlind, bigBlind, highestBet;
    private boolean betRaised;
    private Player raiser;
    CircularIterator<Player> playerIterator;

    private static String TAG = "PokerGame";

    public PokerGame() {
        players = new ArrayList<>();
        phase = Phase.DEAL;
        setupDummyPlayers(5, 10000);
        dealerIndex = 0;
        pot = 0;
        sidePot = 0;
        betRaised = false;
        smallBlind = 0;
        bigBlind = 0;
        playerIterator = new CircularIterator<>(players, 0);
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

    public void startGame() {
        smallBlind = 1;
        bigBlind = 2;
        startDealPhase();
    }

    public void startDealPhase() {
        playerIterator = new CircularIterator<>(players, dealerIndex);
        //pay blinds
        try {
            Player smallPlayer = playerIterator.next();
            Player bigPlayer = playerIterator.next();
            if (smallPlayer.canBet(smallBlind)) {
                smallPlayer.bet(smallBlind);
            } else {
                //can't afford the small blind.
                if (smallPlayer.getChips() != 0) {
                    //we can still afford something at least.
                    smallPlayer.allIn();
                    //TODO: Handle split pot
                }
            }
            if (bigPlayer.canBet(bigBlind)) {
                bigPlayer.bet(bigBlind);
            } else {
                if (bigPlayer.getChips() != 0) {
                    bigPlayer.allIn();
                    //TODO:Handle split pot
                }
            }
        } catch (NotEnoughChipsException e) {
            Log.e(TAG, "Player does not have enough chips to pay the blind.");
        }

        highestBet = bigBlind;

        playPhase(playerIterator.next());
        nextPhase(Phase.FLOP);
    }


    public boolean takeTurn(Player player) {
        //logic for betting.
        if (player.isFolded()) {
            return true;//no action for a player who folded.
        }
        //TODO: Accept user input

        int betAmount = 1337;
        Action action = Action.CALL;
        switch (action) {
            case CALL: {
                try {
                    player.call(highestBet);
                } catch (NotEnoughChipsException e) {
                    return false;
                }
                return true;
            }

            case FOLD: {
                player.fold();
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
                return true;
            }
            case ALLIN: {
                highestBet = player.allIn();
                return true;
            }

        }
        return false;
    }

    public void playPhase(Player roundStarter) {

        //needs to go around the circle once.
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
            }
        }
    }
    /**
     * Handles the preparation for the start of a new phase.
     * - Collect pot, distribute chips out to winners, eliminate losers.
     */
    public void nextPhase(Phase phase) {
        //prepare the board.
        collectToPot();
        //TODO: Logic for choosing which phase should go next.
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
            pot +=  toPot;
            collected+=toPot;

        }
        return collected;
    }
    /*
    *
     */

    public class PokerGameBuilder {
        //TODO: Create a builder to create a custom game with settings.
    }

}
