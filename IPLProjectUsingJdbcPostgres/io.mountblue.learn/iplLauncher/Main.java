package io.mountblue.learn.iplLauncher;

import io.mountblue.learn.pojo.Delivery;
import io.mountblue.learn.pojo.Match;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String url="jdbc:postgresql://localhost:5432/IPLProject";
    private static final String name="postgres";
    private static final String password="yourPassword";

    public static void main(String[] args) {
        List<Match> matches = getMatches();
        List<Delivery> deliveries = getDeliveries();

        printMatchesPerYear(matches);
        printMatchesWonPerTeam(matches);
        printExtraRunsIn2016(matches, deliveries);
        printSixesPerTeam2016(matches, deliveries);
        printTopEconomicBowlers2015(matches, deliveries);
    }

    private static void printSixesPerTeam2016(List<Match> matches, List<Delivery> deliveries) {
        int currentSixes;
        Map<String, Integer> theSixes2016PerTeam = new HashMap<>();

        List<Match> filtered2016MatchesList = getFilteredMatches(matches, 2016);
        int matchIdLowerLimit = filtered2016MatchesList.get(0).getMatchId();
        int matchIdUpperLimit = filtered2016MatchesList.get(filtered2016MatchesList.size() - 1).getMatchId();

        List<Delivery> filtered2016DeliveriesList = getDeliveries(deliveries, matchIdLowerLimit, matchIdUpperLimit);
        for (Delivery eachDelivery : filtered2016DeliveriesList) {
            if(eachDelivery.getTotalRuns() == 6) {
                if (theSixes2016PerTeam.containsKey(eachDelivery.getBattingTeam())) {
                    currentSixes = theSixes2016PerTeam.get(eachDelivery.getBattingTeam());
                    theSixes2016PerTeam.put(eachDelivery.getBattingTeam(), currentSixes + 1);
                } else
                    theSixes2016PerTeam.put(eachDelivery.getBattingTeam(), 1);
            }
        }
        System.out.println("\nSixes per team in 2016\n----------------");
        for (Map.Entry<String, Integer> entry : theSixes2016PerTeam.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void printTopEconomicBowlers2015(List<Match> matches, List<Delivery> deliveries) {
        Map<String, Float> topEconomicBowlers2015 = new HashMap<>();
        Map<String, Integer> runsConcededPerBowler = new HashMap<>();
        Map<String, Integer> ballsBowledPerBowler = new HashMap<>();

        List<Match> filtered2015MatchesList = getFilteredMatches(matches, 2015);
        int matchIdLowerLimit = filtered2015MatchesList.get(0).getMatchId();
        int matchIdUpperLimit = filtered2015MatchesList.get(filtered2015MatchesList.size() - 1).getMatchId();

        List<Delivery> filtered2015DeliveriesList = getDeliveries(deliveries, matchIdLowerLimit, matchIdUpperLimit);

        for (Delivery eachDelivery : filtered2015DeliveriesList) {
            if (eachDelivery.getWideRuns() > 0 || eachDelivery.getNoBallRuns() > 0) {
                if (runsConcededPerBowler.containsKey(eachDelivery.getBowlerName())) {
                    int currentConcededRuns = runsConcededPerBowler.get(eachDelivery.getBowlerName());
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(), currentConcededRuns
                            + eachDelivery.getWideRuns() + eachDelivery.getNoBallRuns());
                } else {
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(), eachDelivery.getNoBallRuns()
                            + eachDelivery.getWideRuns());
                }
            } else {
                int totalRunsPerBall = eachDelivery.getBatsmanRuns();
                if (runsConcededPerBowler.containsKey(eachDelivery.getBowlerName())) {
                    int currentConcededRuns = runsConcededPerBowler.get(eachDelivery.getBowlerName());
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(), currentConcededRuns + totalRunsPerBall);
                } else {
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(), totalRunsPerBall);
                }
                if (ballsBowledPerBowler.containsKey(eachDelivery.getBowlerName())) {
                    int currentBallsBowled = ballsBowledPerBowler.get(eachDelivery.getBowlerName());
                    ballsBowledPerBowler.put(eachDelivery.getBowlerName(), currentBallsBowled + 1);
                } else {
                    ballsBowledPerBowler.put(eachDelivery.getBowlerName(), 1);
                }
            }

        }

        for (Map.Entry<String, Integer> entry : runsConcededPerBowler.entrySet()) {
            topEconomicBowlers2015.put(entry.getKey(), (entry.getValue() * 6 / (float) ballsBowledPerBowler.get(entry.getKey())));
        }
        System.out.println("\nTop Economic bowlers in IPL 2015\n----------------");
        topEconomicBowlers2015.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));
    }

    private static void printExtraRunsIn2016(List<Match> matches, List<Delivery> deliveries) {
        int currentExtraRuns;
        Map<String, Integer> theExtrasCount2016PerTeam = new HashMap<>();

        List<Match> filtered2016MatchesList = getFilteredMatches(matches, 2016);
        int matchIdLowerLimit = filtered2016MatchesList.get(0).getMatchId();
        int matchIdUpperLimit = filtered2016MatchesList.get(filtered2016MatchesList.size() - 1).getMatchId();

        List<Delivery> filtered2016DeliveriesList = getDeliveries(deliveries, matchIdLowerLimit, matchIdUpperLimit);
        for (Delivery eachDelivery : filtered2016DeliveriesList) {
            if (theExtrasCount2016PerTeam.containsKey(eachDelivery.getBowlingTeam())) {
                currentExtraRuns = theExtrasCount2016PerTeam.get(eachDelivery.getBowlingTeam());
                theExtrasCount2016PerTeam.put(eachDelivery.getBowlingTeam(), currentExtraRuns
                        + eachDelivery.getExtraRuns());
            } else {
                theExtrasCount2016PerTeam.put(eachDelivery.getBowlingTeam(), eachDelivery.getExtraRuns());
            }
        }
        System.out.println("\nExtra runs per team in 2016\n----------------");
        for (Map.Entry<String, Integer> entry : theExtrasCount2016PerTeam.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static List<Match> getFilteredMatches(List<Match> matches, int year) {
        Stream<Match> filteredMatches = matches.stream().filter(eachMatch -> eachMatch.getSeason() == year);
        return filteredMatches.collect(Collectors.toList());
    }

    private static List<Delivery> getDeliveries(List<Delivery> deliveries, int matchIdLowerLimit, int matchIdUpperLimit) {
        Stream<Delivery> filteredDeliveries = deliveries.stream().filter(eachDelivery -> eachDelivery.getMatchId() >= matchIdLowerLimit && eachDelivery.getMatchId() <= matchIdUpperLimit);
        return filteredDeliveries.collect(Collectors.toList());
    }

    private static void printMatchesWonPerTeam(List<Match> matches) {
        HashMap<String, Integer> numberOfMatchesWonPerTeam = new HashMap<>();
        int tieCount = 0;
        for (Match eachMatch : matches) {
            if (numberOfMatchesWonPerTeam.containsKey(eachMatch.getWinner())) {
                int correspondingTeamWinValue = numberOfMatchesWonPerTeam.get(eachMatch.getWinner());
                numberOfMatchesWonPerTeam.put(eachMatch.getWinner(), correspondingTeamWinValue + 1);
            } else {
                if (eachMatch.getWinner()==null) {
                    numberOfMatchesWonPerTeam.put("Tied", tieCount + 1);
                    tieCount++;
                    continue;
                }
                numberOfMatchesWonPerTeam.put(eachMatch.getWinner(), 1);
            }
        }
        System.out.println("\nMatches won per team\n----------------");
        for (Map.Entry<String, Integer> entry : numberOfMatchesWonPerTeam.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void printMatchesPerYear(List<Match> matches) {
        TreeMap<Integer, Integer> numberOfMatchesPerYear = new TreeMap<>();
        for (Match eachMatch : matches) {
            if (numberOfMatchesPerYear.containsKey(eachMatch.getSeason())) {
                int correspondingSeasonValue = numberOfMatchesPerYear.get(eachMatch.getSeason());
                numberOfMatchesPerYear.put(eachMatch.getSeason(), correspondingSeasonValue + 1);
            } else {
                numberOfMatchesPerYear.put(eachMatch.getSeason(), 1);
            }
        }
        System.out.println("\nMatches per Year\n----------------");
        for (Map.Entry<Integer, Integer> entry : numberOfMatchesPerYear.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static List<Match> getMatches() {
        List<Match> allMatchesData = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;
        try ( Connection con = DriverManager.getConnection(url,name,password)){
            st = con.prepareStatement("select * from Public.\"Matches\"");
            rs = st.executeQuery();
            while (rs.next()) {
                int matchId = rs.getInt("id");
                int season = rs.getInt("season");
                String city = rs.getString("city");
                String team1 = rs.getString("team1");
                String team2 = rs.getString("team2");
                String result = rs.getString("result");
                String winner = rs.getString("winner");

                Match eachMatch = new Match(matchId,season,city,team1,team2,result,winner);
                allMatchesData.add(eachMatch);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allMatchesData;
    }

    private static List<Delivery> getDeliveries() {
        List<Delivery> allDeliveriesData = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;
        try ( Connection con = DriverManager.getConnection(url,name,password)){
            st = con.prepareStatement("select * from Public.\"Deliveries\"");
            rs = st.executeQuery();
            while (rs.next()) {
                int matchId = rs.getInt("match_id");
                int innings = rs.getInt("inning");
                int over = rs.getInt("over");
                int ball = rs.getInt("ball");
                String battingTeam=rs.getString("batting_team");
                String bowlingTeam=rs.getString("bowling_team");
                String bowlerName=rs.getString("bowler");
                int wideRuns = rs.getInt("wide_runs");
                int byeRuns = rs.getInt("bye_runs");
                int legByeRuns = rs.getInt("legbye_runs");
                int noBallRuns = rs.getInt("noball_runs");
                int penaltyRuns = rs.getInt("penalty_runs");
                int batsmanRuns = rs.getInt("batsman_runs");
                int extraRuns = rs.getInt("extra_runs");
                int totalRuns = rs.getInt("total_runs");

                Delivery eachDelivery=new Delivery(matchId, innings, over, ball,
                                    battingTeam, bowlingTeam, bowlerName, wideRuns,
                                byeRuns, legByeRuns, noBallRuns, penaltyRuns, batsmanRuns,
                                    extraRuns, totalRuns);
                allDeliveriesData.add(eachDelivery);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allDeliveriesData;
    }
}
