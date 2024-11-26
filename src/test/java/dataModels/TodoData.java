package dataModels;

import pojo.Todo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoData {

    private boolean deleted;
    private Todo todoToInsert;
    private Todo todoToRetrieve;

}
