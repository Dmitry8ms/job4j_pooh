package ru.job4j.pooh;

public enum StatusCodes {
    OK_200("200"),
    CREATED_201("201"),
    NO_CONTENT_204("204"),
    BAD_REQUEST_400("400");
    private final String code;
    StatusCodes(String code) {
        this.code = code;
    }
    public String code() {
        return code;
    }
}
