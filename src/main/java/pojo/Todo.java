package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    @JsonProperty("id")
    private long id;
    @JsonProperty("text")
    private String text;
    @JsonProperty("completed")
    private boolean completed;

}