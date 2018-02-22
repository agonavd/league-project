package com.sprigs.league.adapters;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sprigs.league.R;
import com.sprigs.league.models.Match;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.parseColor;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.MyViewHolder> {

    private final List<Match> matches;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar homeProgressBar;
        private ProgressBar awayProgressBar;
        private TextView homeTeam, awayTeam, homeTeamScore, awayTeamScore;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.homeTeam = itemView.findViewById(R.id.homeTeam);
            this.awayTeam = itemView.findViewById(R.id.awayTeam);
            this.homeTeamScore = itemView.findViewById(R.id.homeTeamScore);
            this.awayTeamScore = itemView.findViewById(R.id.awayTeamScore);
            this.homeProgressBar = itemView.findViewById(R.id.homeProgressBar);
            this.awayProgressBar = itemView.findViewById(R.id.awayProgressBar);
        }
    }

    public ResultsAdapter(List<Match> matches) {
        this.matches = matches;

    }

    @Override
    public ResultsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.results_row_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ResultsAdapter.MyViewHolder holder, int position) {
        TextView homeTeam = holder.homeTeam;
        TextView awayTeam = holder.awayTeam;
        TextView homeTeamScore = holder.homeTeamScore;
        TextView awayTeamScore = holder.awayTeamScore;
        ProgressBar homeProgressBar = holder.homeProgressBar;
        ProgressBar awayProgressBar = holder.awayProgressBar;
        homeTeam.setText(matches.get(position).getHomeTeam());
        awayTeam.setText(matches.get(position).getAwayTeam());


        homeTeamScore.setText(matches.get(position).getHomeScore() + "");
        awayTeamScore.setText(matches.get(position).getAwayScore() + "");

        if (!homeTeamScore.getText().equals("0")) {
            homeProgressBar.setVisibility(View.GONE);
            awayProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

}
