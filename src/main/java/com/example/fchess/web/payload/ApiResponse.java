package com.example.fchess.web.payload;

public class ApiResponse {
    private boolean result;
    private String message;



    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
