package com.neopragma.legacy.round15;

import java.util.Objects;

/**
 * Modern immutable Name value object using modern string handling.
 * 
 * This modernizes the legacy NameImpl by:
 * - Making the class immutable with final fields
 * - Using ModernStringFormatter instead of StringBuilder
 * - Implementing proper validation with meaningful return types
 * - Adding null-safe construction and operations
 * - Using modern string formatting patterns
 * - Providing factory methods for different name formats
 */
public final class ModernName {
    private final String firstName;
    private final String middleName;
    private final String lastName;
    
    /**
     * Private constructor to enforce validation through factory methods.
     */
    private ModernName(String firstName, String middleName, String lastName) {
        this.firstName = firstName != null ? firstName.trim() : "";
        this.middleName = middleName != null ? middleName.trim() : "";
        this.lastName = lastName != null ? lastName.trim() : "";
    }
    
    /**
     * Factory method for creating a name with standard English format.
     */
    public static ModernName of(String firstName, String middleName, String lastName) {
        return new ModernName(firstName, middleName, lastName);
    }
    
    /**
     * Factory method for creating a name from Spanish naming convention.
     * Combines primer and segundo apellidos into a single last name.
     */
    public static ModernName ofSpanish(String primerNombre, String segundoNombre, 
                                     String primerApellido, String segundoApellido) {
        String combinedLastName = ModernStringFormatter.formatSpanishLastName(primerApellido, segundoApellido);
        return new ModernName(primerNombre, segundoNombre, combinedLastName);
    }
    
    /**
     * Factory method for creating a name with just first and last names.
     */
    public static ModernName of(String firstName, String lastName) {
        return new ModernName(firstName, null, lastName);
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Formats name as "Last, First Middle" using modern string formatting.
     * Replaces legacy StringBuilder concatenation.
     */
    public String formatLastNameFirst() {
        return ModernStringFormatter.formatLastNameFirst(firstName, middleName, lastName);
    }
    
    /**
     * Formats name as "First Middle Last" using modern string joining.
     */
    public String formatFullName() {
        return ModernStringFormatter.formatFullName(firstName, middleName, lastName);
    }
    
    /**
     * Alias for formatFullName() for API compatibility.
     */
    public String getFullName() {
        return formatFullName();
    }
    
    /**
     * Formats name for display with optional middle initial.
     */
    public String formatDisplayName() {
        if (lastName.isEmpty()) {
            return firstName;
        }
        
        if (firstName.isEmpty()) {
            return lastName;
        }
        
        String middleInitial = middleName.isEmpty() ? "" : middleName.substring(0, 1) + ".";
        return ModernStringFormatter.joinWithDelimiter(" ", firstName, middleInitial, lastName);
    }
    
    /**
     * Validates the name and returns a ValidationResult instead of magic numbers.
     * Demonstrates modern validation patterns.
     */
    public ValidationResult validateName() {
        if (firstName.isEmpty() && lastName.isEmpty()) {
            return ValidationResult.MISSING_REQUIRED_FIELD;
        }
        
        if (firstName.isEmpty()) {
            return ValidationResult.INVALID_FORMAT; // Missing first name
        }
        
        if (lastName.isEmpty()) {
            return ValidationResult.INVALID_FORMAT; // Missing last name
        }
        
        return ValidationResult.VALID;
    }
    
    /**
     * Checks if this name is valid.
     */
    public boolean isValid() {
        return validateName().isValid();
    }
    
    /**
     * Returns initials using modern string processing.
     */
    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        
        if (!firstName.isEmpty()) {
            initials.append(firstName.charAt(0));
        }
        
        if (!middleName.isEmpty()) {
            initials.append(middleName.charAt(0));
        }
        
        if (!lastName.isEmpty()) {
            initials.append(lastName.charAt(0));
        }
        
        return initials.toString().toUpperCase();
    }
    
    /**
     * Returns a new ModernName with updated first name.
     * Demonstrates immutable update patterns.
     */
    public ModernName withFirstName(String newFirstName) {
        return new ModernName(newFirstName, this.middleName, this.lastName);
    }
    
    /**
     * Returns a new ModernName with updated last name.
     */
    public ModernName withLastName(String newLastName) {
        return new ModernName(this.firstName, this.middleName, newLastName);
    }
    
    /**
     * Returns a new ModernName with updated middle name.
     */
    public ModernName withMiddleName(String newMiddleName) {
        return new ModernName(this.firstName, newMiddleName, this.lastName);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ModernName modernName = (ModernName) obj;
        return Objects.equals(firstName, modernName.firstName) &&
               Objects.equals(middleName, modernName.middleName) &&
               Objects.equals(lastName, modernName.lastName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, middleName, lastName);
    }
    
    @Override
    public String toString() {
        return String.format("ModernName{firstName='%s', middleName='%s', lastName='%s'}", 
                           firstName, middleName, lastName);
    }
}
