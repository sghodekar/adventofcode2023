package com.example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

class Part {
    int x;
    int m;
    int a;
    int s;

    public Part(int x, int m, int a, int s) {
        this.x = x;
        this.m = m;
        this.a = a;
        this.s = s;
    }

    @Override
    public String toString() {
        return "Part{" +
                "x=" + x +
                ", m=" + m +
                ", a=" + a +
                ", s=" + s +
                '}';
    }
}

class Transition {
    char category;
    char condition;
    int value;
    Step nextStep; //if condition is true

    public Transition(char category, char condition, int value, Step nextStep) {
        this.category = category;
        this.condition = condition;
        this.value = value;
        this.nextStep = nextStep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return category == that.category && condition == that.condition && value == that.value && nextStep.equals(that.nextStep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, condition, value, nextStep);
    }
}

class Step {
    String stepName;
    List<Transition> transitions;
    Step nextStep; //if all transitions conditions failed

    public Step(String stepName) {
        this.stepName = stepName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return stepName.equals(step.stepName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepName);
    }
}

public class Advent19AplentyWorkflow extends FileProcessor {

    List<String> lines = new ArrayList<>();
    List<String> workflowLines = new ArrayList<>();
    List<String> partLines = new ArrayList<>();

    List<Integer> lessThanx = new ArrayList<>();
    List<Integer> lessThanm = new ArrayList<>();
    List<Integer> lessThana = new ArrayList<>();
    List<Integer> lessThans = new ArrayList<>();
    List<Integer> greaterThanx = new ArrayList<>();
    List<Integer> greaterThanm = new ArrayList<>();
    List<Integer> greaterThana = new ArrayList<>();
    List<Integer> greaterThans = new ArrayList<>();

    @Override
    public String processLine(String s) {
        lines.add(s);
        return null;
    }

    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("js");

    private Predicate<Integer> createPredicate(String expression) {
        // Using "js" as the engine name for JavaScript evaluation

        return value -> {
            try {
                // Replace "value" in the expression with the actual value
                String modifiedExpression = expression.replace("value", value.toString());
                Object result = engine.eval(modifiedExpression);
                return (Boolean) result;
            } catch (ScriptException e) {
                // Handle script evaluation exception
                e.printStackTrace();
                return false;
            }
        };
    }

    void populateList(char xmas, char cmp, int value) {
        if (cmp == '<' ) {
            if (xmas == 'x') {
                lessThanx.add(value);
            }
            if (xmas == 'm') {
                lessThanm.add(value);
            }
            if (xmas == 'a') {
                lessThana.add(value);
            }
            if (xmas == 's') {
                lessThans.add(value);
            }
        }
        if (cmp == '>' ) {
            if (xmas == 'x') {
                greaterThanx.add(value);
            }
            if (xmas == 'm') {
                greaterThanm.add(value);
            }
            if (xmas == 'a') {
                greaterThana.add(value);
            }
            if (xmas == 's') {
                greaterThans.add(value);
            }
        }
    }

    public static void main(String[] args) {
        Advent19AplentyWorkflow aw = new Advent19AplentyWorkflow();

        aw.readFile("advent19_input.txt");

        AtomicBoolean afterEmptyLine = new AtomicBoolean(false);
        aw.lines.forEach(line -> {
            if (line.isEmpty()) {
                afterEmptyLine.set(true);
            } else {
                if (afterEmptyLine.get()) {
                    aw.partLines.add(line);
                } else {
                    aw.workflowLines.add(line);
                }
            }
        });

        System.out.println(aw.workflowLines);
        System.out.println(aw.partLines);

        Map<String, Step> stepsMap = new HashMap<>();

        Step A = new Step("A");
        Step R = new Step("R");
        stepsMap.put("A", A);
        stepsMap.put("R", R);

        aw.workflowLines.stream().forEach(workflow -> {
            String[] parts = workflow.split("\\{");
            String stepName = parts[0].trim();
            String transitions = parts[1].replace("}", "").trim();
            Step step = stepsMap.getOrDefault(stepName, new Step(stepName));
            stepsMap.put(stepName, step);

            String[] secondparts = transitions.split(",");
            List<Transition> tlist = new ArrayList<>();
            for (int i=0; i < secondparts.length-1; i++) {
                String[] conditions = secondparts[i].split(":");
                Step s = stepsMap.getOrDefault(conditions[1], new Step(conditions[1]));
                stepsMap.put(stepName, s);
                Transition t = new Transition(conditions[0].charAt(0), conditions[0].charAt(1), Integer.parseInt(conditions[0].substring(2, conditions[0].length())), s);
                aw.populateList(conditions[0].charAt(0), conditions[0].charAt(1), Integer.parseInt(conditions[0].substring(2, conditions[0].length())));
                tlist.add(t);
            }
            step.transitions = tlist;
            Step nextStep = stepsMap.getOrDefault(secondparts[secondparts.length-1], new Step(secondparts[secondparts.length-1]));
            step.nextStep = nextStep;
            stepsMap.put(stepName, step);
            System.out.println(stepName + "  "+ transitions);
        });
        System.out.println("");

        List<Part> parts = new ArrayList<>();
        aw.partLines.stream().forEach(p -> {
            String[] c = p.split(",");
            Part part = new Part(Integer.parseInt(c[0].split("=")[1]),
                                 Integer.parseInt(c[1].split("=")[1]),
                                 Integer.parseInt(c[2].split("=")[1]),
                                 Integer.parseInt(c[3].split("=")[1].replace("}", "").trim()));
            System.out.println(part.toString());
            parts.add(part);
        });

        final long[] sum = {0};
        parts.forEach(p -> {
            String decision = aw.part1(stepsMap, p);
            if ("A".equals(decision)) {
                sum[0] = sum[0] + p.x + p.m + p.a + p.s;
            }
        });
        System.out.println(sum[0]);

        aw.part2v2(stepsMap);
    }


    public String part1(Map<String, Step> stepsMap, Part p) {
        AtomicReference<Step> nextStep = new AtomicReference<>(stepsMap.get("in"));
        AtomicBoolean conditionVal = new AtomicBoolean(false);
        while (!("A".equals(nextStep.get().stepName) || "R".equals(nextStep.get().stepName))) {
            for (Transition t : nextStep.get().transitions) {
                Field field = null;
                try {
                    field = p.getClass().getDeclaredField(String.valueOf(t.category));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                Object value = null;
                try {
                    value = field.get(p);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                Predicate<Integer> predicate = createPredicate("value " + t.condition + " " + t.value);
                conditionVal.set(predicate.test((Integer) value));
               // System.out.print(nextStep.get().stepName + " -->");
                if (conditionVal.get()) {
                    nextStep.set(stepsMap.get(t.nextStep.stepName));
                    break;
                }
            }
            if (!conditionVal.get()) {
                nextStep.set(stepsMap.get(nextStep.get().nextStep.stepName));
            }
        }
        //System.out.println(nextStep.get().stepName);
        return nextStep.get().stepName;
    }

/*    private long calculate(Map<String, Step> stepsMap, Part p) {
        String decision = part1(stepsMap, p);
        long total = 0;
        if ("A".equals(decision)) {
            total = (long)p.x * (long)p.m * (long)p.a * (long)p.s;
        }
        return total;
    }*/

    void part2(Map<String, Step> stepsMap) {

        final long[] sum = {0};
        AtomicLong max = new AtomicLong();
        lessThanx.forEach(x -> {
            lessThanm.forEach(m ->{
                lessThana.forEach(a->{
                    lessThans.forEach(s-> {
                        Part p = new Part(x-1,m-1,a-1,s-1);
                        String decision = part1(stepsMap, p);
                        long total = 0;
                        if ("A".equals(decision)) {
                            total = (long)p.x * (long)p.m * (long)p.a * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }
                        System.out.println("1   "+ total);
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                    greaterThans.forEach(s->{
                        Part p = new Part(x-1,m-1,a-1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (long)p.x * (long)p.m * (long)p.a * (4000l - (long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                        System.out.println("2   "+ total);
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
                greaterThana.forEach(a->{
                    lessThans.forEach(s-> {
                        Part p = new Part(x-1,m-1,a+1,s-1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (long)p.x * (long)p.m *  (4000l -(long)p.a) * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }
                        System.out.println("3   "+ total);
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                    greaterThans.forEach(s->{
                        Part p = new Part(x-1,m-1,a+1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (long)p.x * (long)p.m *  (4000l -(long)p.a) * (4000l - (long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
            });
            greaterThanm.forEach(m ->{
                lessThana.forEach(a->{
                    lessThans.forEach(s-> {
                        Part p = new Part(x-1,m+1,a-1,s-1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (long)p.x * (4000l -(long)p.m)* (long)p.a * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }
                    });

                    sum[0] = sum[0] + max.get();
                    max.set(0);

                    greaterThans.forEach(s->{
                        Part p = new Part(x-1,m+1,a-1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (long)p.x * (4000l -(long)p.m)* (long)p.a * (4000l -(long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
                greaterThana.forEach(a->{
                    lessThans.forEach(s-> {
                        Part p = new Part(x-1,m+1,a+1,s-1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (long)p.x * (4000l -(long)p.m)* (4000l -(long)p.a) * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);

                    greaterThans.forEach(s->{
                        Part p = new Part(x-1,m+1,a+1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (long)p.x * (4000l -(long)p.m)* (4000l -(long)p.a) * (4000l -(long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
            });
        });

        greaterThanx.forEach(x -> {
            lessThanm.forEach(m ->{
                lessThana.forEach(a->{

                    lessThans.forEach(s-> {
                        Part p = new Part(x+1,m-1,a-1,s-1);
                        String decision = part1(stepsMap, p);
                        long total = 0;
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (long)p.m * (long)p.a * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }

                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                    greaterThans.forEach(s->{
                        Part p = new Part(x+1,m-1,a-1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (long)p.m * (long)p.a * (4000l - (long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
                greaterThana.forEach(a->{
                    lessThans.forEach(s-> {
                        Part p = new Part(x+1,m-1,a+1,s-1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (long)p.m *  (4000l -(long)p.a) * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                    greaterThans.forEach(s->{
                        Part p = new Part(x+1,m-1,a+1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (long)p.m *  (4000l -(long)p.a) * (4000l - (long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
            });
            greaterThanm.forEach(m ->{
                lessThana.forEach(a->{
                    lessThans.forEach(s-> {
                        Part p = new Part(x+1,m+1,a-1,s-1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (4000l -(long)p.m)* (long)p.a * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                    greaterThans.forEach(s->{
                        Part p = new Part(x+1,m+1,a-1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (4000l -(long)p.m)* (long)p.a * (4000l -(long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
                greaterThana.forEach(a->{
                    lessThans.forEach(s-> {
                        Part p = new Part(x+1,m+1,a+1,s-1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (4000l -(long)p.m)* (4000l -(long)p.a) * (long)p.s;
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                    greaterThans.forEach(s->{
                        Part p = new Part(x+1,m+1,a+1,s+1);
                        long total = 0;
                        String decision = part1(stepsMap, p);
                        if ("A".equals(decision)) {
                            total = (4000l -(long)p.x) * (4000l -(long)p.m)* (4000l -(long)p.a) * (4000l -(long)p.s);
                            if (total > max.get())
                                max.set(total);
                        }
                    });
                    sum[0] = sum[0] + max.get();
                    max.set(0);
                });
            });
        });
        System.out.println(sum[0] );
    }

    public static int[] getRange(String cmp, int split, int low, int high) {
        int[] lohi = new int[2];

        switch (cmp) {
            case "<":
                lohi[0] = low;
                lohi[1] = Math.min(high, split - 1);
                break;
            case ">":
                lohi[0] = Math.max(low, split + 1);
                lohi[1] = high;
                break;
            case "<=":
                lohi[0] = low;
                lohi[1] = Math.min(high, split);
                break;
            case ">=":
                lohi[0] = Math.max(low, split );
                lohi[1] = high;
                break;
        }
        return lohi;
    }

    public static List<Integer> getRanges(char category, String cmp, int split, int xl, int xh, int ml, int mh, int al, int ah, int sl, int sh) {
        switch(category) {
            case 'x' :
                int[] xlohi = getRange(cmp, split, xl, xh);
                xl = xlohi[0];
                xh = xlohi[1];
                break;
            case 'm' :
                int[] mlohi = getRange(cmp, split, ml, mh);
                ml = mlohi[0];
                mh = mlohi[1];
                break;
            case 'a' :
                int[] alohi = getRange(cmp, split, al, ah);
                al = alohi[0];
                ah = alohi[1];
                break;
            case 's' :
                int[] slohi = getRange(cmp, split, sl, sh);
                sl = slohi[0];
                sh = slohi[1];
                break;
        }
        return Arrays.asList(xl, xh, ml, mh, al, ah, sl,sh);
    }

    Deque<List<Object>>  rangeQueue = new LinkedList<>();

    void part2v2(Map<String, Step> stepMap) {
        List<Object> o = Arrays.asList("in", 1, 4000, 1, 4000, 1, 4000, 1, 4000);
        rangeQueue.add(o);
        String state = null;
        long sum = 0;
        while (!rangeQueue.isEmpty()) {
            List<Object> rq = rangeQueue.poll();
            System.out.println(rq);

            final int[] xl = {(Integer) rq.get(1)};
            final int[] xh = { (Integer)rq.get(2) };
            final int[] ml = {(Integer) rq.get(3)};
            final int[] mh = { (Integer)rq.get(4) };
            final int[] al = {(Integer) rq.get(5)};
            final int[] ah = { (Integer)rq.get(6) };
            final int[] sl = {(Integer) rq.get(7)};
            final int[] sh = { (Integer)rq.get(8) };


            if (xl[0] > xh[0] || ml[0] > mh[0] || al[0] > ah[0] || sl[0] > sh[0])
                continue;

            if ("A".equals(rq.get(0))) {
                long score = (long) (xh[0] - xl[0] + 1) * (mh[0] - ml[0] + 1) * (ah[0] - al[0] + 1) * (sh[0] - sl[0] +1);
                System.out.println("score ="+score);
                sum = sum +score;
            } else {
                if ("R".equals(rq.get(0))) {
                    continue;
                }
                else {
                    System.out.println(rq.get(0));
                    Step rule = stepMap.get(rq.get(0));
                    AtomicReference<List<Integer>> ranges = new AtomicReference<>();
                    rule.transitions.forEach(t -> {
                        List<Object> l = new ArrayList<>();
                        l.add(t.nextStep.stepName);
                        ranges.set(getRanges(t.category, String.valueOf(t.condition), t.value, xl[0], xh[0], ml[0], mh[0], al[0], ah[0], sl[0], sh[0]));
                        l.addAll(ranges.get());
                        rangeQueue.add(l);
                        System.out.println(ranges.get());
                        List<Integer> oppRanges = getRanges(t.category, '>' == t.condition ? "<=" : ">=", t.value, xl[0], xh[0], ml[0], mh[0], al[0], ah[0], sl[0], sh[0]);
                        ranges.set(oppRanges);
                        xl[0] = oppRanges.get(0); xh[0] = oppRanges.get(1);
                        ml[0] = oppRanges.get(2); mh[0] = oppRanges.get(3);
                        al[0] = oppRanges.get(4); ah[0] = oppRanges.get(5);
                        sl[0] = oppRanges.get(6); sh[0] = oppRanges.get(7);
                        System.out.println("==>" + ranges.get());
                    });
                    List<Object> l = new ArrayList<>();
                    l.add(rule.nextStep.stepName);
                    l.addAll(ranges.get());
                    System.out.println(ranges.get());
                    rangeQueue.add(l);
                }
            }
        }
        System.out.println("sum= "+sum);
    }
}
