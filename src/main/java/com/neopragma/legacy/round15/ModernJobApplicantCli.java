package com.neopragma.legacy.round15;

import java.util.HashMap;
import java.util.Map;

/**
 * Modern CLI demonstration for job applicant processing.
 * 
 * This demonstrates modernization of legacy CLI patterns by:
 * - Replacing Scanner-based input loops with command pattern
 * - Using functional programming for CLI workflow definition
 * - Implementing modern input validation and error handling
 * - Providing clean separation between CLI logic and business logic
 * - Demonstrating reusable CLI abstractions
 * - Using builder pattern for flexible CLI configuration
 */
public final class ModernJobApplicantCli {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ModernJobApplicantCli() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Main method demonstrating modern CLI patterns.
     * Replaces legacy while(!done) Scanner loops with command-based approach.
     */
    public static void main(String[] args) {
        ModernCliApplication app = createJobApplicantApplication();
        app.run();
    }
    
    /**
     * Creates a modern CLI application for job applicant processing.
     * Demonstrates builder pattern and command registration.
     */
    private static ModernCliApplication createJobApplicantApplication() {
        ModernCliApplication.JobApplicantWorkflow workflow = new ModernCliApplication.JobApplicantWorkflow();
        
        Map<String, String> customPrompts = new HashMap<>();
        customPrompts.put("command", "Enter command (add, list, help, quit): ");
        
        return ModernCliApplication.builder()
                .withWelcomeMessage("Job Applicant Management System\nType 'help' for available commands.")
                .withInputProcessor(new ModernCliApplication.ConsoleInputProcessor(customPrompts))
                .withCommand("add", workflow.createAddApplicantCommand())
                .withCommand("list", workflow.createListApplicantsCommand())
                .withCommand("demo", createDemoCommand())
                .withExitCommand("quit")
                .build();
    }
    
    /**
     * Creates a demo command showing modern patterns.
     * Demonstrates functional command creation with validation.
     */
    private static ModernCliApplication.Command createDemoCommand() {
        return ModernCliApplication.Command.withMessage(
            "Demo completed: Modern CLI patterns demonstrated successfully!",
            () -> {
                System.out.println("Demonstrating modern CLI patterns:");
                System.out.println("- Command pattern for CLI operations");
                System.out.println("- Functional programming for input processing");
                System.out.println("- Builder pattern for application configuration");
                System.out.println("- Optional-based null-safe input handling");
                System.out.println("- Clean separation of CLI and business logic");
            }
        );
    }
    
    /**
     * Legacy CLI comparison utility.
     * Shows the difference between old and new patterns.
     */
    public static class LegacyComparison {
        
        /**
         * Demonstrates legacy CLI patterns for comparison.
         * This shows what we're replacing with modern patterns.
         */
        public static void showLegacyPattern() {
            System.out.println("Legacy CLI Pattern (what we're replacing):");
            System.out.println("```java");
            System.out.println("boolean done = false;");
            System.out.println("Scanner scanner = new Scanner(System.in);");
            System.out.println("while (!done) {");
            System.out.println("    System.out.println(\"First name?\");");
            System.out.println("    firstName = scanner.nextLine();");
            System.out.println("    if (firstName.equals(\"quit\")) {");
            System.out.println("        done = true;");
            System.out.println("        break;");
            System.out.println("    }");
            System.out.println("    // ... more input handling");
            System.out.println("}");
            System.out.println("```");
        }
        
        /**
         * Demonstrates modern CLI patterns.
         * Shows the improved approach with functional programming.
         */
        public static void showModernPattern() {
            System.out.println("Modern CLI Pattern (new approach):");
            System.out.println("```java");
            System.out.println("ModernCliApplication app = ModernCliApplication.builder()");
            System.out.println("    .withCommand(\"add\", workflow.createAddApplicantCommand())");
            System.out.println("    .withCommand(\"list\", workflow.createListApplicantsCommand())");
            System.out.println("    .withInputProcessor(new ConsoleInputProcessor())");
            System.out.println("    .build();");
            System.out.println("app.run();");
            System.out.println("```");
        }
    }
}
