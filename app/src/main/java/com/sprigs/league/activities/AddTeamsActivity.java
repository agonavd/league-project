package com.sprigs.league.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sprigs.league.ApiEndpointInterface;
import com.sprigs.league.R;
import com.sprigs.league.helpers.DatabaseHelper;
import com.sprigs.league.helpers.RecyclerItemTouchHelper;
import com.sprigs.league.adapters.AddTeamAdapter;
import com.sprigs.league.models.League;
import com.sprigs.league.models.Team;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTeamsActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    int leaguePosition;
    int leagueId;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.teamList)
    RecyclerView teamsRecyclerView;

    @BindView(R.id.addTeamFab)
    FloatingActionMenu floatingActionMenu;

    FloatingActionButton addTeamButton, scheduleMatchesButton;
    DatabaseHelper databaseHelper;
    AddTeamAdapter addTeamAdapter;
    List<Team> teamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        leaguePosition = intent.getIntExtra("leaguePosition", 0);
        databaseHelper = new DatabaseHelper(AddTeamsActivity.this);

        leagueId = databaseHelper.getAllLeagues().get(leaguePosition).getId();
        setTitle(databaseHelper.getAllLeagues().get(leaguePosition).getLeagueName());

        teamList = databaseHelper.getAllTeams(leagueId);
        teamsRecyclerView.setHasFixedSize(true);

        addTeamAdapter = new AddTeamAdapter(teamList);
        setLinearLayout();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(teamsRecyclerView);
    }

    @OnClick(R.id.addTeam)
    public void fabButtonClicked() {
        showAddDialog();
    }

    @OnClick(R.id.scheduleMatches)
    public void addTeamButtonClicked() {
        if (databaseHelper.getAllTeams(leagueId).size() <= 1) {
            Toast.makeText(AddTeamsActivity.this, "Enter More than 1 Team", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(AddTeamsActivity.this, ScheduleMatchesActivity.class);
            intent.putExtra("leaguePosition", leaguePosition);
            startActivity(intent);
        }
    }


    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        final String name = teamList.get(viewHolder.getAdapterPosition()).getTeamName();

        final Team deletedTeam = teamList.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        databaseHelper.deleteTeam(teamList.get(viewHolder.getAdapterPosition()));
        addTeamAdapter.removeItem(viewHolder.getAdapterPosition());

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, name + " removed ", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTeamAdapter.restoreItem(deletedTeam, deletedIndex);
                databaseHelper.addTeam(deletedTeam);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        teamsRecyclerView.setLayoutManager(linearLayoutManager);
        teamsRecyclerView.setAdapter(addTeamAdapter);
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTeamsActivity.this);
        builder.setTitle("Add Team");

        final EditText input = new EditText(AddTeamsActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String teamName = input.getText().toString();

                if (teamName.matches("")) {
                    Toast.makeText(AddTeamsActivity.this, "You did not enter a Team Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (databaseHelper.isTeamInList(teamName, leagueId)) {
                    Toast.makeText(AddTeamsActivity.this, "This team is in List", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseHelper.addTeam(new Team(teamName, leagueId));
                teamList.clear();
                teamList.addAll(databaseHelper.getAllTeams(leagueId));
                addTeamAdapter.notifyDataSetChanged();
                input.setText("");

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
}

