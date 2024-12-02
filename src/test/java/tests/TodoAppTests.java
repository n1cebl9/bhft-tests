package tests;

import data.TodoDataParameters;
import dataModels.TodoAppDataModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TodoAppTests extends TestBase {

    /*
    Allure report preparation and logging not implemented

    GET request is performed in each validation test to check how todoapp works

    Cases:
    - POST and GET
        - id == -1 (code 400)
        - id == null (code 400)
        - id == 0
        - id == 1
        - text == ''
        - text == null (code 400)
        - text == '   '
        - text == '&'
        - text == '1'
        - text is random string
        - completed == true
        - completed == false
        - completed == null (code 400)
    - PUT and GET
        - put new text == ''
        - put new text == null (code 401, existing should not be updated)
        - put new text == '   '
        - put new text == '&'
        - put new text == '1'
        - put new text is random string
        - change completed status
        - todos is not exist [manual check]
    - DELETE and GET
        - regular
        - unauthorized [manual check]
        - todos is not exist [manual check]
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
    - websocket
        - id and completed values
        - text values

     */

    // to ensure that there is no old data and test is clean
    @BeforeMethod
    public void beforeMethod() {
        todoAppManager
                .deleteAll();
    }

    @DataProvider
    public Object[] postAndGetDataProvider() {
        return new Object[] {
                // id field
                new TodoAppDataModel("id == -1")
                        .prepareData(
                                new TodoDataParameters()
                                        .withStatusCode(400)
                                        .withId(-1L)
                ),
                new TodoAppDataModel("id == null")
                        .prepareData(
                                new TodoDataParameters()
                                        .withStatusCode(400)
                                        .withId(null)
                ),
                new TodoAppDataModel("id == 0")
                        .prepareData(
                                new TodoDataParameters()
                                        .withId(0L)
                ),
                new TodoAppDataModel("id == 1")
                        .prepareData(
                                new TodoDataParameters()
                                        .withId(1L)
                ),
                // text field
                new TodoAppDataModel("text == ''")
                        .prepareData(
                                new TodoDataParameters()
                                        .withText("")
                ),
                new TodoAppDataModel("text == null")
                        .prepareData(
                                new TodoDataParameters()
                                        .withStatusCode(400)
                                        .withText(null)
                ),
                new TodoAppDataModel("text == '   '")
                        .prepareData(
                                new TodoDataParameters()
                                        .withText("   ")
                ),
                new TodoAppDataModel("text == '&'")
                        .prepareData(
                                new TodoDataParameters()
                                        .withText("&")
                ),
                new TodoAppDataModel("text == '1'")
                        .prepareData(
                                new TodoDataParameters()
                                        .withText("1")
                ),
                new TodoAppDataModel("text is random string")
                        .prepareData(new TodoDataParameters()),
                // completed field
                new TodoAppDataModel("completed == true")
                        .prepareData(
                                new TodoDataParameters()
                                        .withCompleted(true)
                        ),
                new TodoAppDataModel("completed == false")
                        .prepareData(
                                new TodoDataParameters()
                                        .withCompleted(false)
                ),
                new TodoAppDataModel("completed == null")
                        .prepareData(
                                new TodoDataParameters()
                                        .withStatusCode(400)
                                        .withCompleted(null)
                ),
        };
    }

    @Test(testName = "POST & GET request", dataProvider = "postAndGetDataProvider")
    public void postAndGetTest(TodoAppDataModel dataModel) {
        todoAppManager
                // insert data into todoapp
                .postTodo(dataModel)
                // get newly created todos
                .getTodo(dataModel);
        validationManager
                // validate that actual == expected
                .validateTodo(dataModel)
                // if data is invalid it should not be found
                .validateInvalidTodoIsMissing(dataModel);
    }

    @DataProvider
    public Object[] putAndGetDataProvider() {
        return new Object[] {
                new TodoAppDataModel("put new text == ''")
                        .prepareData()
                        .prepareTodoForPut(
                                new TodoDataParameters()
                                        .withText("")
                ),
                new TodoAppDataModel("put new text == null")
                        .prepareData()
                        .prepareTodoForPut(
                                new TodoDataParameters()
                                        .withStatusCode(401)
                                        .withText(null)
                ),
                new TodoAppDataModel("put new text == '   '")
                        .prepareData()
                        .prepareTodoForPut(
                                new TodoDataParameters()
                                        .withText("   ")
                ),
                new TodoAppDataModel("put new text == '&'")
                        .prepareData()
                        .prepareTodoForPut(
                                new TodoDataParameters()
                                        .withText("&")
                ),
                new TodoAppDataModel("put new text == '1'")
                        .prepareData()
                        .prepareTodoForPut(
                                new TodoDataParameters()
                                        .withText("1")
                ),
                new TodoAppDataModel("put new text == random string")
                        .prepareData()
                        .prepareTodoForPut(
                                new TodoDataParameters()
                ),
                new TodoAppDataModel("change completed status")
                        .prepareData()
                        .prepareTodoForPut(
                                new TodoDataParameters()
                                        .withCompleted(true)
                )
        };
    }

    @Test(testName = "PUT & GET request", dataProvider = "putAndGetDataProvider")
    public void putAndGetTest(TodoAppDataModel dataModel) {
        // POST request is needed to create existing todos
        todoAppManager
                // insert data into todoapp
                .postTodo(dataModel);

        todoAppManager
                .putTodo(dataModel)
                // get updated todos
                .getTodo(dataModel);
        validationManager
                // validate that actual == expected
                .validateTodo(dataModel)
                // if data is invalid it should not be found
                .validateInvalidTodoIsMissing(dataModel);
    }

    @DataProvider
    public Object[] deleteAndGetDataProvider() {
        return new Object[] {
                new TodoAppDataModel("regular")
                        .prepareData(
                                new TodoDataParameters()
                )
        };
    }

    @Test(testName = "DELETE & GET request", dataProvider = "deleteAndGetDataProvider")
    public void deleteAndGetTest(TodoAppDataModel dataModel) {
        // POST request is needed to create existing todos
        todoAppManager
                // insert data into todoapp
                .postTodo(dataModel);

        // delete, get, validate
        todoAppManager
                .deleteTodo(dataModel)
                .getTodo(dataModel);
        validationManager
                .validateTodoIsMissing(dataModel);
    }

    @DataProvider
    public Object[] getParametersDataProvider() {
        return new Object[] {
                new TodoAppDataModel("only offset")
                        .prepareData(50)
                        .withOffset(10)
                        .calculateExpectedCount(),
                new TodoAppDataModel("only limit")
                        .prepareData(200)
                        .withLimit(100)
                        .calculateExpectedCount(),
                new TodoAppDataModel("offset & limit > expected count")
                        .prepareData(50)
                        .withOffset(5)
                        .withLimit(46)
                        .calculateExpectedCount(),
                new TodoAppDataModel("offset & limit < expected count")
                        .prepareData(50)
                        .withOffset(5)
                        .withLimit(44)
                        .calculateExpectedCount(),
                new TodoAppDataModel("offset & limit == expected count")
                        .prepareData(50)
                        .withOffset(5)
                        .withLimit(45)
                        .calculateExpectedCount()
        };
    }

    @Test(testName = "GET parameters", dataProvider = "getParametersDataProvider")
    public void getParametersTest(TodoAppDataModel dataModel) {
        todoAppManager
                .postTodo(dataModel)
                .getTodo(dataModel);
        validationManager
                .validateTodoCount(dataModel);
    }

    @DataProvider
    public Object[] postSimplePerformanceDataProvider() {
        return new Object[] {
                new TodoAppDataModel("100")
                        .prepareData(100),
                new TodoAppDataModel("1000")
                        .prepareData(1000),
                new TodoAppDataModel("5000")
                        .prepareData(5000),
                new TodoAppDataModel("10000")
                        .prepareData(10000)
        };
    }

    @Test(testName = "POST simple performance", dataProvider = "postSimplePerformanceDataProvider")
    public void postSimplePerformanceTest(TodoAppDataModel dataModel) {
        long startTime = System.currentTimeMillis();

        todoAppManager
                .postTodo(dataModel);

        double secondsSpent = (double) (System.currentTimeMillis() - startTime) / 1000;

        System.out.println("count: " + dataModel.getDataList().size() + ", time spent in seconds: " + secondsSpent);
    }

    @DataProvider
    public Object[] websocketDataProvider() {
        return new Object[] {
                new TodoAppDataModel("id and completed values")
                        .prepareData(
                                new TodoDataParameters()
                                        .withId(0L)
                                        .withText("")
                                        .withCompleted(true),
                                new TodoDataParameters()
                                        .withId(1L)
                                        .withText("")
                        ),
                new TodoAppDataModel("text values")
                        .prepareData(
                                new TodoDataParameters(),
                                new TodoDataParameters()
                                        .withText("&"),
                                new TodoDataParameters()
                                        .withText("   "),
                                new TodoDataParameters()
                                        .withText("1"),
                                new TodoDataParameters()
                                        .withText("")
                        )
        };
    }

    @Test(testName = "websocket", dataProvider = "websocketDataProvider")
    public void websocketTest(TodoAppDataModel dataModel) {
        // open websocket session and link websocket messages list to dataModel
        webSocketManager
                .openSession()
                .linkMessageList(dataModel);

        // post new todos
        todoAppManager
                .postTodo(dataModel);
        // validate websocket messages about newly added todos
        validationManager
                .validateWebsocketMessage(dataModel);
    }
}
