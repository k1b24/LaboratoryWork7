package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.server.utils.DataManager;

public class RemoveByID extends AbstractCommand {

    public RemoveByID(DataManager target) {
        super("remove_by_id", "Удалить человека из коллекции по"
                + " его ID, принимает на вход [ID]", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        long id = super.getDataManager().removeHumanByIdAndUserName(request.getNumberArgumentToSend(), request.getUserLogin());
        if (id > 0) {
            return new CommandResponse(new SuccessMessage("Человек с номером " + id + " был успешно удален"));
        } else if (id == 0) {
            return new CommandResponse(new ErrorMessage("Человека с таким номером не существует или он вам не принадлежит"));
        } else {
            return new CommandResponse(new ErrorMessage("Человек не был удален, произошла ошибка на сервере"));
        }
    }
}
