package com.kiastu.pokerbuddy;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiastu.pokerbuddy.model.Player;

import java.util.List;

/**
 * Created by kiastu on 05/09/15.
 */
public class PokerListAdapter extends ArrayAdapter<Player> {

    private List<Player> playerList;
    private Context context;
    public PokerListAdapter(Context context, List<Player> playerList){
        super(context, R.layout.list_player_layout, playerList);
        this.playerList = playerList;
        this.context = context;

    }
    @Override
    public int getCount(){
        return playerList.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_player_layout, parent, false);
        LinearLayout playerRow = (LinearLayout) rowView.findViewById(R.id.player_row);
        TextView nameText = (TextView) rowView.findViewById(R.id.text_player_name);
        TextView chipsText = (TextView) rowView.findViewById(R.id.text_player_chips);
        TextView currentBetText = (TextView) rowView.findViewById(R.id.text_player_current_bet);
        TextView foldedText = (TextView) rowView.findViewById(R.id.text_player_is_folded);

        Player currentPlayer = playerList.get(position);
        nameText.setText(currentPlayer.getName());
        chipsText.setText(currentPlayer.getChips()+"");
        currentBetText.setText(currentPlayer.getCurrentBet()+"");
        if(currentPlayer.isFolded()){
            foldedText.setText("Yes");
        }else{
            foldedText.setText("No");
        }
        return rowView;
    }
    public void setSelected(int index) {
    //TODO: Change color in list for selected index.
    }

}
