package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.common.util.console_workers.SuccessMessage;

import java.util.List;
import java.util.stream.Collectors;

public class Help extends AbstractCommand {

    public Help() {
        super("help", "Вывести справку по доступным командам", false, null);
    }

    @Override
    public Object execute(CommandRequest request) {
        List<AbstractCommand> listToReturn = getCommandsList();
        return new CommandResponse(new SuccessMessage(listToReturn.stream()
                .map(AbstractCommand::getDescription)
                .collect(Collectors.joining("\n"))));
    }
}
