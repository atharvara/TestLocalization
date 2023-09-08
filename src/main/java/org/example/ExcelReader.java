package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelReader {

    public static Map<String, String> readContentFromExcel(String filePath, String sheetName, String columnName) throws IOException {
        Map<String, String> frenchContent = new HashMap<>();

        FileInputStream inputStream = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(inputStream);

        // Get the specified sheet
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet with name " + sheetName + " not found.");
        }

        // Find the "French" column based on the column name
        int columnIndex = -1;
        Row headerRow = sheet.getRow(0);
        Iterator<Cell> cellIterator = headerRow.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getStringCellValue().equals("French")) {
                columnIndex = cell.getColumnIndex();
                break;
            }
        }

        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column 'French' not found.");
        }

        // Iterate through rows and read French content into the frenchContent map
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String key = row.getCell(29) != null ? row.getCell(29).getStringCellValue() : ""; // Assuming keys are in the first column
            Cell frenchCell = row.getCell(columnIndex);
            String frenchValue = frenchCell != null ? frenchCell.getStringCellValue() : ""; // French content
            frenchContent.put(key, frenchValue);
        }

        workbook.close();
        inputStream.close();

        return frenchContent;
    }
}
