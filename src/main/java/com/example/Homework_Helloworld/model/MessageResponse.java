package com.example.Homework_Helloworld.model;

public class MessageResponse {
    private String massage;
    
    public MessageResponse() {}
    
    public MessageResponse(String massage) {
        this.massage = massage;
    }
    
    public String getMassage() {
        return massage;
    }
    
    public void setMassage(String massage) {
        this.massage = massage;
    }
}
