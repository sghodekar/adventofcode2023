package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Advent1sumOfCalibrationValues {

    static Integer findFirstNumber(String s) {
        int i = 0;
        while (i < s.length() && !Character.isDigit(s.charAt(i))) {
            i++;
        }

        if (i < s.length()) {
            return Integer.parseInt(s.substring(i,i+1));
        }
        return null;
    }

    static Integer findLastNumber(String s) {
        int i = s.length();
        while (i > 0 && !Character.isDigit(s.charAt(i-1))) {
            i--;
        }

        if (i >= 0) {
            return Integer.parseInt(s.substring(i-1,i));
        }
        return null;
    }

    static int calibratedNumber(int firstNumber, int lastNumber) {
        return firstNumber * 10 + lastNumber;
    }

    static String readLine() throws IOException {
        // File path is passed as parameter
        File file = new File("D:\\Santosh\\loancalculator\\src\\main\\resources\\advent1_input.txt");

        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)

        // Creating an object of BufferedReader class
        BufferedReader br   = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String st;
        // Condition holds true till
        // there is character in a string
        int c = 0;

        while ((st = br.readLine()) != null) {
            System.out.print(st + "          ");
            st = replaceFirstWithNumber(st);
            st = replaceLastWithNumber(st);
            System.out.println(st);
            c= c +calibratedNumber(findFirstNumber(st), findLastNumber(st));
        }
        System.out.println(c);
        return null;
    }

    static String replaceFirstWithNumber(String s) {
        List<String> numbers = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight",  "nine", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Map<String, String> wordtonumber= Map.of("one", "1", "two", "2", "three", "3", "four", "4", "five", "5", "six", "6", "seven", "7",
                "eight", "8", "nine", "9");

        int idx = s.length();
        int len = 0;
        for (String num : numbers) {
            if (s.contains(num)) {
                if (s.indexOf(num) < idx) {
                    idx = s.indexOf(num);
                    len = num.length();
                };
            }
        }

        if (idx < s.length() && len > 1) {
            String subString = s.substring(idx, idx + len);
            s = s.replaceFirst(subString, wordtonumber.get(subString));
        }
/*        for (Map.Entry<String, String> e : wordtonumber.entrySet()) {
            if (s.contains(e.getKey())) {
                s = s.replaceAll(e.getKey(), e.getValue());
            }
        }*/

        return s;
    }

    static String reverse(String s) {
        StringBuilder sb = new StringBuilder().append(s);
        return sb.reverse().toString();
    }

    static String replaceLastWithNumber(String s) {
        List<String> numbers = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight",  "nine", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Map<String, String> wordtonumber= Map.of("one", "1", "two", "2", "three", "3", "four", "4", "five", "5", "six", "6", "seven", "7",
                "eight", "8", "nine", "9");

        s = reverse(s);
        int idx = s.length();
        int len = 0;
        for (String num : numbers) {
            String revnum = reverse(num);
            if (s.contains(revnum)) {
                if (s.indexOf(revnum) < idx) {
                    idx = s.indexOf(revnum);
                    len = revnum.length();
                };
            }
        }

        if (idx < s.length() && len > 1) {
            String subString = s.substring(idx, idx + len);
            s = s.replaceFirst(subString, wordtonumber.get(reverse(subString)));
        }
/*        for (Map.Entry<String, String> e : wordtonumber.entrySet()) {
            if (s.contains(e.getKey())) {
                s = s.replaceAll(e.getKey(), e.getValue());
            }
        }*/

        return reverse(s);
    }

    public static void main(String[] str) throws IOException {
        readLine();
    }
}
