package kib.lab7.common.util.client_server_communication.requests;

import kib.lab7.common.abstractions.RequestInterface;

public class LoginRequest implements RequestInterface  {

    private String login;
    private String password;
    private String clientInfo;

    @Override
    public void setClientInfo(String clientInfoToSet) {
        this.clientInfo = clientInfoToSet;
    }

    @Override
    public String getClientInfo() {
        return clientInfo;
    }

    @Override
    public void setUserLogin(String loginToSet) {
        this.login = loginToSet;
    }

    @Override
    public String getUserLogin() {
        return login;
    }

    @Override
    public void setUserPassword(String passwordToSet) {
        this.password = passwordToSet;
    }

    @Override
    public String getUserPassword() {
        return password;
    }

    @Override
    public Class<?> getType() {
        return this.getClass();
    }
}
