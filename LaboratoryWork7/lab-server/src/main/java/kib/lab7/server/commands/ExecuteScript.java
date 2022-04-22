package kib.lab7.server.commands;

import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.common.util.client_server_communication.requests.CommandRequest;

public class ExecuteScript extends AbstractCommand {

    public ExecuteScript() {
        super("execute_script", "Выполнить скрипт из файла, принимает"
                + " на вход один аргумент [file_path]", false, null);
    }

    @Override
    public Object execute(CommandRequest request) {
        return null;
    }
}
