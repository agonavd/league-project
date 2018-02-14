package com.sprigs.league.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sprigs.league.R;
import com.sprigs.league.activities.AddLeaguesActivity;
import com.sprigs.league.activities.LeagueResultsActivity;
import com.sprigs.league.helpers.DatabaseHelper;
import com.sprigs.league.models.League;

import java.util.List;


public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.MyViewHolder> {

    private Context context;
    private List<League> leagueList;
    private LeagueAdapterListener clickListener;
    private DatabaseHelper databaseHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView leagueName, numberOfTeams;
        public ImageView thumbnail, overflow;
        public CardView cardView;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            this.leagueName = view.findViewById(R.id.title);
            this.numberOfTeams = view.findViewById(R.id.count);
            this.thumbnail = view.findViewById(R.id.thumbnail);
            this.overflow = view.findViewById(R.id.overflow);
            this.cardView = view.findViewById(R.id.card_view);
            this.progressBar = view.findViewById(R.id.progressBar);
        }
    }


    public LeagueAdapter(Context context, List<League> leagueList, LeagueAdapterListener clickListener) {
        this.context = context;
        this.leagueList = leagueList;
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.league_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        League league = leagueList.get(position);
        databaseHelper = new DatabaseHelper(context);
        final int leagueId = leagueList.get(position).getId();
        holder.leagueName.setText(league.getLeagueName());
        holder.numberOfTeams.setText(databaseHelper.getNumberOfTeams(leagueId) + " teams");

        final ProgressBar progressBar = holder.progressBar;

        if (checkIfLeagueFinished(position)){
            progressBar.setProgress(100);
        } else {
            progressBar.setProgress(0);
        }

        holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Uri logoPath = Uri.parse(league.getLeagueLogo());
        Glide.with(context)
                .load(logoPath)
                .into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.overflow);
                popup.inflate(R.menu.league_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_league:
                                League league = leagueList.get(position);
                                leagueList.remove(league);
                                databaseHelper.deleteLeague(league);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Deleted the League", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.restart_league:
                                databaseHelper.deleteMatches(leagueId);
                                databaseHelper.changeLeagueState(leagueId, 0);
                                decrementProgressBar(progressBar);
                                return true;
                            default:
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onCardSelected(position, holder.thumbnail);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onCardSelected(position, holder.thumbnail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }


    public interface LeagueAdapterListener {

        void onCardSelected(int position, ImageView thumbnail);
    }

    public void decrementProgressBar(final ProgressBar progressBar) {
        final Handler handler = new Handler();
        final int delay = 100;
        handler.post(new Runnable() {
            public void run() {
                int newProgress = progressBar.getProgress() - (progressBar.getMax() / 50);
                if (newProgress > 0) {
                    progressBar.setProgress(newProgress);
                    handler.postDelayed(this, delay);
                } else {
                    Toast.makeText(context, "Restarted the League", Toast.LENGTH_SHORT).show();
                    progressBar.setProgress(0);
                }
            }
        });
    }

    private boolean checkIfLeagueFinished(int leaguePosition) {
        try {
            if (databaseHelper.getAllLeagues().get(leaguePosition).getLeagueState() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }
}
