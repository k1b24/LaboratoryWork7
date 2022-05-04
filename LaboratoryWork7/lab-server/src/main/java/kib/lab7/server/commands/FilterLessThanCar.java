package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.utils.DataManager;

public class FilterLessThanCar extends AbstractCommand {

    public FilterLessThanCar(DataManager target) {
        super("filter_less_than_car", "Вывести элементы, значение скорости которых"
                + " меньше заданного, принимает аргумент [Speed]", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        int speedFilter = request.getNumberArgumentToSend();
        return new CommandResponse(new SuccessMessage("Элементы коллекции, значение скорости которых меньше " + speedFilter + ":"),
                super.getDataManager().getCollectionManager().filterByCarSpeed(speedFilter));
    }
}
