package com.example.mychat;

public class Message {
    private String msg;
    private String receiver;
    private String sender;

    Message(String sender2, String receiver2, String msg2) {
        this.sender = sender2;
        this.receiver = receiver2;
        this.msg = msg2;
    }

    public String getSender() {
        return this.sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public String getMsg() {
        return this.msg;
    }
}
