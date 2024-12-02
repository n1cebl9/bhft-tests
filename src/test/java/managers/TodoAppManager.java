package managers;

import data.TodoData;
import dataModels.TodoAppDataModel;
import pojo.Todo;
import interfaces.RestInterface;

import java.util.List;

public class TodoAppManager {

    private final RestInterface<Todo> restApiClient;

    public TodoAppManager(RestInterface<Todo> restApiClient) {
        this.restApiClient = restApiClient;
    }

    public TodoAppManager getTodo(TodoAppDataModel dataModel) {
        List<Todo> todos = restApiClient.get(200, dataModel.getParams());

        for (TodoData todoData : dataModel.getDataList()) {
            todos.stream()
                    .filter(t -> t.getId().equals(todoData.getTodoToSent().getId()))
                    .findAny()
                    .ifPresentOrElse(
                            todoData::setTodoFromApp,
                            () -> todoData.setTodoFromApp(null));
        }

        return this;
    }

    public TodoAppManager postTodo(TodoAppDataModel dataModel) {
        for (TodoData todoData : dataModel.getDataList()) {
            // if todoData does not specify code use default 201
            int code = todoData.getStatusCode() == 0 ? 201 : todoData.getStatusCode();

            restApiClient.post(code, todoData.getTodoToSent());
        }

        return this;
    }

    public TodoAppManager putTodo(TodoAppDataModel dataModel) {
        for (TodoData todoData : dataModel.getDataList()) {
            // if todoData does not specify code use default 200
            int code = todoData.getStatusCodeAfterUpdate() == 0 ? 200 : todoData.getStatusCodeAfterUpdate();

            restApiClient.put(code, todoData.getTodoToUpdate());
        }

        return this;
    }

    public TodoAppManager deleteTodo(TodoAppDataModel dataModel) {
        for (TodoData todoData : dataModel.getDataList()) {
            // if todoData does not specify code use default 204
            int code = todoData.getStatusCode() == 0 ? 204 : todoData.getStatusCode();

            restApiClient.delete(code, todoData.getTodoToSent().getId());
        }

        return this;
    }

    public TodoAppManager deleteAll() {
        List<Todo> todos = restApiClient.get(200);

        for (Todo todo : todos) {
            restApiClient.delete(204, todo.getId());
        }

        return this;
    }

}
