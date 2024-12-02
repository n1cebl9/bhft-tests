package managers;

import dataModels.BaseDataModel;
import interfaces.WebSocketInterface;
import pojo.Message;

public class WebSocketManager {

    private final WebSocketInterface<Message> webSocketClient;

    public WebSocketManager(WebSocketInterface<Message> webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public WebSocketManager openSession() {
        webSocketClient.openSession();

        return this;
    }

    // link message list to dataModel
    public WebSocketManager linkMessageList(BaseDataModel dataModel) {
        dataModel.setMessageList(webSocketClient.getMessageList());

        return this;
    }

    public WebSocketManager closeSession() {
        webSocketClient.closeSession();

        return this;
    }

}
