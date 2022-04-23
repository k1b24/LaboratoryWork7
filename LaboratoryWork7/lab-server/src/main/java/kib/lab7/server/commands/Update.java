package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.server.utils.DataManager;

public class Update extends AbstractCommand {

    public Update(DataManager target) {
        super("update", "Обновить элемент коллекции по его ID, принимает на вход [ID, Имя,"
                + " наличие героизма(true/false), наличие зубочистки(true/false), скорость удара]", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        long id = request.getNumberArgumentToSend();
        long returnedId = super.getDataManager().updateByIdAndUser(request.getHumanToSend(), id, request.getUserLogin());
        if (returnedId > 0) {
            return new CommandResponse(new SuccessMessage("Человек с id " + returnedId + "был обновлен"));
        } else if (returnedId == 0) {
            return new CommandResponse(new ErrorMessage("Человек не был обновлен, он вам не принадлежит"));
        } else {
            return new CommandResponse(new ErrorMessage("Человек не был обновлен, произошла ошибка при работе с базой данных"));
        }
    }
}
