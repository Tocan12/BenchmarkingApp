package com.example.benchmarkingscs.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;

public class CPUModel {

    private static final String LOG_FILE = "benchmark_log.txt";
        private Context context;  // Add context variable
        // Integer calculations
        public void initiateLogFile() {
            clearLogFile();
    }    public CPUModel(Context context) {
        this.context = context;
    }
    public long performIntegerCalculations() {
        long startTime = System.nanoTime();
        int result = 0;
        for (int i = 0; i < 1000000; i++) {
            long opStartTime = System.nanoTime();
            result += i;
            long opEndTime = System.nanoTime();

            if(i < 100) {
                //log the operation time into a file using a logging method in this class
                double timeTaken = (opEndTime - opStartTime) / 1_000_000; //log this
                logOperationTime("performIntegerCalculations", timeTaken);
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Floating-point calculations
    public long performFloatingPointCalculations() {
        long startTime = System.nanoTime();
        double result = 0;
        for (int i = 0; i < 1000000; i++) {
            long opStartTime = System.nanoTime();
            result += Math.sqrt(i);
            long opEndTime = System.nanoTime();

            if(i < 100) {
                //log the operation time into a file using a logging method in this class
                double timeTaken = (opEndTime - opStartTime) / 1_000_000; //log this
                logOperationTime("performFloatingPointCalculations", timeTaken);
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Compression simulation
    public long performCompressionSimulation() {
        long startTime = System.nanoTime();
        byte[] data = new byte[1024 * 1024]; // 1MB of data
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            deflater.deflate(buffer);
        }
        deflater.end();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Sieve of Eratosthenes
    public long performSieveOfEratosthenes() {
        long startTime = System.nanoTime();
        int limit = 100000;
        boolean[] sieve = new boolean[limit + 1];
        for (int i = 2; i <= Math.sqrt(limit); i++) {
            long opStartTime = System.nanoTime();
            if (!sieve[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    sieve[j] = true;
                }
            }
            long opEndTime = System.nanoTime();

            if(i < 100) {
                //log the operation time into a file using a logging method in this class
                double timeTaken = (opEndTime - opStartTime) / 1_000_000; //log this
                logOperationTime("performSieveOfEratosthenes", timeTaken);
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Matrix multiplication
    public long performMatrixMultiplication() {
        long startTime = System.nanoTime();
        int N = 500;
        double[][] matrixA = new double[N][N];
        double[][] matrixB = new double[N][N];
        double[][] result = new double[N][N];

        // Initialize matrices
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrixA[i][j] = Math.random();
                matrixB[i][j] = Math.random();
            }
        }

        // Perform matrix multiplication
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                long opStartTime = System.nanoTime();
                for (int k = 0; k < N; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
                long opEndTime = System.nanoTime();

                if(i < 100) {
                    //log the operation time into a file using a logging method in this class
                    double timeTaken = (opEndTime - opStartTime) / 1_000_000; //log this
                    logOperationTime("performMatrixMultiplication", timeTaken);
                }
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Fast Fourier Transform (FFT)
    public long performFFT() {
        long startTime = System.nanoTime();
        int N = 1024;
        double[] real = new double[N];
        double[] imag = new double[N];

        // Initialize input
        for (int i = 0; i < N; i++) {
            real[i] = Math.random();
            imag[i] = Math.random();
        }

        // Perform FFT
        fft(real, imag);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    private void fft(double[] real, double[] imag) {
        int N = real.length;
        if (N <= 1) return;

        double[] evenReal = new double[N / 2];
        double[] evenImag = new double[N / 2];
        double[] oddReal = new double[N / 2];
        double[] oddImag = new double[N / 2];

        for (int i = 0; i < N / 2; i++) {
            evenReal[i] = real[2 * i];
            evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1];
            oddImag[i] = imag[2 * i + 1];
        }

        fft(evenReal, evenImag);
        fft(oddReal, oddImag);

        for (int i = 0; i < N / 2; i++) {
            double cos = Math.cos(2 * Math.PI * i / N);
            double sin = Math.sin(2 * Math.PI * i / N);
            double tempReal = cos * oddReal[i] + sin * oddImag[i];
            double tempImag = cos * oddImag[i] - sin * oddReal[i];
            real[i] = evenReal[i] + tempReal;
            imag[i] = evenImag[i] + tempImag;
            real[i + N / 2] = evenReal[i] - tempReal;
            imag[i + N / 2] = evenImag[i] - tempImag;
        }
    }

    // Trial Division
    public long performTrialDivision() {
        long startTime = System.nanoTime();
        int limit = 100000;
        for (int i = 2; i <= limit; i++) {
            long opStartTime = System.nanoTime();
            boolean isPrime = true;
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            long opEndTime = System.nanoTime();

            if(i < 100) {
                //log the operation time into a file using a logging method in this class
                double timeTaken = (opEndTime - opStartTime) / 1_000_000; //log this
                logOperationTime("performTrialDivision", timeTaken);
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Tower of Hanoi
    public long performTowerOfHanoi() {
        long startTime = System.nanoTime();
        int numDisks = 20;
        towerOfHanoi(numDisks, 'A', 'C', 'B');
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    private void towerOfHanoi(int n, char fromRod, char toRod, char auxRod) {
        if (n == 1) {
            return;
        }
        towerOfHanoi(n - 1, fromRod, auxRod, toRod);
        towerOfHanoi(n - 1, auxRod, toRod, fromRod);
    }

    // Fibonacci Sequence
    public long performFibonacciCalculations(int n) {
        long startTime = System.nanoTime();
        fibonacciRecursive(n);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    private int fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    private void logOperationTime(String methodName, double timeTaken) {
        try {
            // Using context to open the file in internal storage
            FileOutputStream fos = context.openFileOutput(LOG_FILE, Context.MODE_APPEND);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

            writer.write("Method: " + methodName + ", Time: " + timeTaken + " ms");
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public Map<String, Double> readLogData() {
        Map<String, Double> logData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", Time: ");
                if (parts.length == 2) {
                    String methodName = parts[0].replace("Method: ", "").trim();  // Clean method name
                    double timeTaken = Double.parseDouble(parts[1].replace(" ms", "").trim());
                    logData.put(methodName, timeTaken);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }

        return logData;
    }


    public void clearLogFile() {
        try (FileOutputStream fos = context.openFileOutput(LOG_FILE, Context.MODE_PRIVATE)) {
            fos.write(new byte[0]); // Truncate the file
        } catch (IOException ignored) {

        }
    }

}
