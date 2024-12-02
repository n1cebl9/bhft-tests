package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RestApiParameters {

    private String url;
    private String login;
    private String password;

}
