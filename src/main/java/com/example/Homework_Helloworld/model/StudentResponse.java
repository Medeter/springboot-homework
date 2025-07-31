package com.example.Homework_Helloworld.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse {
    private boolean success;
    private String message;
    private Student data;
    private Map<String, String> errors;
    
    public StudentResponse() {}
    
    public StudentResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public StudentResponse(boolean success, String message, Student data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public StudentResponse(boolean success, Map<String, String> errors) {
        this.success = success;
        this.errors = errors;
    }
    
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
    
    public Student getData() {
        return data;
    }
    
    public void setData(Student data) {
        this.data = data;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
    
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
