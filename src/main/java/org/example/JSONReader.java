package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class JSONReader {

    public static Map<String, String> readContentFromJSON(String filePath) throws IOException {
        Map<String, String> content = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filePath);

        // Read the JSON file into a JsonNode
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        // Iterate through the JSON properties
        Iterator<Entry<String, JsonNode>> fields = rootNode.fields();
        while (fields.hasNext()) {
            Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            String value = entry.getValue().asText();
            content.put(key, value);
        }

        return content;
    }
}
