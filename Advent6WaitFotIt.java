package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Advent6WaitFotIt {

    static List<Long> getDistances(long time) {
        List<Long> distances = new ArrayList<>();
        long dist;
        for(int i=0; i<=time; i++) {
            dist = i * (time-i);
            distances.add(dist);
        }
        return distances;
    }

    static long getExceedCount(List<Long> distances, long dist) {
        long counter = 0;
        for(long d : distances) {
            if (d > dist)
                counter++;
        }
        return counter;
    }

    static long getExceedCount(long iptime, long ipdist) {
        long dist;
        long count = 0;
        for(int i=0; i<=iptime; i++) {
            dist = i * (iptime-i);
            if (dist > ipdist)
                count++;
        }
        System.out.println(count);
        return count;
    }

    public static void main(String[] args) {

        List<Long> inputTimes = Arrays.asList(41777096l);
        List<Long> inputDistances = Arrays.asList(249136211271011l);

        //List<Long> inputTimes = Arrays.asList(15l);
        //List<Long> inputDistances = Arrays.asList(40l);

        getExceedCount(inputTimes.get(0), inputDistances.get(0));
        long totalways = 1;
/*        for(int i=0; i < 4; i++){
            List<Long> distances = getDistances(inputTimes.get(i));
            //System.out.println(distances);
            totalways = totalways * getExceedCount(distances, inputDistances.get(i));
        }*/
        //System.out.println(totalways);
    }
}
