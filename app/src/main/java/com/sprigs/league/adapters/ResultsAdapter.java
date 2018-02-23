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

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Color.parseColor;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.MyViewHolder> {

    private final List<Match> matches;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.homeProgressBar)
        ProgressBar homeProgressBar;

        @BindView(R.id.awayProgressBar)
        ProgressBar awayProgressBar;

        @BindView(R.id.homeTeam)
        TextView homeTeam;

        @BindView(R.id.awayTeam)
        TextView awayTeam;

        @BindView(R.id.homeTeamScore)
        TextView homeTeamScore;

        @BindView(R.id.awayTeamScore)
        TextView awayTeamScore;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
        holder.homeTeam.setText(matches.get(position).getHomeTeam());
        holder.awayTeam.setText(matches.get(position).getAwayTeam());

        holder.homeTeamScore.setText(matches.get(position).getHomeScore() + "");
        holder.awayTeamScore.setText(matches.get(position).getAwayScore() + "");

        if (!holder.homeTeamScore.getText().equals("0")) {
            holder.homeProgressBar.setVisibility(View.GONE);
            holder.awayProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

}
