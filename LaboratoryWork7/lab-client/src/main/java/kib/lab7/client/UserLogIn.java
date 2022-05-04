package kib.lab7.client;

import kib.lab7.common.abstractions.RequestInterface;
import kib.lab7.common.util.client_server_communication.requests.LoginRequest;
import kib.lab7.common.util.client_server_communication.responses.AuthenticationResponse;
import kib.lab7.common.util.client_server_communication.requests.SignUpRequest;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.common.util.console_workers.SuccessMessage;
import kib.lab7.common.util.console_workers.TextSender;

import java.io.IOException;
import java.util.Scanner;

public class UserLogIn {

    private String login;
    private String password;
    private final TextSender textSender = new TextSender(System.out);
    private final Scanner scanner = new Scanner(System.in);
    private final ConnectionHandlerClient connectionHandler;

    public UserLogIn(ConnectionHandlerClient connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void startAuthentication() {
        textSender.printMessage(new SuccessMessage("Вы хотите авторизироваться (1) или создать нового пользователя (2)?"));
        String answer = scanner.nextLine().trim();
        if ("1".equals(answer)) {
            loginUser();
        } else if ("2".equals(answer)) {
            registerNewUser();
        } else {
            textSender.printMessage(new ErrorMessage("Ошибка ввода"));
            startAuthentication();
        }
    }

    private void loginUser() {
        RequestInterface loginRequest = new LoginRequest();
        textSender.printMessage(new SuccessMessage("Введите имя пользователя: "));
        String inputedLogin = scanner.nextLine();
        loginRequest.setUserLogin(inputedLogin);
        textSender.printMessage(new SuccessMessage("Введите пароль: "));
        String inputedPassword = scanner.nextLine();
        loginRequest.setUserPassword(inputedPassword);
        try {
            connectionHandler.sendRequest(loginRequest);
            AuthenticationResponse response = (AuthenticationResponse) connectionHandler.recieveResponse();
            textSender.printMessage(response.getMessage());
            if (response.getResponseSuccess()) {
                this.login = inputedLogin;
                this.password = inputedPassword;
            } else {
                loginUser();
            }
        } catch (IOException e) {
            textSender.printMessage(new ErrorMessage("Произошла ошибка при отправке запроса на сервер. Пожалуйста, повторите ввод"));
            loginUser();
        } catch (ClassNotFoundException e) {
            textSender.printMessage(new ErrorMessage("Произошла ошибка при чтении ответа от сервера. Пожалуйста, повторите ввод"));
            loginUser();
        }
    }

    private void registerNewUser() {
        RequestInterface signUpRequest = new SignUpRequest();
        textSender.printMessage(new SuccessMessage("Введите желаемый логин: "));
        String inputedLogin = scanner.nextLine();
        signUpRequest.setUserLogin(inputedLogin);
        textSender.printMessage(new SuccessMessage("Введите пароль: "));
        String inputedPassword = scanner.nextLine();
        signUpRequest.setUserPassword(inputedPassword);
        try {
            connectionHandler.sendRequest(signUpRequest);
            AuthenticationResponse response = (AuthenticationResponse) connectionHandler.recieveResponse();
            textSender.printMessage(response.getMessage());
            if (response.getResponseSuccess()) {
                this.login = inputedLogin;
                this.password = inputedPassword;
            } else {
                registerNewUser();
            }
        } catch (IOException e) {
            textSender.printMessage(new ErrorMessage("Произошла ошибка при отправке запроса на сервер. Пожалуйста, повторите ввод"));
            registerNewUser();
        } catch (ClassNotFoundException e) {
            textSender.printMessage(new ErrorMessage("Произошла ошибка при чтении ответа от сервера. Пожалуйста, повторите ввод"));
            registerNewUser();
        }
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
