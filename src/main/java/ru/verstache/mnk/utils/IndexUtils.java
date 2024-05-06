package ru.verstache.mnk.utils;

import java.util.ArrayList;
import java.util.List;

public class IndexUtils {

    public static List<List<Integer>> findIndexCombinations(int n, int k) {
        System.out.println("Set size: " + n + ", subset size K: " + k);
        List<List<Integer>> accumulatedIndexesCombinations = new ArrayList<>((int) computeCombinations(n, k));
        List<Integer> combinationList = new ArrayList<>(k);
        findIndexCombinationsWithBound(n, k, 0, combinationList, accumulatedIndexesCombinations);
        return accumulatedIndexesCombinations;
    }

    private static void findIndexCombinationsWithBound(int n, int k, int startIndex, List<Integer> combinationList, List<List<Integer>> accumulatedIndexesCombinations) {
        if (k == 0) {
            accumulatedIndexesCombinations.add(new ArrayList<>(combinationList));
            return;
        }
        for (int i = startIndex; i < n; i++) {
            combinationList.add(i);
            findIndexCombinationsWithBound(n, k-1, i + 1, combinationList, accumulatedIndexesCombinations);
            combinationList.remove(combinationList.size() - 1);
        }
    }

    private static long computeCombinations(int n, int k) {
        if (k < 0 || k > n) {
            return 0;
        }
        long numerator = factorial(n);
        long denominator1 = factorial(k);
        long denominator2 = factorial(n - k);
        return numerator / (denominator1 * denominator2);
    }

    private static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        long factorial = 1;
        for (int i = 2; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }
}
