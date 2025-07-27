package com.neopragma.legacy.round15;

import java.util.Objects;

/**
 * Modern immutable SSN value object using Java 8 patterns.
 * 
 * This modernizes the legacy SsnImpl by:
 * - Making the class immutable with final fields
 * - Using factory methods for construction
 * - Implementing proper equals(), hashCode(), and toString()
 * - Using modern string formatting instead of StringBuilder
 * - Leveraging the SsnValidator for validation logic
 * - Providing null-safe operations
 */
public final class ModernSsn {
    private final String ssn;
    
    /**
     * Private constructor to enforce validation through factory methods.
     */
    private ModernSsn(String ssn) {
        this.ssn = SsnValidator.normalize(ssn);
        SsnValidator.validateOrThrow(this.ssn);
    }
    
    /**
     * Factory method to create a valid SSN.
     * 
     * @param ssn the SSN string (with or without dashes)
     * @return ModernSsn instance
     * @throws SsnValidationException if the SSN is invalid
     */
    public static ModernSsn of(String ssn) {
        return new ModernSsn(ssn);
    }
    
    /**
     * Factory method that returns null for invalid SSNs instead of throwing.
     * Demonstrates modern null-safe patterns.
     * 
     * @param ssn the SSN string to validate
     * @return ModernSsn instance or null if invalid
     */
    public static ModernSsn ofNullable(String ssn) {
        try {
            return new ModernSsn(ssn);
        } catch (SsnValidationException e) {
            return null;
        }
    }
    
    /**
     * Returns the raw SSN digits (no formatting).
     */
    public String getValue() {
        return ssn;
    }
    
    /**
     * Formats the SSN with dashes (XXX-XX-XXXX).
     * Uses modern String.format instead of StringBuilder.
     */
    public String getFormatted() {
        if (ssn.length() != 9) {
            return ssn; // Return as-is if somehow invalid
        }
        
        return String.format("%s-%s-%s", 
                           ssn.substring(0, 3),
                           ssn.substring(3, 5),
                           ssn.substring(5));
    }
    
    /**
     * Alias for getFormatted() for API compatibility.
     */
    public String getFormattedSsn() {
        return getFormatted();
    }
    
    /**
     * Validates this SSN and returns the validation result.
     * Useful for checking validation status without exceptions.
     */
    public ValidationResult validate() {
        return SsnValidator.validate(ssn);
    }
    
    /**
     * Checks if this SSN is valid.
     */
    public boolean isValid() {
        return validate().isValid();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ModernSsn modernSsn = (ModernSsn) obj;
        return Objects.equals(ssn, modernSsn.ssn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ssn);
    }
    
    @Override
    public String toString() {
        return String.format("ModernSsn{value='%s'}", getFormatted());
    }
}
