package com.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

class Record {
    TreeSet<Long> sourceSet = new TreeSet<>();
    TreeMap<Long, Long> sourceDestination = new TreeMap<>();
    TreeMap<Long, Long> sourceRange = new TreeMap<>();
    TreeMap<Long, Long> destinationRange = new TreeMap<>();
}

public class Advent5SeedToLocation extends FileProcessor {

    List<String> inputlines = new ArrayList<>();

    List<String> seeds = new ArrayList<>();
    Record seedToSoil = new Record();
    Record soilToFertilizer = new Record();
    Record fertilizerToWater = new Record();
    Record waterToLight = new Record();
    Record lightToTemperature = new Record();
    Record temperatureToHumidity = new Record();
    Record humidityToLocation = new Record();
    List<Long> locations = new ArrayList<>();

    void prepareMap(Map<Long, Long> map, int sourceRangeStart, int destinationRangeStart, int range) {
        for(int i=0; i < range; i++) {
            map.put((long) (sourceRangeStart+i), (long) (destinationRangeStart+i));
        }
    }

    long[] parseAndSeparateValues(String st) {
        String[] tokens = st.trim().split("\\s+");
        long[] values = {Long.parseLong(tokens[0]), Long.parseLong(tokens[1]), Long.parseLong(tokens[2])};
        return values;
    }

    Record prepareRecord(Record rec, List<String> entries) {
        entries.forEach(entry ->{
            long[] val = parseAndSeparateValues(entry);
            rec.sourceSet.add(val[1]);
            rec.sourceDestination.put(val[1], val[0]);
            rec.sourceRange.put(val[1], val[2]);
            rec.destinationRange.put(val[0], val[2]);
        });
        return rec;
    }

    long findDestinationForSource(Record rec, long source) {

        if (source < rec.sourceSet.first())
            return source;

        if (source >= (rec.sourceSet.last() + rec.sourceRange.get(rec.sourceSet.last())))
            return source;

        if (rec.sourceSet.contains(source)) {
            return rec.sourceDestination.get(source);
        }

        for (Long key : rec.sourceSet) {
            if (source < (key + rec.sourceRange.get(key))) {
                long l = rec.sourceDestination.get(key) + source - key;
                return l;
            }
        }

        return 0;
    }

    List<String> getInputListOfGroup(List<String> inputList, String criteria) {
        List<String> mapInput = new ArrayList<>();
        boolean flag = false;
        for (String line : inputList) {
            if (criteria.equals(line.trim())) {
                flag= true;
                continue;
            }
            if (flag && line.trim().isEmpty()) {
                flag= false;
                break;
            }
            if (flag) {
                mapInput.add(line);
            }
        }
        return mapInput;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        Advent5SeedToLocation sl = new Advent5SeedToLocation();

        sl.readFile("advent5_input.txt");

        List<String> seedSoil = sl.getInputListOfGroup(sl.inputlines, "seed-to-soil map:");
        List<String> soilFertilizer = sl.getInputListOfGroup(sl.inputlines, "soil-to-fertilizer map:");
        List<String> fertilizerWater = sl.getInputListOfGroup(sl.inputlines, "fertilizer-to-water map:");
        List<String> waterLight = sl.getInputListOfGroup(sl.inputlines, "water-to-light map:");
        List<String> lightTemperature = sl.getInputListOfGroup(sl.inputlines, "light-to-temperature map:");
        List<String> temperatureHumidity = sl.getInputListOfGroup(sl.inputlines, "temperature-to-humidity map:");
        List<String> humidityToLocation = sl.getInputListOfGroup(sl.inputlines, "humidity-to-location map:");

        sl.seeds = Arrays.asList(sl.inputlines.get(0).split("[:]")[1].trim().split("\\s+"));
        List<Record> records = new ArrayList<>();
        records.add(sl.prepareRecord(sl.seedToSoil, seedSoil));
        records.add(sl.prepareRecord(sl.soilToFertilizer, soilFertilizer));
        records.add(sl.prepareRecord(sl.fertilizerToWater, fertilizerWater));
        records.add(sl.prepareRecord(sl.waterToLight, waterLight));
        records.add(sl.prepareRecord(sl.lightToTemperature, lightTemperature));
        records.add(sl.prepareRecord(sl.temperatureToHumidity, temperatureHumidity));
        records.add(sl.prepareRecord(sl.humidityToLocation, humidityToLocation));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), "UTF-8"), 512));
        out.println("Test");
        out.flush();

        AtomicBoolean skip = new AtomicBoolean(false);
        Map<Long, String> locationTOSeed = new HashMap<>();
        List<Long> minlocations = new ArrayList<>();

        sl.seeds.parallelStream().forEach( seed -> {
            int index = sl.seeds.indexOf(seed);
            AtomicLong minlocation = new AtomicLong();
            if (index%2 == 0) {
                long source = Long.parseLong(seed);
                long inc = Long.parseLong(sl.seeds.get(index+1));
                out.println("processing "+source+"  "+inc);
                out.flush();
                for(long i=source; i<source+inc; i++) {
                    long s = i;
                    for (Record record : records) {
                        s = sl.findDestinationForSource(record, s);
                    }
                    if (minlocation.get() == 0) {
                        minlocation.set(s);
                    }
                    if (s < minlocation.get()) {
                        minlocation.set(s);
                    }
                    //locationTOSeed.put(source, seed);
                }
                out.println(index+ "  "+minlocation);
                out.flush();
                minlocations.add(minlocation.get());
            }
            skip.set(!skip.get());
        });

        out.println(minlocations);
        out.println(Collections.min(minlocations));
        out.flush();

        Map<Long, Long> seedToSoilMap = new TreeMap<>();

        Record seedToSoil = new Record();
        sl.prepareRecord(seedToSoil, Arrays.asList("50 98 2", "52 50 48"));

    }

    @Override
    public String processLine(String s) {
        inputlines.add(s);
        return null;
    }
}
