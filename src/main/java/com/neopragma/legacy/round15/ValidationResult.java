package com.neopragma.legacy.round15;

/**
 * Enum to replace magic number return codes from validation methods.
 * 
 * This modernizes the legacy approach of returning integer codes (0, 1, 2, 3, 4, 6)
 * with a type-safe enum that provides:
 * - Clear semantic meaning for each validation state
 * - Type safety (no invalid codes possible)
 * - Self-documenting code
 * - Easy extensibility for new validation cases
 * 
 * Benefits over magic numbers:
 * - Eliminates need to remember what each number means
 * - Prevents invalid return codes
 * - Makes code more readable and maintainable
 * - Enables IDE autocompletion and refactoring support
 */
public enum ValidationResult {
    VALID(0, "Valid"),
    INVALID_FORMAT(1, "Invalid format"),
    INVALID_AREA_NUMBER(2, "Invalid area number - cannot start with 000, 666, or 9"),
    INVALID_SERIAL_NUMBER(3, "Invalid serial number - cannot be 0000"),
    SPECIAL_CASE_INVALID(4, "Special case - this number is reserved and cannot be used"),
    MISSING_REQUIRED_FIELD(6, "Missing required field");
    
    private final int legacyCode;
    private final String message;
    
    ValidationResult(int legacyCode, String message) {
        this.legacyCode = legacyCode;
        this.message = message;
    }
    
    /**
     * Returns the legacy integer code for backward compatibility.
     * This allows gradual migration from integer-based validation.
     */
    public int getLegacyCode() {
        return legacyCode;
    }
    
    /**
     * Returns a human-readable description of the validation result.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Convenience method to check if validation passed.
     */
    public boolean isValid() {
        return this == VALID;
    }
    
    /**
     * Factory method to create ValidationResult from legacy integer codes.
     * Useful for migrating existing code that uses integer return values.
     */
    public static ValidationResult fromLegacyCode(int code) {
        for (ValidationResult result : values()) {
            if (result.legacyCode == code) {
                return result;
            }
        }
        throw new IllegalArgumentException("Unknown validation code: " + code);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%d): %s", name(), legacyCode, message);
    }
}
