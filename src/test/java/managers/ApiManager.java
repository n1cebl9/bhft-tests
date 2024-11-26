package managers;

import dataModels.TodoData;
import pojo.Todo;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@AllArgsConstructor
public class ApiManager {

    private final String url;
    private final String login;
    private final String pass;

    private List<Todo> getTodo(Map<String, String> params) {
        return given()
                .queryParams(params)
                .get(url + "/todos")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList("$", Todo.class);
    }

    public ApiManager getTodo(List<TodoData> todoDataList, Map<String, String> params) {
        List<Todo> todos = getTodo(params);

        for (TodoData todoData : todoDataList) {
            todos.stream()
                    .filter(t -> t.getId() == todoData.getTodoToInsert().getId())
                    .findAny()
                    .ifPresentOrElse(
                            todoData::setTodoToRetrieve,
                            () -> todoData.setTodoToRetrieve(null));
        }

        return this;
    }

    public ApiManager getTodo(List<TodoData> todoDataList) {
        return getTodo(todoDataList, new HashMap<>());
    }

    private void postTodo(Todo todo) {
        given()
                .contentType("application/json")
                .body(todo)
                .post(url + "/todos")
                .then()
                .statusCode(201);
    }

    public ApiManager postTodo(List<TodoData> todoDataList) {
        for (TodoData todoData : todoDataList) {
            postTodo(todoData.getTodoToInsert());
        }

        return this;
    }

    private void putTodo(Todo todo) {
        given()
                .contentType("application/json")
                .body(todo)
                .put(url + "/todos/" + todo.getId())
                .then()
                .statusCode(200);
    }

    public ApiManager putTodo(List<TodoData> todoDataList) {
        for (TodoData todoData : todoDataList) {
            putTodo(todoData.getTodoToInsert());
        }

        return this;
    }

    public ApiManager deleteTodo(long id) {
        given()
                .auth()
                .preemptive()
                .basic(login, pass)
                //.header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .delete(url + "/todos/" + id)
                .then()
                .statusCode(204);

        return this;
    }

    public ApiManager deleteTodo(List<TodoData> todoDataList) {
        for (TodoData todoData : todoDataList) {
            deleteTodo(todoData.getTodoToInsert().getId());
        }

        return this;
    }

    public ApiManager deleteAll() {
        List<Todo> todos = getTodo(new HashMap<>());

        for (Todo todo : todos) {
            deleteTodo(todo.getId());
        }

        return this;
    }

}
