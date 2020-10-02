package com.fucct.tobispring.user;

public class Message {
    String text;

    private Message(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
