package com.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class SPoint {
    int x, y;
    int n = 0;

    public SPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SPoint SPoint = (SPoint) o;
        return x == SPoint.x && y == SPoint.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "SPoint{" +
                "x=" + x +
                ", y=" + y +
                ", n=" + n +
                '}';
    }

}

public class Advent23LongWalk extends FileProcessor{
    @Override
    public String processLine(String s) {
        lines.add(s);
        return null;
    }

    List<String> lines = new ArrayList<>();

    char graph[][];
    boolean visited[][];
    int startX;
    int startY;
    int endX;
    int endY;
    List<SPoint> SPoints = new ArrayList<>();
    SPoint startp;
    SPoint endp;
    public void initializeGraph(String filename) throws UnsupportedEncodingException {
        readFile(filename);

        graph = new char[lines.size()][lines.get(0).length()];
        visited = new boolean[lines.size()][lines.get(0).length()];

        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                graph[row][col] = lines.get(row).charAt(col);
            }
        }
        startX = 0;
        startY = lines.get(0).indexOf('.');
        endX = lines.size()-1;
        endY = lines.get(lines.size()-1).indexOf('.');
        startp = new SPoint(startX, startY);
        endp = new SPoint(endX, endY);
        SPoints.add(startp);
        SPoints.add(endp);

        directionsMap = new HashMap<>();
        directionsMap.put('.', DIRECTIONS);
        directionsMap.put('<', LEFT);
        directionsMap.put('>', RIGHT);
        directionsMap.put('^', UP);
        directionsMap.put('v', DOWN);

        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), "UTF-8"), 512));

    }

    private static final int[][] DIRECTIONS = {  { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
    static final int[][] LEFT = {  { 0, -1 }};
    static int[][] RIGHT = { { 0, 1 }};
    static int[][] UP = {{ -1, 0 }};
    static int[][] DOWN = {{ 1, 0 }};
    Map<Character, int[][]> directionsMap;

    public static void main(String[] args) throws UnsupportedEncodingException {
        Advent23LongWalk lw = new Advent23LongWalk();
        lw.initializeGraph("advent23_input.txt");
        System.out.println(lw.startX + "  "+lw.startY);
        lw.part1();
    }

    Map<SPoint, LinkedHashMap<SPoint, Integer>> adjacencyMatrix = new LinkedHashMap<>();

    void part1() {
        //edge contraction

        for(int x=0; x < graph.length; x++) {
            for(int y=0; y < graph[0].length; y++) {
                if (graph[x][y] == '#')
                    continue;

                int neighbors = 0;
                for(int[] dir : DIRECTIONS) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (0 < nx && nx < graph.length &&
                        0 < ny && ny< graph[0].length &&
                        graph[nx][ny] != '#') {
                        neighbors += 1;
                    }
                }
                if (neighbors >= 3){
                    SPoint p = new SPoint(x, y);
                    SPoints.add(p);
                }
            }
        }

        for(SPoint p : SPoints) {
            adjacencyMatrix.put(p, new LinkedHashMap<>());
            Stack<SPoint> stack = new Stack<>();
            Set<SPoint> seen1 = new HashSet<>();
            seen1.add(p);
            stack.push(p);

            while(!stack.isEmpty()) {
                SPoint cur = stack.pop();

                if (cur.n != 0 && SPoints.contains(cur)){
                    LinkedHashMap<SPoint, Integer> e = adjacencyMatrix.get(p);
                    e.put(cur, cur.n);
                    adjacencyMatrix.put(p, e);
                    continue;
                }

                /* Part1
                for(int[] dir : directionsMap.get(graph[cur.x][cur.y])){*/
                //Part 2
                for(int[] dir : DIRECTIONS){
                    int nx = cur.x + dir[0];
                    int ny = cur.y + dir[1];
                    SPoint np = new SPoint(nx, ny);
                    if (nx >= 0 && nx < graph.length &&
                        ny >= 0 && ny < graph[0].length &&
                        graph[nx][ny] !='#' &&
                        !seen1.contains(np)) {
                        np.n = cur.n + 1;
                        stack.push(np);
                        seen1.add(np);
                    }
                }
            }
        }
        System.out.println(dfs(startp));
    }

    Set<SPoint> seen = new HashSet<>();
    PrintWriter out;
    int dfs(SPoint p) {
        if (p.equals(endp)) {
            return 0;
        }

        AtomicInteger d = new AtomicInteger(Integer.MIN_VALUE);

        Map<SPoint, Integer> np = adjacencyMatrix.get(p);
        if (np!= null) {
            seen.add(p);
            np.forEach((key, value) -> {
                if (!seen.contains(key)) {
                    d.set(Math.max(d.get(), dfs(key) + value));
                    System.out.println("("+key.x +", "+ key.y+") "+value+" "+d.get());
                }
            });
            seen.remove(p);
        }

        return d.get();
    }
}
