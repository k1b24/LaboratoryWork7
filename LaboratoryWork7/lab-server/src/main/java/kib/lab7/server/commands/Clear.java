package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.server.utils.DataManager;

import java.util.Arrays;

public class Clear extends AbstractCommand {

    public Clear(DataManager target) {
        super("clear", "Очистить коллекцию", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        Long[] ids = super.getDataManager().clearByName(request.getUserLogin());
        if (ids != null) {
            return new CommandResponse(new SuccessMessage("Коллекция ваших людей успешно очищена, удалены номера: " + Arrays.toString(ids)));
        } else {
            return new CommandResponse(new ErrorMessage("Коллекция не была очищена либо вам не принадлежит никто из людей, либо на сервере произошла ошибка"));
        }
    }
}
