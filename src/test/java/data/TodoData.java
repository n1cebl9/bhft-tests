package data;

import pojo.Todo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoData {

    private int statusCode;
    private int statusCodeAfterUpdate;
    private boolean valid;
    private boolean validAfterUpdate;

    // object that will be sent
    // for POST, DELETE request
    private Todo todoToSent;
    // for PUT request
    private Todo todoToUpdate;
    // object that will be received from todoapp
    // for GET request
    private Todo todoFromApp;

}
