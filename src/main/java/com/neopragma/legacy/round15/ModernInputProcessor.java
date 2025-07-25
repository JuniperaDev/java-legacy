package com.neopragma.legacy.round15;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Modern input processing utility using functional programming patterns.
 * 
 * This modernizes legacy while-loop input processing by:
 * - Using functional programming for input validation and processing
 * - Implementing builder pattern for complex input scenarios
 * - Providing Stream-based input processing
 * - Adding null-safe input operations
 * - Using Optional for missing/invalid inputs
 * - Demonstrating modern command pattern for user interactions
 */
public final class ModernInputProcessor {
    
    private final Scanner scanner;
    private final Map<String, InputValidator> validators;
    private final Map<String, String> prompts;
    
    /**
     * Private constructor for builder pattern.
     */
    private ModernInputProcessor(Builder builder) {
        this.scanner = builder.scanner != null ? builder.scanner : new Scanner(System.in);
        this.validators = new HashMap<>(builder.validators);
        this.prompts = new HashMap<>(builder.prompts);
    }
    
    /**
     * Creates a new builder for constructing ModernInputProcessor.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Processes user input using functional validation.
     * Replaces legacy while-loop input processing with functional approach.
     */
    public Optional<String> promptFor(String fieldName) {
        String prompt = prompts.getOrDefault(fieldName, fieldName + "?");
        InputValidator validator = validators.getOrDefault(fieldName, InputValidator.alwaysValid());
        
        System.out.println(prompt);
        String input = scanner.nextLine();
        
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return validator.validate(input.trim()) ? 
               Optional.of(input.trim()) : 
               Optional.empty();
    }
    
    /**
     * Processes multiple inputs using Stream API.
     * Demonstrates functional approach to collecting multiple inputs.
     */
    public Map<String, String> promptForMultiple(List<String> fieldNames) {
        if (fieldNames == null || fieldNames.isEmpty()) {
            return new HashMap<>();
        }
        
        return fieldNames.stream()
                        .collect(HashMap::new,
                               (map, field) -> promptFor(field).ifPresent(value -> map.put(field, value)),
                               HashMap::putAll);
    }
    
    /**
     * Processes input with retry logic using functional programming.
     * Replaces manual retry loops with functional retry mechanism.
     */
    public Optional<String> promptWithRetry(String fieldName, int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            if (attempt > 1) {
                System.out.println("Invalid input. Please try again.");
            }
            Optional<String> result = promptFor(fieldName);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }
    
    /**
     * Processes input until a termination condition is met.
     * Modernizes legacy while(!done) patterns with functional streaming.
     */
    public List<String> collectUntil(String fieldName, Predicate<String> terminationCondition) {
        List<String> results = new ArrayList<>();
        
        while (true) {
            Optional<String> input = promptFor(fieldName);
            if (input.isPresent()) {
                String value = input.get();
                if (terminationCondition.test(value)) {
                    break;
                }
                results.add(value);
            }
        }
        
        return results;
    }
    
    /**
     * Processes structured input using command pattern.
     * Demonstrates modern approach to handling different input types.
     */
    public <T> Optional<T> processStructuredInput(String fieldName, Function<String, T> parser) {
        return promptFor(fieldName)
                .map(input -> {
                    try {
                        return parser.apply(input);
                    } catch (Exception e) {
                        System.out.println("Invalid format: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }
    
    /**
     * Closes the scanner resource safely.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
    
    /**
     * Builder class for constructing ModernInputProcessor instances.
     */
    public static class Builder {
        private Scanner scanner;
        private final Map<String, InputValidator> validators = new HashMap<>();
        private final Map<String, String> prompts = new HashMap<>();
        
        public Builder withScanner(Scanner scanner) {
            this.scanner = scanner;
            return this;
        }
        
        public Builder withValidator(String fieldName, InputValidator validator) {
            this.validators.put(fieldName, validator);
            return this;
        }
        
        public Builder withPrompt(String fieldName, String prompt) {
            this.prompts.put(fieldName, prompt);
            return this;
        }
        
        public Builder withField(String fieldName, String prompt, InputValidator validator) {
            this.prompts.put(fieldName, prompt);
            this.validators.put(fieldName, validator);
            return this;
        }
        
        public ModernInputProcessor build() {
            return new ModernInputProcessor(this);
        }
    }
    
    /**
     * Functional interface for input validation.
     */
    @FunctionalInterface
    public interface InputValidator {
        boolean validate(String input);
        
        static InputValidator alwaysValid() {
            return input -> true;
        }
        
        static InputValidator notEmpty() {
            return input -> input != null && !input.trim().isEmpty();
        }
        
        static InputValidator matches(String regex) {
            return input -> input != null && input.matches(regex);
        }
        
        static InputValidator minLength(int minLength) {
            return input -> input != null && input.length() >= minLength;
        }
        
        static InputValidator maxLength(int maxLength) {
            return input -> input != null && input.length() <= maxLength;
        }
        
        static InputValidator and(InputValidator first, InputValidator second) {
            return input -> first.validate(input) && second.validate(input);
        }
        
        static InputValidator or(InputValidator first, InputValidator second) {
            return input -> first.validate(input) || second.validate(input);
        }
    }
}
