package dataModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataModel<D, M> {

    protected String caseDescription = "";

    // every bit of test data (actual and expected) should be stored in this dataList
    @Getter protected final List<D> dataList = new ArrayList<>();

    // generic type for various messages from websockets
    @Getter @Setter protected List<M> messageList;

    @Override
    public String toString() {
        return caseDescription;
    }

}
