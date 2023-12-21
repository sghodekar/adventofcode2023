package com.example;

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Advent4ScratchCards extends FileProcessor {

    List<String> codes = new ArrayList<>();

    Map<Integer, Integer> cardswithCopy = new TreeMap<>();

    public static void main(String[] args) {
        Advent4ScratchCards sc = new Advent4ScratchCards();
        sc.readFile("advent4_input.txt");
        String st = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53";
        AtomicInteger points = new AtomicInteger();
        AtomicInteger cardno = new AtomicInteger(1);
        sc.codes.forEach(s -> points.set(points.get() + sc.splitAndFormLists(s, cardno)));

        System.out.println("Final points: "+ points.get());

        int totalCards = 0;
        for(Map.Entry<Integer, Integer> entry : sc.cardswithCopy.entrySet()) {
            totalCards = totalCards + entry.getValue();
        }

        System.out.println("Total cards = "+totalCards);
    }

    int splitAndFormLists(String st, AtomicInteger cardno) {
        //String st = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53";
        String[] parts = st.split(":")[1].split("[|]");
        List<String> winningNumbers = splitStringToList(parts[0].trim());
        List<String> numbersYouHave = splitStringToList(parts[1].trim());
        List<String> commonNumbers = new ArrayList<>(numbersYouHave);
        System.out.println(winningNumbers);
        System.out.println(numbersYouHave);
        commonNumbers.retainAll(winningNumbers);
        System.out.println(commonNumbers);

        int noOfCards = cardswithCopy.getOrDefault(cardno.get(), 0);
        cardswithCopy.put(cardno.get(), noOfCards+1);

        System.out.println(commonNumbers.size());
        if (commonNumbers.isEmpty()) {
            cardno.set(cardno.get() + 1);
            return 0;
        }
        int points = (int) Math.pow(2, commonNumbers.size() -1);

        for (int i = 1; i<= commonNumbers.size() && (cardno.get() + i <= codes.size()); i++) {
          /*  noOfCards = cardswithCopy.getOrDefault(cardno.get() + i, 0);
            cardswithCopy.put(cardno.get() + i, noOfCards+1);*/
            splitAndFormLists(codes.get(cardno.get() + i -1), new AtomicInteger(cardno.get() + i));
        }

        cardno.set(cardno.get() + 1);

        return points;
    }

    void appendNextCards(int cardno, int matches) {
        if (matches == 0 || cardno > codes.size())
            return;
        else {
            int noOfCards = cardswithCopy.getOrDefault(cardno, 0);
            cardswithCopy.put(cardno, noOfCards+1);
            for (int i = 1; i<= noOfCards; i++) {
                noOfCards = cardswithCopy.getOrDefault(cardno + i, 0);
                cardswithCopy.put(cardno + i, noOfCards+1);
            }
            appendNextCards(cardno + 1, matches - 1);
        }
    }

    private static ArrayList<String> splitStringToList(String inputString) {
        // Create an ArrayList to store the result
        ArrayList<String> result = new ArrayList<>();

        // Use split method to split the input string by spaces
        String[] tokens = inputString.split("\\s+");

        // Add each token to the ArrayList
        for (String token : tokens) {
            result.add(token);
        }

        // Return the ArrayList
        return result;
    }

    @Override
    public String processLine(String s) {
        codes.add(s);
        return null;
    }
}
