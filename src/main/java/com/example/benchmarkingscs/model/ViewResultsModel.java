package com.example.benchmarkingscs.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewResultsModel {

    private static final String PREFERENCES_NAME = "BenchmarkResults";
    private static final String TOTAL_PREFIX = "totalScore_";

    // Method to save total benchmark score
    public void saveTotalScore(Context context, double totalScore) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        long timestamp = System.currentTimeMillis(); // Current time for tracking the score run
        editor.putFloat(TOTAL_PREFIX + timestamp, (float) totalScore);

        editor.apply(); // Apply the changes asynchronously
    }

    // Method to load all total benchmark scores
    public static ArrayList<HashMap<String, String>> loadTotalScores(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<HashMap<String, String>> resultsList = new ArrayList<>();

        // Loop through all saved keys and retrieve results
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith(TOTAL_PREFIX)) {
                try {
                    long timestamp = Long.parseLong(entry.getKey().substring(TOTAL_PREFIX.length()));
                    String timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);

                    float totalScore = sharedPreferences.getFloat(entry.getKey(), 0);

                    // Create a map for this entry
                    HashMap<String, String> resultItem = new HashMap<>();
                    resultItem.put("timestamp", timestampStr);
                    resultItem.put("totalScore", "Total Score: " + totalScore);

                    // Add the item to the list
                    resultsList.add(resultItem);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
             }
        }
        return resultsList;
    }
}
