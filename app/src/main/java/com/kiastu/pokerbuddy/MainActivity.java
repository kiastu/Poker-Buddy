package com.kiastu.pokerbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kiastu.pokerbuddy.model.Phase;
import com.kiastu.pokerbuddy.model.Player;
import com.kiastu.pokerbuddy.model.PlayerAction;


//TODO: Write unit test for logic
public class MainActivity extends Activity {

    private PokerGame game;
    private Button ngButton, callButton, foldButton, raiseButton, allInButton;
    private EditText raiseField;
    private PlayerListAdapter playerListAdapter;
    private RecyclerView playerListView;
    private TextView highestBetText, potText;
    private boolean firstPass;
    private boolean lastActionValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        initText();
        disableButtons();
        newGame();
        lastActionValid = true;
        playerListAdapter = new PlayerListAdapter(this, game.getPlayers());
        playerListView = (RecyclerView) findViewById(R.id.player_list);
        playerListView.setHasFixedSize(true);
        playerListView.setLayoutManager(new LinearLayoutManager(this));
        playerListView.setAdapter(playerListAdapter);
        startGame();
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

    private void initButtons() {
        ngButton = (Button) findViewById(R.id.button_new_game);
        callButton = (Button) findViewById(R.id.button_call);
        foldButton = (Button) findViewById(R.id.button_fold);
        raiseButton = (Button) findViewById(R.id.button_raise);
        allInButton = (Button) findViewById(R.id.button_all_in);

        ngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
                startGame();
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

    private void initText() {
        raiseField = (EditText) findViewById(R.id.edit_raise_amount);
        highestBetText = (TextView) findViewById(R.id.text_highest_bet);
        potText = (TextView) findViewById(R.id.text_pot_amount);

    }

    private void newGame() {
        game = new PokerGame();
        game.setupDummyPlayers(5, 10000);
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
        //TODO: Consider passing only changes to the adapter, and doing a full sync periodically? (improves efficency)
        playerListAdapter.setSelected(game.getCurrentPlayerIndex());
        playerListAdapter.setPlayerList(game.getPlayers());
        playerListAdapter.notifyDataSetChanged();
        highestBetText.setText("Highest Bet: " + game.getHighestBet());
        if (game.getPot() == 0) {
            potText.setText("Current Pot: - ");
        } else {
            potText.setText("Current Pot: " + game.getPot());
        }
    }

    private void startGame() {
        //TODO: Add other things to do before starting a game.
        startRound();
    }

    public void startRound() {
        game.startRound();
        updateUi();
        startPhase();
    }

    public void startPhase() {
        game.startPhase();
        updateUi();
        enableButtons();
        firstPass = true;
    }

    public void endPhase() {

        //TODO: Collect chips and and reset table.
        Phase gamePhase = game.nextPhase();
        if (gamePhase == Phase.DEAL) {
            //new round.
            game.startRound();

        }else {
            Toast toast = Toast.makeText(this,"The "+game.getCurrentPhase()+" is over.",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void takeTurn(PlayerAction.Action action) {
        disableButtons();
        Player currentPlayer = lastActionValid ? game.getNextPlayer() : game.getCurrentPlayer();

        if (currentPlayer.isFolded() || currentPlayer.isAllIn()) {//check if folded or all in.
            takeTurn(action);//go to the next person.
            return;
        }
        int raiseAmount = Integer.parseInt(raiseField.getText().toString());
        switch (action) {
            case CALL: {
                if (currentPlayer.canBet(game.getHighestBet())) {
                    currentPlayer.call(game.getHighestBet());
                    lastActionValid = true;
                } else {
                    //TODO: Provide notification that calling is not possible.
                    enableButtons();
                    lastActionValid = false;
                    return;
                }
                break;
            }
            case FOLD: {
                currentPlayer.fold();
                lastActionValid = true;
                break;
            }
            case RAISE: {
                //check valid raise
                if (currentPlayer.canBet(raiseAmount) && raiseAmount > game.getHighestBet()) {
                    game.raise(raiseAmount);
                    lastActionValid = true;
                } else {
                    //TODO: Provide notification that raising the amount is not possible.
                    enableButtons();
                    lastActionValid = false;
                    return;
                }
                break;
            }
            case ALLIN: {
                game.allIn();
                lastActionValid = true;
                break;
            }
        }
        firstPass = false;
        enableButtons();
        updateUi();
        //tl;dr: check if the next player is a player that shouldn't have a turn.
        if ((game.peekNextPlayer().equals(game.getRaiser()) && game.isBetRaised())||(game.peekNextPlayer().equals(game.getRoundStarter()) && !game.isBetRaised())) {
            endPhase();
        }
    }
}
