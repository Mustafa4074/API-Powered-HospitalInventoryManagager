package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class OpenFDA {
    private final static String apiKey = "M3biHZ89YPBl1plw7OCwQjJLPqyEqyZe4FFofCJi";

    public ArrayList<String> getDrugsForDisease(String disease) {
        try {
            String url = "https://api.fda.gov/drug/label.json?search=openfda.indication.exact:" + disease.replaceAll(" ", "+") + "&limit=10&api_key=" + apiKey;
            String json = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();

            ArrayList<String> drugNames = new ArrayList<>();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            JsonArray results = jsonObject.getAsJsonArray("results");

            for (JsonElement result : results) {
                JsonObject resultObject = result.getAsJsonObject();
                JsonObject openfda = resultObject.getAsJsonObject("openfda");
                JsonElement drugName = openfda.get("brand_name");
                if (drugName != null) {
                    drugNames.add(drugName.getAsString());
                }
            }

            return drugNames;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
}

