package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.utils.DataManager;

public class Add extends AbstractCommand {

    public Add(DataManager target) {
        super("add", "Добавить элемент в коллекцию, принимает на вход [Имя, наличие"
                + " героизма(true/false), наличие зубочистки(true/false), скорость удара]", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        if (super.getDataManager().addHumanBeing(request.getHumanToSend())) {
            return new CommandResponse(new SuccessMessage("Объект успешно добавлен в коллекцию"));
        } else {
            return new CommandResponse(new ErrorMessage("Объект не был добавлен в коллекцию"));
        }
    }
}
