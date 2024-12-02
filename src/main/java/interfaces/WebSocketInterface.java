package interfaces;

import java.util.List;

public interface WebSocketInterface<T> {

    void openSession();

    List<T> getMessageList();

    void closeSession();

}
