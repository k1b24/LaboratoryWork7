package kib.lab7.server.utils;

import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;
import kib.lab7.server.abstractions.AbstractCommand;
import kib.lab7.server.commands.Add;
import kib.lab7.server.commands.AddIfMin;
import kib.lab7.server.commands.Clear;
import kib.lab7.server.commands.ExecuteScript;
import kib.lab7.server.commands.FilterLessThanCar;
import kib.lab7.server.commands.Head;
import kib.lab7.server.commands.Help;
import kib.lab7.server.commands.History;
import kib.lab7.server.commands.Info;
import kib.lab7.server.commands.PrintDescending;
import kib.lab7.server.commands.RemoveByAnyMood;
import kib.lab7.server.commands.RemoveByID;
import kib.lab7.server.commands.Show;
import kib.lab7.server.commands.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandManager {

    private static final int AMOUNT_OF_COMMANDS_TO_SAVE = 10;
    private final Map<String, AbstractCommand> commands = new HashMap<>();
    private final Queue<AbstractCommand> lastExecutedCommands = new ConcurrentLinkedQueue<>();
    private final DataManager dataManager;

    public CommandManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initMap();
    }

    private void initMap() {
        commands.put("add", new Add(dataManager));
        commands.put("add_if_min", new AddIfMin(dataManager));
        commands.put("clear", new Clear(dataManager));
        commands.put("filter_less_than_car", new FilterLessThanCar(dataManager));
        commands.put("head", new Head(dataManager));
        commands.put("info", new Info(dataManager));
        commands.put("print_descending", new PrintDescending(dataManager));
        commands.put("remove_by_any_mood", new RemoveByAnyMood(dataManager));
        commands.put("show", new Show(dataManager));
        commands.put("update", new Update(dataManager));
        commands.put("help", new Help());
        commands.put("execute_script", new ExecuteScript());
        commands.put("remove_by_id", new RemoveByID(dataManager));
        commands.put("history", new History(this));
    }

    public Object executeCommandFromRequest(CommandRequest requestFromClient) {
        if (dataManager.loginUser(requestFromClient.getUserLogin(), requestFromClient.getUserPassword())) {
            Object response;
            if (commands.containsKey(requestFromClient.getCommandNameToSend().toLowerCase())) {
                if (!requestFromClient.isServerRequest() && !commands.get(requestFromClient.getCommandNameToSend().toLowerCase()).isOnlyServerCommand()) {
                    try {
                        appendCommandToHistory(requestFromClient.getCommandNameToSend());
                        response = commands.get(requestFromClient.getCommandNameToSend()).execute(requestFromClient);
                    } catch (IllegalArgumentException e) {
                        response = new CommandResponse(new ErrorMessage(e.getMessage()));
                    }
                } else if (requestFromClient.isServerRequest()) {
                    try {
                        appendCommandToHistory(requestFromClient.getCommandNameToSend());
                        response = commands.get(requestFromClient.getCommandNameToSend()).execute(requestFromClient);
                    } catch (IllegalArgumentException e) {
                        response = new CommandResponse(new ErrorMessage(e.getMessage()));
                    }
                } else {
                    response = new CommandResponse(new ErrorMessage("Такая команда недоступна"));
                }
            } else {
                response = new CommandResponse(new ErrorMessage("Такая команда отсутствует"));
            }
            return response;
        } else {
            return new CommandResponse(new ErrorMessage("Произошла ошибка при проверке прав доступа"));
        }
    }

    public Queue<AbstractCommand> getLastExecutedCommands() {
        return lastExecutedCommands;
    }

    private void appendCommandToHistory(String name) {
        lastExecutedCommands.add(commands.get(name));
        if (lastExecutedCommands.size() == AMOUNT_OF_COMMANDS_TO_SAVE + 1) {
            lastExecutedCommands.poll();
        }
    }
}
