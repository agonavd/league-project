package com.sprigs.league.models;


import com.google.gson.annotations.SerializedName;

public class League {
    private int id;
    private int leagueState;
    @SerializedName("caption")
    private String leagueName;
    private int numberOfTeams;
    private String leagueLogo;


    public League(String leagueName, int numberOfTeams, String leagueLogo, int leagueState) {
        this.leagueName = leagueName;
        this.numberOfTeams = numberOfTeams;
        this.leagueLogo = leagueLogo;
        this.leagueState = leagueState;
    }

    public League() {
    }

    public int getLeagueState() {
        return leagueState;
    }

    public void setLeagueState(int leagueState) {
        this.leagueState = leagueState;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public String getLeagueLogo() {
        return leagueLogo;
    }

    public void setLeagueLogo(String leagueLogo) {
        this.leagueLogo = leagueLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
