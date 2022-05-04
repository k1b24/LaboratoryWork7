package kib.lab7.common.util.console_workers;

public class InputedCommand {

    private final String name;
    private final String[] arguments;

    public InputedCommand(String name, String[] arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public String[] getArguments() {
        return arguments;
    }
}
