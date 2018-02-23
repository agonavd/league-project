package com.sprigs.league.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sprigs.league.R;
import com.sprigs.league.models.Match;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.MyViewHolder> {

    private List<Match> matches;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.homeTeam)
        TextView homeTeam;

        @BindView(R.id.awayTeam)
        TextView awayTeam;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public SchedulesAdapter(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedules_row_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TextView homeTeam = holder.homeTeam;
        TextView awayTeam = holder.awayTeam;

        homeTeam.setText(matches.get(position).getHomeTeam());
        awayTeam.setText(matches.get(position).getAwayTeam());

    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

}
