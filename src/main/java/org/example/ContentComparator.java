package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContentComparator {
    public static void contentComStep() {
        // Specify file paths
        String excelFilePath = "C:\\Users\\athar\\Documents\\content\\Content Validations STG _ MTG.xlsx";
        String jsonFilePath = "C:\\Users\\athar\\Documents\\content\\response.json";

        try {
            // Step 1: Read expected translations from Excel
            Map<String, String> expectedTranslations = ExcelReader.readContentFromExcel(excelFilePath, "Appliance Provisioning (New)","French");

            // Step 2: Read actual content from JSON response
            Map<String, String> actualTranslations = readJsonResponse(jsonFilePath);

            // Step 3: Perform content comparison
            int passCount = 0;
            int failCount = 0;
            int notInLocalization = 0;

            for (String key : expectedTranslations.keySet()) {
                String expectedTranslation = expectedTranslations.get(key);
                String actualTranslation = actualTranslations.get(key);

                if (actualTranslation != null) {
                    if (actualTranslation.equals(expectedTranslation)) {
                        System.out.println(key + " -> Pass");
                        passCount++;
                    } else {
                        System.out.println("Key: " + key);
                        System.out.println("Expected: " + expectedTranslation);
                        System.out.println("Actual: " + actualTranslation);
                        failCount++;
                    }
                } else {
                    System.out.println("Key not found in JSON: " + key);
                    notInLocalization++;
                }
            }

            // Step 4: Print result summary
            System.out.println("\n#################### Result Summary ####################");
            System.out.println("Total Pass: " + passCount);
            System.out.println("Total Fail: " + failCount);
            System.out.println("Not in Localization: " + notInLocalization);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> readJsonResponse(String jsonFilePath) throws Exception {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(jsonFilePath));

        JSONObject jsonObject = (JSONObject) obj;
        JSONObject dataObject = (JSONObject) jsonObject.get("Data");

        Map<String, String> translations = new HashMap<>();

        // Iterate over keys in JSON data
        Iterator<?> keys = dataObject.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String translation = (String) dataObject.get(key);
            translations.put(key, translation);
        }

        return translations;
    }
}
