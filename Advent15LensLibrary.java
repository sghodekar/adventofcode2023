package com.example;

class LLNode {
    String label;
    int focalLength;
    LLNode next;
    LLNode prev;

    public LLNode() {
        this.label = null;
        this.focalLength = 0;
    }

    public LLNode(String label, int focalLength) {
        this.label = label;
        this.focalLength = focalLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LLNode llNode = (LLNode) o;
        return label.equals(llNode.label);
    }

    @Override
    public int hashCode() {
        int currentValue = 0;
        for(int i=0; i < this.label.length(); i++) {
            int asciich = this.label.charAt(i);
            currentValue = currentValue + asciich;
            currentValue = currentValue * 17;
            currentValue = currentValue % 256;
        };
        return currentValue;
    }
}

public class Advent15LensLibrary extends FileProcessor {

    String input;

    @Override
    public String processLine(String s) {
        input = s;
        return null;
    }

    public static void main(String[] args) {
        Advent15LensLibrary ll = new Advent15LensLibrary();

        ll.readFile("advent15_input.txt");
        //part1(ll.input);
        part2(ll.input);
    }

    static void part1(String input) {
        String[] seq= input.split(",");
        long sum = 0;
        for (int i=0; i < seq.length; i++) {
            sum = sum + calculateCurrentValue(seq[i]);
        }
        System.out.println("sum ="+ sum);
    }

    static long calculateCurrentValue(String str) {
        long currentValue = 0;
        for(int i=0; i < str.length(); i++) {
            int asciich = str.charAt(i);
            currentValue = currentValue + asciich;
            currentValue = currentValue * 17;
            currentValue = currentValue % 256;
        }
        System.out.println(currentValue);
        return currentValue;
    }

    static void part2(String input) {
        LLNode[] lensBoxes = new LLNode[256];
        String[] seq= input.split(",");
        long sum = 0;
        for (int i=0; i < seq.length; i++) {
            String[] splitSeq = seq[i].split("=");
            String label = splitSeq[0];
            if (splitSeq.length > 1) {
                int focalLength = Integer.parseInt(splitSeq[1]);
                LLNode lens = new LLNode(label, focalLength);
                addToBox(lensBoxes, lens);
            } else {
                LLNode lens = new LLNode(label.substring(0, label.length()-1), 0);
                removeFromBox(lensBoxes, lens);
            }
        }

        calculatePart2(lensBoxes);

    }

    static void addToBox(LLNode[] lensBoxes, LLNode lens) {
        int hash = lens.hashCode();

        LLNode box = lensBoxes[hash];
        if (box == null) {
            lensBoxes[hash] = lens;
            box = lens;
            return;
        }

        LLNode temp = box;
        while(box != null) {
            if (box.equals(lens)) {
                //replacing first
                if (box.prev == null) {
                    lens.next = box.next;
                    if (box.next !=null) box.next.prev = lens;
                    lensBoxes[hash] = lens;
                    return;
                }

                //replace in middle
                lens.next = box.next;
                box.prev.next = lens;
                lens.prev = box.prev;
                if (box.next != null) box.next.prev = lens;
                box.prev = null;
                return;
            }
            temp = box;
            box = box.next;
        };

        //add in the end;
        temp.next = lens;
        lens.prev = temp;
    }

    static void removeFromBox(LLNode[] lensBoxes, LLNode lens) {
        int hash = lens.hashCode();

        LLNode box = lensBoxes[hash];

        while (box != null) {
            if (box.equals(lens)) {
                if (box.prev == null) {
                    if (box.next == null) {
                        lensBoxes[hash] = null;
                        return;
                    }
                    lensBoxes[hash] = box.next;
                    box = box.next;
                    box.prev = null;
                    return;
                }

                LLNode temp = box.prev;
                temp.next = box.next;
                if (box.next!=null)  box.next.prev = temp;
                if (box.next == null) box = null;
                return;
            }
            box = box.next;
        }

    }

    static void calculatePart2(LLNode[] lensBoxes) {
        int sum = 0;
        for (int i=0; i < lensBoxes.length; i++) {
            int boxNumber = i+1;
            LLNode lens = lensBoxes[i];
            int slot = 1;
            while(lens != null) {
                sum += boxNumber * slot * lens.focalLength;
                slot++;
                System.out.println(lens.label);
                lens = lens.next;
            }
        }
        System.out.println("Sum = "+sum);
    }

}
