package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Advent13PointOfIncidence extends FileProcessor{

    List<List<String>> patterns = new ArrayList<>();
    List<String> inputList = new ArrayList<>();

    @Override
    public String processLine(String s) {
        if (s.isEmpty()) {
            patterns.add(inputList);
            inputList = new ArrayList<>();
        } else {
            inputList.add(s);
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> testList = Arrays.asList("#.##..##.",
                "..#.##.#.",
                "##......#",
                "##......#",
                "..#.##.#.",
                "..##..##.",
                "#.#.##.#.");


        Advent13PointOfIncidence poi = new Advent13PointOfIncidence();
        poi.readFile("advent13_input.txt");

        int sum = 0;
        for (int i = 0; i <poi.patterns.size(); i++ ) {
            List<String> pat = poi.patterns.get(i);
            int p = findPoi2(pat);
            System.out.println(p*100);
            sum = sum + p*100;

                List<String> transposed = transpose(pat);
                p = findPoi2(transposed);
            System.out.println(p);
                sum = sum + p;

            System.out.println();
        }
        System.out.println("SUm = "+ sum);
    }

    static int findPoi2(List<String> pattern) {
        int poi = 0;
        for (int i = 0; i <pattern.size() - 1; i++) {
            boolean matched = false;
            if (pattern.get(i).equals(pattern.get(i+1)) || areStringsOneCharApart(pattern.get(i), pattern.get(i+1))) {
                poi = i;
                matched = true;
            }

            int smudgeCount = 0;
            if (matched) {
                if ( areStringsOneCharApart(pattern.get(poi), pattern.get(poi+1))) {
                    smudgeCount++;
                }
            }

            if (matched) {
                boolean mirrorflag = true;
                int lower = poi-1 , upper = poi + 2;
                while (lower >= 0 && upper < pattern.size() && mirrorflag) {
                    if (!pattern.get(lower).equals(pattern.get(upper))) {
                        if (areStringsOneCharApart(pattern.get(lower), pattern.get(upper))) {
                            smudgeCount++;
                            if ( smudgeCount > 1) {
                                mirrorflag = false;
                            }
                        } else {
                            mirrorflag = false;
                        }
                    }
                    lower--;
                    upper++;

                }
                if (mirrorflag && smudgeCount == 1) {
                    return poi +1;
                }
            }

        }
        return 0;
    }

    static int findPoi1(List<String> pattern) {
        int poi = 0;
        for (int i = 0; i <pattern.size() - 1; i++) {
            boolean matched = false;
            if (pattern.get(i).equals(pattern.get(i+1))) {
                poi = i;
                matched = true;
            }

            if (matched) {
                boolean mirrorflag = true;
                int lower = poi-1 , upper = poi + 2;
                while (lower >= 0 && upper < pattern.size() && mirrorflag) {
                    if (!pattern.get(lower).equals(pattern.get(upper))) {
                            mirrorflag = false;
                    }

                    lower--;
                    upper++;
                }
                if (mirrorflag) {
                    return poi +1;
                }
            }
        }
        return 0;
    }

    static void print(List<String> l) {
        l.forEach(s-> System.out.println(s));
    }

    static List<String> transpose(List<String> org) {
        List<String> transposed = new ArrayList<>();
        List<StringBuilder> transposedsb = new ArrayList<>();

        // Get the number of columns in the original list
        long columns = org.get(0).length();

        // Initialize the transposed list with empty lists
        for (int i = 0; i < columns; i++) {
            transposedsb.add(new StringBuilder());
        }

        // Iterate through the original list and populate the transposed list
        for (String row : org) {
            for (int i = 0; i < columns; i++) {
                transposedsb.get(i).append(row.charAt(i));
            }
        }

        transposedsb.forEach(sb -> transposed.add(sb.toString()));

        return transposed;
    }

    public static boolean areStringsOneCharApart(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;  // If lengths are different, strings are not one character apart
        }

        int differenceCount = 0;

        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                differenceCount++;

                if (differenceCount > 1) {
                    return false;  // More than one difference found
                }
            }
        }

        return differenceCount == 1;
    }
}
