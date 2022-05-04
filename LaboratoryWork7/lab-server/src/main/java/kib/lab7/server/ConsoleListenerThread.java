package kib.lab7.server;

import kib.lab7.common.util.console_workers.CommandListener;
import kib.lab7.common.util.console_workers.InputedCommand;
import kib.lab7.server.utils.Config;

public class ConsoleListenerThread extends Thread {

    @Override
    public void run() {
        CommandListener commandListener = new CommandListener(System.in, "server");
        while (Config.isWorking()) {
            InputedCommand userInputedCommand = commandListener.readCommand();
            if (userInputedCommand == null
                    || ("exit".equalsIgnoreCase(userInputedCommand.getName())
                    && userInputedCommand.getArguments().length == 0)) {
                Config.setIsWorking(false);
            }
        }
    }
}
