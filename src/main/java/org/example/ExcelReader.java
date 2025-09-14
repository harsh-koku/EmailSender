package org.example;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {
    public static List<Map<String, String>> readExcel(String filePath) throws Exception {
        List<Map<String, String>> data = new ArrayList<>();

        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String name = row.getCell(0).getStringCellValue();
            String email = row.getCell(1).getStringCellValue();

            Map<String, String> person = new HashMap<>();
            person.put("name", name);
            person.put("email", email);
            data.add(person);
        }

        workbook.close();
        return data;
    }
}
