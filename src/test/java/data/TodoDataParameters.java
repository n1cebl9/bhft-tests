package data;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
public class TodoDataParameters {

    /*
    TodoDataParameters ensures builder pattern
     */

    private Integer statusCode = 0;
    private Long id = Long.parseLong(RandomStringUtils.randomNumeric(8));
    private String text = RandomStringUtils.randomAlphanumeric(128);
    private Boolean completed = false;

    public TodoDataParameters withStatusCode(Integer statusCode) {
        this.statusCode = statusCode;

        return this;
    }

    public TodoDataParameters withId(Long id) {
        this.id = id;

        return this;
    }

    public TodoDataParameters withText(String text) {
        this.text = text;

        return this;
    }

    public TodoDataParameters withCompleted(Boolean completed) {
        this.completed = completed;

        return this;
    }
}
