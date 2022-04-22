package kib.lab7.server.abstractions;


import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.server.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {

    private static final List<AbstractCommand> CLIENT_COMMANDS_LIST = new ArrayList<>();
    private final String name;
    private final String description;
    private final boolean onlyServerCommand;
    private final DataManager target;

    public AbstractCommand(String name, String description, boolean onlyServerCommand, DataManager target) {
        this.name = name;
        this.description = description;
        this.onlyServerCommand = onlyServerCommand;
        if (!onlyServerCommand) {
            CLIENT_COMMANDS_LIST.add(this);
        }
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public boolean isOnlyServerCommand() {
        return onlyServerCommand;
    }

    public List<AbstractCommand> getCommandsList() {
        return CLIENT_COMMANDS_LIST;
    }

    public String getDescription() {
        return name + ": " + description;
    }

    public abstract Object execute(CommandRequest request);

    public DataManager getDataManager() {
        return target;
    }
}
