package io.mountblue.learn.pojo;


public class Match {
    private int matchId;
    private int season;
    private String city;
    private String team1;
    private String team2;
    private String result;
    private String winner;

    public Match(int matchId, int season, String city, String team1, String team2, String result, String winner) {
        this.matchId = matchId;
        this.season = season;
        this.city = city;
        this.team1 = team1;
        this.team2 = team2;
        this.result = result;
        this.winner = winner;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

}
