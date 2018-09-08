package com.arif.friendchat;

import java.util.Date;

public class ChatMessage {

    public String message;
    public String email;
    public long timestamp;
    public ChatMessage() {
    }
    public ChatMessage(String user_email, String message) {
        this.message = message;
        this.email = user_email;

        // Initialize to current time
        timestamp = new Date().getTime();
    }


}