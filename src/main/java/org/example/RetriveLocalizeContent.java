package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class RetriveLocalizeContent {
    final String apiUrl = "https://stage-api.whrcloud.com/api/v1/client_auth/getContentFile"; // Replace with the actual API endpoint

    static String translationLink;
    RetriveLocalizeContent(){
        System.out.println("Retrive Localize Content"+LocalizationBackend.translationLink);

        translationLink=LocalizationBackend.translationLink;

    }

    public void initialization(){

        System.out.println("**********************************New File**************************************");
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50SWQiOjE1NDI4NiwiUm9sZU5hbWUiOiJTVEFOREFSRF9VU0VSIiwiY29tcGFueUlkIjowLCJVc2VyTmFtZSI6InRlc3R0ZXN0IiwidXNlcl9uYW1lIjoiVjIwN0BNQUlMSU5BVE9SLkNPTSIsIlRTX1NBSUQiOltdLCJzY29wZSI6WyJ0cnVzdCIsInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE2OTQyMjUxNzIsImF1dGhvcml0aWVzIjpbIlNUQU5EQVJEX1VTRVIiXSwianRpIjoiMkdZYUl4QXdtejA4M3UtbS1rWDFRajE1c0x3IiwiY2xpZW50X2lkIjoic3J2c3lzd2NuYXJfc3RnIiwiU0FJRCI6W119.KgWt5YcxNEc79Gc9p7NZXF5AjLtRSQ_gHHt2LzamFb8"); // Replace with your authorization token
        customHeaders.put("Content-Type", "application/json");
        customHeaders.put("wp-client-brand", "Whirlpool");
        customHeaders.put("wp-client-region", "EMEA");
        customHeaders.put("wp-client-country", "IT");

        String jsonResponse = fetchDataFromApi(apiUrl, customHeaders);
        String filePath = "C:\\Users\\athar\\Documents\\content\\response.json";
        saveJsonResponseToFile(jsonResponse,filePath);
        Map<String, String> tokens = parseJsonResponse(jsonResponse);

        // Now you can work with the tokens map
//        if (tokens != null) {
//            for (Map.Entry<String, String> entry : tokens.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                System.out.println("Key: " + key + ", Value: " + value);
//            }
//        }


    }
    private static String fetchDataFromApi(String apiUrl, Map<String, String> customHeaders) {
        String content = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(apiUrl);

            // Include the desired file name in the request body

            translationLink="{\"fileName\": \"" + translationLink + "\"}";

            System.out.println("Body Link"+translationLink);
            StringEntity entity = new StringEntity(translationLink);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            // Set custom headers
            for (Map.Entry<String, String> headerEntry : customHeaders.entrySet()) {
                httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }

            HttpResponse response = httpClient.execute(httpPost);

            // Get response status
            StatusLine statusLine = response.getStatusLine();
            System.out.println("Status Code: " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());

            HttpEntity responseEntity = response.getEntity();


            content = EntityUtils.toString(responseEntity);
            //System.out.println(content);

            return  content;
        }catch (Exception e){
            System.out.println(e);
        }
        return  content;
    }


    private static Map<String, String> parseJsonResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Assuming the data is inside the "Data" field
            JsonNode dataNode = rootNode.get("Data");

            if (dataNode != null) {
                Map<String, String> tokens = new HashMap<>();

                // Iterate through the fields in the "Data" object
                dataNode.fields().forEachRemaining(entry -> {
                    String key = entry.getKey();
                    String value = entry.getValue().asText();
                    tokens.put(key, value);
                });

                return tokens;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void saveJsonResponseToFile(String jsonResponse, String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Check if the parsed JSON is not null
            if (rootNode != null) {
                // Create a FileWriter to write the JSON data to the specified file path
                FileWriter fileWriter = new FileWriter(filePath);

                // Use ObjectMapper to write the JSON data to the file
                objectMapper.writeValue(fileWriter, rootNode);

                // Close the FileWriter
                fileWriter.close();

                System.out.println("JSON response saved to " + filePath);
            } else {
                System.out.println("Failed to parse JSON response.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
