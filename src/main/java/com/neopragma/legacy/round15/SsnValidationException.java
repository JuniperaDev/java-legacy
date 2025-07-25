package com.neopragma.legacy.round15;

/**
 * Modern exception class for SSN validation errors.
 * 
 * This modernizes exception handling by:
 * - Using enum-based error categorization
 * - Providing structured error information
 * - Supporting both legacy integer codes and modern enum types
 * - Including detailed error context
 */
public class SsnValidationException extends RuntimeException {
    private final ValidationResult validationResult;
    private final String invalidValue;
    
    public SsnValidationException(ValidationResult result, String invalidValue) {
        super(String.format("SSN validation failed: %s (value: %s)", 
                          result.getMessage(), invalidValue));
        this.validationResult = result;
        this.invalidValue = invalidValue;
    }
    
    public SsnValidationException(ValidationResult result, String invalidValue, String customMessage) {
        super(customMessage);
        this.validationResult = result;
        this.invalidValue = invalidValue;
    }
    
    public ValidationResult getValidationResult() {
        return validationResult;
    }
    
    public String getInvalidValue() {
        return invalidValue;
    }
    
    /**
     * Returns the legacy error code for backward compatibility.
     */
    public int getLegacyErrorCode() {
        return validationResult.getLegacyCode();
    }
}
