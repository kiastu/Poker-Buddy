package com.kiastu.pokerbuddy;

import android.util.Log;

import com.kiastu.pokerbuddy.Exceptions.NotEnoughChipsException;
import com.kiastu.pokerbuddy.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dakong on 8/17/15.
 */
public class PokerGame {
//    private GameMode gameMode;
//    public enum GameMode {NO_RULES, LIMITED, NO_LIMIT}

    public enum Status {DEAL, FLOP, TURN, RIVER,FINISHED}
    private Status status;
    private List<Player> players;
    private int pot, sidePot, turnIndex,smallBlind,bigBlind;
    private boolean betRaised;

    private static String TAG = "PokerGame";

    public PokerGame() {
        players = new ArrayList<>();
        status = Status.DEAL;
        setupDummyPlayers(5, 10000);
        turnIndex = 0;
        pot = 0;
        sidePot = 0;
        betRaised = false;
        smallBlind = 0;
        bigBlind = 0;
    }

    public void setupDummyPlayers(int numPlayers, int startMoney) {
        if(numPlayers<2){
            Log.e(TAG,"Error, players can't be less than 2");
        }
        for (int i = 0; i < numPlayers; i++) {
            Player dummy = new Player("Player" + i, startMoney);
        }
        Log.d(TAG, "Dummy players completed");
    }

    public void startGame(){
        smallBlind = 1;
        bigBlind = 2;
        startDealPhase();
    }
    public void startDealPhase(){
        if(players.size()<3){
            //2 players, big blind/small blind just alternate.
            try {
                if(players.get(turnIndex).canBet(smallBlind)){
                    players.get(turnIndex).bet(smallBlind);
                }else{
                    //cannot afford the small blind.
                    //check if any chips are left at all.

                }
                if(players.get(turnIndex+1).getChips()<bigBlind){

                }else{
                    players.get(turnIndex+1).bet(bigBlind);
                }
            }catch(NotEnoughChipsException e){
                Log.e(TAG,"Player does not have enough chips to pay the blind.");
            }

        }
        for(Player player:players){

        }
    }
    public void nextTurn(){
        //deal out the starting
    }

    private int collectToPot(){
        return 0;
    }

    public class PokerGameBuilder {

    }

}
