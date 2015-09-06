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


//TODO: Write unit test for logic
//TODO: Add next phase/ next round button
public class MainActivity extends ActionBarActivity {

    private PokerGame game;
    private Button ngButton, callButton, foldButton, raiseButton, allInButton;
    private EditText raiseField;
    private PokerListAdapter pokerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: Remove dummy data set.
        game.setupDummyPlayers(5,10000);
        pokerListAdapter = new PokerListAdapter(this,game.getPlayers());
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
        if (game.getCurrentPhase() == Phase.DEAL) {
            game.payBlinds();
        }
    }

    public void endPhase() {

        //TODO: Collect chips and and reset table.
        game.getPlayerIterator().setIndex(game.getDealerIndex() + 1);
        if(game.nextPhase()==Phase.DEAL){
            //new round.
            game.newRound();
        }
    }

    public void takeTurn(PlayerAction.Action action) {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.isFolded() || currentPlayer.isAllIn()) {
            game.getNextPlayer();
            return;
        }
        if(game.isBetRaised() && currentPlayer.equals(game.getRaiser())){
            //end the phase. We've come back to the same person.
            endPhase();
        }
        int raiseAmount = Integer.parseInt(raiseField.getText().toString());
        switch (action) {
            case CALL: {
                if (currentPlayer.canBet(game.getHighestBet())) {
                    currentPlayer.call(game.getHighestBet());
                } else {
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
                if (currentPlayer.canBet(raiseAmount)) {
                    currentPlayer.bet(raiseAmount);
                    //TODO: Change raise to one function.
                    game.setHighestBet(raiseAmount);
                    game.setRaiser(currentPlayer);
                    game.setBetRaised(true);
                } else {
                    return;
                }
                break;
            }
            case ALLIN: {
                currentPlayer.allIn();
                break;
            }
        }
        currentPlayer = game.getNextPlayer();
        if (currentPlayer.equals(game.getRoundStarter()) && !game.isBetRaised()) {
            endPhase();
        }
        pokerListAdapter.setSelected(game.getPlayerIterator().getIndex());
        updateUi();
    }
}
