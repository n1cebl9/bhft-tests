package tests;

import interfaces.RestInterface;
import interfaces.WebSocketInterface;
import managers.TodoAppManager;
import managers.ValidationManager;
import data.RestApiParameters;
import managers.WebSocketManager;
import org.testng.annotations.AfterMethod;
import pojo.Message;
import utils.TodoAppRestApiClient;
import pojo.Todo;
import utils.TodoAppWebSocketClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class TestBase {

    // gradle.properties that could be used at any time during the test execution
    protected static Properties properties;

    // singleton variables
    protected static TodoAppManager todoAppManager;
    protected static ValidationManager validationManager;
    protected static WebSocketManager webSocketManager;

    static {
        try {
            // try to read gradle.properties file
            properties = loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RestApiParameters restApiParameters = new RestApiParameters(
                properties.getProperty("todoApp.url"),
                properties.getProperty("todoApp.login"),
                properties.getProperty("todoApp.password")
        );

        // prepare rest api client and manager
        RestInterface<Todo> restApiClient = new TodoAppRestApiClient(restApiParameters);
        todoAppManager = new TodoAppManager(restApiClient);

        validationManager = new ValidationManager();

        // prepare websocket client and manager
        WebSocketInterface<Message> webSocketClient = new TodoAppWebSocketClient(properties.getProperty("todoApp.ws"));
        webSocketManager = new WebSocketManager(webSocketClient);
    }


    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        try {
            InputStream input = new FileInputStream("gradle.properties");
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("properties not found");
            throw new IOException();
        }

        return properties;
    }

    @AfterMethod
    public void closeWebSocketSession() {
        webSocketManager.closeSession();
    }

}
