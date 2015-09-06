package com.kiastu.pokerbuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.kiastu.pokerbuddy.model.Phase;
import com.kiastu.pokerbuddy.model.PlayerAction;


public class MainActivity extends ActionBarActivity implements PokerGameInterface {

    PokerGame game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ngButton = (Button)findViewById(R.id.button_new_game);
        ngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create new game.
            }
        });
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
    private void startGame(){
        game = new PokerGame(this);
        game.playRound();
    }
    public PlayerAction onRequirePlayerAction(){
        return new PlayerAction(PlayerAction.Action.ALLIN,0);
    }
    public void onPlayerTurnBegin(){}

    public void onPlayerTurnEnd(PlayerAction.Action action){}
    public void onPhaseStart(Phase currentPhase){}
    public void onPhaseEnd(Phase currentPhase){}
}
