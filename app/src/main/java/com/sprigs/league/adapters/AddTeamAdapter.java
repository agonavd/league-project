package com.sprigs.league.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sprigs.league.R;
import com.sprigs.league.models.Team;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTeamAdapter extends RecyclerView.Adapter<AddTeamAdapter.MyViewHolder> {

    private List<Team> teams;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.team)
        TextView textViewName;

        @BindView(R.id.view_background)
        public RelativeLayout viewBackground;

        @BindView(R.id.view_foreground)
        public RelativeLayout viewForeground;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public AddTeamAdapter(List<Team> data) {
        this.teams = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addteams_cards, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        holder.textViewName.setText(teams.get(listPosition).getTeamName());

    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public void removeItem(int position) {
        teams.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Team item, int position) {
        teams.add(position, item);
        notifyItemInserted(position);
    }
}

