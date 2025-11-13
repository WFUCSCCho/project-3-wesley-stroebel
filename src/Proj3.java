//**********************************************************************
// @file: Proj3.java
// @description: Main class that will implement different sorting alogorithms
//               and compare the times it takes for them to be completes for different lists
// @author: Wesley Stroebel
// @date: November 12, 2025
// **********************************************************************

import java.io.*;
import java.util.*;

public class Proj3 {


    static class InsuranceRecord implements Comparable<InsuranceRecord> {
        //variables that are in my CSV file
        int age;
        String sex;
        double bmi;
        int children;
        String smoker;
        String region;
        double charges;

        public InsuranceRecord(int age, String sex, double bmi, int children,
                               String smoker, String region, double charges) {
            this.age = age;
            this.sex = sex;
            this.bmi = bmi;
            this.children = children;
            this.smoker = smoker;
            this.region = region;
            this.charges = charges;
        }
       //Helper methods compareTo (Help with sorting algorithms) and toString(Help with printing out output).
        @Override
        public int compareTo(InsuranceRecord other) {
            return Double.compare(this.charges, other.charges);
        }

        @Override
        public String toString() {
            return age + "," + sex + "," + bmi + "," + children + ","
                    + smoker + "," + region + "," + charges;
        }
    }

    // Merge Sort: recursively splits list in half,
    // sorts each half, then merges them.
    public static <T extends Comparable<T>> void mergeSort(ArrayList<T> a, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        merge(a, left, mid, right);
    }

    // Merge step for merge sort.
    public static <T extends Comparable<T>> void merge(ArrayList<T> a, int left, int mid, int right) {
        ArrayList<T> temp = new ArrayList<>(right - left + 1);
        int i = left;
        int j = mid + 1;

        while (i <= mid && j <= right) {
            if (a.get(i).compareTo(a.get(j)) <= 0) temp.add(a.get(i++));
            else temp.add(a.get(j++));
        }
        while (i <= mid) temp.add(a.get(i++));
        while (j <= right) temp.add(a.get(j++));

        for (int k = 0; k < temp.size(); k++) {
            a.set(left + k, temp.get(k));
        }
    }

    // Quick Sort: partitions around a pivot, then recursively sorts both sides.
    public static <T extends Comparable<T>> void quickSort(ArrayList<T> a, int left, int right) {
        if (left >= right) return;
        int p = partition(a, left, right);
        quickSort(a, left, p - 1);
        quickSort(a, p + 1, right);
    }

    // Partition using last element as a pivot.
    public static <T extends Comparable<T>> int partition(ArrayList<T> a, int left, int right) {
        T pivot = a.get(right);
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (a.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, right);
        return i + 1;
    }

    // Swap for sorting algorithms.
    static <T> void swap(ArrayList<T> a, int i, int j) {
        T temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // Heap Sort: builds a max heap then repeatedly extracts the max.
    public static <T extends Comparable<T>> void heapSort(ArrayList<T> a, int left, int right) {
        int n = right - left + 1;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(a, i, n - 1);
        for (int i = n - 1; i > 0; i--) {
            swap(a, 0, i);
            heapify(a, 0, i - 1);
        }
    }

    // Restores heap order by sifting down.
    public static <T extends Comparable<T>> void heapify(ArrayList<T> a, int left, int right) {
        int root = left;
        while (true) {
            int largest = root;
            int leftChild = 2 * root + 1;
            int rightChild = 2 * root + 2;

            if (leftChild <= right && a.get(leftChild).compareTo(a.get(largest)) > 0)
                largest = leftChild;
            if (rightChild <= right && a.get(rightChild).compareTo(a.get(largest)) > 0)
                largest = rightChild;
            if (largest != root) {
                swap(a, root, largest);
                root = largest;
            } else break;
        }
    }

    // Bubble Sort: repeatedly compares adjacent pairs and swaps out-of-order elements.
    // Returns total comparisons.
    public static <T extends Comparable<T>> int bubbleSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                comparisons++;
                if (a.get(j).compareTo(a.get(j + 1)) > 0) {
                    swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return comparisons;
    }

    // Odd-Even Transposition Sort: repeatedly compares and swaps adjacent pairs on odd and even passes until the list is sorted.
    public static <T extends Comparable<T>> int transpositionSort(ArrayList<T> a, int size) {
        boolean sorted = false;
        int steps = 0;

        while (!sorted) {
            sorted = true;

            // Odd
            for (int i = 1; i <= size - 2; i += 2) {
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    sorted = false;
                }
            }
            steps++;

            // Even
            for (int i = 0; i <= size - 2; i += 2) {
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    sorted = false;
                }
            }
            steps++;
        }

        return steps;
    }

    // Main sets up input, runs the selected algorithm, and prints results.
    // This is where the program handles command-line arguments and timing.
    public static void main(String[] args) throws IOException {

        if (args.length != 3) {
            System.out.println("Usage: java Proj3 <dataset-file> <algorithm> <N>");
            return;
        }

        String fileName = args[0];
        String algorithm = args[1].toLowerCase();
        int n = Integer.parseInt(args[2]);

        ArrayList<InsuranceRecord> original = readDataset(fileName, n);


        ArrayList<InsuranceRecord> sorted = new ArrayList<>(original);
        Collections.sort(sorted);

        ArrayList<InsuranceRecord> shuffled = new ArrayList<>(sorted);
        Collections.shuffle(shuffled);

        ArrayList<InsuranceRecord> reversed = new ArrayList<>(sorted);
        Collections.sort(reversed, Collections.reverseOrder());

        PrintWriter sortedOut = new PrintWriter(new FileWriter("sorted.txt"));
        File analysisFile = new File("analysis.txt");
        boolean append = analysisFile.exists();
        PrintWriter analysisOut = new PrintWriter(new FileWriter(analysisFile, true));

        if (!append) {
            analysisOut.println("# Sorting Performance Data");
            analysisOut.println("algorithm,inputType,N,metric,value");
        }

        runOneCase(algorithm, "already-sorted", fileName, sorted, analysisOut, sortedOut, n);
        runOneCase(algorithm, "shuffled", fileName, shuffled, analysisOut, sortedOut, n);
        runOneCase(algorithm, "reversed", fileName, reversed, analysisOut, sortedOut, n);

        sortedOut.close();
        analysisOut.close();

        System.out.println("Done.");
    }

    // Will read up the N amount of lines form the file
    private static ArrayList<InsuranceRecord> readDataset(String fileName, int n) throws IOException {
        ArrayList<InsuranceRecord> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        br.readLine(); // skip header

        int count = 0;
        String line;
        while ((line = br.readLine()) != null && count < n) {
            String[] parts = line.split(",");
            data.add(new InsuranceRecord(
                    Integer.parseInt(parts[0].trim()),
                    parts[1].trim(),
                    Double.parseDouble(parts[2].trim()),
                    Integer.parseInt(parts[3].trim()),
                    parts[4].trim(),
                    parts[5].trim(),
                    Double.parseDouble(parts[6].trim())
            ));
            count++;
        }
        br.close();
        return data;
    }

    // Runs one algorithm on one input type and writes results.
    private static void runOneCase(String algorithm, String inputType, String fileName,
                                   ArrayList<InsuranceRecord> input,
                                   PrintWriter analysisOut, PrintWriter sortedOut, int n) {

        ArrayList<InsuranceRecord> list = new ArrayList<>(input);

        if (algorithm.equals("bubble")) {
            long start = System.nanoTime();
            int comps = bubbleSort(list, list.size());
            long end = System.nanoTime();
            double sec = (end - start) / 1e9;

            analysisOut.println(algorithm + "," + inputType + "," + n + ",timeSec," + sec);
            analysisOut.println(algorithm + "," + inputType + "," + n + ",comparisons," + comps);
        }

        else if (algorithm.equals("transposition")) {
            long start = System.nanoTime();
            int steps = transpositionSort(list, list.size());
            long end = System.nanoTime();
            double sec = (end - start) / 1e9;

            analysisOut.println(algorithm + "," + inputType + "," + n + ",comparisons," + steps);
            analysisOut.println(algorithm + "," + inputType + "," + n + ",timeSec," + sec);
        }

        else if (algorithm.equals("merge")) {
            long start = System.nanoTime();
            mergeSort(list, 0, list.size() - 1);
            long end = System.nanoTime();
            double sec = (end - start) / 1e9;

            analysisOut.println(algorithm + "," + inputType + "," + n + ",timeSec," + sec);
        }

        else if (algorithm.equals("quick")) {
            long start = System.nanoTime();
            quickSort(list, 0, list.size() - 1);
            long end = System.nanoTime();
            double sec = (end - start) / 1e9;

            analysisOut.println(algorithm + "," + inputType + "," + n + ",timeSec," + sec);
        }

        else if (algorithm.equals("heap")) {
            long start = System.nanoTime();
            heapSort(list, 0, list.size() - 1);
            long end = System.nanoTime();
            double sec = (end - start) / 1e9;

            analysisOut.println(algorithm + "," + inputType + "," + n + ",timeSec," + sec);
        }

        // Write sorted output
        sortedOut.println("=== " + algorithm + " / " + inputType + " / N=" + n + " ===");
        for (int i = 0; i < list.size(); i++) {
            sortedOut.println(list.get(i).toString());
        }
        sortedOut.println();
    }
}