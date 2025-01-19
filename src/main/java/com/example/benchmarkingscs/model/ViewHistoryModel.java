package com.example.benchmarkingscs.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewHistoryModel {

    private static final String PREFERENCES_NAME = "BenchmarkResults";
    private static final String CPU_PREFIX = "cpuScore_";
    private static final String MEMORY_PREFIX = "memoryScore_";
    private static final String GPU_PREFIX = "gpuFrameCount_";

    // Save CPU Benchmark Result
    public void saveCpuBenchmarkResult(Context context, double cpuScore) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        long timestamp = System.currentTimeMillis();
        editor.putFloat(CPU_PREFIX + timestamp, (float) cpuScore);
        editor.apply();
    }


    public void exportCpuBenchmarkToCsv(Context context) {

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BenchmarkResults");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File cpuFile = new File(directory, "cpu_benchmark_results.csv");

        try {
            FileWriter writer = new FileWriter(cpuFile);
            writer.append("Benchmark Name,Score\n");


            ArrayList<HashMap<String, String>> historyList = loadBenchmarkHistory(context);

            for (HashMap<String, String> entry : historyList) {
                String score = entry.get("score");
                if (score.startsWith("CPU Score:")) {
                    writer.append("").append(score.replace("CPU Score: ", "")).append("\n");
                }
            }

            writer.flush();
            writer.close();


            Toast.makeText(context, "CPU Benchmark saved to: " + cpuFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportMemoryBenchmarkToCsv(Context context) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BenchmarkResults");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Define the Memory file
        File memoryFile = new File(directory, "memory_benchmark_results.csv");

        try {
            FileWriter writer = new FileWriter(memoryFile);
            writer.append("Benchmark Name,Score\n");

            // Fetch the Memory benchmark history
            ArrayList<HashMap<String, String>> historyList = loadBenchmarkHistory(context);

            for (HashMap<String, String> entry : historyList) {
                String score = entry.get("score");
                if (score.startsWith("Memory Score:")) {
                    writer.append("").append(score.replace("Memory Score: ", "")).append("\n");
                }
            }

            writer.flush();
            writer.close();


            Toast.makeText(context, "Memory Benchmark saved to: " + memoryFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportGpuBenchmarkToCsv(Context context) {
        // Get the external storage directory
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BenchmarkResults");
        if (!directory.exists()) {
            directory.mkdirs();
        }


        File gpuFile = new File(directory, "gpu_benchmark_results.csv");

        try {
            FileWriter writer = new FileWriter(gpuFile);
            writer.append("Benchmark Name,Score\n");

            ArrayList<HashMap<String, String>> historyList = loadBenchmarkHistory(context);

            for (HashMap<String, String> entry : historyList) {
                String score = entry.get("score");
                if (score.startsWith("GPU Frames:")) {
                    writer.append("").append(score.replace("GPU Frames: ", "")).append("\n");
                }
            }

            writer.flush();
            writer.close();

            Toast.makeText(context, "GPU Benchmark saved to: " + gpuFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Save Memory Benchmark Result
    public void saveMemoryBenchmarkResult(Context context, double memoryScore) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        long timestamp = System.currentTimeMillis(); // Unique timestamp
        editor.putFloat(MEMORY_PREFIX + timestamp, (float) memoryScore);
        editor.apply();
    }
    public void saveGpuBenchmarkResult(Context context, long gpuFrameCount) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        long timestamp = System.currentTimeMillis();
        editor.putFloat(GPU_PREFIX + timestamp, (float) gpuFrameCount);
        editor.apply();
    }


    public static ArrayList<HashMap<String, String>> loadBenchmarkHistory(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<HashMap<String, String>> historyList = new ArrayList<>();

        // Loop through all saved keys and retrieve results
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                String key = entry.getKey();
                if (key.startsWith(CPU_PREFIX)) {
                    long timestamp = Long.parseLong(key.substring(CPU_PREFIX.length()));
                    String timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);

                    float cpuScore = sharedPreferences.getFloat(key, 0);

                    // Create a map for the CPU entry
                    HashMap<String, String> cpuEntry = new HashMap<>();
                    cpuEntry.put("timestamp", timestampStr);
                    cpuEntry.put("score", "CPU Score: " + cpuScore);
                    cpuEntry.put("rawTimestamp", String.valueOf(timestamp)); // Store raw timestamp for sorting
                    historyList.add(cpuEntry);
                } else if (key.startsWith(MEMORY_PREFIX)) {
                    long timestamp = Long.parseLong(key.substring(MEMORY_PREFIX.length()));
                    String timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);

                    float memoryScore = sharedPreferences.getFloat(key, 0);

                    // Create a map for the Memory entry
                    HashMap<String, String> memoryEntry = new HashMap<>();
                    memoryEntry.put("timestamp", timestampStr);
                    memoryEntry.put("score", "Memory Score: " + memoryScore);
                    memoryEntry.put("rawTimestamp", String.valueOf(timestamp)); // Store raw timestamp for sorting
                    historyList.add(memoryEntry);
                }

                     else if (key.startsWith(GPU_PREFIX)) {
                        long timestamp = Long.parseLong(key.substring(GPU_PREFIX.length()));
                        String timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);


                        float gpuFrameCount = sharedPreferences.getFloat(key, 0);
                    // Create a map for the Memory entry
                    HashMap<String, String> gpuEntry = new HashMap<>();
                    gpuEntry.put("timestamp", timestampStr);
                    gpuEntry.put("score", "GPU Frames: " + gpuFrameCount);
                    gpuEntry.put("rawTimestamp", String.valueOf(timestamp)); // Store raw timestamp for sorting
                    historyList.add(gpuEntry);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Sort the list by timestamp (most recent first)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            historyList.sort((entry1, entry2) -> {
                long timestamp1 = Long.parseLong(entry1.get("rawTimestamp"));
                long timestamp2 = Long.parseLong(entry2.get("rawTimestamp"));
                return Long.compare(timestamp2, timestamp1); // Reverse order for most recent first
            });
        }

        for (HashMap<String, String> entry : historyList) {
            entry.remove("rawTimestamp");
        }


        return historyList;
    }

}
