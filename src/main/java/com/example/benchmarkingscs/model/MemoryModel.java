package com.example.benchmarkingscs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class MemoryModel {

    private static final int BYTES_PER_INT = 4;

    // Sequential Memory Bandwidth Test
    // measures the r/w performance of memory in a sequential access pattern
    // calculates how many mb can be processed/sec
    public double performSequentialBandwidthTest() {
        int size = 1_000_000;
        int[] array = new int[size];
        long startTime = System.nanoTime();

        // seq write
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        // seq read
        for (int i = 0; i < size; i++) {
            int value = array[i];
        }

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds

        double dataSizeMB = (size * BYTES_PER_INT) / (1024.0 * 1024.0); // size in mb
        return dataSizeMB / elapsedSeconds; // bandwith in mb/s
    }

    // Random Access Memory Bandwidth Test
    // memory bandwidth for random access patterns
    // simulates scenarios where data is fetched randomly from memory.
    public double performRandomBandwidthTest() {
        int size = 1_000_000;
        int[] array = new int[size];
        Random random = new Random();

        //array with random values
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        long startTime = System.nanoTime();

        // random read access
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(size);
            int value = array[index];
        }

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

        double dataSizeMB = (size * BYTES_PER_INT) / (1024.0 * 1024.0);
        return dataSizeMB / elapsedSeconds;
    }

    // Small Buffer Latency Test
    //  latency for random access to a small buffer
    public long performSmallBufferLatencyTest() {
        int size = 10_000; // Small buffer
        int[] array = new int[size];
        Random random = new Random();

        // array with random values
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        long startTime = System.nanoTime();

        // random access latency
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(size);
            int value = array[index];
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / size; //avg latency per access in ns
    }

    // Large Buffer Latency Test
    //latency for random access to a large buffer
    public long performLargeBufferLatencyTest() {
        int size = 10_000_000;
        int[] array = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        long startTime = System.nanoTime();


        for (int i = 0; i < size; i++) {
            int index = random.nextInt(size);
            int value = array[index];
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / size;
    }

    // Large Object Creation Bandwidth Test
    // tests memory bandwidth by creating and processing large objects like matrices
    public double performLargeObjectCreationBandwidthTest() {
        long startTime = System.nanoTime();

        for (int i = 0; i < 100; i++) {
            int[][] largeMatrix = new int[1000][1000]; //matrix
            for (int row = 0; row < 1000; row++) {
                Arrays.fill(largeMatrix[row], row);
            }
        }

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;
        double totalDataSizeMB = 100 * (1000 * 1000 * BYTES_PER_INT) / (1024.0 * 1024.0);

        return totalDataSizeMB / elapsedSeconds;
    }

    // Write Performance Test
    // how fast memory can be written sequentially
    public double performWritePerformanceTest() {
        int size = 10_000_000; //data set
        int[] array = new int[size];
        long startTime = System.nanoTime();

        // write
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;
        double dataSizeMB = (size * BYTES_PER_INT) / (1024.0 * 1024.0);
        return dataSizeMB / elapsedSeconds;
    }

    // Read Performance Test
    // measures how fast memory can be read sequentially
    public double performReadPerformanceTest() {
        int size = 10_000_000;
        int[] array = new int[size];

        // Initialize array
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        long startTime = System.nanoTime();

        // Sequential read
        for (int i = 0; i < size; i++) {
            int value = array[i];
        }

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;
        double dataSizeMB = (size * BYTES_PER_INT) / (1024.0 * 1024.0);
        return dataSizeMB / elapsedSeconds;
    }

    // Cache Line Test
    // tests how efficiently the CPU can process data aligned to cache lines
    public long performCacheLineTest() {
        int size = 10_000_000;
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        int stride = 64 / BYTES_PER_INT; // cache line size
        long startTime = System.nanoTime();

        // qccess data with cache-aligned stride
        for (int i = 0; i < size; i += stride) {
            int value = array[i];
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / (size / stride);
    }

    // Multi-Threaded Bandwidth Test
    // memory bandwidth using multiple threads for r/w.
    public double performMultiThreadedBandwidthTest() {
        int size = 10_000_000;
        int[] array = new int[size];
        long startTime = System.nanoTime();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < size / 2; i++) {
                array[i] = i;
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = size / 2; i < size; i++) {
                array[i] = i;
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;
        double dataSizeMB = (size * BYTES_PER_INT) / (1024.0 * 1024.0);
        return dataSizeMB / elapsedSeconds;
    }

    // Memory Allocation and Deallocation Test
    // tests how quickly memory can be allocated and deallocated.
    public long performAllocationDeallocationTest() {
        List<int[]> allocations = new ArrayList<>();
        long startTime = System.nanoTime();

        // allocate
        for (int i = 0; i < 1_000; i++) {
            allocations.add(new int[10_000]);
        }

        // deallocate
        allocations.clear();
        System.gc();

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Memory Fragmentation Test
    // simulates memory fragmentation by random  allocating and freeing memory blocks
    public long performMemoryFragmentationTest() {
        List<int[]> allocations = new ArrayList<>();
        Random random = new Random();
        long startTime = System.nanoTime();

        for (int i = 0; i < 10_000; i++) {
            if (random.nextBoolean() && !allocations.isEmpty()) {
                allocations.remove(random.nextInt(allocations.size()));
            } else {
                allocations.add(new int[random.nextInt(10_000)]);
            }
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Memory Pressure Test
    // tests how the system handles memory pressure by trying to allocate after available memory.
    public long performMemoryPressureTest() {
        List<int[]> allocations = new ArrayList<>();
        long allocatedMemory = 0;

        long startTime = System.nanoTime();

        try {
            while (true) {
                int[] block = new int[10_000_000]; // 40mb/block
                allocations.add(block);
                allocatedMemory += block.length * Integer.BYTES;
            }
        } catch (OutOfMemoryError e) {
        }

        long endTime = System.nanoTime();
        System.out.println("Total Allocated Memory: " + allocatedMemory / (1024 * 1024) + " MB");
        return (endTime - startTime) / 1_000_000;
    }

    // Large Object Creation Test
    // measures how quickly large objects can be created in memory.
    public long performLargeObjectCreationTest() {
        long startTime = System.nanoTime();

        for (int i = 0; i < 100; i++) {
            int[][] largeMatrix = new int[1000][1000];
            for (int row = 0; row < 1000; row++) {
                Arrays.fill(largeMatrix[row], row);
            }
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    // Memory Bandwidth Test
    //bandwidth by sorting a large array
    public long performMemoryBandwidthTest() {
        long startTime = System.nanoTime();
        int size = 1_000_000;
        int[] array = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        Arrays.sort(array); // sort
        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

        double dataSizeMB = (size * BYTES_PER_INT) / (1024.0 * 1024.0);
        return (long) (dataSizeMB / elapsedSeconds);
    }

    // Latency Test
    // measures the latency of random memory access.
    public long performLatencyTest() {
        int size = 1_000_000;
        int[] array = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        long startTime = System.nanoTime();

        for (int i = 0; i < 100_000; i++) { // random access 100000 times
            int index = random.nextInt(size);
            int value = array[index];
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }
}
