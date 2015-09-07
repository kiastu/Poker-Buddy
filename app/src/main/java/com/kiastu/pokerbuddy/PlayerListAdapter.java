package com.kiastu.pokerbuddy;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kiastu.pokerbuddy.model.Player;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private List<Player> playerList;
    private Context context;

    public PlayerListAdapter(Context context, List<Player> playerList) {
        this.playerList = playerList;
        this.context = context;

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
    //TODO: Change color in list for selected index.
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        protected TextView nameText;
        protected TextView chipsText;
        protected TextView currentBetText;
        protected TextView foldedText;

        public PlayerViewHolder(View v){
            super(v);
            nameText = (TextView) v.findViewById(R.id.text_player_name);
            chipsText = (TextView) v.findViewById(R.id.text_player_chips);
            currentBetText = (TextView) v.findViewById(R.id.text_player_current_bet);
            foldedText = (TextView) v.findViewById(R.id.text_player_is_folded);
        }
    }

}
