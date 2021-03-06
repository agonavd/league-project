package com.sprigs.league.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sprigs.league.R;
import com.sprigs.league.adapters.SchedulesAdapter;
import com.sprigs.league.helpers.DatabaseHelper;
import com.sprigs.league.models.League;
import com.sprigs.league.models.Match;
import com.sprigs.league.models.Team;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleMatchesActivity extends AppCompatActivity {

    @BindView(R.id.matchesList)
    RecyclerView matchesList;

    List<Match> matches;
    SchedulesAdapter resultsAdapter;
    DatabaseHelper databaseHelper;
    int leaguePosition;
    int leagueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_matches);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        leaguePosition = intent.getIntExtra("leaguePosition", 0);

        databaseHelper = new DatabaseHelper(ScheduleMatchesActivity.this);
        leagueId = databaseHelper.getAllLeagues().get(leaguePosition).getId();

        scheduleMatches();

        matches = databaseHelper.getAllMatches(leagueId);
        matchesList.setHasFixedSize(true);
        resultsAdapter = new SchedulesAdapter(matches);

        setLinearLayout();
    }

    @OnClick(R.id.startLeagueButton)
    public void starLeagueButtonClicked() {
        Intent intent = new Intent(ScheduleMatchesActivity.this, LeagueResultsActivity.class);
        intent.putExtra("leaguePosition", leaguePosition);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        databaseHelper.deleteMatches(leagueId);
        super.onBackPressed();
    }

    private void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        matchesList.setLayoutManager(linearLayoutManager);
        matchesList.setAdapter(resultsAdapter);
    }

    private void scheduleMatches() {
        List<Team> teams = databaseHelper.getAllTeams(leagueId);
        for (int homeIndex = 0; homeIndex < teams.size(); homeIndex++) {
            for (int awayIndex = 0; awayIndex < teams.size(); awayIndex++) {
                if (homeIndex != awayIndex) {
                    databaseHelper.addMatch(new Match(teams.get(homeIndex).getTeamName(),
                            teams.get(awayIndex).getTeamName(), leagueId));
                }
            }
        }
    }
}
