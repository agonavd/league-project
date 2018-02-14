package com.sprigs.league.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sprigs.league.models.League;
import com.sprigs.league.models.Match;
import com.sprigs.league.models.Team;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "league";

    private static final String TABLE_TEAMS = "teams";
    private static final String TABLE_MATCHES = "matches";
    private static final String TABLE_LEAGUES = "leagues";

    private static final String KEY_TEAM_ID = "id";
    private static final String KEY_TEAM_LEAGUE_ID = "league_id";
    private static final String KEY_TEAM_NAME = "name";

    private static final String KEY_MATCH_ID = "id";
    private static final String KEY_MATCH_LEAGUE_ID = "league_id";
    private static final String KEY_MATCH_HOME_TEAM = "home_team";
    private static final String KEY_MATCH_AWAY_TEAM = "away_team";
    private static final String KEY_MATCH_HOME_SCORE = "home_score";
    private static final String KEY_MATCH_AWAY_SCORE = "away_score";

    private static final String KEY_LEAGUE_ID = "id";
    private static final String KEY_LEAGUE_STATE = "league_state";
    private static final String KEY_LEAGUE_NAME = "name";
    private static final String KEY_LEAGUE_NUMBER_OF_TEAMS = "league_team_number";
    private static final String KEY_LEAGUE_LOGO = "league_logo";

    private static final String CREATE_TABLE_TEAM = "CREATE TABLE "
            + TABLE_TEAMS + "(" + KEY_TEAM_ID + " INTEGER PRIMARY KEY," + KEY_TEAM_LEAGUE_ID + " INTEGER," + KEY_TEAM_NAME
            + " TEXT" + ")";

    private static final String CREATE_TABLE_MATCHES = "CREATE TABLE "
            + TABLE_MATCHES + "(" + KEY_MATCH_ID + " INTEGER PRIMARY KEY," + KEY_MATCH_LEAGUE_ID + " INTEGER," + KEY_MATCH_HOME_TEAM
            + " TEXT," + KEY_MATCH_AWAY_TEAM + " TEXT," + KEY_MATCH_HOME_SCORE
            + " INTEGER," + KEY_MATCH_AWAY_SCORE + " INTEGER" + ")";

    private static final String CREATE_TABLE_LEAGUE = "CREATE TABLE "
            + TABLE_LEAGUES + "(" + KEY_LEAGUE_ID + " INTEGER PRIMARY KEY," + KEY_LEAGUE_STATE + " INTEGER," + KEY_LEAGUE_NAME
            + " TEXT," + KEY_LEAGUE_NUMBER_OF_TEAMS + " INTEGER," + KEY_LEAGUE_LOGO
            + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TEAM);
        sqLiteDatabase.execSQL(CREATE_TABLE_MATCHES);
        sqLiteDatabase.execSQL(CREATE_TABLE_LEAGUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAGUES);

        onCreate(sqLiteDatabase);
    }

    public void addTeam(Team team) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TEAM_LEAGUE_ID, team.getLeagueId());
        contentValues.put(KEY_TEAM_NAME, team.getTeamName());

        sqLiteDatabase.insert(TABLE_TEAMS, null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteTeam(Team team) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_TEAMS, KEY_TEAM_ID + " = ?",
                new String[]{String.valueOf(team.getId())});

        sqLiteDatabase.close();
    }

    public boolean isTeamInList(String teamName) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_TEAMS + " WHERE " + KEY_TEAM_NAME + " =?";

        Cursor cursor = db.rawQuery(selectString, new String[]{teamName});

        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;

            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
        }

        cursor.close();
        db.close();
        return hasObject;
    }

    public int getNumberOfTeams(int leagueId) {
        String countQuery = "SELECT  * FROM " + TABLE_TEAMS + " WHERE " + KEY_TEAM_LEAGUE_ID + " = ?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, new String[]{String.valueOf(leagueId)});

        return cursor.getCount();
    }

    public List<Team> getAllTeams(int leagueId) {
        List<Team> teams = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_TEAMS + " WHERE " + KEY_TEAM_LEAGUE_ID + " = ?";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, new String[]{String.valueOf(leagueId)});

        if (cursor.moveToFirst()) {
            do {
                Team team = new Team();
                team.setId(Integer.parseInt(cursor.getString(0)));
                team.setLeagueId(Integer.parseInt(cursor.getString(1)));
                team.setTeamName(cursor.getString(2));

                teams.add(team);
            } while (cursor.moveToNext());
        }

        return teams;
    }

    public void addLeague(League league) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LEAGUE_STATE, league.getLeagueState());
        contentValues.put(KEY_LEAGUE_NAME, league.getLeagueName());
        contentValues.put(KEY_LEAGUE_NUMBER_OF_TEAMS, league.getNumberOfTeams());
        contentValues.put(KEY_LEAGUE_LOGO, league.getLeagueLogo());

        sqLiteDatabase.insert(TABLE_LEAGUES, null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteLeague(League league) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_LEAGUES, KEY_LEAGUE_ID + " = ?",
                new String[]{String.valueOf(league.getId())});

        sqLiteDatabase.delete(TABLE_MATCHES, KEY_MATCH_LEAGUE_ID + " = ?",
                new String[]{String.valueOf(league.getId())});

        sqLiteDatabase.delete(TABLE_TEAMS, KEY_TEAM_LEAGUE_ID + " = ?",
                new String[]{String.valueOf(league.getId())});

        sqLiteDatabase.close();
    }


    public List<League> getAllLeagues() {
        List<League> leagues = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_LEAGUES;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                League league = new League();
                league.setId(Integer.parseInt(cursor.getString(0)));
                league.setLeagueState(Integer.parseInt(cursor.getString(1)));
                league.setLeagueName(cursor.getString(2));
                league.setNumberOfTeams(Integer.parseInt(cursor.getString(3)));
                league.setLeagueLogo(cursor.getString(4));

                leagues.add(league);
            } while (cursor.moveToNext());
        }

        return leagues;
    }

    public int changeLeagueState(int leagueId, int leagueState) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LEAGUE_STATE, leagueState);

        return sqLiteDatabase.update(TABLE_LEAGUES, contentValues, KEY_LEAGUE_ID + " = ?",
                new String[]{String.valueOf(leagueId)});
    }

    public void addMatch(Match match) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MATCH_LEAGUE_ID, match.getLeagueId());
        contentValues.put(KEY_MATCH_HOME_TEAM, match.getHomeTeam());
        contentValues.put(KEY_MATCH_AWAY_TEAM, match.getAwayTeam());
        contentValues.put(KEY_MATCH_HOME_SCORE, 0);
        contentValues.put(KEY_MATCH_AWAY_SCORE, 0);

        sqLiteDatabase.insert(TABLE_MATCHES, null, contentValues);
        sqLiteDatabase.close();
    }

    public List<Match> getAllMatches(int leagueId) {
        List<Match> matches = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_MATCHES + " WHERE " + KEY_MATCH_LEAGUE_ID + " = ?";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, new String[]{String.valueOf(leagueId)});

        if (cursor.moveToFirst()) {
            do {
                Match match = new Match();
                match.setId(Integer.parseInt(cursor.getString(0)));
                match.setLeagueId(Integer.parseInt(cursor.getString(1)));
                match.setHomeTeam(cursor.getString(2));
                match.setAwayTeam(cursor.getString(3));
                match.setHomeScore(Integer.parseInt(cursor.getString(4)));
                match.setAwayScore(Integer.parseInt(cursor.getString(5)));

                matches.add(match);
            } while (cursor.moveToNext());
        }

        return matches;
    }

    public int playMatch(Match match) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MATCH_HOME_SCORE, (int) (Math.random() * 5) + 1);
        contentValues.put(KEY_MATCH_AWAY_SCORE, (int) (Math.random() * 5) + 1);

        return sqLiteDatabase.update(TABLE_MATCHES, contentValues, KEY_MATCH_ID + " = ?",
                new String[]{String.valueOf(match.getId())});
    }

    public void deleteMatches(int leagueId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.delete(TABLE_MATCHES, KEY_MATCH_LEAGUE_ID + " = ?",
                new String[]{String.valueOf(leagueId)});

        sqLiteDatabase.close();
    }

}
