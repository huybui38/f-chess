package com.example.fchess.web.payload;

import java.util.*;

public class ApiResponse {
    private String message;
    private Map<String, Object> body;
    private List<String> errors;
    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.body = new LinkedHashMap<>();
        this.errors = new ArrayList<>();
    }

    public int getStatus() {
        return status;
    }

    public void setErrors(List<String> errors){
        this.errors = errors;
    }
    public void addError(String msg){
        errors.add(msg);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String,Object> toMap(){
        body.put("timestamp", new Date());
        body.put("status", status);
        body.put("message", message);
        if (errors.size() > 0){
            body.put("errors", errors);
        }
        return body;
    }
}
