package com.neopragma.legacy.round15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Modern HTTP client utility demonstrating Java 8+ patterns.
 * 
 * This modernizes legacy Apache HttpClient usage by:
 * - Using builder pattern for URL construction
 * - Implementing try-with-resources for proper resource management
 * - Adding functional programming patterns for response processing
 * - Using Optional for null-safe operations
 * - Providing async operations with CompletableFuture
 * - Demonstrating modern error handling patterns
 * - Creating reusable HTTP request abstractions
 */
public final class ModernHttpClient {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ModernHttpClient() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Performs HTTP GET request with modern resource management.
     * Replaces legacy manual try-finally blocks with try-with-resources.
     */
    public static Optional<String> get(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return Optional.empty();
                }
                
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(entity.getContent()))) {
                    
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return Optional.of(result.toString());
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Performs HTTP GET request with functional response processing.
     * Demonstrates modern approach to handling HTTP responses with functions.
     */
    public static <T> Optional<T> get(String url, Function<String, T> responseProcessor) {
        return get(url).map(responseProcessor);
    }
    
    /**
     * Builds URL using modern builder pattern.
     * Replaces manual URIBuilder usage with fluent API.
     */
    public static class UrlBuilder {
        private final URIBuilder uriBuilder;
        
        private UrlBuilder() {
            this.uriBuilder = new URIBuilder();
        }
        
        public static UrlBuilder create() {
            return new UrlBuilder();
        }
        
        public UrlBuilder scheme(String scheme) {
            uriBuilder.setScheme(scheme);
            return this;
        }
        
        public UrlBuilder host(String host) {
            uriBuilder.setHost(host);
            return this;
        }
        
        public UrlBuilder path(String path) {
            uriBuilder.setPath(path);
            return this;
        }
        
        public UrlBuilder parameter(String name, String value) {
            uriBuilder.setParameter(name, value);
            return this;
        }
        
        public Optional<String> build() {
            try {
                URI uri = uriBuilder.build();
                return Optional.of(uri.toString());
            } catch (URISyntaxException e) {
                return Optional.empty();
            }
        }
    }
    
    /**
     * Performs asynchronous HTTP GET request using CompletableFuture.
     * Demonstrates modern async programming patterns.
     */
    public static CompletableFuture<Optional<String>> getAsync(String url) {
        return CompletableFuture.supplyAsync(() -> get(url));
    }
    
    /**
     * Performs asynchronous HTTP GET with response processing.
     * Combines async operations with functional programming.
     */
    public static <T> CompletableFuture<Optional<T>> getAsync(String url, Function<String, T> responseProcessor) {
        return getAsync(url).thenApply(optionalResponse -> 
            optionalResponse.map(responseProcessor));
    }
    
    /**
     * Creates a specialized client for ZIP code API calls.
     * Demonstrates domain-specific HTTP client abstractions.
     */
    public static class ZipCodeClient {
        private static final String API_HOST = "api.zippopotam.us";
        private static final String API_SCHEME = "http";
        
        public static Optional<String> lookupByZipCode(String zipCode) {
            if (zipCode == null || zipCode.length() < 5) {
                return Optional.empty();
            }
            
            Optional<String> url = UrlBuilder.create()
                    .scheme(API_SCHEME)
                    .host(API_HOST)
                    .path("/us/" + zipCode.substring(0, 5))
                    .build();
            
            return url.flatMap(ModernHttpClient::get);
        }
        
        public static CompletableFuture<Optional<String>> lookupByZipCodeAsync(String zipCode) {
            if (zipCode == null || zipCode.length() < 5) {
                return CompletableFuture.completedFuture(Optional.empty());
            }
            
            Optional<String> url = UrlBuilder.create()
                    .scheme(API_SCHEME)
                    .host(API_HOST)
                    .path("/us/" + zipCode.substring(0, 5))
                    .build();
            
            return url.map(ModernHttpClient::getAsync)
                     .orElse(CompletableFuture.completedFuture(Optional.empty()));
        }
    }
    
    /**
     * HTTP response wrapper providing modern response handling.
     * Demonstrates encapsulation of HTTP response data.
     */
    public static class HttpResponse {
        private final String body;
        private final boolean success;
        
        private HttpResponse(String body, boolean success) {
            this.body = body;
            this.success = success;
        }
        
        public static HttpResponse success(String body) {
            return new HttpResponse(body, true);
        }
        
        public static HttpResponse failure() {
            return new HttpResponse("", false);
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public Optional<String> getBody() {
            return success ? Optional.of(body) : Optional.empty();
        }
        
        public <T> Optional<T> map(Function<String, T> mapper) {
            return getBody().map(mapper);
        }
    }
    
    /**
     * Performs HTTP GET with detailed response information.
     * Provides more control over response handling than simple Optional<String>.
     */
    public static HttpResponse getWithResponse(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return HttpResponse.failure();
                }
                
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(entity.getContent()))) {
                    
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return HttpResponse.success(result.toString());
                }
            }
        } catch (IOException e) {
            return HttpResponse.failure();
        }
    }
}
