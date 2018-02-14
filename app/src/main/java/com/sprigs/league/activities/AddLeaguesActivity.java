package com.sprigs.league.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sprigs.league.R;
import com.sprigs.league.adapters.LeagueAdapter;
import com.sprigs.league.helpers.DatabaseHelper;
import com.sprigs.league.models.League;

import java.util.ArrayList;
import java.util.List;

public class AddLeaguesActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    public RecyclerView leaguesRecyclerView;
    public LeagueAdapter leagueAdapter;
    public List<League> leagueList;
    League league;
    String leagueName = "";
    FloatingActionButton addLeagueFab;
    public TextView noLeagues;
    public ImageView logo;
    DatabaseHelper databaseHelper;
    String logoPath;
    int leagueId;
    int leagueState;
    int checkForPic = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        league = new League();
        databaseHelper = new DatabaseHelper(AddLeaguesActivity.this);

        leaguesRecyclerView = findViewById(R.id.recycler_view);
        noLeagues = findViewById(R.id.noLeagues);

        leaguesRecyclerView.setVisibility(View.VISIBLE);
        noLeagues.setVisibility(View.GONE);

        addLeagueFab = findViewById(R.id.addLeagueFab);
        addLeagueFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        leaguesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    addLeagueFab.hide();
                else if (dy < 0)
                    addLeagueFab.show();
            }
        });

        leagueList = databaseHelper.getAllLeagues();
        leagueAdapter = new LeagueAdapter(this, leagueList, new LeagueAdapter.LeagueAdapterListener() {
            @Override
            public void onCardSelected(int position, ImageView thumbnail) {
                if (checkIfLeagueFinished(position)) {
                Intent intent = new Intent(AddLeaguesActivity.this, AddTeamsActivity.class);
                    intent.putExtra("leaguePosition", position);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AddLeaguesActivity.this, LeagueResultsActivity.class);
                    intent.putExtra("leaguePosition", position);
                    startActivity(intent);
                }
            }
        });


        setGridLayout();
    }


    private void setGridLayout() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        leaguesRecyclerView.setLayoutManager(mLayoutManager);
        leaguesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        leaguesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        leaguesRecyclerView.setAdapter(leagueAdapter);
    }

    private void showAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);

        final EditText leagueNameInput = alertLayout.findViewById(R.id.leagueName);
        final Button addLogo = alertLayout.findViewById(R.id.addLogo);

        leagueNameInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        logo = alertLayout.findViewById(R.id.logo);
        Glide.with(AddLeaguesActivity.this).load(R.drawable.default_picture).centerCrop().fitCenter().into(logo);
        AlertDialog.Builder alert = new AlertDialog.Builder(AddLeaguesActivity.this);
        alert.setTitle("Add a new League");
        alert.setView(alertLayout);
        alert.setCancelable(false);

        addLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }

        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String leagueName = leagueNameInput.getText().toString();

                if (leagueName.matches("")) {
                    Toast.makeText(AddLeaguesActivity.this, "You did not enter a League Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if ( checkForPic == 0){
                    Toast.makeText(AddLeaguesActivity.this, "You did not add a Logo", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseHelper.addLeague(new League(leagueName, 0, logoPath, 0));
                leagueList.clear();
                leagueList.addAll(databaseHelper.getAllLeagues());
                leagueAdapter.notifyDataSetChanged();
                leaguesRecyclerView.setVisibility(View.VISIBLE);
                noLeagues.setVisibility(View.GONE);
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            checkForPic = 1;
            if (requestCode == PICK_IMAGE) {
                Uri selectedImageURI = data.getData();
                logoPath = selectedImageURI.toString();
                Glide.with(AddLeaguesActivity.this).load(selectedImageURI).centerCrop().fitCenter().into(logo);
            }
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private boolean checkIfLeagueFinished(int position) {
        try {
            if (databaseHelper.getAllLeagues().get(position).getLeagueState() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return true;
    }
}
