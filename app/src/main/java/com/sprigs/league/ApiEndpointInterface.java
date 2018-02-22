package com.sprigs.league;


import com.sprigs.league.models.League;
import com.sprigs.league.models.Team;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiEndpointInterface {

    @GET("v1/competitions/{id}/teams")
    Call<List<Team>> getTeams(@Path("id") int leagueId);

    @GET("v1/competitions/?season=2017")
    Call<List<League>> getLeagues();
}
