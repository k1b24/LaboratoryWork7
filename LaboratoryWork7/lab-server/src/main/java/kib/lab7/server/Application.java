package kib.lab7.server;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.server.db_utils.DBFiller;
import kib.lab7.server.utils.AcceptedRequest;
import kib.lab7.server.utils.ByteBufferDeserializerSupplier;
import kib.lab7.server.utils.Config;
import kib.lab7.server.utils.ConnectionHandlerServer;
import kib.lab7.server.utils.DataManager;
import kib.lab7.server.utils.RequestWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private static final int MAX_PORT_VALUE = 65535;
    private ConnectionHandlerServer connectionHandlerServer;
    private final ConsoleListenerThread consoleListenerThread = new ConsoleListenerThread();
    private final Scanner scanner = new Scanner(System.in);
    private final DataManager dataManager = new DataManager();
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

    /**
     * Публичный метод, запускающий работу серверного приложения
     * Заполняет коллекцию и вводит с консоли порт, на котором будет слушать данные с клиента
     */
    public void launchApplication() {
        try {
            dataManager.getDbManager().initializeDB();
        } catch (SQLException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Не удалось создать таблицы в базе данных"));
            return;
        }
        boolean fillingResult = fillCollection();
        if (fillingResult) {
            try {
                Integer port = inputPort();
                if (port != null) {
                    connectionHandlerServer = new ConnectionHandlerServer(port);
                    Config.getTextSender().printMessage(new SuccessMessage("Сервер запущен"));
                } else {
                    return;
                }
            } catch (IOException e) {
                Config.getTextSender().printMessage(new ErrorMessage("Не удалось открыть канал для прослушивания"));
                return;
            }
            consoleListenerThread.start();
            launchMainLoop();
        }
    }

    private void launchMainLoop() {
        RequestWorker requestWorker = new RequestWorker(dataManager);
        while (Config.isWorking()) {
            try {
                AcceptedRequest acceptedRequest = connectionHandlerServer.listen();
                if (acceptedRequest != null) {
                    ByteBufferDeserializerSupplier byteBufferDeserializerSupplier = new ByteBufferDeserializerSupplier(acceptedRequest.getRecievedBytes());
                    CompletableFuture.supplyAsync(byteBufferDeserializerSupplier, executorService)
                            .thenApplyAsync(requestWorker::getResponse, executorService)
                            .thenAcceptAsync(response -> {
                                try {
                                    connectionHandlerServer.sendResponse(response, acceptedRequest.getSocketAddress());
                                } catch (IOException e) {
                                    Config.getTextSender().printMessage(new ErrorMessage("Не удалось отправить ответ клиенту"));
                                }
                            }, executorService);
                }
            } catch (IOException e) {
                Config.getTextSender().printMessage(new ErrorMessage("Не удалось получить пакет с клиента"));
            } catch (ClassNotFoundException e) {
                Config.getTextSender().printMessage(new ErrorMessage("Клиент прислал пакет, который невозможно десериализовать"));
            }
        }
        try {
            executorService.shutdown();
            connectionHandlerServer.closeServer();
        } catch (IOException e) {
            Config.getTextSender().printMessage(new ErrorMessage("При закрытии сервера произошла ошибка, "
                    + "сервер закончил работу некорректно"));
        }
    }

    private Integer inputPort() {
        try {
            Config.getTextSender().printMessage(new SuccessMessage("Пожалуйста, введите порт сервера в"
                    + " с которым вы хотите работать"));
            String inputedPort = scanner.nextLine();
            int port = Integer.parseInt(inputedPort);
            if (port >= 1 && port <= MAX_PORT_VALUE) {
                return port;
            } else {
                Config.getTextSender().printMessage(new ErrorMessage("Вы ввели неверный порт, повторите ввод"));
                return inputPort();
            }
        } catch (NoSuchElementException e) {
            return null;
        } catch (NumberFormatException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Вы ввели неверный порт, повторите ввод"));
            return inputPort();
        }
    }

    private boolean fillCollection() {
        DBFiller dbFiller = new DBFiller();
        try {
            dataManager.getCollectionManager().fillWithArray((ArrayList<HumanBeing>) dbFiller.getArrayListOfHumanBeings());
        } catch (SQLException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при работе с базой данных"));
            return false;
        }
        return true;
    }
}
