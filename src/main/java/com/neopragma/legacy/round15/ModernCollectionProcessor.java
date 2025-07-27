package com.neopragma.legacy.round15;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Modern collection processing utilities demonstrating Java 8+ patterns.
 * 
 * This modernizes legacy array/loop processing by:
 * - Using Stream API instead of manual for/while loops
 * - Leveraging functional programming with predicates and functions
 * - Implementing modern collection operations (filter, map, reduce)
 * - Using Set/List collections instead of arrays for better performance
 * - Providing null-safe collection operations
 * - Demonstrating parallel processing capabilities
 */
public final class ModernCollectionProcessor {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ModernCollectionProcessor() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Checks if a value exists in a collection using Stream API.
     * Replaces legacy: for (int i = 0; i < array.length; i++) { if (array[i].equals(value)) ... }
     */
    public static <T> boolean contains(Collection<T> collection, T value) {
        if (collection == null || value == null) {
            return false;
        }
        
        return collection.stream()
                        .anyMatch(item -> Objects.equals(item, value));
    }
    
    /**
     * Finds the first matching element using Stream API with predicate.
     * Demonstrates functional programming approach to searching.
     */
    public static <T> Optional<T> findFirst(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || predicate == null) {
            return Optional.empty();
        }
        
        return collection.stream()
                        .filter(predicate)
                        .findFirst();
    }
    
    /**
     * Filters collection elements using Stream API.
     * Replaces manual loop-based filtering with functional approach.
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || predicate == null) {
            return new ArrayList<>();
        }
        
        return collection.stream()
                        .filter(predicate)
                        .collect(Collectors.toList());
    }
    
    /**
     * Transforms collection elements using Stream API map operation.
     * Demonstrates functional transformation instead of manual loops.
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (collection == null || mapper == null) {
            return new ArrayList<>();
        }
        
        return collection.stream()
                        .map(mapper)
                        .collect(Collectors.toList());
    }
    
    /**
     * Counts elements matching a condition using Stream API.
     * Replaces manual counter loops with functional counting.
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || predicate == null) {
            return 0;
        }
        
        return collection.stream()
                        .filter(predicate)
                        .count();
    }
    
    /**
     * Groups collection elements by a classifier function.
     * Demonstrates advanced Stream API operations for data organization.
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> classifier) {
        if (collection == null || classifier == null) {
            return new HashMap<>();
        }
        
        return collection.stream()
                        .collect(Collectors.groupingBy(classifier));
    }
    
    /**
     * Processes collection elements in parallel for better performance.
     * Demonstrates modern parallel processing capabilities.
     */
    public static <T, R> List<R> parallelMap(Collection<T> collection, Function<T, R> mapper) {
        if (collection == null || mapper == null) {
            return new ArrayList<>();
        }
        
        return collection.parallelStream()
                        .map(mapper)
                        .collect(Collectors.toList());
    }
    
    /**
     * Reduces collection to a single value using Stream API.
     * Replaces manual accumulation loops with functional reduction.
     */
    public static <T> Optional<T> reduce(Collection<T> collection, java.util.function.BinaryOperator<T> accumulator) {
        if (collection == null || accumulator == null) {
            return Optional.empty();
        }
        
        return collection.stream()
                        .reduce(accumulator);
    }
    
    /**
     * Creates a Set from array for O(1) lookup performance.
     * Modernizes array-based contains operations.
     */
    public static <T> Set<T> arrayToSet(T[] array) {
        if (array == null) {
            return new HashSet<>();
        }
        
        return Arrays.stream(array)
                    .collect(Collectors.toSet());
    }
    
    /**
     * Validates SSN against special cases using modern Set operations.
     * Replaces legacy for-loop array iteration with O(1) Set lookup.
     */
    public static boolean isSpecialCaseSsn(String ssn, Set<String> specialCases) {
        if (ssn == null || specialCases == null) {
            return false;
        }
        
        return specialCases.contains(ssn);
    }
    
    /**
     * Processes lines from input using Stream API.
     * Replaces while-loop line reading with functional processing.
     */
    public static List<String> processLines(Stream<String> lines, Predicate<String> filter) {
        if (lines == null || filter == null) {
            return new ArrayList<>();
        }
        
        return lines.filter(filter)
                   .map(String::trim)
                   .filter(line -> !line.isEmpty())
                   .collect(Collectors.toList());
    }
    
    /**
     * Creates a range of integers using IntStream.
     * Replaces manual for-loop index generation with functional approach.
     */
    public static List<Integer> range(int start, int end) {
        return IntStream.range(start, end)
                       .boxed()
                       .collect(Collectors.toList());
    }
    
    /**
     * Partitions collection into two groups based on predicate.
     * Demonstrates advanced Stream API partitioning operations.
     */
    public static <T> Map<Boolean, List<T>> partition(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || predicate == null) {
            Map<Boolean, List<T>> result = new HashMap<>();
            result.put(true, new ArrayList<>());
            result.put(false, new ArrayList<>());
            return result;
        }
        
        return collection.stream()
                        .collect(Collectors.partitioningBy(predicate));
    }
    
    /**
     * Finds distinct elements using Stream API.
     * Replaces manual duplicate checking with functional deduplication.
     */
    public static <T> List<T> distinct(Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        }
        
        return collection.stream()
                        .distinct()
                        .collect(Collectors.toList());
    }
    
    /**
     * Sorts collection using Stream API with custom comparator.
     * Demonstrates functional sorting instead of manual sorting loops.
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        if (collection == null) {
            return new ArrayList<>();
        }
        
        return collection.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
    }
    
}
