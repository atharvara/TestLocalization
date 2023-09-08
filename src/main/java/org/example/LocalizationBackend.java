package org.example;

import com.fasterxml.jackson.databind.JsonNode; // You'll need Jackson for JSON parsing
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost; // Use HttpPost for sending file name in the body
import org.apache.http.entity.StringEntity; // Use StringEntity to include the file name
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LocalizationBackend {
    static String translationLink;

    public static void main(String[] args) {
        String apiUrl = "https://stage-api.whrcloud.com/api/v1/client_auth/getContentFile"; // Replace with the actual API endpoint
        RetriveLocalizeContent retriveLocalizeContent;
        ContentComparator contentComparator=new ContentComparator();
        // Replace these with the user's selected region and language
        String selectedRegion = "CA";
        String selectedLanguage = "fre";

        // Construct the file name based on user selections
        String fileName = "Localization_" + selectedRegion + "_" + selectedLanguage;

        // Create custom headers if needed
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50SWQiOjE1NDI4NiwiUm9sZU5hbWUiOiJTVEFOREFSRF9VU0VSIiwiY29tcGFueUlkIjowLCJVc2VyTmFtZSI6InRlc3R0ZXN0IiwidXNlcl9uYW1lIjoiVjIwN0BNQUlMSU5BVE9SLkNPTSIsIlRTX1NBSUQiOltdLCJzY29wZSI6WyJ0cnVzdCIsInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE2OTQyMjUxNzIsImF1dGhvcml0aWVzIjpbIlNUQU5EQVJEX1VTRVIiXSwianRpIjoiMkdZYUl4QXdtejA4M3UtbS1rWDFRajE1c0x3IiwiY2xpZW50X2lkIjoic3J2c3lzd2NuYXJfc3RnIiwiU0FJRCI6W119.KgWt5YcxNEc79Gc9p7NZXF5AjLtRSQ_gHHt2LzamFb8"); // Replace with your authorization token
        customHeaders.put("Content-Type", "application/json");
        customHeaders.put("wp-client-brand", "Whirlpool");
        customHeaders.put("wp-client-region", "EMEA");
        customHeaders.put("wp-client-country", "IT");


        String desiredFileName = "{\"fileName\": \"NAR/MAYTAG/MOBILEAPP/CATEGORIES/ScantoConnect.json\"}";

        // Fetch JSON response from the API by sending the desired file name in the request body
        String jsonResponse = fetchDataFromApi(apiUrl, desiredFileName, customHeaders);
        // Parse the JSON response
        Map<String, String> localizationData = parseJsonResponse(jsonResponse);

        // Print all localization data
        for (Map.Entry<String, String> entry : localizationData.entrySet()) {
            String file = entry.getKey();
            String url = entry.getValue();
            System.out.println("File Name: " + file);
            System.out.println("URL: " + url);
            System.out.println(); // Add a newline for separation
        }
        translationLink=localizationData.get(fileName);
        retriveLocalizeContent=new RetriveLocalizeContent();
        retriveLocalizeContent.initialization();

        System.out.println("*************************Comparison Performing*********************************");
        ContentComparator.contentComStep();


    }



    private static String fetchDataFromApi(String apiUrl, String desiredFileName, Map<String, String> customHeaders) {
        String content = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(apiUrl);
            desiredFileName = "{\"fileName\": \"NAR/MAYTAG/MOBILEAPP/MAFMaster.json\"}";

            // Include the desired file name in the request body
            StringEntity entity = new StringEntity(desiredFileName);
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
            System.out.println(content);

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

            Map<String, String> localizationData = new HashMap<>();

            // Assuming the data is inside the "Data" field
            JsonNode dataNode = rootNode.get("Data");

            System.out.println(dataNode);
            if (dataNode != null && dataNode.has("Configurations")) {
                for (JsonNode configNode : dataNode.get("Configurations")) {
                    String configFileName = configNode.get("fileName").asText();
                    String configUrl = configNode.get("URL").asText();
                    localizationData.put(configFileName, configUrl);
                }
            }

            return localizationData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}