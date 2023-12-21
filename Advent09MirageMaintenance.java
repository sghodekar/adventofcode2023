package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Advent09MirageMaintenance extends FileProcessor {

    List<String> inputs = new ArrayList<>();
    ConcurrentMap<Integer, Integer> finalValueMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        Advent09MirageMaintenance mm = new Advent09MirageMaintenance();
        mm.readFile("advent9_input.txt");

        // Create a thread pool with a fixed number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<Integer>> futureList = new ArrayList<>();

        // Submit tasks to the thread pool
        for (int i = 0; i < mm.inputs.size(); i++) {
            Callable<Integer> task = new TaskWithReturnValue("Task " + i, mm.inputs.get(i) );
            Future<Integer> future = executorService.submit(task);
            futureList.add(future);
        }

        // Shutdown the executor service when no more tasks are submitted
        executorService.shutdown();

        try {
            // Wait until all tasks are completed or until the timeout is reached
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int sum = 0;
        // Retrieve the results from the futures
        for (Future<Integer> future : futureList) {
            try {
                Integer result = future.get();
                sum = sum + result;
                System.out.println("Task result: " + result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sum);
    }

    @Override
    public String processLine(String s) {
        inputs.add(s);
        return null;
    }
}

class TaskWithReturnValue implements Callable<Integer> {
    private String taskName;
    String history;

    public TaskWithReturnValue(String taskName, String _history) {
        this.taskName = taskName;
        this.history = _history;
    }

    @Override
    public Integer call() {
        System.out.println("Executing task: " + taskName);

        List<Integer> historyInt = getIntList();
        // Perform the actual work of the task here
        Collections.reverse(historyInt);
        getdiff(historyInt);

        System.out.println("Task completed: " + taskName);

        return historyInt.get(historyInt.size() -1);
    }

    int getdiff(List<Integer> historyIntList) {
        int diff;
        if (historyIntList.get(0) == 0 && historyIntList.get(1) == 0) {
            return 0;
        } else {
            List<Integer> newDiffList = new ArrayList<>();
            for(int i=1; i < historyIntList.size(); i++) {
                newDiffList.add(historyIntList.get(i) - historyIntList.get(i-1));
            }
            diff = historyIntList.get(historyIntList.size() - 1) + getdiff(newDiffList);
            historyIntList.add(diff);
        }
        return diff;
    }

    List<Integer> getIntList() {
        // Step 1: Split the input string into individual tokens
        String[] tokens = history.split(" ");

        // Step 2: Create an ArrayList to store integers
        List<Integer> integerList = new ArrayList<>();

        // Step 3: Parse each token into an integer and add it to the list
        for (String token : tokens) {
            try {
                int number = Integer.parseInt(token);
                integerList.add(number);
            } catch (NumberFormatException e) {
                // Handle the case where a token is not a valid integer
                System.err.println("Error parsing token: " + token);
            }
        }
        return integerList;
    }
}
