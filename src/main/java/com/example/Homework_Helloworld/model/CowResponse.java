package com.example.Homework_Helloworld.model;

import java.util.Map;

public class CowResponse {
    private boolean success;
    private String message;
    private Object data;
    private Map<String, String> errors;
    
    // Constructors
    public CowResponse() {}
    
    public CowResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public CowResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public CowResponse(boolean success, Map<String, String> errors) {
        this.success = success;
        this.errors = errors;
        this.message = "❌ ข้อมูลไม่ถูกต้อง";
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
    
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
