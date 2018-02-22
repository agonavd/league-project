package com.sprigs.league.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sprigs.league.R;
import com.sprigs.league.adapters.ResultsAdapter;
import com.sprigs.league.helpers.DatabaseHelper;
import com.sprigs.league.models.League;
import com.sprigs.league.models.Match;

import java.io.Serializable;
import java.util.List;

public class LeagueResultsActivity extends AppCompatActivity implements Serializable {

    RecyclerView resultsList;
    List<Match> matches;
    League league;
    Match match;
    Handler handler;
    ResultsAdapter resultsAdapter;
    DatabaseHelper databaseHelper;
    int i = 0;
    int leaguePosition;
    int leagueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_results);
        Intent intent = getIntent();
        leaguePosition = intent.getIntExtra("leaguePosition", 0);
        match = new Match();
        league = new League();
        databaseHelper = new DatabaseHelper(LeagueResultsActivity.this);
        leagueId = databaseHelper.getAllLeagues().get(leaguePosition).getId();
        matches = databaseHelper.getAllMatches(leagueId);

        resultsList = findViewById(R.id.resultsList);
        resultsList.setHasFixedSize(true);
        resultsAdapter = new ResultsAdapter(matches);

        setLinearLayout();

        if (checkIfLeagueFinished()) {
            handler = new Handler();
            final int delay = 2000;
            handler.post(new Runnable() {
                public void run() {
                    databaseHelper.playMatch(matches.get(i));
                    matches.clear();
                    matches.addAll(databaseHelper.getAllMatches(leagueId));
                    resultsAdapter.notifyDataSetChanged();
                    i++;
                    if (i < matches.size()) {
                        handler.postDelayed(this, delay);
                    } else {
                        databaseHelper.changeLeagueState(leagueId, 1);
                        Toast.makeText(LeagueResultsActivity.this, "League has Ended", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.results_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newLeague:
                Intent intent = new Intent(LeagueResultsActivity.this, AddLeaguesActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultsList.setLayoutManager(linearLayoutManager);
        resultsList.setAdapter(resultsAdapter);
    }

    private boolean checkIfLeagueFinished() {
        if (databaseHelper.getAllLeagues().get(leaguePosition).getLeagueState() != 0) {
            Toast.makeText(this, "League has been played", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
}
