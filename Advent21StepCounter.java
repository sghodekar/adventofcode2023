package com.example;

import java.util.*;

class Coord {
    int x;
    int y;
    int noOfStepsRemaining;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(int x, int y, int noOfStepsRemaining) {
        this.x = x;
        this.y = y;
        this.noOfStepsRemaining = noOfStepsRemaining;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class Advent21StepCounter extends FileProcessor{
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
    public void initializeGraph(String filename) {
        readFile(filename);

        graph = new char[lines.size()][lines.get(0).length()];
        visited = new boolean[lines.size()][lines.get(0).length()];

        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                graph[row][col] = lines.get(row).charAt(col);
                if (lines.get(row).charAt(col) == 'S') {
                    startX = row;
                    startY = col;
                }

            }
        }
    }

    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    public static void main(String[] args) {
        Advent21StepCounter sc = new Advent21StepCounter();
        sc.initializeGraph("advent21_input.txt");
        System.out.println(sc.startX + "  "+sc.startY);
        sc.part1(sc.startX, sc.startY, 8);
        sc.part2();
    }

    int part1(int sx, int sy, int n){

        Set<Coord> currentPositions = new HashSet<>();

        currentPositions.add(new Coord(sx, sy, n));

        for (int i = 0; i < n; i++) {
            Set<Coord> newPositions = new HashSet<>();

            for (Coord pos : currentPositions) {
                for (int[] dir : DIRECTIONS) {
                    int newRow = pos.x + dir[0];
                    int newCol = pos.y + dir[1];

                    if (isValid(newRow, newCol, graph) && (graph[newRow][newCol] == '.' || graph[newRow][newCol] == 'S')) {
                        newPositions.add(new Coord(newRow, newCol));
                    }
                }
            }

            currentPositions = newPositions;
        }

        System.out.println(currentPositions.size());
        return currentPositions.size();

    }

    void part1v1(){
        visited[startX][startY] = true;
        Deque<Coord> deque = new ArrayDeque();
        Coord c = new Coord(startX, startY, 65);
        deque.push(c);
        Set<Coord> ans = new HashSet<>();

        while (!deque.isEmpty()) {
            Coord c1 = deque.pollFirst();

            if (c1.noOfStepsRemaining % 2 == 0) {
                ans.add(c1);
            }
            if (c1.noOfStepsRemaining == 0) {
                continue;
            }

            for (int[] dir : DIRECTIONS) {
                int nx = c1.x + dir[0];
                int ny = c1.y + dir[1];
                if (nx < 0 || nx > graph.length-1 ||
                    ny < 0 || ny > graph[0].length-1 ||
                    graph[nx][ny] == '#' ||
                    visited[nx][ny]) {
                    continue;
                }
                visited[nx][ny] = true;
                Coord nc = new Coord(nx, ny, c1.noOfStepsRemaining-1);
                deque.addLast(nc);
            }
        }

        int count = 0;
        for(int x=0; x < graph.length; x++){
            for(int y=0; y < graph[0].length; y++){
                if(visited[x][y])
                    count++;
            }
        }

        System.out.println(ans.size());
    }

    static boolean isValid(int row, int col, char[][] grid) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    void part2() {
        int sr = lines.size()/2;
        int sc = lines.size()/2;
        long steps = 26501365;
        int size = lines.size();
        long grid_width = steps / size - 1;

        double odd = Math.pow((grid_width / 2 * 2 + 1), 2);
        double even = Math.pow(((grid_width + 1) / 2 * 2), 2);

        long odd_points = part1(sr, sc, size * 2 + 1);
        long even_points = part1(sr, sc, size * 2);

        long corner_t = part1(size - 1, sc, size - 1);
        long corner_r = part1(sr, 0, size - 1);
        long corner_b = part1(0, sc, size - 1);
        long corner_l = part1(sr, size - 1, size - 1);

        long small_tr = part1(size - 1, 0, size / 2 - 1);
        long small_tl = part1(size - 1, size - 1, size / 2 - 1);
        long small_br = part1(0, 0, size / 2 - 1);
        long small_bl = part1(0, size - 1, size / 2 - 1);

        long large_tr = part1(size - 1, 0, size * 3 / 2 - 1);
        long large_tl = part1(size - 1, size - 1, size * 3 / 2 - 1);
        long large_br = part1(0, 0, size * 3 / 2 - 1);
        long large_bl = part1(0, size - 1, size * 3 / 2 - 1);

        long ans = (long) (odd * odd_points +
                        even * even_points +
                        corner_t + corner_r + corner_b + corner_l +
                        (grid_width + 1) * (small_tr + small_tl + small_br + small_bl) +
                        grid_width * (large_tr + large_tl + large_br + large_bl));

        System.out.println(ans);

    }
}
