package com.example;

import java.util.*;

class Dot {
    int x;
    int y;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dot dot = (Dot) o;
        return x == dot.x && y == dot.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}

public class Advent10PipeMaze {
    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    public List<Coordinate> solve(Maze maze) {
        LinkedList<Coordinate> nextToVisit = new LinkedList<>();
        Coordinate start = maze.getEntry();

        int count = 0;
        int max = 0;
        Coordinate coordinate1 = new Coordinate(start.getX()+1, start.getY()  , maze.getValue(start.getX()+1 , start.getY()), start);
        nextToVisit.add(coordinate1);

        while (!nextToVisit.isEmpty()) {
            Coordinate cur = nextToVisit.remove();
            //System.out.println(cur.getValue());

            if (!maze.isValidLocation(cur.getX(), cur.getY()) || maze.isExplored(cur.getX(), cur.getY())) {
                //System.out.println("path = "+backtrackPath(cur).size());
                continue;
            }

            if (maze.isWall(cur.getX(), cur.getY())) {
                maze.setVisited(cur.getX(), cur.getY(), true);
                continue;
            }

            if (maze.isExit(cur.getX(), cur.getY()) ) {
                    System.out.println("Path length is " + backtrackPath(cur).size());
                    return backtrackPath(cur);
            }

            for (int[] direction : DIRECTIONS) {
                Coordinate coordinate = null;

                if (maze.getValue(cur.getX(), cur.getY()) == 'S' )
                    //if (!maze.isExplored(cur.getX() + direction[0], cur.getY() + direction[1]))
                    if ((cur.getParent()== null) || (!(cur.getParent().getX() == cur.getX() + direction[0] && cur.getParent().getY() == cur.getY() + direction[1])))
                        coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], maze.getValue(cur.getX() + direction[0], cur.getY() + direction[1]), cur);

                if (maze.getValue(cur.getX(), cur.getY()) == 'L' && ((direction[0] == -1 && direction[1] == 0) || (direction[0] == 0 && direction[1] == 1) )) {
                    //if (!maze.isExplored(cur.getX() + direction[0], cur.getY() + direction[1]))
                    if ((cur.getParent()== null) || (!(cur.getParent().getX() == cur.getX() + direction[0] && cur.getParent().getY() == cur.getY() + direction[1])))
                        coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], maze.getValue(cur.getX() + direction[0], cur.getY() + direction[1]), cur);
                }

                if (maze.getValue(cur.getX(), cur.getY()) == '|' && ((direction[0] == -1 && direction[1] == 0) || (direction[0] == 1 && direction[1] == 0) )) {
                    //if (!maze.isExplored(cur.getX() + direction[0], cur.getY() + direction[1]))
                    if ((cur.getParent()== null) || (!(cur.getParent().getX() == cur.getX() + direction[0] && cur.getParent().getY() == cur.getY() + direction[1])))
                        coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], maze.getValue(cur.getX() + direction[0], cur.getY() + direction[1]), cur);
                }

                if (maze.getValue(cur.getX(), cur.getY()) == '-' && ((direction[0] == 0 && direction[1] == -1) || (direction[0] == 0 && direction[1] == 1) )) {
                    //if (!maze.isExplored(cur.getX() + direction[0], cur.getY() + direction[1]))
                    if ((cur.getParent()== null) || (!(cur.getParent().getX() == cur.getX() + direction[0] && cur.getParent().getY() == cur.getY() + direction[1])))
                        coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], maze.getValue(cur.getX() + direction[0], cur.getY() + direction[1]), cur);
                }

                if (maze.getValue(cur.getX(), cur.getY()) == 'J' && ((direction[0] == 0 && direction[1] == -1) || (direction[0] == -1 && direction[1] == 0) )) {
                    //if (!maze.isExplored(cur.getX() + direction[0], cur.getY() + direction[1]))
                    if ((cur.getParent()== null) || (!(cur.getParent().getX() == cur.getX() + direction[0] && cur.getParent().getY() == cur.getY() + direction[1])))
                        coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], maze.getValue(cur.getX() + direction[0], cur.getY() + direction[1]), cur);
                }

                if (maze.getValue(cur.getX(), cur.getY()) == '7' && ((direction[0] == 0 && direction[1] == -1) || (direction[0] == 1 && direction[1] == 0) )) {
                    //if (!maze.isExplored(cur.getX() + direction[0], cur.getY() + direction[1]))
                    if ((cur.getParent()== null) || (!(cur.getParent().getX() == cur.getX() + direction[0] && cur.getParent().getY() == cur.getY() + direction[1])))
                        coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], maze.getValue(cur.getX() + direction[0], cur.getY() + direction[1]), cur);
                }

                if (maze.getValue(cur.getX(), cur.getY()) == 'F' && ((direction[0] == 0 && direction[1] == 1) || (direction[0] == 1 && direction[1] == 0) )) {
                    //if (!maze.isExplored(cur.getX() + direction[0], cur.getY() + direction[1]))
                    if ((cur.getParent()== null) || (!(cur.getParent().getX() == cur.getX() + direction[0] && cur.getParent().getY() == cur.getY() + direction[1])))
                        coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], maze.getValue(cur.getX() + direction[0], cur.getY() + direction[1]), cur);
                }

                if (coordinate != null)
                    nextToVisit.add(coordinate);

                maze.setVisited(cur.getX(), cur.getY(), true);
            }
        }
        return Collections.emptyList();
    }

    private List<Coordinate> backtrackPath(Coordinate cur) {
        List<Coordinate> path = new ArrayList<>();
        Coordinate iter = cur;

        while (iter != null) {
            path.add(iter);
            System.out.println(iter.getValue());
            iter = iter.parent;
        }

        return path;
    }

    public static void main(String[] args) {
        Advent10PipeMaze pm = new Advent10PipeMaze();
        Maze maze = new Maze();
        maze.initializeMaze("advent10_input.txt");
        List<Coordinate> path = pm.solve(maze);
        //maze.printPath(path);
        System.out.println(path.size());
        int alldots = maze.findAllDots(maze.maze);
        int count = maze.findAllNotEnclosed();
        int prevCount = 0;
        do {
            prevCount = count;
            count = maze.findAllNotEnclosed();
            System.out.println("prevCount ="+ prevCount +", count = "+count);
        } while(prevCount!=count);

        int noteclosedcounter = 0;
        for(Map.Entry<Dot, String> entry: maze.dotsStatusMap.entrySet()) {
            if (Maze.NotEnclosed.equals(entry.getValue())) {
                noteclosedcounter++;
            }
        }

        System.out.println("noteclosedcounter = "+noteclosedcounter);
        System.out.println("Enclosed counter = "+ (alldots - noteclosedcounter));

        maze.crateMaze2(path);
        maze.addAlternateRowAndColumn2();
        maze.completeLoopForMaze3();

        Maze maze2 = new Maze();
        maze2.maze = maze.maze3;
        pm.countEnclosedDots(maze2);


        maze.reset();
    }

    void countEnclosedDots(Maze maze) {
        int alldots = maze.findAllDots(maze.maze);
        int count = maze.findAllNotEnclosed();
        int prevCount = 0;
        do {
            prevCount = count;
            count = maze.findAllNotEnclosed();
            System.out.println("prevCount ="+ prevCount +", count = "+count);
        } while(prevCount!=count);

        int noteclosedcounter = 0;
        int enclosedOrig = 0;
        for(Map.Entry<Dot, String> entry: maze.dotsStatusMap.entrySet()) {
            if (Maze.NotEnclosed.equals(entry.getValue())) {
                noteclosedcounter++;
            }
            if (Maze.Enclosed.equals(entry.getValue()) && (entry.getKey().x%2 ==0) && (entry.getKey().y%2 ==0)) {
                enclosedOrig++;
            }
        }

        System.out.println("noteclosedcounter = "+noteclosedcounter);
        System.out.println("Enclosed counter = "+ (alldots - noteclosedcounter));
        System.out.println("Enclosed original = "+ enclosedOrig);

    }
}

