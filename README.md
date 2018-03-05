# League-project

'League' is a simple app about football leagues in which you can add your own leagues and insert your teams.
Than the app can schedule all possible matches from those teams,
one team plays the other team twice, one as home team the other one as away team. 
At the end it stimulates a football match and it gives random scores.

# Folder Structure

### Activities
[app/Activities](https://github.com/agonavd/league-project/tree/master/app/src/main/java/com/sprigs/league/activities) Here we have all the user interactions screens. For now we have 4: `AddLeaguesActivity`in which the user can add, delete and restart a League,
`AddTeamsActivity` add the Teams, `ScheduleMatchesActivity` all the posible matches from the team you added are shown, and `LeagueResultsActivity` is the screen where the game stimulations happens and matches get the score.

### Adapters
[app/Adapters](https://github.com/agonavd/league-project/tree/master/app/src/main/java/com/sprigs/league/adapters) Here we have all the RecyclerView adapters for the activities

### Helpers
[app/Adapters](https://github.com/agonavd/league-project/tree/master/app/src/main/java/com/sprigs/league/helpers) For now we have 2 helpers, `DatabaseHelper` here we have created the sqlite database with three Tables: Leagues, Matches, Teams. `RecyclerItemTouchHelper` here we made a interface for handeling the swipe gestures.

### Models
[app.Models](https://github.com/agonavd/league-project/tree/master/app/src/main/java/com/sprigs/league/models) Here are the data models for the database, there are three: League, Match, Team.
