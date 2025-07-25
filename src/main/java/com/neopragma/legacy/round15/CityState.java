package com.neopragma.legacy.round15;

import java.util.Objects;

/**
 * Immutable value object representing city and state information.
 * 
 * This modernizes the legacy CityState class by implementing:
 * - Immutability through final fields and no setters
 * - Proper equals(), hashCode(), and toString() implementations
 * - Null-safe factory methods
 * - Input validation and normalization
 * - Modern Java 8 patterns
 * 
 * Benefits over the legacy class:
 * - Guarantees immutability and thread safety
 * - Provides value semantics with proper equals/hashCode
 * - Null-safe construction patterns
 * - Clear validation and error handling
 * - More maintainable and testable code
 */
public final class CityState {
    private final String city;
    private final String state;
    
    /**
     * Private constructor to enforce validation and normalization.
     */
    private CityState(String city, String state) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty");
        }
        
        this.city = city.trim();
        this.state = state.trim().toUpperCase();
    }
    
    /**
     * Factory method for creating CityState with validation.
     */
    public static CityState of(String city, String state) {
        return new CityState(city, state);
    }
    
    /**
     * Factory method for creating CityState from potentially null values.
     * Demonstrates modern null-safe patterns.
     */
    public static CityState ofNullable(String city, String state) {
        return new CityState(
            city != null ? city : "",
            state != null ? state : ""
        );
    }
    
    public String getCity() {
        return city;
    }
    
    public String getState() {
        return state;
    }
    
    /**
     * Check if this CityState represents valid data.
     */
    public boolean isValid() {
        return !city.isEmpty() && !state.isEmpty();
    }
    
    /**
     * Format for display purposes.
     * Uses modern string formatting instead of StringBuilder.
     */
    public String displayFormat() {
        return String.format("%s, %s", city, state);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityState cityState = (CityState) obj;
        return Objects.equals(city, cityState.city) && 
               Objects.equals(state, cityState.state);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(city, state);
    }
    
    @Override
    public String toString() {
        return String.format("CityState{city='%s', state='%s'}", city, state);
    }
}
