package com.kiastu.pokerbuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kiastu.pokerbuddy.model.Phase;
import com.kiastu.pokerbuddy.model.PlayerAction;


public class MainActivity extends ActionBarActivity implements PokerGameInterface {

    private PokerGame game;
    private Button ngButton,callButton,foldButton,raiseButton,allInButton;
    private EditText raiseField;
    private PlayerAction.Action chosenAction;

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
    private void startGame(){
        game = new PokerGame(this);
        game.playRound();
    }
    public PlayerAction onRequirePlayerAction(){
        enableButtons();
        //TODO: Fix hacky infinite loop here. Maybe needs to re-evaluate how much power the PokerGame class has.
        while(chosenAction==null){
        }
        PlayerAction action;

        if(chosenAction.equals(PlayerAction.Action.RAISE)){
            action =  new PlayerAction(chosenAction,Integer.parseInt(raiseField.getText().toString()));
        }
        else{
            action =  new PlayerAction(chosenAction,0);
        }
        raiseField.setText("");
        chosenAction = null;
        disableButtons();
        return action;
    }
    public void onPlayerTurnBegin(){}

    public void onPlayerTurnEnd(PlayerAction.Action action){}
    public void onPhaseStart(Phase currentPhase){}
    public void onPhaseEnd(Phase currentPhase){
        updateUi();
    }
    private void initButtons(){
        ngButton = (Button)findViewById(R.id.button_new_game);
        callButton = (Button)findViewById(R.id.button_call);
        foldButton = (Button)findViewById(R.id.button_fold);
        raiseButton = (Button)findViewById(R.id.button_raise);
        allInButton = (Button)findViewById(R.id.button_all_in);
        raiseField = (EditText)findViewById(R.id.edit_raise_amount);

        ngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create new game.
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAction = PlayerAction.Action.CALL;
            }
        });
        foldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAction = PlayerAction.Action.FOLD;
            }
        });
        raiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAction = PlayerAction.Action.RAISE;
            }
        });allInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenAction = PlayerAction.Action.ALLIN;
            }
        });
    }
    private void enableButtons(){
        callButton.setEnabled(true);
        foldButton.setEnabled(true);
        raiseButton.setEnabled(true);
        allInButton.setEnabled(true);
    }
    private void disableButtons(){
        callButton.setEnabled(false);
        foldButton.setEnabled(false);
        raiseButton.setEnabled(false);
        allInButton.setEnabled(false);
    }
    private void updateUi(){

    }
}
