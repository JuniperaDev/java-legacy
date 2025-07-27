package com.neopragma.legacy.round15;

import java.util.Objects;

/**
 * Modern immutable Address value object using builder pattern and Java 8 features.
 * 
 * This modernizes the legacy AddressImpl by:
 * - Making the class immutable with final fields
 * - Using builder pattern for flexible construction
 * - Integrating with modernized CityState value object
 * - Implementing proper equals(), hashCode(), and toString()
 * - Adding validation and null-safety
 * - Providing factory methods for different construction scenarios
 */
public final class ModernAddress {
    private final String zipCode;
    private final CityState cityState;
    
    /**
     * Private constructor to enforce builder pattern usage.
     */
    private ModernAddress(Builder builder) {
        this.zipCode = builder.zipCode != null ? builder.zipCode.trim() : "";
        this.cityState = builder.cityState;
        
        validate();
    }
    
    /**
     * Validates the address components.
     */
    private void validate() {
        if (zipCode.isEmpty()) {
            throw new IllegalArgumentException("Zip code cannot be null or empty");
        }
        if (cityState == null || !cityState.isValid()) {
            throw new IllegalArgumentException("Valid city and state are required");
        }
    }
    
    /**
     * Factory method to create address from city, state, and zip code.
     */
    public static ModernAddress of(String city, String state, String zipCode) {
        return new Builder()
            .withCityState(CityState.of(city, state))
            .withZipCode(zipCode)
            .build();
    }
    
    /**
     * Factory method to create address from CityState and zip code.
     */
    public static ModernAddress of(CityState cityState, String zipCode) {
        return new Builder()
            .withCityState(cityState)
            .withZipCode(zipCode)
            .build();
    }
    
    /**
     * Factory method for creating address with city/state lookup.
     * This replaces the legacy constructor that took CityStateLookup.
     */
    public static ModernAddress fromZipCode(String zipCode, CityStateLookupFunction lookupFunction) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zip code cannot be null or empty");
        }
        
        CityState cityState = lookupFunction.lookup(zipCode);
        return new Builder()
            .withCityState(cityState)
            .withZipCode(zipCode)
            .build();
    }
    
    /**
     * Returns a new builder for constructing ModernAddress instances.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public String getCity() {
        return cityState.getCity();
    }
    
    public String getState() {
        return cityState.getState();
    }
    
    public CityState getCityState() {
        return cityState;
    }
    
    /**
     * Returns a formatted address string for display.
     */
    public String getDisplayFormat() {
        return String.format("%s %s", cityState.displayFormat(), zipCode);
    }
    
    /**
     * Checks if this address has valid components.
     */
    public boolean isValid() {
        return !zipCode.isEmpty() && cityState != null && cityState.isValid();
    }
    
    /**
     * Returns a new ModernAddress with updated zip code.
     * Demonstrates immutable update patterns.
     */
    public ModernAddress withZipCode(String newZipCode) {
        return new Builder()
            .withCityState(this.cityState)
            .withZipCode(newZipCode)
            .build();
    }
    
    /**
     * Returns a new ModernAddress with updated city and state.
     * Demonstrates immutable update patterns.
     */
    public ModernAddress withCityState(String city, String state) {
        return new Builder()
            .withCityState(CityState.of(city, state))
            .withZipCode(this.zipCode)
            .build();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ModernAddress address = (ModernAddress) obj;
        return Objects.equals(zipCode, address.zipCode) && 
               Objects.equals(cityState, address.cityState);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(zipCode, cityState);
    }
    
    @Override
    public String toString() {
        return String.format("ModernAddress{city='%s', state='%s', zipCode='%s'}", 
                           getCity(), getState(), zipCode);
    }
    
    /**
     * Builder class for constructing ModernAddress instances.
     * Demonstrates modern builder pattern with method chaining.
     */
    public static class Builder {
        private String zipCode;
        private CityState cityState;
        
        public Builder withZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }
        
        public Builder withCityState(CityState cityState) {
            this.cityState = cityState;
            return this;
        }
        
        public Builder withCityAndState(String city, String state) {
            this.cityState = CityState.of(city, state);
            return this;
        }
        
        public ModernAddress build() {
            return new ModernAddress(this);
        }
    }
    
    /**
     * Functional interface for city/state lookup operations.
     * This modernizes the dependency on CityStateLookup by using functional programming.
     */
    @FunctionalInterface
    public interface CityStateLookupFunction {
        CityState lookup(String zipCode);
    }
}
