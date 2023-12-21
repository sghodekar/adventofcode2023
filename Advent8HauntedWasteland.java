package com.example;

import java.util.*;

class Node {
    String value;
    Node left;
    Node right;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(value, node.value) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

public class Advent8HauntedWasteland extends FileProcessor{

    String path;
    Map<String, String> inputMap = new HashMap<>();
    Node root = new Node();
    Map<String, Node> strNodeMap = new HashMap<>();

    @Override
    public String processLine(String s) {
        if (s != null && !"".equals(s)) {
            if (s.contains("(")){
                String[] parts = s.split("( = )");
            List<String> values = Arrays.asList(parts);
            inputMap.put(parts[0].trim(), parts[1].trim());
            } else {
                path = s.trim();
            }
        }
        return null;
    }

    void formTree() {

        inputMap.forEach((key, value) -> {
            Node n = strNodeMap.getOrDefault(key, new Node());
            n.value = key;
            strNodeMap.put(key, n);

            String[] values = value.substring(1,9).split(",\\s*");

            Node l = strNodeMap.getOrDefault(values[0], new Node());
            l.value = values[0];
            strNodeMap.put(values[0], l);
            n.left = l;

            Node r = strNodeMap.getOrDefault(values[1], new Node());
            r.value = values[1];
            strNodeMap.put(values[1], r);
            n.right = r;

        });
    }

    long traverseTree(Node root) {
        Node current = root;

        long counter = 0;
        boolean found = false;
        do {
            for (char c : path.toCharArray()) {
                if (c == 'L')
                    current = current.left;
                else
                    current = current.right;
                counter++;
                if (current.value.endsWith("Z")) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        System.out.println(counter);
        return counter;
    }

    public static void main(String[] args) {
        Advent8HauntedWasteland hw = new Advent8HauntedWasteland();
        hw.readFile("advent8_input.txt");
        System.out.println("path = "+hw.path);

        hw.formTree();
        System.out.println("Tree formed");
//        hw.traverseTree(hw.root);

        List<Long> counters = new ArrayList<>();
        hw.strNodeMap.forEach((key, value) -> {
            if (key.endsWith("A")) {
                System.out.println("For Node = "+key);
                counters.add(hw.traverseTree(value));
            }
        });

        System.out.println("final = "+calculateLCM(counters));
    }

    // Method to calculate GCD using the Euclidean algorithm
    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Method to calculate LCM of two numbers
    private static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    // Method to calculate LCM of a list of numbers
    private static long calculateLCM(List<Long> numbers) {
        if (numbers.size() == 0) {
            return 0; // LCM is undefined for an empty list
        }

        long result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = lcm(result, numbers.get(i));
        }
        return result;
    }
}
