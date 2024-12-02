package dataModels;

import data.TodoData;
import data.TodoDataParameters;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import pojo.Message;
import pojo.Todo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TodoAppDataModel extends BaseDataModel<TodoData, Message> {

    /*
    dataModel stores all data for tests,
    it has methods to prepare new data,
    all data transactions should be made through dataModel to ensure easy access to all test data in all the time

    prepareData is the main method to prepare test data, has some overloads for single and multiple preparations
     */

    // parameters specific to todoapp
    private final Map<String, String> params = new HashMap<>();
    private int expectedCount;

    public TodoAppDataModel(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    // create new todos data and store it in dataList
    // prepareData method creates TodoData for each TodoDataParameters
    public TodoAppDataModel prepareData(TodoDataParameters... dataParametersArgs) {
        for (TodoDataParameters dataParameters : dataParametersArgs) {
            TodoData todoData = new TodoData();

            int code = dataParameters.getStatusCode();
            todoData.setStatusCode(code);
            // if code is not specified data should be counted as valid
            // additionally 2xx codes are also should be counted as valid
            todoData.setValid(code == 0 || code == 200 || code == 201 || code == 204);

            Todo todo = new Todo(dataParameters.getId(), dataParameters.getText(), dataParameters.getCompleted());

            todoData.setTodoToSent(todo);

            dataList.add(todoData);
        }

        return this;
    }

    public TodoAppDataModel prepareData() {
        return prepareData(new TodoDataParameters());
    }

    public TodoAppDataModel prepareData(int count) {
        for (int i = 0; i < count; i++) {
            prepareData(new TodoDataParameters());
        }

        return this;
    }

    // update existing todos with new values
    public TodoAppDataModel prepareTodoForPut(TodoDataParameters dataParameters) {
        for (TodoData todoData : dataList) {
            int code = dataParameters.getStatusCode();
            // update status code
            todoData.setStatusCodeAfterUpdate(code);
            // update valid
            todoData.setValidAfterUpdate(code == 0 || code == 200 || code == 201 || code == 204);

            // prepare updated todos
            Todo todoToUpdate = new Todo();
            todoToUpdate.setId(todoData.getTodoToSent().getId());
            todoToUpdate.setText(dataParameters.getText());
            todoToUpdate.setCompleted(dataParameters.getCompleted());
            todoData.setTodoToUpdate(todoToUpdate);
        }

        return this;
    }

    public TodoAppDataModel withOffset(int value) {
        params.put("offset", String.valueOf(value));

        return this;
    }

    public TodoAppDataModel withLimit(int value) {
        params.put("limit", String.valueOf(value));

        return this;
    }

    // retrieve parameters from map and calculate expected count
    public TodoAppDataModel calculateExpectedCount() {
        int offset = params.get("offset") == null ? 0 : Integer.parseInt(params.get("offset"));
        int limit = params.get("limit") == null ? 0 : Integer.parseInt(params.get("limit"));
        expectedCount = dataList.size() - offset;
        expectedCount = limit != 0 && limit < expectedCount ? limit : expectedCount;

        return this;
    }

}
