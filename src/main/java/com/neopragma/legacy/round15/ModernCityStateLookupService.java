package com.neopragma.legacy.round15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Modern city/state lookup service using Result types and functional programming.
 * 
 * This modernizes the legacy CityStateLookupImpl by:
 * - Replacing checked exceptions with Result types
 * - Using modern HTTP client patterns
 * - Implementing async operations with CompletableFuture
 * - Adding proper input validation and error categorization
 * - Using functional programming for error handling
 * - Providing both sync and async APIs
 */
public final class ModernCityStateLookupService {
    
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("\\d{5}(-\\d{4})?");
    private static final String API_BASE_URL = "http://api.zippopotam.us/us/";
    private static final int TIMEOUT_MS = 5000;
    
    private final Executor executor;
    
    /**
     * Creates a new lookup service with default thread pool.
     */
    public ModernCityStateLookupService() {
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "CityStateLookup");
            t.setDaemon(true);
            return t;
        });
    }
    
    /**
     * Creates a new lookup service with custom executor.
     */
    public ModernCityStateLookupService(Executor executor) {
        this.executor = executor;
    }
    
    /**
     * Synchronous lookup that returns a Result instead of throwing exceptions.
     */
    public LookupResult<CityState> lookup(String zipCode) {
        if (!isValidZipCode(zipCode)) {
            return LookupResult.failure("Invalid zip code format: " + zipCode);
        }
        
        try {
            String normalizedZip = normalizeZipCode(zipCode);
            String response = performHttpRequest(normalizedZip);
            CityState cityState = parseResponse(response);
            return LookupResult.success(cityState);
            
        } catch (IOException e) {
            return LookupResult.failure("Network error during lookup: " + e.getMessage());
        } catch (Exception e) {
            return LookupResult.failure("Unexpected error during lookup: " + e.getMessage());
        }
    }
    
    /**
     * Asynchronous lookup using CompletableFuture.
     */
    public CompletableFuture<LookupResult<CityState>> lookupAsync(String zipCode) {
        return CompletableFuture.supplyAsync(() -> lookup(zipCode), executor);
    }
    
    /**
     * Validates zip code format.
     */
    private boolean isValidZipCode(String zipCode) {
        return zipCode != null && ZIP_CODE_PATTERN.matcher(zipCode.trim()).matches();
    }
    
    /**
     * Normalizes zip code to 5-digit format.
     */
    private String normalizeZipCode(String zipCode) {
        return zipCode.trim().substring(0, 5);
    }
    
    /**
     * Performs HTTP request using modern Java HTTP client patterns.
     */
    private String performHttpRequest(String zipCode) throws IOException {
        URL url = new URL(API_BASE_URL + zipCode);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "ModernCityStateLookup/1.0");
            
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP request failed with code: " + responseCode);
            }
            
            return readResponse(connection);
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * Reads HTTP response using modern try-with-resources.
     */
    private String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
    
    /**
     * Parses JSON response using simple string parsing (avoiding external dependencies).
     * In a real implementation, this would use a proper JSON library.
     */
    private CityState parseResponse(String jsonResponse) {
        try {
            String city = extractJsonValue(jsonResponse, "place name");
            String state = extractJsonValue(jsonResponse, "state abbreviation");
            
            if (city.isEmpty() || state.isEmpty()) {
                throw new RuntimeException("Invalid response format: missing city or state");
            }
            
            return CityState.of(city, state);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Simple JSON value extraction (for demonstration purposes).
     * In production, use a proper JSON library.
     */
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return "";
        }
        
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return "";
        }
        
        return json.substring(startIndex, endIndex);
    }
    
    /**
     * Factory method for creating a lookup function compatible with ModernAddress.
     */
    public static ModernAddress.CityStateLookupFunction createLookupFunction() {
        ModernCityStateLookupService service = new ModernCityStateLookupService();
        return zipCode -> {
            LookupResult<CityState> result = service.lookup(zipCode);
            return result.getValueOrDefault(CityState.ofNullable("Unknown", "Unknown"));
        };
    }
    
    /**
     * Factory method for creating an async lookup function.
     */
    public static Function<String, CompletableFuture<CityState>> createAsyncLookupFunction() {
        ModernCityStateLookupService service = new ModernCityStateLookupService();
        return zipCode -> service.lookupAsync(zipCode)
            .thenApply(result -> result.getValueOrDefault(CityState.ofNullable("Unknown", "Unknown")));
    }
    
    /**
     * Functional interface for async lookup operations.
     */
    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
    }
}
