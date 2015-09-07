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
    private boolean firstPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        disableButtons();
        startGame();
        playerListAdapter = new PlayerListAdapter(this,game.getPlayers(),game.getCurrentPlayerIndex());
        playerListView = (RecyclerView)findViewById(R.id.player_list);
        playerListView.setHasFixedSize(true);
        playerListView.setLayoutManager(new LinearLayoutManager(this));
        playerListView.setAdapter(playerListAdapter);

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
        raiseField = (EditText) findViewById(R.id.edit_raise_amount);

        ngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        playerListAdapter.setPlayerList(game.getPlayers());
        playerListAdapter.notifyDataSetChanged();
    }
    private void startGame() {
        game = new PokerGame();
        game.setupDummyPlayers(5,10000);
        startRound();
    }
    public void startRound() {
        game.startRound();
        startPhase();
    }

    public void startPhase() {
        game.startPhase();
        enableButtons();
        firstPass = true;
    }

    public void endPhase() {

        //TODO: Collect chips and and reset table.

        if(game.nextPhase()==Phase.DEAL){
            //new round.
            game.startRound();
            return;
        }
    }

    public void takeTurn(PlayerAction.Action action) {
        disableButtons();
        Player currentPlayer = game.getNextPlayer();

        if (currentPlayer.equals(game.getRoundStarter()) && !game.isBetRaised()&&!firstPass) {
            endPhase();
        }

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
                    //TODO: Provide notification that calling is not possible.
                    enableButtons();
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
                if (currentPlayer.canBet(raiseAmount) && raiseAmount > game.getHighestBet()) {
                    game.raise(raiseAmount);
                } else {
                    //TODO: Provide notification that raising the amount is not possible.
                    enableButtons();
                    return;
                }
                break;
            }
            case ALLIN: {
                currentPlayer.allIn();
                break;
            }
        }
        playerListAdapter.setSelected(game.getCurrentPlayerIndex());
        firstPass = false;
        enableButtons();
        updateUi();
    }
}
