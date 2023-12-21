package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RotateStringsClockwise {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        stringList.add("One");
        stringList.add("Two");
        stringList.add("Three");
        stringList.add("Four");
        stringList.add("Five");

        int rotationSteps = 2; // Number of steps to rotate clockwise

        System.out.println("Original List: " + stringList);

        rotateClockwise(stringList, rotationSteps);

        System.out.println("Rotated List: " + stringList);
    }

    public static void rotateClockwise(List<String> list, int steps) {
        int size = list.size();
        steps = steps % size; // Adjust steps in case it's greater than the list size

        // Use Collections.rotate to perform the rotation
        Collections.rotate(list, steps);
    }
}
