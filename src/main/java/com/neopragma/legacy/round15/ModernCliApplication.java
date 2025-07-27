package com.neopragma.legacy.round15;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Modern CLI application framework demonstrating Java 8+ patterns.
 * 
 * This modernizes legacy Scanner-based CLI patterns by:
 * - Using command pattern for CLI operations
 * - Implementing functional programming for input processing
 * - Adding builder pattern for CLI application configuration
 * - Using Optional for null-safe input handling
 * - Providing reusable CLI abstractions and validation
 * - Demonstrating modern error handling without exceptions
 * - Creating fluent API for CLI workflow definition
 */
public final class ModernCliApplication {
    
    private final Map<String, Command> commands;
    private final InputProcessor inputProcessor;
    private final OutputProcessor outputProcessor;
    private final String welcomeMessage;
    private final String exitCommand;
    
    /**
     * Private constructor for builder pattern.
     */
    private ModernCliApplication(Builder builder) {
        this.commands = new HashMap<>(builder.commands);
        this.inputProcessor = builder.inputProcessor;
        this.outputProcessor = builder.outputProcessor;
        this.welcomeMessage = builder.welcomeMessage;
        this.exitCommand = builder.exitCommand;
    }
    
    /**
     * Creates a new builder for constructing ModernCliApplication.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Runs the CLI application with modern event loop.
     * Replaces legacy while(!done) patterns with functional approach.
     */
    public void run() {
        outputProcessor.display(welcomeMessage);
        
        boolean running = true;
        while (running) {
            Optional<String> input = inputProcessor.promptFor("command");
            
            if (!input.isPresent()) {
                continue;
            }
            
            String commandName = input.get().trim().toLowerCase();
            
            if (exitCommand.equals(commandName)) {
                running = false;
                outputProcessor.display("Goodbye!");
                continue;
            }
            
            Command command = commands.get(commandName);
            if (command != null) {
                CommandResult result = command.execute();
                handleCommandResult(result);
            } else {
                outputProcessor.displayError("Unknown command: " + commandName);
                displayHelp();
            }
        }
        
        inputProcessor.close();
    }
    
    /**
     * Handles command execution results using functional patterns.
     */
    private void handleCommandResult(CommandResult result) {
        if (result.isSuccess()) {
            result.getMessage().ifPresent(outputProcessor::display);
        } else {
            result.getError().ifPresent(outputProcessor::displayError);
        }
    }
    
    /**
     * Displays available commands using modern formatting.
     */
    private void displayHelp() {
        outputProcessor.display("Available commands:");
        commands.keySet().stream()
               .sorted()
               .forEach(cmd -> outputProcessor.display("  " + cmd));
        outputProcessor.display("  " + exitCommand);
    }
    
    /**
     * Builder class for constructing ModernCliApplication instances.
     */
    public static class Builder {
        private final Map<String, Command> commands = new HashMap<>();
        private InputProcessor inputProcessor = new ConsoleInputProcessor();
        private OutputProcessor outputProcessor = new ConsoleOutputProcessor();
        private String welcomeMessage = "Welcome! Type 'help' for available commands.";
        private String exitCommand = "quit";
        
        public Builder withCommand(String name, Command command) {
            this.commands.put(name.toLowerCase(), command);
            return this;
        }
        
        public Builder withInputProcessor(InputProcessor inputProcessor) {
            this.inputProcessor = inputProcessor;
            return this;
        }
        
        public Builder withOutputProcessor(OutputProcessor outputProcessor) {
            this.outputProcessor = outputProcessor;
            return this;
        }
        
        public Builder withWelcomeMessage(String welcomeMessage) {
            this.welcomeMessage = welcomeMessage;
            return this;
        }
        
        public Builder withExitCommand(String exitCommand) {
            this.exitCommand = exitCommand;
            return this;
        }
        
        public ModernCliApplication build() {
            commands.put("help", () -> CommandResult.success("Available commands listed above"));
            return new ModernCliApplication(this);
        }
    }
    
    /**
     * Command interface for CLI operations using functional programming.
     */
    @FunctionalInterface
    public interface Command {
        CommandResult execute();
        
        static Command of(Supplier<CommandResult> supplier) {
            return supplier::get;
        }
        
        static Command simple(Runnable action) {
            return () -> {
                action.run();
                return CommandResult.success();
            };
        }
        
        static Command withMessage(String message, Runnable action) {
            return () -> {
                action.run();
                return CommandResult.success(message);
            };
        }
    }
    
    /**
     * Command result wrapper for modern error handling.
     */
    public static class CommandResult {
        private final boolean success;
        private final String message;
        private final String error;
        
        private CommandResult(boolean success, String message, String error) {
            this.success = success;
            this.message = message;
            this.error = error;
        }
        
        public static CommandResult success() {
            return new CommandResult(true, null, null);
        }
        
        public static CommandResult success(String message) {
            return new CommandResult(true, message, null);
        }
        
        public static CommandResult failure(String error) {
            return new CommandResult(false, null, error);
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public Optional<String> getMessage() {
            return Optional.ofNullable(message);
        }
        
        public Optional<String> getError() {
            return Optional.ofNullable(error);
        }
    }
    
    /**
     * Input processor interface for modern input handling.
     */
    public interface InputProcessor {
        Optional<String> promptFor(String fieldName);
        void close();
    }
    
    /**
     * Output processor interface for modern output handling.
     */
    public interface OutputProcessor {
        void display(String message);
        void displayError(String error);
    }
    
    /**
     * Console-based input processor implementation.
     */
    public static class ConsoleInputProcessor implements InputProcessor {
        private final Scanner scanner;
        private final Map<String, String> prompts;
        
        public ConsoleInputProcessor() {
            this.scanner = new Scanner(System.in);
            this.prompts = new HashMap<>();
            setupDefaultPrompts();
        }
        
        public ConsoleInputProcessor(Map<String, String> customPrompts) {
            this.scanner = new Scanner(System.in);
            this.prompts = new HashMap<>(customPrompts);
            setupDefaultPrompts();
        }
        
        private void setupDefaultPrompts() {
            prompts.putIfAbsent("command", "Enter command: ");
            prompts.putIfAbsent("firstName", "First name: ");
            prompts.putIfAbsent("middleName", "Middle name: ");
            prompts.putIfAbsent("lastName", "Last name: ");
            prompts.putIfAbsent("ssn", "SSN: ");
            prompts.putIfAbsent("zipCode", "Zip Code: ");
        }
        
        @Override
        public Optional<String> promptFor(String fieldName) {
            String prompt = prompts.getOrDefault(fieldName, fieldName + ": ");
            System.out.print(prompt);
            
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                return Optional.empty();
            }
            
            return Optional.of(input.trim());
        }
        
        @Override
        public void close() {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    /**
     * Console-based output processor implementation.
     */
    public static class ConsoleOutputProcessor implements OutputProcessor {
        
        @Override
        public void display(String message) {
            System.out.println(message);
        }
        
        @Override
        public void displayError(String error) {
            System.err.println("Error: " + error);
        }
    }
    
    /**
     * Job applicant CLI workflow demonstrating modern patterns.
     */
    public static class JobApplicantWorkflow {
        private final List<JobApplicantData> applicants = new ArrayList<>();
        
        public Command createAddApplicantCommand() {
            return () -> {
                try {
                    JobApplicantData applicant = collectApplicantData();
                    applicants.add(applicant);
                    return CommandResult.success("Applicant added successfully: " + applicant.getFullName());
                } catch (Exception e) {
                    return CommandResult.failure("Failed to add applicant: " + e.getMessage());
                }
            };
        }
        
        public Command createListApplicantsCommand() {
            return () -> {
                if (applicants.isEmpty()) {
                    return CommandResult.success("No applicants found.");
                }
                
                StringBuilder result = new StringBuilder("Applicants:\n");
                for (int i = 0; i < applicants.size(); i++) {
                    result.append(String.format("%d. %s\n", i + 1, applicants.get(i).getFullName()));
                }
                
                return CommandResult.success(result.toString());
            };
        }
        
        private JobApplicantData collectApplicantData() {
            ConsoleInputProcessor input = new ConsoleInputProcessor();
            
            String firstName = input.promptFor("firstName").orElse("");
            String middleName = input.promptFor("middleName").orElse("");
            String lastName = input.promptFor("lastName").orElse("");
            String ssn = input.promptFor("ssn").orElse("");
            String zipCode = input.promptFor("zipCode").orElse("");
            
            return new JobApplicantData(firstName, middleName, lastName, ssn, zipCode);
        }
    }
    
    /**
     * Simple data class for job applicant information.
     */
    public static class JobApplicantData {
        private final String firstName;
        private final String middleName;
        private final String lastName;
        private final String ssn;
        private final String zipCode;
        
        public JobApplicantData(String firstName, String middleName, String lastName, String ssn, String zipCode) {
            this.firstName = firstName != null ? firstName : "";
            this.middleName = middleName != null ? middleName : "";
            this.lastName = lastName != null ? lastName : "";
            this.ssn = ssn != null ? ssn : "";
            this.zipCode = zipCode != null ? zipCode : "";
        }
        
        public String getFullName() {
            return ModernStringFormatter.formatFullName(firstName, middleName, lastName);
        }
        
        public String getFirstName() { return firstName; }
        public String getMiddleName() { return middleName; }
        public String getLastName() { return lastName; }
        public String getSsn() { return ssn; }
        public String getZipCode() { return zipCode; }
    }
}
