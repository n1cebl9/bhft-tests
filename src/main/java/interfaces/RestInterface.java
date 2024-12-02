package interfaces;

import java.util.List;
import java.util.Map;

public interface RestInterface<T> {

    List<T> get(int code, Map<String, String> params);

    List<T> get(int code);

    void post(int code, T t);

    void put(int code, T t);

    void delete(int code, Object value);

}
