package com.rocketpartners.onboarding.posdiscountengine.utils;


import lombok.NonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for reading files line by line, splitting each line by a delimiter.
 */
public class FileLineReader {

    /**
     * Read a TSV file and return the data as a list of string arrays. If the delimiter is null, the entire line is
     * added as a single element in the array.
     *
     * @param filePath  the path to the TSV file
     * @param delimiter the delimiter to split the lines by; if null, the entire line is added as a single element
     * @return the data as a list of string arrays
     * @throws RuntimeException if an error occurs while reading the file
     */
    public List<String[]> read(@NonNull String filePath, String delimiter) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (delimiter == null) {
                    data.add(new String[]{line});
                    continue;
                }
                String[] fields = line.split(delimiter);
                data.add(fields);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}

