package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class Pattern {
    List<String> pattern;

    public Pattern(List<String> pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern1 = (Pattern) o;
        return pattern.equals(pattern1.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern);
    }
}

public class Advent14ParabolicReflector extends FileProcessor {

    List<String> inputs = new ArrayList<>();

    @Override
    public String processLine(String s) {
        inputs.add(s);
        return null;
    }

    public static void main(String[] args) {
        Advent14ParabolicReflector pr = new Advent14ParabolicReflector();

        pr.readFile("advent14_input.txt");

        List<String> newpositions = pr.inputs;

        part1(newpositions);
        part2(newpositions);
    }

    static void part1(List<String> rockPositions) {
        List<String> newPositions = north(rockPositions);
        calculateNorthLoad(newPositions);
    }

    static void part2(List<String> rockPositions) {

        List<Pattern> patternsList = new ArrayList<>();
        List<String> newpositions = rockPositions;
        List<String> prevpos = newpositions;
        int counter = 0, index = 0;
        long i = 1l;
        for (; i < 1000000000l; i++) {
            prevpos = newpositions;
            newpositions = cycle(newpositions);
            counter++;
            Pattern p = new Pattern(newpositions);
            if (patternsList.contains(p)) {
                index = patternsList.indexOf(p) + 1 ;
                System.out.println(" index = "+index);
                break;
            }
            patternsList.add(p);
        }

        long remaining = (1000000000l - (index-1)) % (patternsList.size() - (index-1) );

        newpositions = prevpos;
        for (long j=1 ; j <= remaining; j++) {
            newpositions = cycle(newpositions);
        }

        calculateNorthLoad(newpositions);
    }

    public static List<String> cycle(List<String> rockPositions) {
        List<String> north = north(rockPositions);
        /*System.out.println();
        System.out.println("north");
        print(north);*/
        List<String> west = west(north);
        /*System.out.println();
        System.out.println("west");
        print(west);*/
        List<String> south = south(west);
        /*System.out.println();
        System.out.println("south");
        print(south);*/
        List<String> east = east(south);
        /*System.out.println();
        System.out.println("east");
        print(east);*/
        return east;
    }

    static List<String> north(List<String> inputs) {
        List<String> transposed = StringUtil.transpose(inputs);
        List<String> north = StringUtil.transpose(west(transposed));
        /*System.out.println("North");
        print(north);*/
        return north;

    }

    public static List<String> west(List<String> rockpositions ) {

        List<String> newRockPositions = new ArrayList<>();

        for(String rockpos : rockpositions) {

            for (int i=1; i < rockpos.length(); i++) {
                int j=i;

                do {
                    if (rockpos.charAt(j) == 'O' && rockpos.charAt(j - 1) == '.') {
                        rockpos = StringUtil.swapCharacters(rockpos, j, j - 1);
                    } else {
                        break;
                    }
                    j--;
                }while (j>0);
            }
            newRockPositions.add(rockpos);
        }
       /* System.out.println("West");
        print(newRockPositions);*/
        return newRockPositions;

    }

    static List<String> south(List<String> rockPositions) {
        Collections.reverse(rockPositions);
        List<String> north = north(rockPositions);
        Collections.reverse(north);

        return north; //its actually south
    }

    static List<String> east(List<String> rockPositions) {
        List<String> reversed = StringUtil.reverseStrings(rockPositions);
        List<String> west = west(reversed);
        List<String> east = StringUtil.reverseStrings(west);
/*        System.out.println();
        System.out.println("East");
        print(east);*/
        return east;
    }

    static  void calculateNorthLoad(List<String> newRockPositions) {
        long sum = 0;
        for (int row=0; row < newRockPositions.size(); row++) {
            int Oocurrances = StringUtil.countOccurrences(newRockPositions.get(row), 'O');
            sum = sum + Oocurrances * (newRockPositions.size() - row);
        }

        System.out.println("Sum = "+ sum);

    }

    static void print(List<String> l) {
        l.forEach(s-> {StringUtil.printStringWithSpace(s);
            System.out.println();} );
    }
}
