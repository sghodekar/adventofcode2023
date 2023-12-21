package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Maze extends FileProcessor {

    char[][] maze;
    char[][] maze2;
    boolean[][] visited;
    private boolean[][] visitedfrom;
    boolean[][][] visiteddir;
    private Coordinate start;
    private Coordinate end;
    private String fileText;
    List<String> lines = new ArrayList<>();

    public void initializeMaze(String filename) {
        readFile(filename);

        maze = new char[lines.size()][lines.get(0).length()];
        visited = new boolean[lines.size()][lines.get(0).length()];
        visitedfrom = new boolean[lines.size()][lines.get(0).length()];
        visiteddir = new boolean[lines.size()][lines.get(0).length()][5];

        for (int row = 0; row < getHeight(); row++) {
            if (lines.get(row).length() != getWidth()) {
                throw new IllegalArgumentException("line " + (row + 1) + " wrong length (was " + lines.get(row).length() + " but should be " + getWidth() + ")");
            }

            for (int col = 0; col < getWidth(); col++) {
                maze[row][col] = lines.get(row).charAt(col);
                if (lines.get(row).charAt(col) == 'S') {
                    start = new Coordinate(row, col, lines.get(row).charAt(col));
                    end = start;
                }
            }
        }
        //printmaze();
    }

    void cleanup() {
        visited = new boolean[lines.size()][lines.get(0).length()];
        visitedfrom = new boolean[lines.size()][lines.get(0).length()];
        visiteddir = new boolean[lines.size()][lines.get(0).length()][5];
    }

    HashMap<Dot, String> dotsStatusMap = new HashMap<>();

    int findAllDots(char[][] m) {
        int emptyCount = 0;
        for (int x=0; x<getHeight(); x++) {
            for (int y=0; y<getWidth(); y++) {
                if (m[x][y] == '.') {
                    emptyCount++;
                    Dot d = new Dot(x, y);
                    dotsStatusMap.put(d, Enclosed);
                }
            }
        }
        System.out.println("dot count: "+emptyCount);
        return emptyCount;
    }

    private boolean[][] enclosed;

    static String Enclosed = "Enclosed";
    static String NotEnclosed = "NotEnclosed";

    int findAllNotEnclosed() {
        AtomicInteger count = new AtomicInteger();

        dotsStatusMap.forEach((key, value) -> {
            if (!isEnclosed(key)) {
                count.getAndIncrement();
                dotsStatusMap.put(key, NotEnclosed);
            }
        });
        return count.get();
    }

    boolean isEnclosed(Dot d) {
        int x = d.x, y = d.y;
        if (x == 0 || x == (getHeight() -1)) {
            return false;
        }
        if (y == 0 || y == (getWidth() -1)) {
            return false;
        }

        Dot adjcentDot = new Dot(x-1, y);
        if (NotEnclosed.equals(dotsStatusMap.get(adjcentDot)))
            return false;

        adjcentDot = new Dot(x+1, y);
        if (NotEnclosed.equals(dotsStatusMap.get(adjcentDot)))
            return false;

        adjcentDot = new Dot(x, y-1);
        if (NotEnclosed.equals(dotsStatusMap.get(adjcentDot)))
            return false;

        adjcentDot = new Dot(x, y+1);
        if (NotEnclosed.equals(dotsStatusMap.get(adjcentDot)))
            return false;

        return true;
    }

    public int getHeight() {
        return maze.length;
    }

    public int getWidth() {
        return maze[0].length;
    }

    public Coordinate getEntry() {
        return start;
    }

    public Coordinate getExit() {
        return end;
    }

    public boolean isExit(int x, int y) {
        return x == end.getX() && y == end.getY();
    }

    public boolean isStart(int x, int y) {
        return x == start.getX() && y == start.getY();
    }

    public boolean isExplored(int row, int col) {
        return visited[row][col];
    }

    public boolean isExploredWithParent(int row, int col, int prow, int pcol) {
        return visited[row][col] && visitedfrom[prow][pcol];
    }

    public boolean isWall(int row, int col) {
        return maze[row][col] == '.';
    }

    public void setVisited(int row, int col, boolean value) {
        visited[row][col] = value;
    }

    public void setVisitedFrom(int row, int col, boolean value) {
        visitedfrom[row][col] = value;
    }

    public boolean isValidLocation(int row, int col) {
        if (row < 0 || row >= getHeight() || col < 0 || col >= getWidth()) {
            return false;
        }
        return true;
    }

    public char getValue(int x, int y) {
        if (isValidLocation(x, y))
            return maze[x][y];
        return '.';
    }

    public void printmaze(char[][] m) {
        for (int x=0; x<getHeight();x++){
            for (int y=0; y<getWidth();y++){
                System.out.print(m[x][y]);
            }
            System.out.println("");
        }
    }
/*    public void printPath(List<Coordinate> path) {
        int[][] tempMaze = Arrays.stream(maze)
                .map(int[]::clone)
                .toArray(int[][]::new);

        for (Coordinate coordinate : path) {
            if (isStart(coordinate.getX(), coordinate.getY()) || isExit(coordinate.getX(), coordinate.getY())) {
                continue;
            }
            tempMaze[coordinate.getX()][coordinate.getY()] = PATH;
        }
        System.out.println(toString(tempMaze));
    }*/

/*    public String toString(int[][] maze) {
        StringBuilder result = new StringBuilder(getWidth() * (getHeight() + 1));
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                if (maze[row][col] == ROAD) {
                    result.append(' ');
                } else if (maze[row][col] == WALL) {
                    result.append('#');
                } else if (maze[row][col] == START) {
                    result.append('S');
                } else if (maze[row][col] == EXIT) {
                    result.append('E');
                } else {
                    result.append('.');
                }
            }
            result.append('\n');
        }
        return result.toString();
    }*/

    public void reset() {
        for (int i = 0; i < visited.length; i++)
            Arrays.fill(visited[i], false);
    }

    @Override
    public String processLine(String s) {
        lines.add(s);
        return null;
    }

    void crateMaze2(List<Coordinate> path) {
        maze2 = new char[getHeight()][getWidth()];
        for(int x=0; x < getHeight(); x++) {
            for(int y=0; y < getWidth(); y++) {
                if (path.contains(new Coordinate(x, y, maze[x][y]))) {
                    maze2[x][y] = maze[x][y];
                } else {
                    maze2[x][y] = '.';
                }
            }
        }
        printmaze(maze2);
    }

    char[][] maze3;
    void addAlternateRowAndColumn2() {
        maze3 = new char[getHeight()*2][getWidth()*2];
        for(int x=0; x < getHeight()*2; x++) {
            for(int y=0; y < getWidth()*2; y++) {
                if (x%2 == 1) {
                    maze3[x][y] = '.';
                    continue;
                }
                if (y%2 == 1) {
                    maze3[x][y] = '.';
                }
                else
                    maze3[x][y] = maze2[x/2][y/2];
            }
        }
        System.out.println("Printing maze 3 ");
        for (int x=0; x<getWidth()*2;x++){
            for (int y=0; y<getWidth()*2;y++){
                System.out.print(maze3[x][y]);
            }
            System.out.println("");
        }
    }

    void completeLoopForMaze3() {
        for(int x=0; x < getHeight()*2-1; x++) {
            for (int y = 1; y < getWidth() * 2-1; y++) {
                if (maze3[x][y] == '.') {
                    if ((maze3[x][y-1] == 'L' || maze3[x][y-1] == '-' || maze3[x][y-1] == 'F' || maze3[x][y-1] == 'S') &&
                        (maze3[x][y+1] == '7' || maze3[x][y+1] == '-' || maze3[x][y+1] == 'J' || maze3[x][y+1] == 'S')) {
                        maze3[x][y] = '-';
                    }
                    if ((x!=0) && (maze3[x-1][y] == '7' || maze3[x-1][y] == '|' || maze3[x-1][y] == 'F' || maze3[x-1][y] == 'S') &&
                        (maze3[x+1][y] == 'L' || maze3[x+1][y] == '|' || maze3[x+1][y] == 'J' || maze3[x+1][y] == 'S')) {
                        maze3[x][y] = '|';
                    }
                }
            }
        }
        System.out.println("Printing maze 3 after looping");
        for (int x=0; x<getWidth()*2;x++){
            for (int y=0; y<getWidth()*2;y++){
                System.out.print(maze3[x][y]);
            }
            System.out.println("");
        }
    }
}

