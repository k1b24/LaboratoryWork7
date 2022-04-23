 package kib.lab7.common.util.client_server_communication;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.entities.enums.Mood;
import kib.lab7.common.util.StringToTypeConverter;
import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.HumanInfoInput;
import kib.lab7.common.util.console_workers.InputedCommand;
import kib.lab7.common.util.console_workers.TextSender;

import java.util.Arrays;

public class RequestCreator {

    private static final int AMOUNT_OF_ARGS_FOR_HUMAN_BEING_REQUEST = 4;
    private static final int AMOUNT_OF_ARGS_FOR_HUMAN_BEING_AND_NUMBER_REQUEST = 5;
    private final TextSender textSender;

    public RequestCreator(TextSender textSender) {
        this.textSender = textSender;
    }


    public CommandRequest createRequestFromInputedCommand(InputedCommand inputedCommand, String user, String password) {
        CommandRequest request;
        if (AvailableCommands.COMMANDS_WITHOUT_ARGUMENTS.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == 0) {
            //Обработка команды без аргументов
            request = new CommandRequest(inputedCommand.getName());
        } else if (AvailableCommands.COMMANDS_WITH_NUMBER_ARGUMENT.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == 1) {
            //Обработка команды с числовым аргументом
            request = createNumberRequest(inputedCommand);
        } else if (AvailableCommands.COMMANDS_WITH_MOOD_ARGUMENT.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == 1) {
            //Обработка команды с аргументом "настроение"
            request = createMoodRequest(inputedCommand);
        } else if (AvailableCommands.COMMANDS_WITH_HUMAN_BEING_ARGUMENT.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == AMOUNT_OF_ARGS_FOR_HUMAN_BEING_REQUEST) {
            //Обработка команды с аргументом "HumanBeing"
            request = createHumanBeingRequest(inputedCommand);
        } else if (AvailableCommands.COMMANDS_WITH_HUMAN_BEING_AND_NUMBER_ARGUMENTS.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == AMOUNT_OF_ARGS_FOR_HUMAN_BEING_AND_NUMBER_REQUEST) {
            //Обработка команды с аргументами "число" и "человек"
            request = createHumanBeingAndNumberRequest(inputedCommand);
        } else {
            request = null;
        }
        if (request != null) {
            request.setUserLogin(user);
            request.setUserPassword(password);
            if (request.getHumanToSend() != null) {
                request.getHumanToSend().setAuthor(user);
            }
        }
        return request;
    }

    private CommandRequest createHumanBeingAndNumberRequest(InputedCommand inputedCommand) {
        int num;
        try {
            num = (int) StringToTypeConverter.toObject(Integer.class, inputedCommand.getArguments()[0]);
        } catch (IllegalArgumentException e) {
            textSender.printMessage(new ErrorMessage("Введен неправильный числовой аргумент"));
            return null;
        }
        try {
            String[] argumentsForHuman = Arrays.copyOfRange(inputedCommand.getArguments(), 1, inputedCommand.getArguments().length);
            HumanInfoInput humanInfoInput = new HumanInfoInput(argumentsForHuman, textSender);
            humanInfoInput.inputHuman();
            HumanBeing humanForRequest = humanInfoInput.getNewHumanToInput();
            return new CommandRequest(inputedCommand.getName(), num, humanForRequest);
        } catch (IllegalArgumentException e) {
            textSender.printMessage(new ErrorMessage(e.getMessage()));
            return null;
        }
    }

    private CommandRequest createHumanBeingRequest(InputedCommand inputedCommand) {
        try {
            HumanInfoInput humanInfoInput = new HumanInfoInput(inputedCommand.getArguments(), textSender);
            humanInfoInput.inputHuman();
            HumanBeing humanForRequest = humanInfoInput.getNewHumanToInput();
            return new CommandRequest(inputedCommand.getName(), humanForRequest);
        } catch (IllegalArgumentException e) {
            textSender.printMessage(new ErrorMessage(e.getMessage()));
            return null;
        }
    }

    private CommandRequest createMoodRequest(InputedCommand inputedCommand) {
        if ("".equals(inputedCommand.getArguments()[0])) {
            return new CommandRequest(inputedCommand.getName(), (Mood) null);
        } else {
            try {
                return new CommandRequest(inputedCommand.getName(), Mood.valueOf(inputedCommand.getArguments()[0].toUpperCase()));
            } catch (IllegalArgumentException e) {
                textSender.printMessage(new ErrorMessage("Такого настроения не существует,"
                        + " введите одно из: " + Arrays.toString(Mood.values())));
                return null;
            }
        }
    }

    private CommandRequest createNumberRequest(InputedCommand inputedCommand) {
        try {
            return new CommandRequest(inputedCommand.getName(), (int) StringToTypeConverter.toObject(Integer.class, inputedCommand.getArguments()[0]));
        } catch (IllegalArgumentException e) {
            textSender.printMessage(new ErrorMessage("Введен неправильный числовой аргумент"));
            return null;
        }
    }
}
