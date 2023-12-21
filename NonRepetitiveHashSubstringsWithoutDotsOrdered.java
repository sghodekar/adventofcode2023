package com.example;

import java.util.LinkedList;
import java.util.List;

public class NonRepetitiveHashSubstringsWithoutDotsOrdered {

    public static void main(String[] args) {
        String inputString = ".#.###.#.######";

        List<String> nonRepetitiveSubstrings = findNonRepetitiveHashSubstringsWithoutDotsOrdered(inputString);

        System.out.println("Non-repetitive substrings containing '#' without dots '.' in order: " + nonRepetitiveSubstrings);
    }

    public static List<String> findNonRepetitiveHashSubstringsWithoutDotsOrdered(String input) {
        List<String> nonRepetitiveSubstrings = new LinkedList<>();
        int length = input.length();
        StringBuilder contigousGroup = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '#') {
                sb.append('#');
            } else {
                if (i!=0 && input.charAt(i-1) == '#') {
                    nonRepetitiveSubstrings.add(sb.toString());
                    contigousGroup.append(sb.length()).append(",");
                    sb = new StringBuilder();
                }
            }
        }
        if (sb.length() > 0) {
            nonRepetitiveSubstrings.add(sb.toString());
            contigousGroup.append(sb.length());
        } else {
            contigousGroup.deleteCharAt(contigousGroup.length()-1);
        }
        System.out.println(contigousGroup.toString());

        return nonRepetitiveSubstrings;
    }
}