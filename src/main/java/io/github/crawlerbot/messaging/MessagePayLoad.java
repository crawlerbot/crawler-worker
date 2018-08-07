package io.github.crawlerbot.messaging;

import io.github.crawlerbot.enumerations.BrowserOS;

public class MessagePayLoad {
    private String message;
    private MessageObject messageObject;
    private MessageAction messageAction;
    private BrowserOS browserOS;

    public MessagePayLoad() {

    }

    public BrowserOS getBrowserOS() {
        return browserOS;
    }

    public void setBrowserOS(BrowserOS browserOS) {
        this.browserOS = browserOS;
    }

    public MessageObject getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(MessageObject messageObject) {
        this.messageObject = messageObject;
    }

    public MessageAction getMessageAction() {
        return messageAction;
    }

    public void setMessageAction(MessageAction messageAction) {
        this.messageAction = messageAction;
    }

    public String getMessage() {
        return message;
    }

    public MessagePayLoad setMessage(String message) {
        this.message = message;
        return this;
    }
}
