package managers;

import data.TodoData;
import dataModels.TodoAppDataModel;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import pojo.Message;
import pojo.Todo;

import java.util.List;

public class ValidationManager {

    public ValidationManager validateTodo(TodoAppDataModel dataModel) {
        SoftAssertions softAssertions = new SoftAssertions();

        // check only valid data
        for (TodoData todoData : dataModel.getDataList().stream().filter(TodoData::isValid).toList()) {
            // if there is todos for updates use it otherwise use original
            Todo expected = todoData.getTodoToUpdate() != null && todoData.isValidAfterUpdate()
                    ? todoData.getTodoToUpdate()
                    : todoData.getTodoToSent();

            softAssertions.assertThat(todoData.getTodoFromApp())
                    .usingRecursiveComparison()
                    .as("id = " + todoData.getTodoToSent().getId())
                    .isEqualTo(expected);
        }

        softAssertions.assertAll();

        return this;
    }

    public ValidationManager validateTodoCount(TodoAppDataModel dataModel) {
        long presentCount = dataModel.getDataList().stream().filter(d -> d.getTodoFromApp() != null).count();
        Assertions.assertThat(presentCount)
                .as("todos count mismatch")
                .isEqualTo(dataModel.getExpectedCount());

        return this;
    }

    public ValidationManager validateWebsocketMessage(TodoAppDataModel dataModel) {
        SoftAssertions softAssertions = new SoftAssertions();

        for (TodoData todoData : dataModel.getDataList()) {
            Message message = dataModel.getMessageList().stream()
                    .filter(m -> m.getData().getId().equals(todoData.getTodoToSent().getId()))
                    .findAny()
                    .orElse(null);

            if (message == null) {
                softAssertions.fail("message not found for id = " + todoData.getTodoToSent().getId());
                continue;
            }

            softAssertions.assertThat(message.getData())
                    .usingRecursiveComparison()
                    .as("message data for id = " + todoData.getTodoToSent().getId())
                    .isEqualTo(todoData.getTodoToSent());
            softAssertions.assertThat(message.getType())
                    .as("message type for id = " + todoData.getTodoToSent().getId())
                    .isEqualTo("new_todo");
        }

        softAssertions.assertAll();

        return this;
    }

    public ValidationManager validateTodoIsMissing(TodoAppDataModel dataModel) {
        SoftAssertions softAssertions = new SoftAssertions();

        // check only valid data
        for (TodoData todoData : dataModel.getDataList().stream().filter(TodoData::isValid).toList()) {
            softAssertions.assertThat(todoData.getTodoFromApp())
                    .as("id = " + todoData.getTodoToSent().getId())
                    .isNull();
        }

        softAssertions.assertAll();

        return this;
    }

    public ValidationManager validateInvalidTodoIsMissing(TodoAppDataModel dataModel) {
        SoftAssertions softAssertions = new SoftAssertions();

        // check only invalid data
        for (TodoData todoData : dataModel.getDataList().stream().filter(d -> !d.isValid()).toList()) {
            softAssertions.assertThat(todoData.getTodoFromApp())
                    .as("id = " + todoData.getTodoToSent().getId())
                    .isNull();
        }

        softAssertions.assertAll();

        return this;
    }

}
