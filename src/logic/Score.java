package logic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author  Zhimin Xi
 * @version 10/24/2018
 *
 * A class which  stores the number of itemName/wins/losses/ties/total for that result.
 *
 *
 */

public class Score {

    private int wins;
    private int losses;
    private int ties;
    private int total;
    private String itemName;

    public Score(String name) {
        itemName = name;
        wins = 0;
        losses = 0;
        ties = 0;
        total = 0;
    }

    public static ArrayList<Score> ReportScore(ArrayList<Result> results) {

        HashMap<String, Score > map = new HashMap<>();
        ArrayList<Score> scores = new ArrayList<>();

        for (Result result : results) {
            Score item1Score = map.get(result.getItem1Name());
            if (item1Score == null) {
                item1Score = new Score(result.getItem1Name());
                map.put(item1Score.getName(), item1Score);
                scores.add(item1Score);
            }

            Score item2Score = map.get(result.getItem2Name());
            if (item2Score == null) {
                item2Score = new Score(result.getItem2Name());
                map.put(item2Score.getName(), item2Score);
                scores.add(item2Score);
            }

            if (result.getResultCode() == 1)// the code is for item 1 wins...)
            {
                item1Score.incrementWins();
                item1Score.incrementTotal();
                item2Score.incrementLosses();
                item2Score.decrementTotal();


            } else if (result.getResultCode() == 2) //... whatever the code is for item 2 wins ...)
            {
                item2Score.incrementWins();
                item2Score.incrementTotal();
                item1Score.incrementLosses();
                item1Score.decrementTotal();
            } else if (result.getResultCode() == 0)// tie
            {
                item1Score.incrementTies();
                item2Score.incrementTies();
            }
        }
        return scores;
    }

    private void incrementTies() {
        ties += 1;
    }

    private void decrementTotal() {
        total -= 1;
    }

    private void incrementLosses() {
        losses += 1;
    }
    private void incrementTotal() {
         total += 1;
    }


    private void incrementWins() {
        wins += 1;
    }

    public String getName() {
        return itemName;
    }

    public int getWin(){
        return wins;
    }

    public int getLosses(){
        return losses;
    }

    public int getTies(){
        return ties;
    }
    public int getTotal(){
        return total;
    }
}