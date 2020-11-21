package io.mountblue.learn.pojo;

public class Delivery {
    private int matchId;
    private int innings;
    private int over;
    private int ball;
    private String  battingTeam;
    private String bowlingTeam;
    private String bowlerName;
    private int wideRuns;
    private int byeRuns;
    private int legByeRuns;
    private int noBallRuns;
    private int penaltyRuns;
    private int batsmanRuns;
    private int extraRuns;
    private int totalRuns;

    public Delivery(int matchId, int innings, int over, int ball,
                    String battingTeam, String bowlingTeam, String bowlerName,
                    int wideRuns, int byeRuns, int legByeRuns,
                    int noBallRuns, int penaltyRuns, int batsmanRuns,
                    int extraRuns, int totalRuns) {
        this.matchId = matchId;
        this.innings = innings;
        this.over = over;
        this.ball = ball;
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.bowlerName = bowlerName;
        this.wideRuns = wideRuns;
        this.byeRuns = byeRuns;
        this.legByeRuns = legByeRuns;
        this.noBallRuns = noBallRuns;
        this.penaltyRuns = penaltyRuns;
        this.batsmanRuns = batsmanRuns;
        this.extraRuns = extraRuns;
        this.totalRuns = totalRuns;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }
    public void setInnings(int innings) {
        this.innings = innings;
    }
    public void setBattingTeam(String battingTeam) {
        this.battingTeam = battingTeam;
    }
    public void setBowlingTeam(String bowlingTeam) {
        this.bowlingTeam = bowlingTeam;
    }
    public void setBowlerName(String bowlerName) {
        this.bowlerName = bowlerName;
    }
    public void setOver(int over) {
        this.over = over;
    }
    public void setBall(int ball) {
        this.ball = ball;
    }
    public void setWideRuns(int wideRuns) {
        this.wideRuns = wideRuns;
    }
    public void setByeRuns(int byeRuns) {
        this.byeRuns = byeRuns;
    }
    public void setLegByeRuns(int legByeRuns) {
        this.legByeRuns = legByeRuns;
    }
    public void setNoBallRuns(int noBallRuns) {
        this.noBallRuns = noBallRuns;
    }
    public void setPenaltyRuns(int penaltyRuns) {
        this.penaltyRuns = penaltyRuns;
    }
    public void setBatsmanRuns(int batsmanRuns) {
        this.batsmanRuns = batsmanRuns;
    }
    public void setExtraRuns(int extraRuns) {
        this.extraRuns = extraRuns;
    }
    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }
    public int getMatchId() {
        return matchId;
    }
    public int getInnings() {
        return innings;
    }
    public String getBattingTeam() {
        return battingTeam;
    }
    public String getBowlingTeam() {
        return bowlingTeam;
    }
    public String getBowlerName() {
        return bowlerName;
    }
    public int getOver() {
        return over;
    }
    public int getBall() {
        return ball;
    }
    public int getWideRuns() {
        return wideRuns;
    }
    public int getByeRuns() {
        return byeRuns;
    }
    public int getLegByeRuns() {
        return legByeRuns;
    }
    public int getNoBallRuns() {
        return noBallRuns;
    }
    public int getPenaltyRuns() {
        return penaltyRuns;
    }
    public int getBatsmanRuns() {
        return batsmanRuns;
    }
    public int getExtraRuns() {
        return extraRuns;
    }
    public int getTotalRuns() {
        return totalRuns;
    }
}
