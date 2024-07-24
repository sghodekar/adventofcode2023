package com.example;

import java.util.*;

class Advent25MinCut extends FileProcessor{
    // Declaring data members
    // V, E, parent, and rank.
    static int V,E;
    static int parent[];
    static int rank[];
    // Random module to get
    // random integer values.
    static Random rand;
    // Constructor

    public Advent25MinCut() {
    }

    void initialize(int V, int E){

        // Initializing data members.
        this.V=V;
        this.E=E;
        parent=new int[V];
        rank=new int[V];
        rand = new Random();
        // Initializing parent's and
        // rank's by is and 0s.
        for(int i=0;i<V;i++)
        {
            parent[i]=i;
            rank[i]=0;
        }
    }
    // Function to find minimum cut.
    static int minCutKarger(Edge edges[]){
        int vertices=V;

        // Iterating till vertices are
        // greater than 2.
        while(vertices>2)
        {
            // Getting a random integer
            // in the range [0, E-1].
            int i=rand.nextInt(E);

            // Finding leader element to which
            // edges[i].u belongs.
            int set1=find(edges[i].u);
            // Finding leader element to which
            // edges[i].v belongs.
            int set2=find(edges[i].v);

            // If they do not belong
            // to the same set.
            if(set1!=set2)
            {
                System.out.println("Contracting vertices "+edges[i].u+" and "+edges[i].v);
                // Merging vertices u and v into one.
                union(edges[i].u,edges[i].v);
                // Reducing count of vertices by 1.
                vertices--;
            }
        }

        System.out.println(vertices);
        System.out.println("Edges needs to be removed - ");
        // Initializing answer (minCut) to 0.
        int ans=0;
        for(int i=0;i<E;i++)
        {
            // Finding leader element to which
            // edges[i].u belongs.
            int set1=find(edges[i].u);
            // Finding leader element to which
            // edges[i].v belongs.
            int set2=find(edges[i].v);

            // If they are not in the same set.
            if(set1!=set2)
            {
                System.out.println(edges[i].u+" <----> "+edges[i].v);
                // Increasing the ans.
                ans++;
            }
        }
        return ans;
    }
    // Find function
    public static int find(int node){

        // If the node is the parent of
        // itself then it is the leader
        // of the tree.
        if(node==parent[node]) return node;

        //Else, finding parent and also
        // compressing the paths.
        return parent[node]=find(parent[node]);
    }

    // Union function
    static void union(int u,int v){

        // Make u as a leader
        // of its tree.
        u=find(u);

        // Make v as a leader
        // of its tree.
        v=find(v);

        // If u and v are not equal,
        // because if they are equal then
        // it means they are already in
        // same tree and it does not make
        // sense to perform union operation.
        if(u!=v)
        {
            // Checking tree with
            // smaller depth/height.
            if(rank[u]<rank[v])
            {
                int temp=u;
                u=v;
                v=temp;
            }
            // Attaching lower rank tree
            // to the higher one.
            parent[v]=u;

            // If now ranks are equal
            // increasing rank of u.
            if(rank[u]==rank[v])
                rank[u]++;
        }
    }
    // Edge class
    static class Edge{
        // Endpoints u and v
        // of the Edge e.
        int u;
        int v;
        // Constructor for
        // Initializing values.
        Edge(int u,int v){
            this.u=u;
            this.v=v;
        }
    }
    // Driver Function
    public static void main(String args[]){


        // Create an Object of
        // class MinCut.
        Advent25MinCut minCut=new Advent25MinCut();
        minCut.readFile("advent25_input.txt");
        minCut.formGraph();
        // Define V and E beforehand
        int V=vertices,E=edges;
        int mincut = 10;
        do {
        minCut.initialize(V, E);
        // Make an array of edges by giving
        // endpoints of the edge.
/*        Edge edge[]=new Edge[E];

        edge[0]=new Edge(0,3);
        edge[1]=new Edge(3,2);
        edge[2]=new Edge(2,1);
        edge[3]=new Edge(1,0);
        edge[4]=new Edge(0,2);
        edge[5]=new Edge(2,4);
        edge[6]=new Edge(4,1);
        // Finding the size of the minimum cut.
        System.out.println("Count of edges needs to be removed "+ MinCut.minCutKarger(edge));*/

        int edgeCount = 0;
        Edge edge[]=new Edge[edges];
        for(String line: minCut.lines) {
            String[] parts = line.split(": ");
            String[] connectedNodes = parts[1].trim().split("\\s");
            int v1 = nameIntMap.get(parts[0]);

            for(int i=0; i < connectedNodes.length; i++) {
                int v2 = nameIntMap.get(connectedNodes[i]);
                int min = Math.min(v1, v2);
                int max = Math.max(v1, v2);
                edge[edgeCount++] = new Edge(min, max);
            }
        }


            mincut = Advent25MinCut.minCutKarger(edge);
            System.out.println(mincut);
        } while (mincut!=3);
        minCut.part1();
    }

    List<String> lines = new ArrayList<>();
    static  Map<String, Integer> nameIntMap = new LinkedHashMap<>();
    static int vertices = 0;
    static int edges = 0;
    @Override
    public String processLine(String s) {
        lines.add(s);
        return null;
    }

    public void formGraph() {
        int count = 0;

        for(String line: lines) {
            String[] parts = line.split(": ");
            String[] connectedNodes = parts[1].trim().split("\\s");
            if (nameIntMap.containsKey(parts[0])) {
                int v = nameIntMap.get(parts[0]);
            } else {
                nameIntMap.put(parts[0], count++);
            }
            for(int i=0; i < connectedNodes.length; i++) {
                edges+=1;
                if (nameIntMap.containsKey(connectedNodes[i])) {
                    int v = nameIntMap.get(connectedNodes[i]);
                } else {
                    nameIntMap.put(connectedNodes[i], count++);
                }
            }
        }
        System.out.println(nameIntMap);
        vertices = count;
        System.out.println("Vertices = "+ count);
        System.out.println("Edges = "+ edges);

    }

    void part1() {
        int V=vertices,E=edges;

        List<Integer> mins = Arrays.asList(648, 171, 1273);
        List<Integer> maxs = Arrays.asList(1212, 1225, 1360);

/*
        List<Integer> mins = Arrays.asList(0, 9, 6);
        List<Integer> maxs = Arrays.asList(3, 12, 8);
*/

        this.initialize(V, E);

        Advent25SnoverLoad graph = new Advent25SnoverLoad(V);
        System.out.println("Total vertices ="+V );

        int edgeCount = 0;
        Edge edge[]=new Edge[edges];
        for(String line: this.lines) {
            String[] parts = line.split(": ");
            String[] connectedNodes = parts[1].trim().split("\\s");
            int v1 = nameIntMap.get(parts[0]);

            for(int i=0; i < connectedNodes.length; i++) {
                int v2 = nameIntMap.get(connectedNodes[i]);
                if (mins.contains(v1)&& maxs.contains(v2) || mins.contains(v2)&& maxs.contains(v1))
                    continue;
                int min = Math.min(v1, v2);
                int max = Math.max(v1, v2);
                graph.makeEdge(min, max, 1);
            }
        }
        int num1 = graph.bfs(648);
        int num2 = graph.bfs(1225);

        int ans = num1 * num2;
        System.out.println("ans ="+ans);
    }

}

