package kib.lab7.common.util.client_server_communication.requests;

import kib.lab7.common.abstractions.RequestInterface;
import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.entities.enums.Mood;

import java.io.Serializable;

public class CommandRequest implements Serializable, RequestInterface {

    private final String commandNameToSend;
    private HumanBeing humanToSend = null;
    private Integer numberArgumentToSend = null;
    private Mood moodArgumentToSend = null;
    private String clientInfo = null;
    private boolean serverRequest = false;
    private String userPassword;
    private String userLogin;

    public CommandRequest(String name) {
        this.commandNameToSend = name;
    }

    public CommandRequest(String name, HumanBeing human) {
        this.commandNameToSend = name;
        this.humanToSend = human;
    }

    public CommandRequest(String name, int argument, HumanBeing human) {
        this.commandNameToSend = name;
        this.numberArgumentToSend = argument;
        this.humanToSend = human;
    }

    public CommandRequest(String name, int argument) {
        this.commandNameToSend = name;
        this.numberArgumentToSend = argument;
    }

    public CommandRequest(String name, Mood argument) {
        this.commandNameToSend = name;
        this.moodArgumentToSend = argument;
    }

    public String getCommandNameToSend() {
        return commandNameToSend;
    }

    public HumanBeing getHumanToSend() {
        return humanToSend;
    }

    public int getNumberArgumentToSend() {
        return numberArgumentToSend;
    }

    public Mood getMoodArgumentToSend() {
        return moodArgumentToSend;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    @Override
    public void setUserLogin(String login) {
        this.userLogin = login;
    }

    @Override
    public String getUserLogin() {
        return userLogin;
    }

    @Override
    public void setUserPassword(String password) {
        this.userPassword = password;
    }

    @Override
    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public Class<?> getType() {
        return this.getClass();
    }

    @Override
    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public String toString() {
        return "Имя команды: " + commandNameToSend
                + (humanToSend == null ? "" : " / Информация о человеке: " + humanToSend)
                + (numberArgumentToSend == null ? "" : " / Числовой аргумент = " + numberArgumentToSend)
                + (moodArgumentToSend == null ? "" : " / Настроение: " + moodArgumentToSend);
    }

    public boolean isServerRequest() {
        return serverRequest;
    }

    public void setServerRequest(boolean serverRequest) {
        this.serverRequest = serverRequest;
    }
}
