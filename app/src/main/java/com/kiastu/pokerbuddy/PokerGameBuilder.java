package com.kiastu.pokerbuddy;

// Possibly have all of this for a settings builder class?
public class PokerGameBuilder {
    private PokerGame pokerGame;
    public PokerGameBuilder(){
        pokerGame = new PokerGame(null);
    }

    private PokerGame build(){
        return pokerGame;
    }
}
