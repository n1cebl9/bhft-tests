package tests;

import managers.ApiManager;
import managers.ValidationManager;
import ws.Client;

public abstract class TestBase {

    // could be replaced with environment variables
    private static final String HTTP = "http://127.0.0.1:8080";
    // ! port 8080 to connect to websocket
    private static final String WEBSOCKET = "ws://127.0.0.1:8080/ws";
    private static final String LOGIN = "admin";
    private static final String PASS = "admin";

    protected static ApiManager apiManager;
    protected static ValidationManager validationManager;
    protected static Client webSocketClient;

    static {
        apiManager = new ApiManager(HTTP, LOGIN, PASS);
        webSocketClient = new Client(WEBSOCKET);
        validationManager = new ValidationManager();
    }

}
