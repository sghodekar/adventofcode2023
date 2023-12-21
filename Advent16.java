package com.example;

import java.util.LinkedList;

public class Advent16 extends FileProcessor{

    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    static final int UP=1;
    static final int DOWN=2;
    static final int LEFT=3;
    static final int RIGHT=4;

    @Override
    public String processLine(String s) {

        return null;
    }

    public static void main(String[] args) {
        Advent16 o = new Advent16();

        Maze maze = new Maze();
        maze.initializeMaze("advent16_input.txt");

        // part1
        Coordinate start = new Coordinate(0, 0, maze.getValue(0, 0));
        part1(maze, start);
        System.out.println("Part 1 ="+printmaze(maze));
        maze.cleanup();
        part2(maze);

    }

    static void part2(Maze maze) {

        int maxenergized = 0;
        for (int i = 0; i < maze.getHeight(); i++) {
            Coordinate leftParent = new Coordinate(i, -1, maze.getValue(i, 0));
            Coordinate left = new Coordinate(i, 0, maze.getValue(i, 0), leftParent);
            part1(maze, left);
            int c = printmaze(maze);
            if (c > maxenergized) maxenergized = c;
            maze.cleanup();
        }

        for (int i = 0; i < maze.getHeight(); i++) {
            Coordinate rightParent = new Coordinate(i, maze.getWidth(), maze.getValue(i, maze.getWidth() - 1));
            Coordinate right = new Coordinate(i, maze.getWidth() - 1, maze.getValue(i, maze.getWidth() - 1), rightParent);
            part1(maze, right);
            int c = printmaze(maze);
            if (c > maxenergized) maxenergized = c;
            maze.cleanup();
        }

        for (int i = 0; i < maze.getWidth(); i++) {
            Coordinate upParent = new Coordinate(-1, i, maze.getValue(0, i));
            Coordinate up = new Coordinate(0, i, maze.getValue(0, i), upParent);
            part1(maze, up);
            int c = printmaze(maze);
            if (c > maxenergized) maxenergized = c;
            maze.cleanup();
        }


        for (int i = 0; i < maze.getWidth(); i++) {
            Coordinate downParent = new Coordinate(maze.getHeight(), i, maze.getValue(maze.getHeight() - 1, i));
            Coordinate down = new Coordinate(maze.getHeight() - 1, i, maze.getValue(maze.getHeight() - 1, i), downParent);
            part1(maze, down);
            int c = printmaze(maze);
            if (c > maxenergized) maxenergized = c;
            maze.cleanup();
        }
        System.out.println("max = "+maxenergized);
    }


    static long part1(Maze maze, Coordinate start) {
        long value = 0;

        LinkedList<Coordinate> nextToVisit = new LinkedList<>();
        nextToVisit.add(start);

        while (!nextToVisit.isEmpty()) {
            Coordinate cur = nextToVisit.remove();

            if (!maze.isValidLocation(cur.getX(), cur.getY())) {
                //System.out.println("path = "+backtrackPath(cur).size());
                continue;
            }

            if (maze.getValue(cur.getX(), cur.getY()) == '.') {

                    //move right
                if (cur.getX() == cur.getParent().getX() && cur.getY() > cur.getParent().getY()) {
                    nextToVisit.add(right(cur, maze));
                }
                    //move left
                if (cur.getX() == cur.getParent().getX() && cur.getY() < cur.getParent().getY()) {
                    nextToVisit.add(left(cur, maze));
                }

                //move up
                if (cur.getY() == cur.getParent().getY() && cur.getX() < cur.getParent().getX()) {
                    nextToVisit.add(up(cur, maze));
                }

                //move down
                if (cur.getY() == cur.getParent().getY() && cur.getX() > cur.getParent().getX()) {
                    nextToVisit.add(down(cur, maze));
                }

            }

            if (maze.getValue(cur.getX(), cur.getY()) == '|') {
                //move up and down
                if (cur.getX() == cur.getParent().getX()) {
                    nextToVisit.add(up(cur, maze));
                    nextToVisit.add(down(cur, maze));
                }

                //move up
                if (cur.getY() == cur.getParent().getY() && cur.getX() < cur.getParent().getX()) {
                    nextToVisit.add(up(cur, maze));
                }

                //move up
                if (cur.getY() == cur.getParent().getY() && cur.getX() > cur.getParent().getX()) {
                    nextToVisit.add(down(cur, maze));
                }
            }

            if (maze.getValue(cur.getX(), cur.getY()) == '-') {
                //move up and down
                if (cur.getY() == cur.getParent().getY()) {
                    nextToVisit.add(left(cur, maze));
                    nextToVisit.add(right(cur, maze));
                }

                //move left
                if (cur.getX() == cur.getParent().getX() && cur.getY() < cur.getParent().getY()) {
                    nextToVisit.add(left(cur, maze));
                }

                //move right
                if (cur.getX() == cur.getParent().getX() && cur.getY() > cur.getParent().getY()) {
                    nextToVisit.add(right(cur, maze));
                }
            }

            if (maze.getValue(cur.getX(), cur.getY()) == '/') {

                //move up
                if (cur.getX() == cur.getParent().getX() && cur.getY() > cur.getParent().getY()) {
                    if (maze.visiteddir[cur.getX()][cur.getY()][UP] )
                        continue;
                    nextToVisit.add(up(cur, maze));
                }

                //move down
                if (cur.getX() == cur.getParent().getX() && cur.getY() < cur.getParent().getY()) {
                    if (maze.visiteddir[cur.getX()][cur.getY()][DOWN] )
                        continue;
                    nextToVisit.add(down(cur, maze));
                }

                //move left
                if (cur.getX() > cur.getParent().getX() && cur.getY() == cur.getParent().getY()) {
                    if (maze.visiteddir[cur.getX()][cur.getY()][LEFT] )
                        continue;
                    nextToVisit.add(left(cur, maze));
                }

                //move right
                if (cur.getX() < cur.getParent().getX() && cur.getY() == cur.getParent().getY()) {
                    if (maze.visiteddir[cur.getX()][cur.getY()][RIGHT] )
                        continue;
                    nextToVisit.add(right(cur, maze));
                }
            }

            if (maze.getValue(cur.getX(), cur.getY()) == '\\') {
                if (cur.getParent() == null) {
                    nextToVisit.add(down(cur, maze));
                } else {

                    //move down
                    if (cur.getX() == cur.getParent().getX() && cur.getY() > cur.getParent().getY()) {
                        if (maze.visiteddir[cur.getX()][cur.getY()][DOWN] )
                            continue;
                        nextToVisit.add(down(cur, maze));
                    }

                    //move up
                    if (cur.getX() == cur.getParent().getX() && cur.getY() < cur.getParent().getY()) {
                        if (maze.visiteddir[cur.getX()][cur.getY()][UP] )
                            continue;
                        nextToVisit.add(up(cur, maze));
                    }

                    //move right
                    if (cur.getX() > cur.getParent().getX() && cur.getY() == cur.getParent().getY()) {
                        if (maze.visiteddir[cur.getX()][cur.getY()][RIGHT] )
                            continue;
                        nextToVisit.add(right(cur, maze));
                    }

                    //move left
                    if (cur.getX() < cur.getParent().getX() && cur.getY() == cur.getParent().getY()) {
                        if (maze.visiteddir[cur.getX()][cur.getY()][LEFT] )
                            continue;
                        nextToVisit.add(left(cur, maze));
                    }
                }
            }
            maze.setVisited(cur.getX(), cur.getY(), true);
        }

        return value;
    }

    static Coordinate up(Coordinate cur, Maze maze) {
        maze.visiteddir[cur.getX()][cur.getY()][UP] = true;
        return new Coordinate(cur.getX() -1, cur.getY() , maze.getValue(cur.getX() -1 , cur.getY()), cur);
    }

    static Coordinate down(Coordinate cur, Maze maze) {
        maze.visiteddir[cur.getX()][cur.getY()][DOWN] = true;
        return new Coordinate(cur.getX() +1, cur.getY() , maze.getValue(cur.getX() +1 , cur.getY()), cur);
    }

    static Coordinate left(Coordinate cur, Maze maze) {
        maze.visiteddir[cur.getX()][cur.getY()][LEFT] = true;
        return new Coordinate(cur.getX() , cur.getY() -1  , maze.getValue(cur.getX() , cur.getY() -1), cur);
    }

    static Coordinate right(Coordinate cur, Maze maze) {
        maze.visiteddir[cur.getX()][cur.getY()][RIGHT] = true;
        return new Coordinate(cur.getX(), cur.getY() + 1 , maze.getValue(cur.getX(), cur.getY() + 1), cur);
    }

    public static int printmaze(Maze maze) {
        int c=0;
        for (int x=0; x< maze.getHeight();x++){
            for (int y=0; y< maze.getWidth();y++){
                if (maze.isExplored(x,y)) {
                    //System.out.print("#");
                    c++;
                }
                /*else
                    System.out.print(".");*/
            }
           // System.out.println();
        }
        //System.out.println("Height = "+maze.getHeight() + " Width ="+ maze.getWidth());
        //System.out.println("part 1="+c);
        return c;
    }

}
