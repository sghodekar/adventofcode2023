package com.example;

import java.util.*;

class CustomStringComparator implements Comparator<String> {

    private List<Character> customOrder;

    public CustomStringComparator(List<Character> customOrder) {
        this.customOrder = customOrder;
    }

    @Override
    public int compare(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int minLength = Math.min(len1, len2);

        for (int i = 0; i < minLength; i++) {
            char char1 = str1.charAt(i);
            char char2 = str2.charAt(i);

            int index1 = customOrder.indexOf(char1);
            int index2 = customOrder.indexOf(char2);

            if (index1 != index2) {
                return Integer.compare(index2, index1);
            }
        }

        return Integer.compare(len1, len2);
    }
}

class CustomStringComparator2 implements Comparator<String> {

    private List<Character> customOrder;

    public CustomStringComparator2(List<Character> customOrder) {
        this.customOrder = customOrder;
    }

    @Override
    public int compare(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int minLength = Math.min(len1, len2);

        for (int i = 0; i < minLength; i++) {
            char char1 = str1.charAt(i);
            char char2 = str2.charAt(i);

            if(i==0) {
                int com = Character.compare(char2, char1);
                if (com!=0) {
                    return com;
                }
            }

            int index1 = customOrder.indexOf(char1);
            int index2 = customOrder.indexOf(char2);

            if (index1 != index2) {
                return Integer.compare(index1, index2);
            }
        }

        return Integer.compare(len1, len2);
    }
}

class CamelCards implements Comparator<String> {
    static List<Character> cardsOrder= Arrays.asList('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
    CustomStringComparator comparator = new CustomStringComparator(cardsOrder);

    @Override
    public int compare(String o1, String o2) {
        Map<Character, Integer> ccStr1 = CharacterCount.countCharacters(o1);
        Map<Character, Integer> ccStr2 = CharacterCount.countCharacters(o2);

        List<Integer> c1 = CharacterCount.sortedCharCounts(ccStr1);
        List<Integer> c2 = CharacterCount.sortedCharCounts(ccStr2);

        int comparison = 0;
        for(int i=0; i<5; i++) {
            if (i < c1.size() && i < c2.size()) {
                comparison = c1.get(i).compareTo(c2.get(i));
                if (comparison != 0)
                    return comparison;
            }
        }

        return comparator.compare(o1, o2);
    }
}

class CamelCards2 implements Comparator<String> {
    static List<Character> cardsOrder= Arrays.asList('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J');
    CustomStringComparator comparator = new CustomStringComparator(cardsOrder);
    CustomStringComparator2 comparator2 = new CustomStringComparator2(cardsOrder);

    @Override
    public int compare(String o1, String o2) {
        Map<Character, Integer> ccStr1 = CharacterCount.countCharacters(o1);
        Map<Character, Integer> ccStr2 = CharacterCount.countCharacters(o2);

        List<String> countCharCombined1 = combine(ccStr1);
        List<String> countCharCombined2 = combine(ccStr2);

        Collections.sort(countCharCombined1, comparator2);
        Collections.sort(countCharCombined2, comparator2);

        System.out.println("countCharCombined1 "+countCharCombined1);
        System.out.println("countCharCombined2 "+countCharCombined2);

        List<String> appendedwithJ1 = appendWithJ(countCharCombined1);
        List<String> appendedwithJ2 = appendWithJ(countCharCombined2);

        System.out.println("appendedwithJ1 "+appendedwithJ1);
        System.out.println("appendedwithJ2 "+appendedwithJ2);

        List<Integer> c1 = CharacterCount.sortedCharCounts(ccStr1);
        List<Integer> c2 = CharacterCount.sortedCharCounts(ccStr2);

        int comparison = 0;
        for(int i=0; i<5; i++) {
            if (i < appendedwithJ1.size() && i < appendedwithJ2.size()) {
                comparison = Character.compare(appendedwithJ1.get(i).charAt(0), appendedwithJ2.get(i).charAt(0));
                if (comparison != 0)
                    return comparison;
            }
        }

        return comparator.compare(o1, o2);
    }

    private List<String> combine(Map<Character, Integer> ccStr) {
        List<String> countCharCombined = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : ccStr.entrySet()) {
            String combined = entry.getValue() + "" + entry.getKey();
            countCharCombined.add(combined);
        }
        return countCharCombined;
    }

    private List<String> appendWithJ(List<String> withJ) {
        List<String> withoutJ = new ArrayList<>();
        String s1 = (withJ.get(0).contains("J")) ? (withJ.size() > 1) ? withJ.get(1) : null : withJ.get(0);
        int index = 0;
        if (s1 != null)
            index = withJ.indexOf(s1);
        else
            return withJ;

        int i = -1;
        String newstr = "";
        boolean flag = false;
        for(String s: withJ) {
              if (s.contains("J")) {
                  flag = true;
                  i= withJ.indexOf(s);
                  char count1 = s1.charAt(0);
                  char c2 = s1.charAt(1);
                  char count2 = s.charAt(0);
                  int total = Character.getNumericValue(count1) + Character.getNumericValue(count2);
                  newstr = total + "" + c2;
              }
        }
        if (flag) {
            if (i > index) {
                withJ.remove(i);
                withJ.remove(index);
            } else {
                withJ.remove(index);
                withJ.remove(i);
            }
            withJ.add(0, newstr);
        }
        return  withJ;

    }
}


public class Advent7CamelCards extends FileProcessor{

    Map<String, Integer> cardsBidMap = new TreeMap<>(new CamelCards2());

    @Override
    public String processLine(String s) {
        String[] tokens = s.trim().split("\\s+");
        cardsBidMap.put(tokens[0], Integer.parseInt(tokens[1]));
        return null;
    }

    public static void main(String[] args) {
        TreeMap<String, Integer> cardsBid= new TreeMap<>(new CamelCards2());

        cardsBid.put("32T3K", 765);
        cardsBid.put("T55J5", 684);
        cardsBid.put("KJJ77", 28);
        cardsBid.put("77KJ2", 220);
        cardsBid.put("772J2", 483);

        System.out.println(cardsBid.keySet());

        Advent7CamelCards cc = new Advent7CamelCards();
        cc.readFile("advent7_input.txt");

        int counter = 1;
        long sum = 0;
        for (Map.Entry<String, Integer> entry : cc.cardsBidMap.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue());
            sum = sum + (entry.getValue() * counter);
            counter++;
        }

        System.out.println(sum);

    }
}
