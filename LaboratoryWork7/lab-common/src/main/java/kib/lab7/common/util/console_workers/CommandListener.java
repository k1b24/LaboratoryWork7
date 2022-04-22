package kib.lab7.common.util.console_workers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс отвечающий за работу с пользователем в интерактивном режиме
 */
public class CommandListener {

    private final Scanner scanner;
    private final String username;

    /**
     * Конструктор
     */
    public CommandListener(InputStream inputStream, String username) {
        this.scanner = new Scanner(inputStream);
        this.username = username;
    }

    public InputedCommand readCommand() {
        try {
            SmartSplitter splitter = new SmartSplitter();
            System.out.print(username + " ↪ ");
            String line = scanner.nextLine();
            String[] inputString = splitter.smartSplit(line).toArray(new String[0]);
            String commandName = inputString[0].toLowerCase();
            String[] commandArgs = Arrays.copyOfRange(inputString, 1, inputString.length);
            return new InputedCommand(commandName, commandArgs);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
