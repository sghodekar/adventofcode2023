package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReverseStringsInList {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        stringList.add("One");
        stringList.add("Two");
        stringList.add("Three");
        stringList.add("Four");
        stringList.add("Five");

        System.out.println("Original List: " + stringList);

        List<String> reversedList = reverseStrings(stringList);

        System.out.println("Reversed List: " + reversedList);
    }

    public static List<String> reverseStrings(List<String> list) {
        return list.stream()
                .map(s -> new StringBuilder(s).reverse().toString())
                .collect(Collectors.toList());
    }
}