package com.example;

import java.util.*;

public class CharacterCount {

    public static void main(String[] args) {
        String inputString = "AA5AA";
        Map<Character, Integer> charCountMap = countCharacters(inputString);

        // Display the results
        System.out.println("Character counts in the string:");
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue());
        }

        List<Integer> counts = sortedCharCounts(charCountMap);
        System.out.println(counts);
    }

    public static Map<Character, Integer> countCharacters(String str) {
        // Create a map to store character counts
        Map<Character, Integer> charCountMap = new HashMap<>();

        // Iterate through the characters in the string
        for (char c : str.toCharArray()) {
            // Update the count in the map
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }

        return charCountMap;
    }

    public static List<Integer> sortedCharCounts(Map<Character, Integer> charCountMap) {
        List<Integer> counts = new ArrayList<Integer>(charCountMap.values());
        Collections.sort(counts, Collections.reverseOrder());
        return counts;
    }
}