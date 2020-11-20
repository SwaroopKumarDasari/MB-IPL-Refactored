package IPLLauncher;

import Pojo.Delivery;
import Pojo.Match;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static final int MATCH_ID = 0;
    public static final int SEASON = 1;
    public static final int CITY = 2;
    public static final int TEAM1 = 4;
    public static final int TEAM2 = 5;
    public static final int RESULT = 8;
    public static final int WINNER = 10;

    public static final int DELIVERY_MATCH_ID = 0;
    public static final int DELIVERY_INNINGS = 1;
    public static final int DELIVERY_BATTING_TEAM = 2;
    private static final int DELIVERY_BOWLING_TEAM = 3;
    private static final int DELIVERY_OVER = 4;
    private static final int DELIVERY_BALL = 5;
    private static final int DELIVERY_BOWLER = 8;
    private static final int DELIVERY_WIDE = 10;
    private static final int DELIVERY_BYE = 11;
    private static final int DELIVERY_LEG_BYE = 12;
    private static final int DELIVERY_NO_BALL = 13;
    private static final int DELIVERY_PENALTY = 14;
    private static final int DELIVERY_BATSMEN_RUNS = 15;
    private static final int DELIVERY_EXTRA_RUNS = 16;
    private static final int DELIVERY_TOTAL_RUNS = 17;

    public static void main(String[] args) {
        List<Match> matches=readMatches();
        List<Delivery> deliveries=readDeliveries();

        getMatchesPerYear(matches);
        getMatchesWonPerTeam(matches);
        getExtraRunsIn2016(matches,deliveries);
        getSixesPerTeam2016(matches,deliveries);
        getTopEconomicBowlers2015(matches,deliveries);


    }

    private static void getSixesPerTeam2016(List<Match> matches, List<Delivery> deliveries) {
        int currentSixes;
        Map<String, Integer> theSixes2016PerTeam = new HashMap<>();

        List<Match> filtered2016MatchesList = getMatches(matches, 2016);
        int matchIdLowerLimit=filtered2016MatchesList.get(0).getMatchId();
        int matchIdUpperLimit=filtered2016MatchesList.get(filtered2016MatchesList.size()-1).getMatchId();

        List<Delivery> filtered2016DeliveriesList=getDeliveries(deliveries, matchIdLowerLimit, matchIdUpperLimit);
        for (Delivery eachDelivery : filtered2016DeliveriesList) {
            if(theSixes2016PerTeam.containsKey(eachDelivery.getBattingTeam()) && eachDelivery.getTotalRuns()==6) {
                currentSixes = theSixes2016PerTeam.get(eachDelivery.getBattingTeam());
                theSixes2016PerTeam.put(eachDelivery.getBattingTeam(), currentSixes+1);
            }
            else if(eachDelivery.getTotalRuns()==6){
                theSixes2016PerTeam.put(eachDelivery.getBattingTeam(),1);
            }
        }
        System.out.println("\nSixes per team in 2016\n----------------");
        for(Map.Entry<String,Integer> entry: theSixes2016PerTeam.entrySet())
        {
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
    }

    private static void getTopEconomicBowlers2015(List<Match> matches, List<Delivery> deliveries) {
        Map<String,Float> topEconomicBowlers2015 = new HashMap<>();
        Map<String,Integer> runsConcededPerBowler = new HashMap<>();
        Map<String,Integer> ballsBowledPerBowler = new HashMap<>();

        List<Match> filtered2015MatchesList = getMatches(matches, 2015);
        int matchIdLowerLimit=filtered2015MatchesList.get(0).getMatchId();
        int matchIdUpperLimit=filtered2015MatchesList.get(filtered2015MatchesList.size()-1).getMatchId();

        List<Delivery> filtered2015DeliveriesList=getDeliveries(deliveries, matchIdLowerLimit, matchIdUpperLimit);

        for(Delivery eachDelivery:filtered2015DeliveriesList){
            if(eachDelivery.getWideRuns()>0 || eachDelivery.getNoBallRuns()>0){
                if(runsConcededPerBowler.containsKey(eachDelivery.getBowlerName())) {
                    int currentConcededRuns = runsConcededPerBowler.get(eachDelivery.getBowlerName());
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(), currentConcededRuns
                            + eachDelivery.getWideRuns()+ eachDelivery.getNoBallRuns());
                }
                else{
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(),eachDelivery.getNoBallRuns()
                                                                        + eachDelivery.getWideRuns());
                }

            }
            else {
                // if not, add totalRuns-(legByes+byes) to eachBowlerConcededRuns2k15;
                int totalRunsPerBall=eachDelivery.getBatsmanRuns();
                if(runsConcededPerBowler.containsKey(eachDelivery.getBowlerName())) {
                    int currentConcededRuns = runsConcededPerBowler.get(eachDelivery.getBowlerName());
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(), currentConcededRuns+totalRunsPerBall);
                }
                else{
                    runsConcededPerBowler.put(eachDelivery.getBowlerName(),totalRunsPerBall);
                }
                //add balls(just +1) to that eachBowlerBowledBalls2k15;
                if(ballsBowledPerBowler.containsKey(eachDelivery.getBowlerName())) {
                    int currentBallsBowled = ballsBowledPerBowler.get(eachDelivery.getBowlerName());
                    ballsBowledPerBowler.put(eachDelivery.getBowlerName(), currentBallsBowled+1);
                }
                else{
                    ballsBowledPerBowler.put(eachDelivery.getBowlerName(),1);
                }
            }

        }

        for(Map.Entry<String,Integer> entry: runsConcededPerBowler.entrySet()) {
            topEconomicBowlers2015.put(entry.getKey(), (entry.getValue()*6/(float)ballsBowledPerBowler.get(entry.getKey())));
        }
        //sort the Map based on economy
        System.out.println("\nTop Economic bowlers in IPL 2015\n----------------");
        topEconomicBowlers2015.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));
    }

    private static void getExtraRunsIn2016(List<Match> matches, List<Delivery> deliveries) {
        int currentExtraRuns;
        Map<String, Integer> theExtrasCount2016PerTeam = new HashMap<>();

        List<Match> filtered2016MatchesList = getMatches(matches, 2016);
        int matchIdLowerLimit=filtered2016MatchesList.get(0).getMatchId();
        int matchIdUpperLimit=filtered2016MatchesList.get(filtered2016MatchesList.size()-1).getMatchId();

        List<Delivery> filtered2016DeliveriesList=getDeliveries(deliveries, matchIdLowerLimit, matchIdUpperLimit);
        for (Delivery eachDelivery : filtered2016DeliveriesList) {
            if(theExtrasCount2016PerTeam.containsKey(eachDelivery.getBowlingTeam())) {
                currentExtraRuns = theExtrasCount2016PerTeam.get(eachDelivery.getBowlingTeam());
                theExtrasCount2016PerTeam.put(eachDelivery.getBowlingTeam(), currentExtraRuns
                                                                            +eachDelivery.getExtraRuns());
            }
            else{
                theExtrasCount2016PerTeam.put(eachDelivery.getBowlingTeam(),eachDelivery.getExtraRuns());
            }
        }
        System.out.println("\nExtra runs per team in 2016\n----------------");
        for(Map.Entry<String,Integer> entry: theExtrasCount2016PerTeam.entrySet())
        {
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
    }

    private static List<Match> getMatches(List<Match> matches, int year) {
        Stream<Match> filteredMatches = matches.stream().filter(eachMatch -> eachMatch.getSeason() == year);
        return filteredMatches.collect(Collectors.toList());
    }

    private static List<Delivery> getDeliveries(List<Delivery> deliveries, int matchIdLowerLimit, int matchIdUpperLimit) {
        Stream<Delivery> filteredDeliveries= deliveries.stream().filter(eachDelivery-> eachDelivery.getMatchId() >= matchIdLowerLimit && eachDelivery.getMatchId() <= matchIdUpperLimit);
        return filteredDeliveries.collect(Collectors.toList());
    }

    private static void getMatchesWonPerTeam(List<Match> matches) {
        HashMap<String,Integer> numberOfMatchesWonPerTeam=new HashMap<>();
        int tieCount=0;
        for(Match eachMatch:matches){
            if(numberOfMatchesWonPerTeam.containsKey(eachMatch.getWinner())){
                int correspondingTeamWinValue=numberOfMatchesWonPerTeam.get(eachMatch.getWinner());
                numberOfMatchesWonPerTeam.put(eachMatch.getWinner(),correspondingTeamWinValue+1);
            }
            else{
                if(eachMatch.getWinner().equals("")){
                    numberOfMatchesWonPerTeam.put("Tied",tieCount+1);
                    tieCount++;
                    continue;
                }
                numberOfMatchesWonPerTeam.put(eachMatch.getWinner(),1);
            }
        }
        System.out.println("\nMatches won per team\n----------------");
        for(Map.Entry<String,Integer> entry: numberOfMatchesWonPerTeam.entrySet())
        {
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
    }

    private static void getMatchesPerYear(List<Match> matches) {
        TreeMap<Integer,Integer> numberOfMatchesPerYear=new TreeMap<>();
        for(Match eachMatch:matches){
            if(numberOfMatchesPerYear.containsKey(eachMatch.getSeason())){
                int correspondingSeasonValue=numberOfMatchesPerYear.get(eachMatch.getSeason());
                numberOfMatchesPerYear.put(eachMatch.getSeason(),correspondingSeasonValue+1);
            }
            else{
                numberOfMatchesPerYear.put(eachMatch.getSeason(),1);
            }
        }
        System.out.println("\nMatches per Year\n----------------");
        for(Map.Entry<Integer,Integer> entry: numberOfMatchesPerYear.entrySet())
        {
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
    }

    private static List<Match> readMatches() {
        List<String> getStringsFromMatchesFile=readFileData("matches.csv");
        List<Match> allMatchesData=new ArrayList<>();
        int counter=0;
        while(counter<getStringsFromMatchesFile.size()){
            Match match=new Match();
            String[] eachMatch=getStringsFromMatchesFile.get(counter).split(",");
            match.setMatchId(Integer.parseInt(eachMatch[MATCH_ID]));
            match.setSeason(Integer.parseInt(eachMatch[SEASON]));
            match.setCity(eachMatch[CITY]);
            match.setTeam1(eachMatch[TEAM1]);
            match.setTeam2(eachMatch[TEAM2]);
            match.setResult(eachMatch[RESULT]);
            match.setWinner(eachMatch[WINNER]);
            allMatchesData.add(match);
            counter++;
        }
        return allMatchesData;
    }

    private static List<Delivery> readDeliveries() {
        List<String> getStringsFromDeliveriesFile=readFileData("deliveries.csv");
        List<Delivery> allDeliveriesData=new ArrayList<>();
        int counter=0;
        while(counter<getStringsFromDeliveriesFile.size()){
            Delivery delivery=new Delivery();
            String[] eachDelivery=getStringsFromDeliveriesFile.get(counter).split(",");
            delivery.setMatchId(Integer.
                    parseInt(eachDelivery[DELIVERY_MATCH_ID]));
            delivery.setInnings(Integer.parseInt(eachDelivery[DELIVERY_INNINGS]));
            delivery.setBattingTeam(eachDelivery[DELIVERY_BATTING_TEAM]);
            delivery.setBowlingTeam(eachDelivery[DELIVERY_BOWLING_TEAM]);
            delivery.setOver(Integer.parseInt(eachDelivery[DELIVERY_OVER]));
            delivery.setBall(Integer.parseInt(eachDelivery[DELIVERY_BALL]));
            delivery.setBowlerName(eachDelivery[DELIVERY_BOWLER]);
            delivery.setWideRuns(Integer.parseInt(eachDelivery[DELIVERY_WIDE]));
            delivery.setByeRuns(Integer.parseInt(eachDelivery[DELIVERY_BYE]));
            delivery.setLegByeRuns(Integer.parseInt(eachDelivery[DELIVERY_LEG_BYE]));
            delivery.setNoBallRuns(Integer.parseInt(eachDelivery[DELIVERY_NO_BALL]));
            delivery.setPenaltyRuns(Integer.parseInt(eachDelivery[DELIVERY_PENALTY]));
            delivery.setBatsmanRuns(Integer.parseInt(eachDelivery[DELIVERY_BATSMEN_RUNS]));
            delivery.setExtraRuns(Integer.parseInt(eachDelivery[DELIVERY_EXTRA_RUNS]));
            delivery.setTotalRuns(Integer.parseInt(eachDelivery[DELIVERY_TOTAL_RUNS]));
            allDeliveriesData.add(delivery);
            counter++;
        }
        return allDeliveriesData;
    }

    public static List<String> readFileData(String fileName){
        List<String> eachRowOfFile= new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            //skip headings
            br.readLine();
            String eachMatch=br.readLine();
            while(eachMatch!=null)
            {
                eachRowOfFile.add(eachMatch);
                eachMatch=br.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return eachRowOfFile;
    }
}
