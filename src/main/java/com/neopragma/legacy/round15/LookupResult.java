package com.neopragma.legacy.round15;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Modern Result type for handling success/failure scenarios without checked exceptions.
 * 
 * This modernizes exception handling by:
 * - Replacing checked exceptions with explicit Result types
 * - Using functional programming patterns for error handling
 * - Providing type-safe success/failure operations
 * - Enabling method chaining and composition
 * - Making error handling explicit in method signatures
 */
public abstract class LookupResult<T> {
    
    /**
     * Creates a successful result containing a value.
     */
    public static <T> LookupResult<T> success(T value) {
        return new Success<>(value);
    }
    
    /**
     * Creates a failed result containing an error.
     */
    public static <T> LookupResult<T> failure(String errorMessage) {
        return new Failure<>(errorMessage);
    }
    
    /**
     * Creates a failed result from an exception.
     */
    public static <T> LookupResult<T> failure(Exception exception) {
        return new Failure<>(exception.getMessage());
    }
    
    /**
     * Returns true if this result represents success.
     */
    public abstract boolean isSuccess();
    
    /**
     * Returns true if this result represents failure.
     */
    public boolean isFailure() {
        return !isSuccess();
    }
    
    /**
     * Returns the value if successful, or throws RuntimeException if failed.
     */
    public abstract T getValue();
    
    /**
     * Returns the value if successful, or the default value if failed.
     */
    public abstract T getValueOrDefault(T defaultValue);
    
    /**
     * Returns the error message if failed, or null if successful.
     */
    public abstract String getErrorMessage();
    
    /**
     * Transforms the value if successful, or returns the same failure.
     */
    public abstract <U> LookupResult<U> map(Function<T, U> mapper);
    
    /**
     * Flat-maps the value if successful, or returns the same failure.
     */
    public abstract <U> LookupResult<U> flatMap(Function<T, LookupResult<U>> mapper);
    
    /**
     * Executes the consumer if successful.
     */
    public abstract LookupResult<T> ifSuccess(Consumer<T> consumer);
    
    /**
     * Executes the consumer if failed.
     */
    public abstract LookupResult<T> ifFailure(Consumer<String> consumer);
    
    /**
     * Success implementation of LookupResult.
     */
    private static final class Success<T> extends LookupResult<T> {
        private final T value;
        
        private Success(T value) {
            this.value = Objects.requireNonNull(value, "Success value cannot be null");
        }
        
        @Override
        public boolean isSuccess() {
            return true;
        }
        
        @Override
        public T getValue() {
            return value;
        }
        
        @Override
        public T getValueOrDefault(T defaultValue) {
            return value;
        }
        
        @Override
        public String getErrorMessage() {
            return null;
        }
        
        @Override
        public <U> LookupResult<U> map(Function<T, U> mapper) {
            try {
                return success(mapper.apply(value));
            } catch (Exception e) {
                return failure(e);
            }
        }
        
        @Override
        public <U> LookupResult<U> flatMap(Function<T, LookupResult<U>> mapper) {
            try {
                return mapper.apply(value);
            } catch (Exception e) {
                return failure(e);
            }
        }
        
        @Override
        public LookupResult<T> ifSuccess(Consumer<T> consumer) {
            consumer.accept(value);
            return this;
        }
        
        @Override
        public LookupResult<T> ifFailure(Consumer<String> consumer) {
            return this;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Success<?> success = (Success<?>) obj;
            return Objects.equals(value, success.value);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
        
        @Override
        public String toString() {
            return String.format("Success{value=%s}", value);
        }
    }
    
    /**
     * Failure implementation of LookupResult.
     */
    private static final class Failure<T> extends LookupResult<T> {
        private final String errorMessage;
        
        private Failure(String errorMessage) {
            this.errorMessage = Objects.requireNonNull(errorMessage, "Error message cannot be null");
        }
        
        @Override
        public boolean isSuccess() {
            return false;
        }
        
        @Override
        public T getValue() {
            throw new RuntimeException("Cannot get value from failed result: " + errorMessage);
        }
        
        @Override
        public T getValueOrDefault(T defaultValue) {
            return defaultValue;
        }
        
        @Override
        public String getErrorMessage() {
            return errorMessage;
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public <U> LookupResult<U> map(Function<T, U> mapper) {
            return (LookupResult<U>) this;
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public <U> LookupResult<U> flatMap(Function<T, LookupResult<U>> mapper) {
            return (LookupResult<U>) this;
        }
        
        @Override
        public LookupResult<T> ifSuccess(Consumer<T> consumer) {
            return this;
        }
        
        @Override
        public LookupResult<T> ifFailure(Consumer<String> consumer) {
            consumer.accept(errorMessage);
            return this;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Failure<?> failure = (Failure<?>) obj;
            return Objects.equals(errorMessage, failure.errorMessage);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(errorMessage);
        }
        
        @Override
        public String toString() {
            return String.format("Failure{errorMessage='%s'}", errorMessage);
        }
    }
}
