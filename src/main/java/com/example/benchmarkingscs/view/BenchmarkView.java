package com.example.benchmarkingscs.view;

import android.content.Context;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.example.benchmarkingscs.MainActivity;
import com.example.benchmarkingscs.R;

import java.util.Map;

public class BenchmarkView {
    private Context context;
    private TextView cpuResultTextView;
    private TextView memoryResultTextView;
    private TextView gpuResultTextView;  // New GPU TextView
    private LinearLayout graphContainer;  // Container for the graph

    public BenchmarkView(Context context) {
        this.context = context;
        this.cpuResultTextView = ((MainActivity) context).findViewById(R.id.cpu_benchmark_result);
        this.memoryResultTextView = ((MainActivity) context).findViewById(R.id.memory_benchmark_result);
        this.gpuResultTextView = ((MainActivity) context).findViewById(R.id.gpu_benchmark_result);  // Initialize GPU TextView
        this.graphContainer = ((MainActivity) context).findViewById(R.id.lineChart);  // Get the graph container
    }

    // Display the CPU Benchmark Score
    public void displayCpuNormalizedScore(double normalizedScore) {
        cpuResultTextView.setText("CPU Benchmark Score: " + String.format("%.2f", normalizedScore));
    }

    // Display the Memory Benchmark Score
    public void displayMemoryBenchmarkScore(double normalizedScore) {
        memoryResultTextView.setText("Memory Benchmark Score: " + String.format("%.2f", normalizedScore));
    }

    // Display the Total Benchmark Score
    public void displayTotalBenchmarkScore(double totalScore) {
        TextView totalResultTextView = ((MainActivity) context).findViewById(R.id.total_benchmark_result);
        totalResultTextView.setText("Total Benchmark Score: " + String.format("%.2f", totalScore));
    }

    // Display the GPU Benchmark Result
    public void displayGpuBenchmarkResult(long frameCount) {
        gpuResultTextView.setText("GPU Benchmark Result: " + frameCount);
    }

    // Method to display the graph
    public void displayGraph(Map<String, Double> benchmarkResults) {
        BenchmarkGraphView graphView = new BenchmarkGraphView(context, benchmarkResults);
        graphContainer.removeAllViews();
        graphContainer.addView(graphView);
    }
}
