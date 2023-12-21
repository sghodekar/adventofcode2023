package com.example;

import java.util.*;

public class Advent17ClumsyCrucibles extends FileProcessor{

    private static final int INF = Integer.MAX_VALUE;

    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    public static void dijkstra(int[][] graph) {
        int vertices = graph.length;
        int[] distance = new int[vertices];
        Arrays.fill(distance, INF);

        LinkedList<Coordinate> nextToVisit = new LinkedList<>();
        Coordinate start = new Coordinate(0, 0, 0 , 0);
        nextToVisit.add(start);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Node(0, 0, 0, 0, 0,0));

        HashSet<Node> visited = new HashSet<>();

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            if (current.x == graph.length -1 && current.y == graph[0].length-1) {
                System.out.println(current.distance);
                return;
            }

            if(visited.contains(current))
                continue;

            //if (current.directionCount < 3 && !(current.directionx==0 && current.directiony==0)) {
            if ( current.directionCount < 10 && !(current.directionx==0 && current.directiony==0)) {
                int nextx = current.x + current.directionx;
                int nexty = current.y + current.directiony;
                if (0<= nextx && nextx < graph.length && 0 <= nexty && nexty < graph[0].length) {
                    priorityQueue.add(new Node(current.distance + graph[nextx][nexty], nextx, nexty, current.directionx, current.directiony, current.directionCount+1));
                }
            }

            int iterationCount = 0;
            if (current.directionCount >= 4 || (current.directionx==0 && current.directiony==0)) {
                for (int[] direction : DIRECTIONS) {
                    if (!(direction[0] == current.directionx && direction[1] == current.directiony)
                            && !(direction[0] == -1 * current.directionx && direction[1] == -1 * current.directiony)) {
                        int nextx = current.x + direction[0];
                        int nexty = current.y + direction[1];

                        if (0 <= nextx && nextx < graph.length && 0 <= nexty && nexty < graph[0].length) {
                            priorityQueue.add(new Node(current.distance + graph[nextx][nexty], nextx, nexty, direction[0], direction[1], 1));
                        }
                    }
                }
            }

            visited.add(current);

        }

        // Print the shortest distances
        System.out.println("Shortest distances from vertex " + start + ":");
        for (int i = 0; i < vertices; i++) {
            System.out.println("To vertex " + i + ": " + distance[i]);
        }
    }

    public static void main(String[] args) {

        Advent17ClumsyCrucibles cc = new Advent17ClumsyCrucibles();
        cc.initializeGraph("advent17_input.txt");

        dijkstra(cc.graph);
    }

    @Override
    public String processLine(String s) {
        lines.add(s);
        return null;
    }

    List<String> lines = new ArrayList<>();

    static class Node implements Comparable<Node> {
        int distance;
        int x;
        int y;
        int directionx;
        int directiony;
        int directionCount;

        public Node(int distance, int x, int y, int directionx, int directiony, int directionCount) {
            this.distance = distance;
            this.x = x;
            this.y = y;
            this.directionx = directionx;
            this.directiony = directiony;
            this.directionCount = directionCount;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y
                    && directionx == node.directionx
                    && directiony == node.directiony
                    && directionCount == node.directionCount;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, directionx, directiony, directionCount);
        }
    }

    int graph[][];
    public void initializeGraph(String filename) {
        readFile(filename);

        graph = new int[lines.size()][lines.get(0).length()];

        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                graph[row][col] = Integer.parseInt(String.valueOf(lines.get(row).charAt(col)));
            }
        }
    }
}

