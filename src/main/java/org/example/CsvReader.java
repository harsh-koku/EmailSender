package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvReader {
    public static List<Map<String, String>> readCsv(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("CSV file is empty");
            }
            
            String[] headers = headerLine.split(",");
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new HashMap<>();
                
                for (int i = 0; i < Math.min(headers.length, values.length); i++) {
                    record.put(headers[i].trim(), values[i].trim());
                }
                data.add(record);
            }
        }
        
        return data;
    }
}
