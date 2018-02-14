package com.sprigs.league.models;


import java.io.Serializable;

public class Match implements Serializable {

    private int id;
    private int leagueId;
    private String homeTeam;
    private String awayTeam;
    private int homeScore = 0;
    private int awayScore = 0;

    public Match(String homeTeam, String awayTeam, int leagueId) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.leagueId = leagueId;
    }

    public Match() {}


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

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
}
