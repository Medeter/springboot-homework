package com.example.Homework_Helloworld.model;

public class MessageResponse {
    private String message;
    private String status;
    
    public MessageResponse() {}
    
    public MessageResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
