package com.example.benchmarkingscs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.benchmarkingscs.controller.BenchmarkController;
import com.example.benchmarkingscs.model.CPUModel;
import com.example.benchmarkingscs.model.MemoryModel;
import com.example.benchmarkingscs.model.ViewHistoryModel;
import com.example.benchmarkingscs.model.ViewResultsModel;
import com.example.benchmarkingscs.view.BenchmarkView;
import com.example.benchmarkingscs.view.ViewHistoryActivity;
import com.example.benchmarkingscs.view.ViewResultsActivity;

public class MainActivity extends AppCompatActivity {

    private BenchmarkController benchmarkController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize models, view, and controller
        CPUModel cpuModel = new CPUModel(this);
        MemoryModel memoryModel = new MemoryModel();
        BenchmarkView view = new BenchmarkView(this);
        benchmarkController = new BenchmarkController(cpuModel, memoryModel, view, getApplicationContext()); // Added context
        cpuModel.initiateLogFile(); // Initialize the log file
        // Set up the CPU benchmark button
        Button cpuBenchmarkButton = findViewById(R.id.cpu_benchmark_btn);
        cpuBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                benchmarkController.startCpuBenchmark(); // Trigger CPU benchmark
            }
        });

        // Set up the memory benchmark button
        Button memoryBenchmarkButton = findViewById(R.id.memory_benchmark_btn);
        memoryBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                benchmarkController.startMemoryBenchmark(); // Trigger Memory benchmark
            }
        });

        // Set up the View History"button
        Button viewHistoryButton = findViewById(R.id.view_history_btn);
        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewHistoryActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
            }
        });
        Button exportButton = findViewById(R.id.export_results_btn);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewHistoryModel().exportCpuBenchmarkToCsv(MainActivity.this);
                new ViewHistoryModel().exportMemoryBenchmarkToCsv(MainActivity.this);
                new ViewHistoryModel().exportGpuBenchmarkToCsv(MainActivity.this);
             //   new ViewResultsModel().exportToCsv(MainActivity.this);
            }
        });


        // Set up the View Results button
        Button viewResultsButton = findViewById(R.id.view_results_btn);
        viewResultsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewResultsActivity.class);
            startActivity(intent);
        });

        // Set up the GPU  button
        Button gpuBenchmarkButton = findViewById(R.id.gpu_benchmark_btn);
        gpuBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the GPU benchmark
                benchmarkController.startGpuBenchmark();
            }
        });

    }
}
