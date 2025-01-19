package com.example.benchmarkingscs.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.example.benchmarkingscs.model.CPUModel;
import com.example.benchmarkingscs.model.MemoryModel;
import com.example.benchmarkingscs.model.GPUModel;
import com.example.benchmarkingscs.model.ViewHistoryModel;
import com.example.benchmarkingscs.model.ViewResultsModel;
import com.example.benchmarkingscs.view.BenchmarkView;

public class BenchmarkController {
    private CPUModel cpuModel;
    private MemoryModel memoryModel;
    private GPUModel gpuModel;
    private BenchmarkView view;
    private ViewHistoryModel historyModel;
    private Context context;
    private boolean isCpuScoreReady = false;
    private boolean isMemoryScoreReady = false;
    private  boolean isGpuScoreReady= false;
    private double cpuScore = -1;      // Store CPU score
    private double memoryScore = -1;   // Store Memory score
    private long gpuFrameCount = -1;   // Store GPU result

    public BenchmarkController(CPUModel cpuModel, MemoryModel memoryModel, BenchmarkView view, Context context) {
        this.cpuModel = cpuModel;
        this.memoryModel = memoryModel;
        this.view = view;
        this.historyModel = new ViewHistoryModel();
        this.context = context;
        this.gpuModel = new GPUModel(context, view); // Initialize GPUModel
    }

    // Start the CPU benchmark
    public void startCpuBenchmark() {
        new CpuBenchmarkTask().execute();
    }

    // Start the Memory benchmark
    public void startMemoryBenchmark() {
        new MemoryBenchmarkTask().execute();
    }

    // Start the GPU benchmark
    public void startGpuBenchmark() {

        GPUModel gpuModel = new GPUModel(context, view);

        new GpuBenchmarkTask().execute();
    }

    private void computeAndDisplayTotalScore() {
        if (isCpuScoreReady && isMemoryScoreReady && isGpuScoreReady) { // Ensure all scores are ready
            double totalScore = (cpuScore + memoryScore + gpuFrameCount) / 3.0;


            view.displayTotalBenchmarkScore(totalScore);


            ViewResultsModel resultsModel = new ViewResultsModel();
            resultsModel.saveTotalScore(context, totalScore);

            isCpuScoreReady = false;
            isMemoryScoreReady = false;
            isGpuScoreReady = false;
        }
    }


    // AsyncTask for CPU benchmarking
    private class CpuBenchmarkTask extends AsyncTask<Void, Void, Double> {
        @Override
        protected Double doInBackground(Void... voids) {
            cpuModel.clearLogFile();
            long integerTime = cpuModel.performIntegerCalculations();
            long floatingPointTime = cpuModel.performFloatingPointCalculations();
            long compressionTime = cpuModel.performCompressionSimulation();
            long sieveTime = cpuModel.performSieveOfEratosthenes();
            long matrixMultiplicationTime = cpuModel.performMatrixMultiplication();
            long fftTime = cpuModel.performFFT();
            long trialDivisionTime = cpuModel.performTrialDivision();
            long towerOfHanoiTime = cpuModel.performTowerOfHanoi();
            long fibonacciTime = cpuModel.performFibonacciCalculations(20);


            return calculateGeometricMean(
                    integerTime, floatingPointTime, compressionTime, sieveTime,
                    matrixMultiplicationTime, fftTime, trialDivisionTime,
                    towerOfHanoiTime, fibonacciTime
            );
        }

        @Override
        protected void onPostExecute(Double result) {
            cpuScore = result;
            isCpuScoreReady = true;

            view.displayCpuNormalizedScore(cpuScore);
            computeAndDisplayTotalScore();
            historyModel.saveCpuBenchmarkResult(context, cpuScore);
        }
    }

    // AsyncTask for Memory benchmarking
    private class MemoryBenchmarkTask extends AsyncTask<Void, Void, Double> {
        @Override
        protected Double doInBackground(Void... voids) {
            long memoryBandwidthTime = memoryModel.performMemoryBandwidthTest();
            long latencyTime = memoryModel.performLatencyTest();
            long fragmentationTime = memoryModel.performMemoryFragmentationTest();
            long pressureTestTime = memoryModel.performMemoryPressureTest();
            long largeObjectCreationTime = memoryModel.performLargeObjectCreationTest();

            double sequentialBandwidth = memoryModel.performSequentialBandwidthTest();
            double randomBandwidth = memoryModel.performRandomBandwidthTest();
            long smallBufferLatency = memoryModel.performSmallBufferLatencyTest();
            long largeBufferLatency = memoryModel.performLargeBufferLatencyTest();
            double largeObjectBandwidth = memoryModel.performLargeObjectCreationBandwidthTest();
            double writePerformance = memoryModel.performWritePerformanceTest();
            double readPerformance = memoryModel.performReadPerformanceTest();
            long cacheLineLatency = memoryModel.performCacheLineTest();
            double multiThreadedBandwidth = memoryModel.performMultiThreadedBandwidthTest();

            long latencyScore = calculateGeometricMeanForLatency(
                    latencyTime, smallBufferLatency, largeBufferLatency, cacheLineLatency,fragmentationTime,pressureTestTime,largeObjectCreationTime
            );

            long bandwidthScore = calculateHarmonicMeanForBandwidth(
                    memoryBandwidthTime, sequentialBandwidth, randomBandwidth,
                    largeObjectBandwidth, writePerformance, readPerformance,
                    multiThreadedBandwidth
            );

            return (latencyScore + bandwidthScore) / 2.0;
        }

        @Override
        protected void onPostExecute(Double result) {
            memoryScore = result;
            isMemoryScoreReady = true;
            view.displayMemoryBenchmarkScore(memoryScore);
            computeAndDisplayTotalScore();
            historyModel.saveMemoryBenchmarkResult(context, memoryScore);
        }
    }

    // AsyncTask for GPU benchmarking
    private class GpuBenchmarkTask extends AsyncTask<Void, Void, Long> {
        @Override
        protected Long doInBackground(Void... voids) {
            gpuModel.run();
            return gpuModel.getFrameCount();
        }

        @Override
        protected void onPostExecute(Long result) {
            gpuFrameCount = result;
            isGpuScoreReady = true;
            view.displayGpuBenchmarkResult(gpuFrameCount);
            computeAndDisplayTotalScore();
            historyModel.saveGpuBenchmarkResult(context, gpuFrameCount);
        }

    }

    private long calculateGeometricMeanForLatency(long... latencies) {
        double product = 1.0;
        for (long latency : latencies) {
            product *= latency;
        }
        return (long) Math.pow(product, 1.0 / latencies.length);
    }

    private long calculateHarmonicMeanForBandwidth(double... bandwidths) {
        double sum = 0.0;
        for (double bandwidth : bandwidths) {
            sum += 1.0 / bandwidth;
        }
        return (long) (bandwidths.length / sum);
    }

    private double calculateGeometricMean(long... values) {
        double product = 1.0;
        for (long value : values) {
            product *= value;
        }
        return Math.pow(product, 1.0 / values.length);
    }

}
