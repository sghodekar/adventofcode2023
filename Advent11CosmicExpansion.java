package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

class HashPos {

    public HashPos(long x, long y, long value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    long x;
    long y;
    long value;
    boolean visited;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashPos hashPos = (HashPos) o;
        return x == hashPos.x && y == hashPos.y && value == hashPos.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, value);
    }

    @Override
    public String toString() {
        return "HashPos{" +
                "x=" + x +
                ", y=" + y +
                ", value=" + value +
                ", visited=" + visited +
                '}';
    }
}


public class Advent11CosmicExpansion extends FileProcessor {


    List<String> inputrows = new ArrayList<>();

    List<Integer> emptyX = new ArrayList<>();
    List<Integer> emptyY = new ArrayList<>();

    static long expansion = 1000000;

    public static void main(String[] args) {

        Advent11CosmicExpansion ce = new Advent11CosmicExpansion();
        ce.readFile("advent11_input.txt");
        System.out.println("Original");
        print(ce.inputrows);

        System.out.println("Expand");

        ce.emptyX = ce.expand2(ce.inputrows);
        //print(ce.inputrows);

        System.out.println("Transpose");
        List<String> transposed = ce.transpose(ce.inputrows);
        print(transposed);

        System.out.println("Expand Transposed");
        ce.emptyY = ce.expand2(transposed);


//        System.out.println("Transpose back expanded transpose");
//        List<String> orgExpanded = ce.transpose(transposeExpanded);
//        print(orgExpanded);

        List<HashPos> hashPositions = ce.getHashPositions(ce.inputrows);
        System.out.println(hashPositions);

        ce.findPairAndCalculateDistance2(hashPositions);
    }

    static long findNoOfEmptyGalaxies(long v1, long v2, List<Integer> emptyList) {
        long count = 0;

        if (v1 == v2) {
            return 0;
        }

        long min = Math.min(v1, v2);

        if (v2 == min) {
            v2 = v1;
            v1 = min;
        }

        long finalV = v1;
        long finalV1 = v2;
        count = (int) emptyList.stream().filter(v -> finalV < v && v < finalV1).count();

        return count;
    }

    void findPairAndCalculateDistance(List<HashPos> hashPositions) {
        long total = 0;
        for (int i=0; i < hashPositions.size(); i++) {
            HashPos hpI = hashPositions.get(i);
            hpI.visited = true;
            for (int j=0; j < hashPositions.size(); j++) {
                HashPos hpJ = hashPositions.get(j);
                if (hpJ.visited)
                    continue;

                long dist = Math.abs(hpJ.x-hpI.x) + Math.abs(hpJ.y-hpI.y);
                System.out.println("Distance between ("+hpI.toString()+") and ("+hpJ.toString() + ") is "+ dist) ;
                total = total + dist;
            }
        }
        System.out.println("Total is "+total);
    }

    void findPairAndCalculateDistance2(List<HashPos> hashPositions) {
        long total = 0;
        for (int i=0; i < hashPositions.size(); i++) {
            HashPos hpI = hashPositions.get(i);
            hpI.visited = true;
            for (int j=0; j < hashPositions.size(); j++) {
                HashPos hpJ = hashPositions.get(j);
                if (hpJ.visited)
                    continue;

                long noofXempty = findNoOfEmptyGalaxies(hpI.x, hpJ.x, emptyX) ;
                long noofYempty = findNoOfEmptyGalaxies(hpI.y, hpJ.y, emptyY) ;
                long distX = Math.abs(hpJ.x-hpI.x) - noofXempty + noofXempty * expansion;
                long distY = Math.abs(hpJ.y-hpI.y) - noofYempty + noofYempty * expansion;
                long dist = distX + distY;
                System.out.println("Distance between ("+hpI.toString()+") and ("+hpJ.toString() + ") is "+ dist) ;
                total = total + dist;
            }
        }
        System.out.println("Total is "+total);
    }

    public static List<Integer> getCharacterPositions(String input, char targetChar) {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == targetChar) {
                positions.add(i);
            }
        }

        return positions;
    }

    List<HashPos> getHashPositions(List<String> orgExpanded) {
        List<HashPos> hashPositions = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        for(int i=0; i < orgExpanded.size(); i++) {
            List<Integer> charPositions = getCharacterPositions(orgExpanded.get(i), '#');
            if (!charPositions.isEmpty()) {
                long finalI = i;
                charPositions.forEach(pos -> {
                    counter.getAndIncrement();
                    HashPos hp = new HashPos(finalI, pos, counter.get());
                    hashPositions.add(hp);
                });
            }
        }
        return hashPositions;
    }

    List<HashPos> getHashPositions2(List<String> orgExpanded) {
        List<HashPos> hashPositions = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        for(int i=0; i < orgExpanded.size(); i++) {
            List<Integer> charPositions = getCharacterPositions(orgExpanded.get(i), '#');
            if (!charPositions.isEmpty()) {
                long finalI = i;
                charPositions.forEach(pos -> {
                    long noOfEmptyXGalaxies = expansion * findNoOfEmptyGalaxies(0, finalI, emptyX);
                    long noOfEmptyYGalaxies = expansion * findNoOfEmptyGalaxies(0, pos, emptyY);
                    counter.getAndIncrement();
                    HashPos hp = new HashPos(finalI+noOfEmptyXGalaxies, pos+noOfEmptyYGalaxies, counter.get());
                    hashPositions.add(hp);
                });
            }
        }
        return hashPositions;
    }

    List<String> expand(List<String> org) {
        List<String> expandedInputRows = new ArrayList<>();
        org.forEach(row -> {
            expandedInputRows.add(row);
            if (!row.contains("#")) {
                expandedInputRows.add(row);
            }
        });
        return expandedInputRows;
    }

    List<Integer> expand2(List<String> org) {
        List<Integer> empty = new ArrayList<>();
        final int[] rowcount = {0};
        org.forEach(row -> {
            if (!row.contains("#")) {
                System.out.println("org.indexOf(row) = "+ rowcount[0]);
                empty.add(rowcount[0]);
            }
            rowcount[0]++;
        });
        return empty;
    }

    List<String> transpose(List<String> org) {
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

    static void print(List<String> l) {
        l.forEach(str -> System.out.println(str));
    }

    @Override
    public String processLine(String s) {
        inputrows.add(s);
        return null;
    }
}
