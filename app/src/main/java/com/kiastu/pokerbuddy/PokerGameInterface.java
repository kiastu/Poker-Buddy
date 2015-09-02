package com.kiastu.pokerbuddy;

import com.kiastu.pokerbuddy.model.Phase;
import com.kiastu.pokerbuddy.model.PlayerAction;

public interface PokerGameInterface {
    /**
     * Called when a player must make a choice
     * @return PlayerAction - The action that the player took.
     */
        PlayerAction onRequirePlayerAction();
        void onPlayerTurnBegin();
        void onPlayerTurnEnd(PlayerAction.Action action);

    /**
     * Phase control callbacks.
     * @param phase - The phase starting or ending.
     */
        void onPhaseStart(Phase phase);
        void onPhaseEnd(Phase phase);
}
