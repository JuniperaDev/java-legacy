package com.neopragma.legacy.round15;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Modern string formatting utilities demonstrating Java 8+ patterns.
 * 
 * This modernizes legacy StringBuilder/StringBuffer usage by:
 * - Using String.format for template-based formatting
 * - Leveraging String.join for delimiter-based concatenation
 * - Implementing StringJoiner for complex joining scenarios
 * - Using Stream API for functional string processing
 * - Providing null-safe string operations
 * - Demonstrating modern concatenation patterns
 */
public final class ModernStringFormatter {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ModernStringFormatter() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Formats SSN using String.format instead of StringBuilder.
     * Replaces legacy: StringBuilder sb = new StringBuilder(ssn.substring(0,3)); sb.append("-"); ...
     */
    public static String formatSsn(String ssn) {
        if (ssn == null || ssn.length() != 9) {
            return ssn != null ? ssn : "";
        }
        
        return String.format("%s-%s-%s", 
                           ssn.substring(0, 3),
                           ssn.substring(3, 5),
                           ssn.substring(5));
    }
    
    /**
     * Formats name in "Last, First Middle" format using String.format.
     * Replaces legacy StringBuilder concatenation with modern template formatting.
     */
    public static String formatLastNameFirst(String firstName, String middleName, String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            return "";
        }
        
        if (firstName == null || firstName.isEmpty()) {
            return lastName;
        }
        
        if (middleName == null || middleName.isEmpty()) {
            return String.format("%s, %s", lastName, firstName);
        }
        
        return String.format("%s, %s %s", lastName, firstName, middleName);
    }
    
    /**
     * Formats full name using String.join for simple concatenation.
     * Demonstrates modern approach to joining non-null strings.
     */
    public static String formatFullName(String firstName, String middleName, String lastName) {
        return Arrays.stream(new String[]{firstName, middleName, lastName})
                     .filter(name -> name != null && !name.trim().isEmpty())
                     .map(String::trim)
                     .collect(Collectors.joining(" "));
    }
    
    /**
     * Formats Spanish name combining first and last names.
     * Uses modern null-safe concatenation instead of StringBuilder.
     */
    public static String formatSpanishLastName(String primerApellido, String segundoApellido) {
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            return "";
        }
        
        if (segundoApellido == null || segundoApellido.trim().isEmpty()) {
            return primerApellido.trim();
        }
        
        return String.format("%s %s", primerApellido.trim(), segundoApellido.trim());
    }
    
    /**
     * Builds address string using StringJoiner for complex formatting.
     * Demonstrates modern approach to conditional string building.
     */
    public static String formatAddress(String street, String city, String state, String zipCode) {
        StringJoiner addressJoiner = new StringJoiner(", ");
        
        if (street != null && !street.trim().isEmpty()) {
            addressJoiner.add(street.trim());
        }
        
        if (city != null && !city.trim().isEmpty() && state != null && !state.trim().isEmpty()) {
            addressJoiner.add(String.format("%s, %s", city.trim(), state.trim()));
        }
        
        if (zipCode != null && !zipCode.trim().isEmpty()) {
            addressJoiner.add(zipCode.trim());
        }
        
        return addressJoiner.toString();
    }
    
    /**
     * Accumulates lines into a single string using String.join.
     * Replaces legacy StringBuffer line-by-line accumulation.
     */
    public static String joinLines(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return "";
        }
        
        return lines.stream()
                   .filter(line -> line != null)
                   .collect(Collectors.joining("\n"));
    }
    
    /**
     * Builds JSON-like string using modern formatting.
     * Demonstrates template-based approach instead of manual concatenation.
     */
    public static String formatPersonInfo(String firstName, String lastName, String ssn, String city, String state) {
        return String.format(
            "Person{firstName='%s', lastName='%s', ssn='%s', location='%s, %s'}",
            nullSafe(firstName),
            nullSafe(lastName),
            formatSsn(ssn),
            nullSafe(city),
            nullSafe(state)
        );
    }
    
    /**
     * Creates a delimited string from variable arguments.
     * Uses modern varargs and Stream API instead of manual StringBuilder loops.
     */
    public static String joinWithDelimiter(String delimiter, String... values) {
        if (values == null || values.length == 0) {
            return "";
        }
        
        return Arrays.stream(values)
                     .filter(value -> value != null && !value.trim().isEmpty())
                     .map(String::trim)
                     .collect(Collectors.joining(delimiter));
    }
    
    /**
     * Builds a formatted list string using functional programming.
     * Replaces manual StringBuilder iteration with Stream operations.
     */
    public static String formatList(List<String> items, String prefix, String suffix) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        
        String content = items.stream()
                             .filter(item -> item != null && !item.trim().isEmpty())
                             .map(String::trim)
                             .collect(Collectors.joining(", "));
        
        return String.format("%s%s%s", 
                           nullSafe(prefix), 
                           content, 
                           nullSafe(suffix));
    }
    
    /**
     * Utility method for null-safe string operations.
     */
    private static String nullSafe(String value) {
        return value != null ? value : "";
    }
    
    /**
     * Demonstrates modern string building with conditional logic.
     * Uses functional approach instead of imperative StringBuilder manipulation.
     */
    public static String buildConditionalString(String base, String... conditionalParts) {
        if (base == null || base.trim().isEmpty()) {
            return "";
        }
        
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(base.trim());
        
        Arrays.stream(conditionalParts)
              .filter(part -> part != null && !part.trim().isEmpty())
              .map(String::trim)
              .forEach(joiner::add);
        
        return joiner.toString();
    }
}
