package com.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advetn3GearRatios extends FileProcessor {

    public List<String> schematicList = new LinkedList<>();

    List<Integer> validNumbers = new ArrayList<>();

    String symbolPattern = "[-!@#$%^&*+=/]";

    String symbolPattern2 = "[*]";

    Map<String, ArrayList<Integer>> pair = new HashMap<>();

    public static void main(String[] args) {

        Advetn3GearRatios gr = new Advetn3GearRatios();
        //String inputString = "...234...%....11..@.08";

        //separateNumbers(inputString);

        //findSymbols(inputString, symbolPattern);

        gr.readFile("advent3_input.txt");
        gr.processInputs();
        gr.addValidNumbers();
        gr.iteratePairMultiplyAdd();
    }

    public void addValidNumbers() {
        int sum = 0;
        for(int num:validNumbers) {
            System.out.println(num);
            sum = sum + num;
        }
        System.out.println(sum);
    }

    public void processInputs() {
        // Accessing elements and their previous and next elements
        ListIterator<String> iterator = schematicList.listIterator();


        while (iterator.hasNext()) {
            int currentIndex = iterator.nextIndex();
            // Accessing the previous element
            String previousElement = null;
            if (iterator.hasPrevious()) {
                previousElement = iterator.previous();
                iterator.next(); // Move the iterator back to the current element
            }

            String currentElement = iterator.next();

            // Accessing the next element
            String nextElement = null;
            if (iterator.hasNext()) {
                nextElement = iterator.next();
                iterator.previous(); // Move the iterator back to the current element
            }

            List<String> numbersWithIndexes = separateNumbers(currentElement);
            findValidNumbers(schematicList.indexOf(currentElement), numbersWithIndexes, currentElement, previousElement, nextElement, symbolPattern2);

/*            System.out.println("Index: " + currentIndex +
                    ", Current Element: " + currentElement +
                    ", Previous Element: " + previousElement +
                    ", Next Element: " + nextElement);*/
        }
    }

    void findValidNumbers(int linePos, List<String> numbersWithIndexes, String currentElement, String previousElement, String nextElement, String symbolPattern) {
        numbersWithIndexes.forEach(item -> {
            String[] str = item.split(",");
            int num = Integer.parseInt(str[0]);
            int startIndex = Integer.parseInt(str[1]);
            int endIndex = Integer.parseInt(str[2]);

            boolean valid = false;

            valid = findSymbols(currentElement.substring(startIndex-1, endIndex+2), symbolPattern);
            if (valid) {
                findPairAndAdd(currentElement, linePos, startIndex, endIndex, num);
            }

            if (previousElement != null && !valid) {
                valid = findSymbols(previousElement.substring(startIndex-1, endIndex+2), symbolPattern);
                if (valid) {
                    findPairAndAdd(previousElement, linePos-1, startIndex, endIndex, num);
                }
            }

            if (nextElement != null && !valid) {
                valid = findSymbols(nextElement.substring(startIndex-1, endIndex+2), symbolPattern);
                if (valid) {
                    findPairAndAdd(nextElement, linePos+1, startIndex, endIndex, num);
                }
            }

            if (valid)
                validNumbers.add(num);
        });
    }

    void iteratePairMultiplyAdd() {
        int sum = 0;
        for (Map.Entry<String, ArrayList<Integer>> entry : pair.entrySet()) {
            System.out.println(entry.getKey() +" - "+ entry.getValue());
            List<Integer> l = entry.getValue();
            int product = 0;
            if (l.size() > 1) {
                System.out.println("value - "+l);
                product = l.get(0) * l.get(1);
            }
            sum = sum + product;
        }
        System.out.println("sum is ="+sum);
    }

    private void findPairAndAdd(String line, int linePos, int startIndex, int endIndex, int num) {
        int charPos = startIndex - 1 + line.substring(startIndex-1, endIndex+2).indexOf('*');
        String key = linePos + "-" + charPos;
        ArrayList<Integer> l = pair.getOrDefault(key, new ArrayList<>());
        l.add(num);
        pair.put(key, l);
    }


    static List<String> separateNumbers(String inputString) {
        List<String> numberswithindexes = new ArrayList<>();
        char[] characters = inputString.toCharArray();

        for (int i = 0; i < characters.length; i++) {
            if (Character.isDigit(characters[i])) {
                int startIndex = i;

                // Find the end index of the number
                while (i < characters.length && Character.isDigit(characters[i])) {
                    i++;
                }

                int endIndex = i - 1;

                // Extract and print the number along with its index positions
                String number = inputString.substring(startIndex, endIndex + 1);
                String entry = number+","+startIndex+","+endIndex;
                numberswithindexes.add(entry);
                //System.out.println("Number: " + number + ", Start Index: " + startIndex + ", End Index: " + endIndex);
            }
        }
        return numberswithindexes;
    }

    static boolean findSymbols(String inputString, String symbolPattern) {
        Pattern pattern = Pattern.compile(symbolPattern);
        Matcher matcher = pattern.matcher(inputString);

        System.out.println("Symbols found:");

        while (matcher.find()) {
            System.out.println("Symbol: " + matcher.group() + ", Index: " + matcher.start());
            return true;
        }
        return false;
    }

    @Override
    public String processLine(String s) {
        schematicList.add("."+s+".");
        return null;
    }
}
