package kib.lab7.server.commands;

import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.utils.DataManager;

import java.util.ArrayList;

public class Head extends AbstractCommand {

    public Head(DataManager target) {
        super("head", "Вывести первый элемент коллекции(голову очереди)", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        HumanBeing head = super.getDataManager().getCollectionManager().returnHead();
        ArrayList<HumanBeing> listToReturn = new ArrayList<>();
        listToReturn.add(head);
        if (head != null) {
            return new CommandResponse(new SuccessMessage("Первый элемент коллекции: "), listToReturn);
        } else {
            return new CommandResponse(new ErrorMessage("Коллекция пустая :("));
        }
    }
}
