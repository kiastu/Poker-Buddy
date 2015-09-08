package com.kiastu.pokerbuddy.model;

/**
 * Created by kiastu on 02/09/15.
 */
public enum Phase {DEAL("Deal"), FLOP("Flop"), TURN("Turn"), RIVER("River"),FINISHED("Finished");
    private String value;

    Phase(String value){
        this.value = value;
    }
    @Override
    public String toString(){
        return value;
    }
};
