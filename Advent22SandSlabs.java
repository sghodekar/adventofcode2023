package com.example;

import lombok.Getter;

import java.util.*;

@Getter
class Brick{
    int x1, y1, z1, x2, y2, z2;
    List<Brick> immediateOverlapping = new ArrayList<>();
    Set<Brick> restedOn = new HashSet<>();

    public Brick(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brick brick = (Brick) o;
        return x1 == brick.x1 && y1 == brick.y1 && z1 == brick.z1 && x2 == brick.x2 && y2 == brick.y2 && z2 == brick.z2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public String toString() {
        return "Brick{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", z1=" + z1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", z2=" + z2 +
                '}';
    }
}

class BricksComparator implements Comparator<Brick> {

    @Override
    public int compare(Brick o1, Brick o2) {
        int z1Comparison = Integer.compare(o1.z1, o2.z1);
        if (z1Comparison != 0) {
            return z1Comparison;
        }
        int z2Comparison = Integer.compare(o1.z2, o2.z2);
        if (z2Comparison != 0) {
            return z2Comparison;
        }
        int y1Comparison = Integer.compare(o1.y1, o2.y1);
        if (y1Comparison != 0) {
            return y1Comparison;
        }
        int y2Comparison = Integer.compare(o1.y2, o2.y2);
        if (y2Comparison != 0) {
            return y2Comparison;
        }
        int x1Comparison = Integer.compare(o1.x1, o2.x1);
        if (x1Comparison != 0) {
            return x1Comparison;
        }
        return Integer.compare(o1.x2, o2.x2);
    }
}

public class Advent22SandSlabs extends FileProcessor{

    TreeSet<Brick> bricks = new TreeSet<>(new BricksComparator());

    @Override
    public String processLine(String s) {
        String line = s.replace('~', ',');
        String[] c = line.split(",");
        int[] co = StringUtil.stringArrayToIntArray(c);
        Brick b = new Brick(co[0], co[1], co[2], co[3], co[4], co[5]);
        bricks.add(b);
        return null;
    }

    public static void main(String[] args) {
        Advent22SandSlabs ss = new Advent22SandSlabs();
        ss.readFile("advent22_input.txt");
        //ss.bricks.sort(Comparator.comparingInt(Brick::getZ1));
        System.out.println(ss.bricks);
        System.out.println(ss.bricks.size());
        part1(ss.bricks);
    }

    public static void part1(TreeSet<Brick> bricks) {
        //Brick groudBrick = new Brick(0,0,0,9,9,0);
        //bricks.add(groudBrick);
        List<Brick> bricksList = new LinkedList<>(bricks);
        //BidirectionalTreeSetIterator<Brick> iterator = new BidirectionalTreeSetIterator<>(bricks);
        int outerCount = 0;

        for (int i = 0; i < bricksList.size(); i++) {
            int maxz = 1;
            Brick nextb = bricksList.get(i);
            List<Brick> nonOVerlapping = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                Brick b = bricksList.get(j);
                if (overlap(b, nextb)) {
                    maxz = Math.max(maxz, b.z2 + 1);
                }
            }

            int z2 = nextb.z2;
            z2 -= nextb.z1 - maxz;
            int z1 = maxz;
            bricksList.remove(i);
            bricks.remove(nextb);
            Brick newBrick = new Brick(nextb.x1, nextb.y1, z1, nextb.x2, nextb.y2, z2);
            bricksList.add(i, newBrick);
            bricks.add(newBrick);
        }

        System.out.println(bricks);

        bricksList = new LinkedList<>(bricks);
        for (int i = 0; i < bricksList.size(); i++) {
            Brick nextb = bricksList.get(i);
            for (int j = 0; j < i; j++) {
                Brick b = bricksList.get(j);
                if ((b.z2 == nextb.z1-1) && overlap(b, nextb)) {
                    nextb.restedOn.add(b);
                    b.immediateOverlapping.add(nextb);
                }
            }
        }

/*        boolean change = false;
        do {
            change = false;
            for (int i = 0; i < bricksList.size(); i++) {
                Brick b = bricksList.get(i);
                List<Brick> nonOVerlapping = new ArrayList<>();
               // for (int j = i+1; j < bricksList.size(); j++) {
                int j = i+1;
                    Brick nextb = bricksList.get(j);
                    boolean overlap = false;
                    if (Math.max(b.z1, b.z2) < Math.min(nextb.z1, nextb.z2)) {
                        boolean isDirectOverlap = true;
                        if (overlap(b, nextb)) {
                            for (int k = 0; k < b.immediateOverlapping.size(); k++) {
                                if (overlap(b.immediateOverlapping.get(k), nextb))
                                    isDirectOverlap = false;

                            }
                            for (int l=0; l< nonOVerlapping.size();  l++){
                                if(overlap(nonOVerlapping.get(l), nextb)) {
                                    isDirectOverlap = false;
                                }
                            }
                            if (isDirectOverlap) {
                                int z2 = b.z2 + (nextb.z2 - nextb.z1) + 1;
                                int z1 = b.z1 + 1;
                                Brick newBrick = new Brick(nextb.x1, nextb.y1, z1, nextb.x2, nextb.y2, z2);
                                newBrick.restedOn.add(b);
                                newBrick.restedOn.addAll(nextb.restedOn);
                                b.immediateOverlapping.add(newBrick);
                                bricks.add(b);
                                bricksList.remove(j);
                                bricks.remove(nextb);
                                bricksList.add(j, newBrick);
                                bricks.add(newBrick);
                                overlap = true;
                                change = true;
                            }
                        }
                        if (!overlap) {
                            nonOVerlapping.add(nextb);
                        }
                    }
                }
            }
        }
        while (change);*/

        System.out.println(bricksList);
        System.out.println(bricksList.size());
        Set<Brick> restedOn = new HashSet<>();
        Set<Brick> restedOnOne = new HashSet<>();
        int total = 0;
        for (Brick b: bricksList) {
            if (b.restedOn.size() > 1) {
                for (Brick r: b.restedOn) {
                    if (r.z2<b.z1)
                        restedOn.add(r);
                }
            }
            if (b.restedOn.size() == 1) {
                for (Brick r: b.restedOn) {
                    restedOnOne.add(r);
                }
            }
            if (b.immediateOverlapping.isEmpty()) {
                total++;
            }
        }
        restedOn.removeAll(restedOnOne);
        System.out.println(restedOn.size());
        System.out.println(total);
        System.out.println(total + restedOn.size());

        part2(bricksList, restedOnOne);
    }

    static void part2(List<Brick> bricksList, Set<Brick> restedOnOne) {
        long total = 0;
        TreeSet<Brick> restOne = new TreeSet<>(new BricksComparator());
        restOne.addAll(restedOnOne);

        for(Brick b: bricksList) {
            Set<Brick> falling = new HashSet<>();
            Deque<Brick> queue = new ArrayDeque<>();
            if (restedOnOne.contains(b))
                queue.add(b);

            for(Brick o: b.immediateOverlapping) {
                if (o.restedOn.size() == 1)
                    queue.add(o);
            }
            falling.addAll(queue);
            falling.add(b);

            while (!queue.isEmpty()) {
                Brick b1 = queue.pollFirst();
                List<Brick> supports = b1.immediateOverlapping;

                for (Brick s : supports) {
                    if (falling.containsAll(s.restedOn)) {
                        queue.push(s);
                        falling.add(s);
                    }
                }
            }

            total =total + falling.size() -1;
        }
        System.out.println("Total = "+ total);
    }

    static boolean overlap(Brick b1, Brick b2) {
        if ((Math.max(b1.x1, b2.x1) <= Math.min(b1.x2, b2.x2)) &&
            (Math.max(b1.y1, b2.y1) <= Math.min(b1.y2, b2.y2))) {
            return true;
        }
        return false;
    }

}

