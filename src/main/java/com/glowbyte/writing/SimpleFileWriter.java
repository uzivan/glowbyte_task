package com.glowbyte.writing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SimpleFileWriter {
    private static final String PATH =  "src/main/resources/result.txt";
    public static void writeToFile(List<String> stringList) {
        if(Files.exists(Paths.get(PATH))) {
            try {
                Files.write(Paths.get(PATH), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                System.err.println("Error when deleting file contents: " + e.getMessage());
            }
        }

        for(String string: stringList) {
            try {
                byte[] bytes = (string + "\n").getBytes();
                Files.write(Paths.get(PATH), bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.err.println("Error when writing to file: " + e.getMessage());
            }
        }
    }
}
