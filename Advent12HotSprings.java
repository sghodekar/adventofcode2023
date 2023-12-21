package com.example;

import java.util.*;

class Key {
    public Key(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    int i;
    int j;
    int k;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return i == key.i && j == key.j && k == key.k;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, k);
    }
}

public class Advent12HotSprings extends FileProcessor {

    List<String> inputList = new ArrayList<>();

    static Map<Key, Long> dp = new HashMap<Key, Long>();

    public static void main(String[] args) {
        List<String> inputs = Arrays.asList("???.### 1,1,3",
                ".??..??...?##. 1,1,3",
                "?#?#?#?#?#?#?#? 1,3,1,6",
                "????.#...#... 4,1,1",
                "????.######..#####. 1,6,5",
                "?###???????? 3,2,1");

        Advent12HotSprings hs = new Advent12HotSprings();
        hs.readFile("advent12_input.txt");

        List<String> newInputs = getFiveFold(hs.inputList);

        long sum = 0;
        for (int i = 0; i<newInputs.size(); i++) {
            sum = sum + part1(newInputs.get(i));
            dp.clear();
        }
        System.out.println(sum);
    }

    static List<String> getFiveFold(List<String> inputs) {
        List<String> newInputs = new ArrayList<>();
        inputs.stream().forEach(input -> {
            String[] parts = input.split("\\s+");
            String newInput = parts[0] + '?' + parts[0]+ '?' + parts[0]+ '?' + parts[0]+ '?' + parts[0] +" "+parts[1] + "," + parts[1] + "," + parts[1] + "," + parts[1]+ "," + parts[1];
            //String newInput = parts[0] + '?' + parts[0]+" "+parts[1] + "," + parts[1];
            newInputs.add(newInput);
        });
        return newInputs;
    }

    public static String findNonRepetitiveHashSubstringsWithoutDotsOrdered(String input) {
        List<String> nonRepetitiveSubstrings = new LinkedList<>();
        int length = input.length();
        StringBuilder contiguousGroup = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '#') {
                sb.append('#');
            } else {
                if (i!=0 && input.charAt(i-1) == '#') {
                    nonRepetitiveSubstrings.add(sb.toString());
                    contiguousGroup.append(sb.length()).append(",");
                    sb = new StringBuilder();
                }
            }
        }
        if (sb.length() > 0) {
            nonRepetitiveSubstrings.add(sb.toString());
            contiguousGroup.append(sb.length());
        } else {
            if (contiguousGroup.length() > 0)
                contiguousGroup.deleteCharAt(contiguousGroup.length()-1);
        }

        return contiguousGroup.toString();
    }

    static boolean isValidSpring(String calculated, String contiguousInput) {
        String contiguousCalculated = findNonRepetitiveHashSubstringsWithoutDotsOrdered(calculated);
        return contiguousInput.equals(contiguousCalculated);
    }

    static boolean isValid(String springs, String groups) {
        return true;
    }

    static void appendInAll(List<StringBuilder>totalArrangements, char c) {
        totalArrangements.stream().forEach(sb -> sb.append(c));
    }

    static long checkPossibility(String springs, String groups, int i)
    {
        if (i == springs.length())
            return (isValidSpring(springs, groups) ? 1 : 0);

        if (springs.charAt(i) == '?') {
            return checkPossibility(springs.substring(0, i) + "#" + springs.substring(i + 1), groups, i + 1) +
                    checkPossibility(springs.substring(0, i) + "." + springs.substring(i + 1), groups, i + 1);
        } else
            return checkPossibility(springs, groups, i+1);
    }

    // i = current position within dots
    // bi = current poisition within blocks or group
    // current = length of current block of #
    static long checkPossibility2(String springs, int[] groups, int i, int bi, int current)
    {
        Key key = new Key(i, bi, current);
        if (dp.containsKey(key)) {
            return dp.get(key);
        }
        if (i == springs.length()) {
            if (bi == groups.length && current == 0) {
                return 1l;
            } else {
                if (bi == groups.length -1 && groups[bi] == current) {
                    return 1l;
                }
                else
                    return 0;
            }
        }

        long ans = 0;

        for (char c : new char[] { '.', '#' }) {
            if (springs.charAt(i) == c || springs.charAt(i) == '?'){
                if (c == '.' && current == 0) {
                    ans += checkPossibility2(springs, groups, i+1, bi, 0);
                } else {
                    if (c == '.' && current > 0 && bi < groups.length && groups[bi] == current) {
                        ans += checkPossibility2(springs, groups, i+1, bi+1, 0);

                    } else if (c == '#') {
                        ans +=  checkPossibility2(springs, groups, i+1, bi, current+1);
                    }
                }
            }
        }

        dp.put(key, ans);

        return ans;

    }

    static long part1(String input) {
        String[] parts = input.split("\\s+");
        System.out.println(parts[0] + "           " + parts[1]);

        String[] noOfContigousGrups = parts[1].split(",");
        int[] groups = convertStringToIntArray(parts[1]);
        long p = checkPossibility2(parts[0].trim(), groups, 0, 0, 0);
        System.out.println(p);
        return p;
    }

    public static int[] convertStringToIntArray(String input) {
        String[] stringArray = input.split(",");
        int[] intArray = new int[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            // Parse each substring to an integer
            intArray[i] = Integer.parseInt(stringArray[i].trim());
        }

        return intArray;
    }

/*
        System.out.println("noOfContigousGrups = "+ Arrays.asList(noOfContigousGrups));

        List<Character> chList = convertStringToArrayList(parts[0]);
        AtomicInteger possibleArrangements = new AtomicInteger();
        AtomicInteger counter = new AtomicInteger();
        List<StringBuilder> totalArrangements = new ArrayList<>();

        for (int i=0; i < noOfContigousGrups.length;i++) {
            int g = Integer.parseInt(noOfContigousGrups[i]);

            for(int j= counter.get(); j< chList.size(); j++) {
                char ch = chList.get(j);
                if (ch == '.') {
                    appendInAll(totalArrangements, c);
                    counter.getAndIncrement();
                    break;
                }
            }

            for (int c = 0; c < g; c++) {

            }



            for(int j= counter.get(); j< chList.size(); j++) {
                char ch = chList.get(j);
                System.out.println("ch = "+ch);
                if (ch == '.') {
                    counter.getAndIncrement();
                    break;
                }

                boolean flag = true;
                for (int c = 0; c < g; c++) {
                    if (counter.get() + c >= chList.size()) {
                        flag = false;
                        break;
                    }

                    if (parts[0].charAt(counter.get() + c) == '#') {
                        continue;
                    }
                    if (parts[0].charAt(counter.get() + c) == '?'){
                        if (counter.get() + c -1 > -1 && parts[0].charAt(counter.get() + c -1) == '#' )  {
                            flag = false;
                            break;
                        }
                        continue;
                    }
                }
                if (flag && (counter.get() + g < parts[0].length() || parts[0].charAt(g) == '?' || parts[0].charAt(g) == '.')) {
                    possibleArrangements.getAndIncrement();
                }
                counter.getAndIncrement();
                if (counter.get() < parts[0].length() && ch != parts[0].charAt(counter.get()))
                    break;
            }
        }

        System.out.println("possibleArrangements = "+possibleArrangements.get());
        System.out.println();
    }*/

    public static ArrayList<Character> convertStringToArrayList(String input) {
        ArrayList<Character> charList = new ArrayList<>();

        // Check for null or empty string
        if (input == null || input.isEmpty()) {
            return charList;
        }

        // Convert string to ArrayList of characters
        for (char c : input.toCharArray()) {
            charList.add(c);
        }

        return charList;
    }

    @Override
    public String processLine(String s) {
        inputList.add(s);
        return null;
    }
}
