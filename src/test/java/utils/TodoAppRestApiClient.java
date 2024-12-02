package utils;

import data.RestApiParameters;
import interfaces.RestInterface;
import lombok.AllArgsConstructor;
import pojo.Todo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@AllArgsConstructor
public class TodoAppRestApiClient implements RestInterface<Todo> {

    private static final String METHOD = "/todos";
    private static final String CONTENT_TYPE = "application/json";

    public RestApiParameters restApiParameters;

    public List<Todo> get(int statusCode, Map<String, String> params) {
        return given()
                .queryParams(params)
                .get(restApiParameters.getUrl() + METHOD)
                .then()
                .statusCode(statusCode)
                .extract()
                .jsonPath().getList("$", Todo.class);
    }

    public List<Todo> get(int statusCode) {
        return get(statusCode, new HashMap<>());
    }

    public void post(int statusCode, Todo todo) {
        given()
                .contentType(CONTENT_TYPE)
                .body(todo)
                .post(restApiParameters.getUrl() + METHOD)
                .then()
                .statusCode(statusCode);
    }

    public void put(int statusCode, Todo todo) {
        given()
                .contentType(CONTENT_TYPE)
                .body(todo)
                .put(restApiParameters.getUrl() + METHOD + "/" + todo.getId())
                .then()
                .statusCode(statusCode);
    }

    public void delete(int statusCode, Object id) {
        if (!(id instanceof Long)) {
            throw new IllegalArgumentException("not valid type of id");
        }

        given()
                .auth()
                .preemptive()
                .basic(restApiParameters.getLogin(), restApiParameters.getPassword())
                .delete(restApiParameters.getUrl() + METHOD + "/"  + id)
                .then()
                .statusCode(statusCode);
    }

}
