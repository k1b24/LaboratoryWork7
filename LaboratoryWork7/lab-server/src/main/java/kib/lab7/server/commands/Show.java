package kib.lab7.server.commands;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.server.utils.DataManager;
import kib.lab7.server.utils.ObjectSizeAnalyzer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Show extends AbstractCommand {

    public Show(DataManager target) {
        super("show", "Вывести все элементы коллекции", false, target);
    }

    @Override
    public Object execute(CommandRequest request) {
        ArrayList<HumanBeing> people = super.getDataManager().getCollectionManager().getSortedArrayListFromQueue();
        return new CommandResponse(new SuccessMessage("Элементы коллекции: "),
                (ArrayList<HumanBeing>) people.stream().sorted(Comparator.comparing(ObjectSizeAnalyzer::getSize).reversed()).collect(Collectors.toList()));
    }
}
