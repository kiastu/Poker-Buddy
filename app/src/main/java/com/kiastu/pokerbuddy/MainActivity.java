package com.kiastu.pokerbuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kiastu.pokerbuddy.model.Phase;
import com.kiastu.pokerbuddy.model.Player;
import com.kiastu.pokerbuddy.model.PlayerAction;


public class MainActivity extends ActionBarActivity {

    private PokerGame game;
    private Button ngButton, callButton, foldButton, raiseButton, allInButton;
    private EditText raiseField;
    private boolean firstPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        disableButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startGame() {
        game = new PokerGame();
        playRound();
    }

    private void initButtons() {
        ngButton = (Button) findViewById(R.id.button_new_game);
        callButton = (Button) findViewById(R.id.button_call);
        foldButton = (Button) findViewById(R.id.button_fold);
        raiseButton = (Button) findViewById(R.id.button_raise);
        allInButton = (Button) findViewById(R.id.button_all_in);
        raiseField = (EditText) findViewById(R.id.edit_raise_amount);

        ngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create new game.
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeTurn(PlayerAction.Action.CALL);
            }
        });
        foldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeTurn(PlayerAction.Action.FOLD);
            }
        });
        raiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeTurn(PlayerAction.Action.RAISE);
            }
        });
        allInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeTurn(PlayerAction.Action.ALLIN);
            }
        });
    }

    private void enableButtons() {
        callButton.setEnabled(true);
        foldButton.setEnabled(true);
        raiseButton.setEnabled(true);
        allInButton.setEnabled(true);
    }

    private void disableButtons() {
        callButton.setEnabled(false);
        foldButton.setEnabled(false);
        raiseButton.setEnabled(false);
        allInButton.setEnabled(false);
    }

    private void updateUi() {

    }

    public void playRound() {
        game.getPlayerIterator().setIndex(game.getDealerIndex() + 1);
        game.setRoundStarter(game.getPlayers().get(game.getDealerIndex() + 1));
        playPhase();
    }

    public void playPhase() {
        game.setCurrentPlayer(game.getRoundStarter());
        firstPass = true;
        if (game.getCurrentPhase() == Phase.DEAL) {
            game.payBlinds();
        }
    }
    public void endPhase(){
        game.getPlayerIterator().setIndex(game.getDealerIndex() + 1);
        //TODO:check for round end.
    }

    public void takeTurn(PlayerAction.Action action) {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.isFolded()||currentPlayer.isAllIn()) {
            game.getNextPlayer();
            return;
        }
        firstPass = false;
        int raiseAmount = Integer.parseInt(raiseField.getText().toString());
        switch (action) {
            case CALL: {
                if(currentPlayer.canBet(game.getHighestBet())){
                    currentPlayer.call(game.getHighestBet());
                }else{
                    return;
                }
                break;
            }
            case FOLD: {
                currentPlayer.fold();
                break;
            }
            case RAISE: {
                //check valid raise
                if(currentPlayer.canBet(raiseAmount)){
                    currentPlayer.bet(raiseAmount);
                    game.setHighestBet(raiseAmount);
                    game.setBetRaised(true);
                }else{
                    return;
                }
                break;
            }
            case ALLIN: {
                currentPlayer.allIn();
                break;
            }
        }
        //TODO: Handle raises.
        currentPlayer = game.getNextPlayer();
        if(currentPlayer.equals(game.getRoundStarter()) && !game.isBetRaised()){
            endPhase();
        }
        //TODO: Select next player.
    }
}
