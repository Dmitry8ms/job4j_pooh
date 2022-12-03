package ru.job4j.pooh;

public enum HttpRequestType {
    GET("GET"),
    POST("POST");
    private final String value;
    HttpRequestType(String value) {
        this.value = value;
    }
    public String value() {
        return value;
    }
}
