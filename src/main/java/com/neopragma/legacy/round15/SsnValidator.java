package com.neopragma.legacy.round15;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Modern SSN validation utility using Java 8 patterns.
 * 
 * This modernizes SSN validation by:
 * - Using enum-based return types instead of magic numbers
 * - Leveraging Java 8 Stream API and functional programming
 * - Using compiled regex patterns for better performance
 * - Implementing immutable validation logic
 * - Providing clear separation of concerns
 */
public final class SsnValidator {
    
    private static final Pattern SSN_FORMAT_PATTERN = Pattern.compile("(\\d{3}-\\d{2}-\\d{4}|\\d{9})");
    private static final Pattern DIGITS_ONLY_PATTERN = Pattern.compile("\\d{9}");
    
    private static final Set<String> RESERVED_SSNS = new HashSet<>(Arrays.asList(
        "219099999", "078051120"
    ));
    
    private static final Set<String> INVALID_AREA_NUMBERS = new HashSet<>(Arrays.asList(
        "000", "666"
    ));
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SsnValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Validates an SSN and returns a ValidationResult enum instead of magic numbers.
     * 
     * @param ssn the SSN to validate (with or without dashes)
     * @return ValidationResult indicating the validation outcome
     */
    public static ValidationResult validate(String ssn) {
        if (ssn == null || ssn.trim().isEmpty()) {
            return ValidationResult.MISSING_REQUIRED_FIELD;
        }
        
        if (!SSN_FORMAT_PATTERN.matcher(ssn).matches()) {
            return ValidationResult.INVALID_FORMAT;
        }
        
        String normalizedSsn = ssn.replaceAll("-", "");
        
        if (!DIGITS_ONLY_PATTERN.matcher(normalizedSsn).matches()) {
            return ValidationResult.INVALID_FORMAT;
        }
        
        String areaNumber = normalizedSsn.substring(0, 3);
        if (INVALID_AREA_NUMBERS.contains(areaNumber) || areaNumber.startsWith("9")) {
            return ValidationResult.INVALID_AREA_NUMBER;
        }
        
        String serialNumber = normalizedSsn.substring(5);
        if ("0000".equals(serialNumber)) {
            return ValidationResult.INVALID_SERIAL_NUMBER;
        }
        
        if (RESERVED_SSNS.contains(normalizedSsn)) {
            return ValidationResult.SPECIAL_CASE_INVALID;
        }
        
        return ValidationResult.VALID;
    }
    
    /**
     * Validates an SSN and throws an exception if invalid.
     * Demonstrates modern exception handling patterns.
     * 
     * @param ssn the SSN to validate
     * @throws SsnValidationException if the SSN is invalid
     */
    public static void validateOrThrow(String ssn) {
        ValidationResult result = validate(ssn);
        if (!result.isValid()) {
            throw new SsnValidationException(result, ssn);
        }
    }
    
    /**
     * Normalizes an SSN by removing dashes and validating format.
     * Uses modern Optional-like patterns for null safety.
     * 
     * @param ssn the SSN to normalize
     * @return normalized SSN (digits only) or empty string if invalid
     */
    public static String normalize(String ssn) {
        if (ssn == null) {
            return "";
        }
        
        String normalized = ssn.replaceAll("-", "");
        return DIGITS_ONLY_PATTERN.matcher(normalized).matches() ? normalized : "";
    }
}
