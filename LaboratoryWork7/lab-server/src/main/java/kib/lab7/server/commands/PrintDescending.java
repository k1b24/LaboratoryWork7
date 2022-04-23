package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.server.utils.DataManager;

public class PrintDescending extends AbstractCommand {

    public PrintDescending(DataManager target) {
        super("print_descending", "Вывести"
                + " коллекциию в порядке убывания", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        return new CommandResponse(new SuccessMessage("Коллекция в порядке убывания: "),
                super.getDataManager().getCollectionManager().returnDescending());
    }
}
