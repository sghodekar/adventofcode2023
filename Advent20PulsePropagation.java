package com.example;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

interface Module {
    void setInputPulse(String inputPulse);
    String getInputPulse();
    String getName();
    String getOutputPulse();
}

class FlipFlop implements Module{
    String name;
    String inputPulse;
    String outputPulse;
    boolean onOff = false; //on = true, off = false
    List<String> nextModules = new ArrayList<>();
    LinkedList<String> inputFrom = new LinkedList<>();

    public FlipFlop(String name) {
        this.name = name;
    }

    String getPulse(String pulse) {
        if ("low".equals(pulse)) {
            onOff = !onOff;
            if (onOff) {
                return "high";
            } else {
                return "low";
            }
        }
        return null;
    }

    @Override
    public void setInputPulse(String inputPulse) {
        this.inputPulse = inputPulse;
    }

    @Override
    public String getInputPulse() {
        return inputPulse;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOutputPulse() {
        return outputPulse;
    }
}

class Conjunction implements Module{
    String name;
    String inputPulse = "low";
    String outputPulse;
    LinkedList<String> inputFrom = new LinkedList<>();
    Map<String, Integer> inputList = new HashMap();
    List<String> pulseList  = new ArrayList<>();
    List<String> nextModules = new ArrayList<>();

    public Conjunction(String name) {
        this.name = name;
    }

    String getPulse(String input, String pulse) {
        int index = inputList.get(input);
        pulseList.set(index, pulse);

        if (pulseList.contains("low")) {
            return "high";
        } else {
            return "low";
        }
    }

    @Override
    public void setInputPulse(String inputPulse) {
        this.inputPulse = inputPulse;
    }

    @Override
    public String getInputPulse() {
        return inputPulse;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOutputPulse() {
        return outputPulse;
    }
}

class Broadcaster implements Module{
    String inputPulse = "low";
    String outputPulse = "low";
    List<String> nextModules = new ArrayList<>();

    @Override
    public void setInputPulse(String inputPulse) {
        this.inputPulse = inputPulse;
    }

    @Override
    public String getInputPulse() {
        return inputPulse;
    }

    @Override
    public String getName() {
        return "broadcaster";
    }

    @Override
    public String getOutputPulse() {
        return "low";
    }
}

public class Advent20PulsePropagation extends FileProcessor {

    static List<String> lines = new ArrayList<>();
    static Map<String, Module> nameModuleMap = new HashMap<>();
    static Set<String> setOfModuleNames = new HashSet<>();
    static List<String> flipFlops = new ArrayList<>();
    static List<String> conjunctions = new ArrayList<>();

    @Override
    public String processLine(String s) {
        lines.add(s);
        return null;
    }

    public static void main(String[] args) {
        Advent20PulsePropagation pp = new Advent20PulsePropagation();
        pp.readFile("advent20_input.txt");

        lines.stream().forEach(line -> {
            String[] parts = line.split("->");

            if ("broadcaster".equals(parts[0].trim())) {
                Broadcaster b = new Broadcaster();
                String[] modules = parts[1].split(",");
                for (int i=0; i < modules.length; i++) {
                    b.nextModules.add(modules[i].trim());
                    setOfModuleNames.add(modules[i].trim());
                }
                b.inputPulse = "low";
                nameModuleMap.put("broadcaster", b);
            } else {
                if (parts[0].trim().charAt(0) == '%') {
                    FlipFlop ff = new FlipFlop(parts[0].trim().substring(1,3));
                    String[] modules = parts[1].split(",");
                    for (int i=0; i < modules.length; i++) {
                        ff.nextModules.add(modules[i].trim());
                        setOfModuleNames.add(modules[i].trim());
                    }
                    flipFlops.add(ff.name);
                    nameModuleMap.put(ff.name, ff);
                } else if (parts[0].trim().charAt(0) == '&') {
                    Conjunction con = new Conjunction(parts[0].trim().substring(1,3));
                    String[] modules = parts[1].split(",");
                    for (int i=0; i < modules.length; i++) {
                        con.nextModules.add(modules[i].trim());
                        setOfModuleNames.add(modules[i].trim());
                    }
                    conjunctions.add(con.name);
                    nameModuleMap.put(con.name, con);
                }
            }
        });
        lines.stream().forEach(line ->{
            String[] parts = line.split("->");
            String[] modules = parts[1].split(",");
            for (int i=0; i < modules.length; i++) {
                Module m = nameModuleMap.get(modules[i].trim());
                if (m instanceof Conjunction) {
                    Conjunction con = (Conjunction) m;
                    String in = parts[0].substring(1,3);
                    if (!con.inputList.containsKey(in)) {
                        con.inputList.put(in, con.pulseList.size());
                        con.pulseList.add("low");
                    }
                }
            }
        });

        Set<String> diff = Sets.difference(setOfModuleNames, nameModuleMap.keySet() );
        System.out.println(diff);

        part1();

    }

    static boolean isInitialState() {
        boolean flag = true;
        for (String ffname : flipFlops) {
            FlipFlop ff = (FlipFlop) nameModuleMap.get(ffname);
            if (ff.onOff) {
                flag = false;
                return false;
            }
        }

        for (String conname : conjunctions) {
            Conjunction con = (Conjunction) nameModuleMap.get(conname);
            if (con.pulseList.contains("high")) {
                flag = false;
                return false;
            }
        }
        return true;
    }

    static void part1() {

        AtomicInteger lowcount = new AtomicInteger(0);
        AtomicInteger highcount = new AtomicInteger();

        Map<String, Long> conCyclestonr = new HashMap<>();

        boolean found = false;
        int cycle = 1;
        do {

            LinkedList<String> nextToVisit = new LinkedList<>();
            nextToVisit.add("broadcaster");
            lowcount.getAndIncrement();

        while(!nextToVisit.isEmpty()){
            String mname = nextToVisit.remove();
            Module m = nameModuleMap.get(mname);
            String prevModuleOutputPulse = null;
            String moduleOutputPulse = null;

            if (m instanceof Broadcaster) {
                Broadcaster b = (Broadcaster) m;
                b.nextModules.forEach(m1 -> {
                    Module nm = nameModuleMap.get(m1);
                    nm.setInputPulse(b.outputPulse);
                    if (nm instanceof Conjunction) {
                        ((Conjunction)nm).inputFrom.add(b.getName());
                    }
                    if (nm instanceof FlipFlop) {
                        ((FlipFlop)nm).inputFrom.add(b.getName());
                    }
                    nextToVisit.add(m1);
                });
            }

            if (m instanceof FlipFlop) {
                FlipFlop ff = (FlipFlop) m;
                String inputfrom = ff.inputFrom.remove();
                Module in = nameModuleMap.get(inputfrom);

                if (in instanceof Broadcaster) {
                    prevModuleOutputPulse = "low";
                    moduleOutputPulse = ff.getPulse("low");
                }
                if (in instanceof FlipFlop) {
                    prevModuleOutputPulse = ((FlipFlop) in).outputPulse;
                    moduleOutputPulse = ff.getPulse(((FlipFlop) in).outputPulse);
                }
                if (in instanceof Conjunction) {
                    prevModuleOutputPulse = ((Conjunction)in).outputPulse;
                    moduleOutputPulse = ff.getPulse(((Conjunction)in).outputPulse);
                }
                if (moduleOutputPulse!= null)
                    ff.outputPulse = moduleOutputPulse;
                String finalPrevModuleOutputPulse = prevModuleOutputPulse;
                String finalModuleOutputPulse = moduleOutputPulse;
                ff.nextModules.forEach(m1 -> {
                            Module nm = nameModuleMap.get(m1);
                            if (nm instanceof Conjunction) {
                                if (finalModuleOutputPulse!= null)
                                    ((Conjunction) nm).inputFrom.add(ff.name);
                            }
                            if (nm instanceof FlipFlop) {
                                if (finalModuleOutputPulse!= null)
                                    ((FlipFlop) nm).inputFrom.add(ff.name);
                            }
                            if (finalModuleOutputPulse != null)
                                nextToVisit.add(m1);
                        });
                    System.out.println(inputfrom +" - "+ finalPrevModuleOutputPulse +"-->"+ff.name);
                    if ("low".equals(finalPrevModuleOutputPulse)) {
                        lowcount.getAndIncrement();
                        if ("rx".equals(ff.name))
                            found = true;
                    } else {
                        highcount.getAndIncrement();
                    }
                }

            if (m instanceof Conjunction) {
                Conjunction con = (Conjunction) m;
                String inputfrom = con.inputFrom.remove(0);
                Module in = nameModuleMap.get(inputfrom);

                if (in instanceof Broadcaster) {
                    prevModuleOutputPulse = "low";
                    moduleOutputPulse = con.getPulse(in.getName(),"low");
                }
                if (in instanceof FlipFlop) {
                    prevModuleOutputPulse = ((FlipFlop) in).outputPulse;
                    moduleOutputPulse = con.getPulse(in.getName(), ((FlipFlop) in).outputPulse);
                }
                if (in instanceof Conjunction) {
                    prevModuleOutputPulse = ((Conjunction)in).outputPulse;
                    moduleOutputPulse = con.getPulse(in.getName(), ((Conjunction)in).outputPulse);
                }
                con.outputPulse = moduleOutputPulse;
                String finalPrevModuleOutputPulse = prevModuleOutputPulse;
                con.nextModules.forEach(m1 -> {
                            Module nm = nameModuleMap.get(m1);
                            if (nm instanceof Conjunction) {
                                ((Conjunction) nm).inputFrom.add(con.name);
                            }
                            if (nm instanceof FlipFlop) {
                                ((FlipFlop) nm).inputFrom.add(con.name);
                            }

                            nextToVisit.add(m1);
                        });
                    System.out.println(inputfrom +" - "+finalPrevModuleOutputPulse +"-->"+con.name);
                    if ("low".equals(finalPrevModuleOutputPulse)) {
                        lowcount.getAndIncrement();
                        //part2
                        if("lh".equals(con.name) || "fk".equals(con.name) || "ff".equals(con.name) || "mm".equals(con.name)) {
                            conCyclestonr.put(con.name, (long)cycle);
                        }

                    } else {

                        highcount.getAndIncrement();
                    }
                }

        }
        cycle++;
        } while(conCyclestonr.size()<4);//while (cycle <=1000);

        System.out.println(cycle);
        System.out.println(lowcount.get()+"   "+highcount.get());

        System.out.println(Advent8HauntedWasteland.calculateLCM(new ArrayList<>(conCyclestonr.values())));

    }
}
