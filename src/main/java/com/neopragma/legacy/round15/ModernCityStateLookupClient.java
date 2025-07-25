package com.neopragma.legacy.round15;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Modern city/state lookup client demonstrating HTTP modernization.
 * 
 * This modernizes legacy CityStateLookupImpl by:
 * - Using ModernHttpClient for HTTP operations
 * - Implementing functional programming patterns
 * - Adding async operations with CompletableFuture
 * - Using Optional for null-safe operations
 * - Providing clean separation of concerns
 * - Demonstrating modern JSON processing patterns
 */
public final class ModernCityStateLookupClient {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ModernCityStateLookupClient() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Looks up city and state by ZIP code using modern HTTP patterns.
     * Replaces legacy manual HTTP client management with functional approach.
     */
    public static Optional<CityState> lookup(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return ModernHttpClient.ZipCodeClient.lookupByZipCode(zipCode)
                .flatMap(ModernCityStateLookupClient::parseZipCodeResponse);
    }
    
    /**
     * Performs asynchronous city/state lookup.
     * Demonstrates modern async programming with CompletableFuture.
     */
    public static CompletableFuture<Optional<CityState>> lookupAsync(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        
        return ModernHttpClient.ZipCodeClient.lookupByZipCodeAsync(zipCode)
                .thenApply(optionalResponse -> 
                    optionalResponse.flatMap(ModernCityStateLookupClient::parseZipCodeResponse));
    }
    
    /**
     * Parses ZIP code API response using functional programming.
     * Replaces legacy manual JSON parsing with modern Optional chains.
     */
    private static Optional<CityState> parseZipCodeResponse(String jsonResponse) {
        try {
            JsonElement element = new JsonParser().parse(jsonResponse);
            if (!element.isJsonObject()) {
                return Optional.empty();
            }
            
            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray places = jsonObject.getAsJsonArray("places");
            
            if (places == null || places.size() == 0) {
                return Optional.empty();
            }
            
            JsonObject place = places.get(0).getAsJsonObject();
            String city = getJsonString(place, "place name");
            String state = getJsonString(place, "state abbreviation");
            
            if (city.isEmpty() || state.isEmpty()) {
                return Optional.empty();
            }
            
            return Optional.of(CityState.of(city, state));
            
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Safely extracts string value from JSON object.
     * Provides null-safe JSON field access.
     */
    private static String getJsonString(JsonObject jsonObject, String fieldName) {
        JsonElement element = jsonObject.get(fieldName);
        return element != null && !element.isJsonNull() ? element.getAsString() : "";
    }
    
    /**
     * Looks up multiple ZIP codes concurrently.
     * Demonstrates parallel processing with CompletableFuture.
     */
    public static CompletableFuture<java.util.List<Optional<CityState>>> lookupMultiple(String... zipCodes) {
        if (zipCodes == null || zipCodes.length == 0) {
            return CompletableFuture.completedFuture(new java.util.ArrayList<>());
        }
        
        java.util.List<CompletableFuture<Optional<CityState>>> futures = new java.util.ArrayList<>();
        
        for (String zipCode : zipCodes) {
            futures.add(lookupAsync(zipCode));
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    java.util.List<Optional<CityState>> results = new java.util.ArrayList<>();
                    for (CompletableFuture<Optional<CityState>> future : futures) {
                        results.add(future.join());
                    }
                    return results;
                });
    }
    
    /**
     * Validates ZIP code format before lookup.
     * Demonstrates input validation with functional programming.
     */
    public static Optional<CityState> lookupWithValidation(String zipCode) {
        return validateZipCode(zipCode)
                .flatMap(ModernCityStateLookupClient::lookup);
    }
    
    /**
     * Validates ZIP code format using modern patterns.
     * Replaces manual validation with Optional-based validation.
     */
    private static Optional<String> validateZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            return Optional.empty();
        }
        
        String cleaned = zipCode.trim();
        if (cleaned.matches("\\d{5}(-\\d{4})?")) {
            return Optional.of(cleaned);
        }
        
        return Optional.empty();
    }
}
