package com.sprigs.league.models;


import com.google.gson.annotations.SerializedName;

public class Team {
    private int id;
    private int leagueId;
    @SerializedName("name")
    private String teamName;

    public Team(String teamName, int leagueId) {
        this.teamName = teamName;
        this.leagueId = leagueId;
    }

    public Team() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return getTeamName();
    }
}
