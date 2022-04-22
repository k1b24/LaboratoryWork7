package kib.lab7.server.commands;


import kib.lab7.common.entities.enums.Mood;
import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.server.utils.DataManager;

import java.util.Arrays;

public class RemoveByAnyMood extends AbstractCommand {

    public RemoveByAnyMood(DataManager target) {
        super("remove_by_any_mood", "Удалить любого человека из коллекции по его настроению,"
                + " принимает на вход тип настроения " + Arrays.toString(Mood.values()), false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        long removedID = super.getDataManager().removeHumanByAnyMoodAndUserName(request.getMoodArgumentToSend(), request.getUserLogin());
        if (removedID == -1) {
            return new CommandResponse(new ErrorMessage("При работе с базой данных произошла ошибка"));
        } else if (removedID == 0) {
            return new CommandResponse(new ErrorMessage("Вам не принадлежит ни один человек с таким настроением"));
        }
        return new CommandResponse(new SuccessMessage("Случайный человек с настроением "
                + request.getMoodArgumentToSend() + " удален, его ID: " + removedID));
    }
}
