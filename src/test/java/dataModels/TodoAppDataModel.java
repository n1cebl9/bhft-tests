package dataModels;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import pojo.Message;
import pojo.Todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoAppDataModel {

    private String caseDescription = "";
    @Getter private final List<TodoData> todoDataList = new ArrayList<>();
    @Getter private final Map<String, String> params = new HashMap<>();
    @Getter @Setter private List<Message> messageList;
    @Getter private int expectedCount;

    public TodoAppDataModel prepareData(long id, String text, boolean completed) {
        TodoData todoData = new TodoData();

        text = StringUtils.isNotEmpty(text)
                ? text
                : "";

        Todo todo = new Todo(id, text, completed);

        todoData.setTodoToInsert(todo);

        todoDataList.add(todoData);

        return this;
    }

    public TodoAppDataModel prepareData(String text) {
        return prepareData(Long.parseLong(RandomStringUtils.randomNumeric(8)), text, false);
    }

    public TodoAppDataModel prepareData() {
        return prepareData(RandomStringUtils.randomAlphanumeric(128));
    }

    public TodoAppDataModel prepareData(int count) {
        for (int i = 0; i < count; i++) {
            prepareData();
        }

        return this;
    }

    public TodoAppDataModel updateTodoForPut(String text, boolean completed) {
        for (TodoData todoData : todoDataList) {
            todoData.getTodoToInsert().setText(text);
            todoData.getTodoToInsert().setCompleted(completed);
        }

        return this;
    }

    public TodoAppDataModel updateTodoForPut() {
        return updateTodoForPut(RandomStringUtils.randomAlphanumeric(64), true);
    }

    public TodoAppDataModel setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;

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

    public TodoAppDataModel calculateExpectedCount() {
        int offset = params.get("offset") == null ? 0 : Integer.parseInt(params.get("offset"));
        int limit = params.get("limit") == null ? 0 : Integer.parseInt(params.get("limit"));
        expectedCount = todoDataList.size() - offset;
        expectedCount = limit != 0 && limit < expectedCount ? limit : expectedCount;

        return this;
    }

    @Override
    public String toString() {
        return caseDescription;
    }

}
