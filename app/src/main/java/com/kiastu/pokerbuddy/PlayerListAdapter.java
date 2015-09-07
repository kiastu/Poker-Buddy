package com.kiastu.pokerbuddy;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiastu.pokerbuddy.model.Player;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private List<Player> playerList;
    private Context context;
    private int currentPlayerIndex;

    public PlayerListAdapter(Context context, List<Player> playerList) {
        this.playerList = playerList;
        this.context = context;
        this.currentPlayerIndex = 0;
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder playerViewHolder, int position) {
        Player currentPlayer = playerList.get(position);
        playerViewHolder.nameText.setText(currentPlayer.getName());
        playerViewHolder.chipsText.setText(currentPlayer.getChips() + "");
        playerViewHolder.currentBetText.setText(currentPlayer.getCurrentBet() + "");
        if (currentPlayer.isFolded()) {
            playerViewHolder.foldedText.setText("Yes");
        } else {
            playerViewHolder.foldedText.setText("No");
        }
        if(position == currentPlayerIndex){
            // this is the player who's turn it is currently. Change the color.
            playerViewHolder.rowView.setBackgroundColor(context.getResources().getColor(R.color.player_selected));
        }else{
            playerViewHolder.rowView.setBackgroundColor(context.getResources().getColor(R.color.player_list_background));
        }
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //this class inflates the viewholder for everyone.
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_player_layout, viewGroup, false);

        return new PlayerViewHolder(itemView);
    }

    public void setSelected(int index) {
        if(index+1 >=playerList.size()){
            currentPlayerIndex = 0;
        }else {
            currentPlayerIndex = index + 1;
        }
        notifyDataSetChanged();
    }
    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout rowView;
        protected TextView nameText;
        protected TextView chipsText;
        protected TextView currentBetText;
        protected TextView foldedText;

        public PlayerViewHolder(View v) {
            super(v);
            rowView = (LinearLayout) v.findViewById(R.id.player_row);
            nameText = (TextView) v.findViewById(R.id.text_player_name);
            chipsText = (TextView) v.findViewById(R.id.text_player_chips);
            currentBetText = (TextView) v.findViewById(R.id.text_player_current_bet);
            foldedText = (TextView) v.findViewById(R.id.text_player_is_folded);
        }
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
