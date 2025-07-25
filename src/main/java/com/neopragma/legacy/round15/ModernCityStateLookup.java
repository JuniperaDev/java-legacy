package com.neopragma.legacy.round15;

import java.util.HashMap;
import java.util.Map;

/**
 * Modern implementation of city/state lookup using functional programming patterns.
 * 
 * This modernizes the legacy CityStateLookup by:
 * - Using functional interfaces instead of concrete dependencies
 * - Implementing caching for better performance
 * - Providing null-safe operations
 * - Using modern Map operations and Java 8 features
 * - Supporting both synchronous and potentially asynchronous lookups
 */
public final class ModernCityStateLookup {
    
    private final Map<String, CityState> cache = new HashMap<>();
    
    /**
     * Private constructor to prevent direct instantiation.
     * Use factory methods instead.
     */
    private ModernCityStateLookup() {}
    
    /**
     * Creates a lookup function with in-memory caching.
     */
    public static ModernAddress.CityStateLookupFunction createCachedLookup() {
        ModernCityStateLookup instance = new ModernCityStateLookup();
        return instance::lookupWithCache;
    }
    
    /**
     * Creates a lookup function that uses a provided lookup strategy.
     */
    public static ModernAddress.CityStateLookupFunction createCustomLookup(
            ModernAddress.CityStateLookupFunction lookupStrategy) {
        return lookupStrategy;
    }
    
    /**
     * Internal cached lookup implementation.
     */
    private CityState lookupWithCache(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            return CityState.ofNullable("", "");
        }
        
        String normalizedZip = zipCode.trim();
        
        return cache.computeIfAbsent(normalizedZip, this::performLookup);
    }
    
    /**
     * Performs the actual lookup operation.
     * In a real implementation, this would call an external service.
     */
    private CityState performLookup(String zipCode) {
        switch (zipCode) {
            case "12345":
                return CityState.of("Schenectady", "NY");
            case "90210":
                return CityState.of("Beverly Hills", "CA");
            case "10001":
                return CityState.of("New York", "NY");
            case "60601":
                return CityState.of("Chicago", "IL");
            default:
                return CityState.ofNullable("Unknown", "Unknown");
        }
    }
    
    /**
     * Static utility method for simple lookups without caching.
     */
    public static CityState simpleLookup(String zipCode) {
        ModernCityStateLookup lookup = new ModernCityStateLookup();
        return lookup.performLookup(zipCode);
    }
    
    /**
     * Clears the lookup cache.
     */
    public void clearCache() {
        cache.clear();
    }
    
    /**
     * Returns the current cache size.
     */
    public int getCacheSize() {
        return cache.size();
    }
}
