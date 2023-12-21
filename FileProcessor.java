package com.example;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class FileProcessor {

    public String readLine(File file) throws IOException {
        // File path is passed as parameter
        //File file = new File("D:\\Santosh\\loancalculator\\src\\main\\resources\\advent1_input.txt");
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)

        // Creating an object of BufferedReader class
        BufferedReader br   = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String st;
        int c = 0;

        while ((st = br.readLine()) != null) {
            processLine(st);
        }

        return null;
    }
    public void readFile(String fileName) {
        try {
            InputStream inputStream = FileProcessor.class.getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                String content = readFromInputStream(inputStream);
               // System.out.println("File content:\n" + content);
            } else {
                System.err.println("File not found: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
                processLine(line);
            }
        }
        return content.toString();
    }

    public abstract String processLine(String s) ;
}
