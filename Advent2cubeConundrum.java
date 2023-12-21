package com.example;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Advent2cubeConundrum extends FileProcessor {

    public List<List<String>> games = new ArrayList<>();

    int gameCountsMeetingCriteria(List<String> games, Map<String, Integer> criteriaMap) {
        Map<String, Integer> colorCount = new HashMap<>();

        int gamescount = 0;
        // Process each game and update the color counts
        for (String game : games) {
            // Split the game string into color-count pairs
            String[] colorPairs = game.split(", ");
            boolean allpass = true;

            // Update the color count map
            for (String colorPair : colorPairs) {
                String[] parts = colorPair.split(" ");
                if (parts.length == 2) {
                    String color = parts[1];
                    int count = Integer.parseInt(parts[0]);

                    if (count > criteriaMap.get(color))
                        allpass = false;

                }
            }
            if (allpass == true)
                gamescount = gamescount + (games.indexOf(game) + 1);
        }

        System.out.println("Final games Count: "+gamescount);
        return gamescount;
    }

    boolean subGameColorCounter(List<String> games, Map<String, Integer> criteriaMap) {
        // Create a map to store the counts of each color
        Map<String, Integer> colorCount = new HashMap<>();

        boolean allpass = true;
        // Process each game and update the color counts
        for (String game : games) {
            // Split the game string into color-count pairs
            String[] colorPairs = game.split(", ");

            // Update the color count map
            for (String colorPair : colorPairs) {
                String[] parts = colorPair.split(" ");
                if (parts.length == 2) {
                    String color = parts[1];
                    int count = Integer.parseInt(parts[0]);

                    if (count > criteriaMap.get(color))
                        allpass = false;
                    // Update the color count in the map
                    colorCount.put(color, colorCount.getOrDefault(color, 0) + count);
                }
            }
        }

        // Print the final color counts
        System.out.println("Final Color Counts:");
        for (Map.Entry<String, Integer> entry : colorCount.entrySet()) {
            System.out.println(entry.getValue() + " " + entry.getKey());
        }

        return allpass;
    }

    List<String> formGameList(List<List<String>> games) {
        List<String> gameList = new ArrayList<>();
        Map<String, Integer> criteriaMap = new HashMap<>();
        //12 red cubes, 13 green cubes, and 14 blue cubes
        criteriaMap.put("red",12);
        criteriaMap.put("green",13);
        criteriaMap.put("blue",14);

        int count = 0;
        for (List<String> game: games ) {
            StringBuilder sb = new StringBuilder();

            boolean allpass = subGameColorCounter(game, criteriaMap);

            if (allpass) {
                count = count + (games.indexOf(game) + 1);
            }

        }
        System.out.println("****** total is: "+count);
        return null;
    }

    void findMaxOfEachMultiplyAdd(List<List<String>> games) {

        int productSum = 0;

        for (List<String> subgames: games ) {
            Map<String, Integer> colorCount = new HashMap<>();

            // Process each game and update the color counts
            for (String subgame : subgames) {
                // Split the game string into color-count pairs
                String[] colorPairs = subgame.split(", ");

                // Update the color count map
                for (String colorPair : colorPairs) {
                    String[] parts = colorPair.split(" ");
                    if (parts.length == 2) {
                        String color = parts[1];
                        int count = Integer.parseInt(parts[0]);
                        // Update the color count in the map to max of the values
                        if (count > colorCount.getOrDefault(color, 0))
                            colorCount.put(color, count);
                    }
                }
            }

            // Print the final color counts
            System.out.println("Max each Color Counts:");
            int product = 1;
            for (Map.Entry<String, Integer> entry : colorCount.entrySet()) {
                System.out.println(entry.getValue() + " " + entry.getKey());
                product = product * entry.getValue();
            }
            productSum = productSum + product;
        }
        System.out.println("Product Sum = "+ productSum);
    }

    public static void main(String[] args) throws IOException {
        
        Advent2cubeConundrum cc = new Advent2cubeConundrum();

        cc.readLine(new File("D:\\Santosh\\loancalculator\\src\\main\\resources\\advent2_input.txt"));

        Map<String, Integer> criteriaMap = new HashMap<>();
        //12 red cubes, 13 green cubes, and 14 blue cubes
        criteriaMap.put("red",12);
        criteriaMap.put("green",13);
        criteriaMap.put("blue",14);

        // Define the game sequence
        /*List<String> subgames = Arrays.asList(
                "4 green, 2 blue",
                "1 red, 1 blue, 4 green",
                "3 green, 4 blue, 1 red",
                "7 green, 2 blue, 4 red",
                "3 red, 7 green",
                "3 red, 3 green"
        );

        List<String> subgames2 = Arrays.asList(
                "1 blue, 11 red, 1 green",
                "3 blue, 2 red, 4 green",
                "11 red, 2 green, 2 blue",
                "13 green, 5 red, 1 blue",
                "4 green, 8 red, 3 blue"
        );

        List<String> subgames3 = Arrays.asList("3 green, 7 blue, 7 red", "6 green, 3 red, 4 blue", "2 blue, 1 red");
        List<String> subgames4 = Arrays.asList("3 green, 5 blue, 7 red", "6 green, 3 red, 4 blue", "2 blue, 1 red");

        cc.games.add(subgames);
        cc.games.add(subgames2);
        cc.games.add(subgames3);
        cc.games.add(subgames4);*/

        cc.findMaxOfEachMultiplyAdd(cc.games);

        /*List<String> combinedGamesList = cc.formGameList(cc.games);
        System.out.println(combinedGamesList);
*/
        //cc.gameCountsMeetingCriteria(combinedGamesList, criteriaMap);
    }

    @Override
    public String processLine(String s) {
        String[] subgames = s.split(": ")[1].split("; ");
        List<String> game = Arrays.asList(subgames);
        games.add(game);
        return null;
    }

}
