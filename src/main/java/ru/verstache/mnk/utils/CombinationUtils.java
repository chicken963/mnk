package ru.verstache.mnk.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * generates subsets by a following principle:
 * *Given:* [[i1, i2, i3], [i4, i5], [i6, i7]]
 * *Required:* subsets of entities with a specified length (let's say, length = 2) extracted by principle "at most 1 from list"
 * *Output:*
 * [[i1, i4],
 *  [i1, i5],
 *  [i1, i6],
 *  [i1, i7],
 *  [i2, i4],
 *  [i2, i5],
 *  [i2, i6],
 *  [i2, i7],
 *  [i3, i4],
 *  [i3, i5],
 *  [i3, i6],
 *  [i3, i7],
 *  [i4, i6],
 *  [i4, i7],
 *  [i5, i6],
 *  [i5, i7]]
 */
public class CombinationUtils {

    public static <T> List<List<T>> generateCombinations(List<List<T>> sets, int k) {
        List<List<T>> combinations = new ArrayList<>();
        generateCombinationsHelper(sets, k, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private static <T> void generateCombinationsHelper(List<List<T>> sets, int k, int index, List<T> currentCombination, List<List<T>> combinations) {
        if (currentCombination.size() == k) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }
        if (index == sets.size()) {
            return;
        }
        for (T T : sets.get(index)) {
            currentCombination.add(T);
            generateCombinationsHelper(sets, k, index + 1, currentCombination, combinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
        generateCombinationsHelper(sets, k, index + 1, currentCombination, combinations);
    }
}
