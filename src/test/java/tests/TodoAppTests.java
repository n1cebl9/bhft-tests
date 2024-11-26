package tests;

import dataModels.TodoAppDataModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TodoAppTests extends TestBase {

    /*
    Allure report preparation and logging not implemented
    Negative cases not implemented

    Cases:
    - integration CRUD + ws checks
        - id and completed values
            - -1 [not implemented, manual checks]
            - 0 and true
            - 1 and false
        - text values
            - alphanumeric
            - text is "&"
            - text is "   "
            - text is "1"
            - text is empty
    - GET parameters
        - only offset
        - only limit
        - offset & limit > expected count
        - offset & limit < expected count
        - offset & limit == expected count
    - POST simple performance
        - 100
        - 1000
        - 5000
        - 10000

     */

    @DataProvider
    public Object[] integrationDataProvider() {
        return new Object[] {
                new TodoAppDataModel()
                        .setCaseDescription("id and completed values")
                        .prepareData(0, "", true)
                        .prepareData(1, "", false),
                new TodoAppDataModel()
                        .setCaseDescription("text values")
                        .prepareData()
                        .prepareData("&")
                        .prepareData("   ")
                        .prepareData("1")
                        .prepareData("")
        };
    }

    @Test(testName = "integration", dataProvider = "integrationDataProvider")
    public void integrationTest(TodoAppDataModel dataModel) {
        webSocketClient.init();
        // link message list to dataModel
        dataModel.setMessageList(webSocketClient.getMessageList());

        // post, get, validate
        apiManager
                .postTodo(dataModel.getTodoDataList())
                .getTodo(dataModel.getTodoDataList());
        validationManager
                .validateTodo(dataModel.getTodoDataList())
                // validate websocket messages about newly added todos
                .validateWebsocketMessage(dataModel.getMessageList(), dataModel.getTodoDataList());

        // put, get, validate
        dataModel.updateTodoForPut();
        apiManager
                .putTodo(dataModel.getTodoDataList())
                .getTodo(dataModel.getTodoDataList());
        validationManager
                .validateTodo(dataModel.getTodoDataList());

        // delete, get, validate
        apiManager
                .deleteTodo(dataModel.getTodoDataList())
                .getTodo(dataModel.getTodoDataList());
        validationManager
                .validateTodoIsMissing(dataModel.getTodoDataList());
    }

    @DataProvider
    public Object[] getParametersDataProvider() {
        return new Object[] {
                new TodoAppDataModel()
                        .setCaseDescription("only offset")
                        .prepareData(50)
                        .withOffset(10)
                        .calculateExpectedCount(),
                new TodoAppDataModel()
                        .setCaseDescription("only limit")
                        .prepareData(200)
                        .withLimit(100)
                        .calculateExpectedCount(),
                new TodoAppDataModel()
                        .setCaseDescription("offset & limit > expected count")
                        .prepareData(50)
                        .withOffset(5)
                        .withLimit(46)
                        .calculateExpectedCount(),
                new TodoAppDataModel()
                        .setCaseDescription("offset & limit < expected count")
                        .prepareData(50)
                        .withOffset(5)
                        .withLimit(44)
                        .calculateExpectedCount(),
                new TodoAppDataModel()
                        .setCaseDescription("offset & limit == expected count")
                        .prepareData(50)
                        .withOffset(5)
                        .withLimit(45)
                        .calculateExpectedCount()
        };
    }

    @Test(testName = "GET parameters", dataProvider = "getParametersDataProvider")
    public void getParametersTest(TodoAppDataModel dataModel) {
        apiManager
                .deleteAll()
                .postTodo(dataModel.getTodoDataList())
                .getTodo(dataModel.getTodoDataList(), dataModel.getParams());
        validationManager
                .validateTodoCount(dataModel.getTodoDataList(), dataModel.getExpectedCount());
    }

    @DataProvider
    public Object[] postSimplePerformanceDataProvider() {
        return new Object[] {
                new TodoAppDataModel()
                        .setCaseDescription("100")
                        .prepareData(100),
                new TodoAppDataModel()
                        .setCaseDescription("1000")
                        .prepareData(1000),
                new TodoAppDataModel()
                        .setCaseDescription("5000")
                        .prepareData(5000),
                new TodoAppDataModel()
                        .setCaseDescription("10000")
                        .prepareData(10000)
        };
    }

    @Test(testName = "POST simple performance", dataProvider = "postSimplePerformanceDataProvider")
    public void postSimplePerformanceTest(TodoAppDataModel dataModel) {
        apiManager
                .deleteAll();

        long startTime = System.currentTimeMillis();

        apiManager
                .postTodo(dataModel.getTodoDataList());

        double secondsSpent = (double) (System.currentTimeMillis() - startTime) / 1000;

        System.out.println("count: " + dataModel.getTodoDataList().size() + ", time spent in seconds: " + secondsSpent);
    }

    @AfterMethod
    public void closeWebSocketSession() {
        webSocketClient.closeSession();
    }
}
