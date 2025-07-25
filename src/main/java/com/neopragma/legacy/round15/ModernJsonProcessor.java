package com.neopragma.legacy.round15;

import java.util.Optional;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Modern JSON processing utility demonstrating Java 8+ patterns.
 * 
 * This modernizes legacy JSON parsing by:
 * - Using Optional for null-safe JSON operations
 * - Implementing functional programming patterns for JSON processing
 * - Adding type-safe JSON field extraction methods
 * - Providing reusable JSON parsing abstractions
 * - Using Stream API for JSON array processing
 * - Demonstrating modern error handling without exceptions
 * - Creating fluent API for JSON navigation
 */
public final class ModernJsonProcessor {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ModernJsonProcessor() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Parses JSON string safely using Optional.
     * Replaces legacy: JsonElement element = new JsonParser().parse(jsonString);
     */
    public static Optional<JsonElement> parseJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            JsonElement element = new JsonParser().parse(jsonString);
            return Optional.of(element);
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Safely extracts JsonObject from JsonElement.
     * Provides null-safe object extraction with Optional.
     */
    public static Optional<JsonObject> asObject(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return Optional.empty();
        }
        return Optional.of(element.getAsJsonObject());
    }
    
    /**
     * Safely extracts JsonArray from JsonElement.
     * Provides null-safe array extraction with Optional.
     */
    public static Optional<JsonArray> asArray(JsonElement element) {
        if (element == null || !element.isJsonArray()) {
            return Optional.empty();
        }
        return Optional.of(element.getAsJsonArray());
    }
    
    /**
     * Safely extracts string value from JsonObject field.
     * Replaces legacy: String value = jsonObject.get("field").getAsString();
     */
    public static Optional<String> getString(JsonObject jsonObject, String fieldName) {
        if (jsonObject == null || fieldName == null) {
            return Optional.empty();
        }
        
        JsonElement element = jsonObject.get(fieldName);
        if (element == null || element.isJsonNull()) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(element.getAsString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Safely extracts integer value from JsonObject field.
     * Provides type-safe integer extraction with Optional.
     */
    public static Optional<Integer> getInt(JsonObject jsonObject, String fieldName) {
        if (jsonObject == null || fieldName == null) {
            return Optional.empty();
        }
        
        JsonElement element = jsonObject.get(fieldName);
        if (element == null || element.isJsonNull()) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(element.getAsInt());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Safely extracts JsonArray from JsonObject field.
     * Provides null-safe array field extraction.
     */
    public static Optional<JsonArray> getArray(JsonObject jsonObject, String fieldName) {
        if (jsonObject == null || fieldName == null) {
            return Optional.empty();
        }
        
        JsonElement element = jsonObject.get(fieldName);
        return asArray(element);
    }
    
    /**
     * Safely extracts JsonObject from JsonObject field.
     * Provides null-safe nested object extraction.
     */
    public static Optional<JsonObject> getObject(JsonObject jsonObject, String fieldName) {
        if (jsonObject == null || fieldName == null) {
            return Optional.empty();
        }
        
        JsonElement element = jsonObject.get(fieldName);
        return asObject(element);
    }
    
    /**
     * Processes JsonArray elements using functional programming.
     * Replaces manual for-loop array iteration with Stream-like operations.
     */
    public static <T> List<T> mapArray(JsonArray jsonArray, Function<JsonElement, Optional<T>> mapper) {
        List<T> results = new ArrayList<>();
        
        if (jsonArray == null || mapper == null) {
            return results;
        }
        
        for (JsonElement element : jsonArray) {
            mapper.apply(element).ifPresent(results::add);
        }
        
        return results;
    }
    
    /**
     * Finds first matching element in JsonArray using predicate.
     * Demonstrates functional approach to JSON array searching.
     */
    public static Optional<JsonElement> findFirst(JsonArray jsonArray, Function<JsonElement, Boolean> predicate) {
        if (jsonArray == null || predicate == null) {
            return Optional.empty();
        }
        
        for (JsonElement element : jsonArray) {
            if (predicate.apply(element)) {
                return Optional.of(element);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Fluent JSON navigation builder for complex JSON structures.
     * Provides chainable API for navigating nested JSON.
     */
    public static class JsonNavigator {
        private Optional<JsonElement> current;
        
        private JsonNavigator(Optional<JsonElement> element) {
            this.current = element;
        }
        
        public static JsonNavigator from(String jsonString) {
            return new JsonNavigator(parseJson(jsonString));
        }
        
        public static JsonNavigator from(JsonElement element) {
            return new JsonNavigator(Optional.ofNullable(element));
        }
        
        public JsonNavigator asObject() {
            current = current.filter(JsonElement::isJsonObject)
                            .map(JsonElement::getAsJsonObject);
            return this;
        }
        
        public JsonNavigator asArray() {
            current = current.filter(JsonElement::isJsonArray)
                            .map(JsonElement::getAsJsonArray);
            return this;
        }
        
        public JsonNavigator field(String fieldName) {
            current = current
                    .flatMap(ModernJsonProcessor::asObject)
                    .map(obj -> obj.get(fieldName))
                    .filter(element -> element != null && !element.isJsonNull());
            return this;
        }
        
        public JsonNavigator index(int index) {
            current = current
                    .flatMap(ModernJsonProcessor::asArray)
                    .filter(array -> index >= 0 && index < array.size())
                    .map(array -> array.get(index));
            return this;
        }
        
        public Optional<String> asString() {
            return current.flatMap(element -> {
                try {
                    return Optional.of(element.getAsString());
                } catch (Exception e) {
                    return Optional.empty();
                }
            });
        }
        
        public Optional<Integer> asInt() {
            return current.flatMap(element -> {
                try {
                    return Optional.of(element.getAsInt());
                } catch (Exception e) {
                    return Optional.empty();
                }
            });
        }
        
        public Optional<JsonElement> get() {
            return current;
        }
        
        public boolean isPresent() {
            return current.isPresent();
        }
    }
    
    /**
     * Specialized processor for ZIP code API responses.
     * Demonstrates domain-specific JSON processing patterns.
     */
    public static class ZipCodeResponseProcessor {
        
        public static Optional<CityState> processCityStateResponse(String jsonResponse) {
            return JsonNavigator.from(jsonResponse)
                    .asObject()
                    .field("places")
                    .asArray()
                    .index(0)
                    .asObject()
                    .get()
                    .flatMap(ZipCodeResponseProcessor::extractCityState);
        }
        
        private static Optional<CityState> extractCityState(JsonElement placeElement) {
            return asObject(placeElement)
                    .flatMap(place -> {
                        Optional<String> city = getString(place, "place name");
                        Optional<String> state = getString(place, "state abbreviation");
                        
                        if (city.isPresent() && state.isPresent()) {
                            return Optional.of(CityState.of(city.get(), state.get()));
                        }
                        return Optional.empty();
                    });
        }
        
        public static List<CityState> processMultipleCityStates(String jsonResponse) {
            Optional<JsonElement> placesElement = JsonNavigator.from(jsonResponse)
                    .asObject()
                    .field("places")
                    .get();
            
            if (!placesElement.isPresent() || !placesElement.get().isJsonArray()) {
                return new ArrayList<>();
            }
            
            JsonArray placesArray = placesElement.get().getAsJsonArray();
            return mapArray(placesArray, ZipCodeResponseProcessor::extractCityState);
        }
    }
    
    /**
     * Validates JSON structure before processing.
     * Provides structural validation with functional patterns.
     */
    public static boolean hasRequiredFields(JsonObject jsonObject, String... requiredFields) {
        if (jsonObject == null || requiredFields == null) {
            return false;
        }
        
        for (String field : requiredFields) {
            if (!jsonObject.has(field) || jsonObject.get(field).isJsonNull()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Transforms JSON using functional mapping.
     * Demonstrates functional JSON transformation patterns.
     */
    public static <T> Optional<T> transform(String jsonString, Function<JsonElement, Optional<T>> transformer) {
        return parseJson(jsonString).flatMap(transformer);
    }
}
