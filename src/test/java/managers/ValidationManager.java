package managers;

import dataModels.TodoData;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import pojo.Message;

import java.util.List;

public class ValidationManager {

    public ValidationManager validateTodo(List<TodoData> todoDataList) {
        SoftAssertions softAssertions = new SoftAssertions();

        for (TodoData todoData : todoDataList) {
            softAssertions.assertThat(todoData.getTodoToRetrieve())
                    .usingRecursiveComparison()
                    .as("id = " + todoData.getTodoToInsert().getId())
                    .isEqualTo(todoData.getTodoToInsert());
        }

        softAssertions.assertAll();

        return this;
    }

    public ValidationManager validateTodoCount(List<TodoData> todoDataList, int expectedCount) {
        long presentCount = todoDataList.stream().filter(d -> d.getTodoToRetrieve() != null).count();
        Assertions.assertThat(presentCount)
                .as("todos count mismatch")
                .isEqualTo(expectedCount);

        return this;
    }

    public ValidationManager validateWebsocketMessage(List<Message> messageList, List<TodoData> todoDataList) {
        SoftAssertions softAssertions = new SoftAssertions();

        for (TodoData todoData : todoDataList) {
            Message message = messageList.stream()
                    .filter(m -> m.getData().getId() == todoData.getTodoToInsert().getId())
                    .findAny()
                    .orElse(null);

            if (message == null) {
                softAssertions.fail("message not found for id = " + todoData.getTodoToInsert().getId());
                continue;
            }

            softAssertions.assertThat(message.getData())
                    .usingRecursiveComparison()
                    .as("message data for id = " + todoData.getTodoToInsert().getId())
                    .isEqualTo(todoData.getTodoToInsert());
            softAssertions.assertThat(message.getType())
                    .as("message type for id = " + todoData.getTodoToInsert().getId())
                    .isEqualTo("new_todo");
        }

        softAssertions.assertAll();

        return this;
    }

    public ValidationManager validateTodoIsMissing(List<TodoData> todoDataList) {
        SoftAssertions softAssertions = new SoftAssertions();

        for (TodoData todoData : todoDataList) {
            softAssertions.assertThat(todoData.getTodoToRetrieve())
                    .as("id = " + todoData.getTodoToInsert().getId())
                    .isNull();
        }

        softAssertions.assertAll();

        return this;
    }

}
