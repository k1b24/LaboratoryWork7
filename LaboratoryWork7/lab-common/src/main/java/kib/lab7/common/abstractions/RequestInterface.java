package kib.lab7.common.abstractions;

import java.io.Serializable;

public interface RequestInterface extends Serializable {

    void setClientInfo(String clientInfo);

    String getClientInfo();

    void setUserLogin(String login);

    String getUserLogin();

    void setUserPassword(String password);

    String getUserPassword();

    Class<?> getType();
}
