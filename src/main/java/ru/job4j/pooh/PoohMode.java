package ru.job4j.pooh;

public enum PoohMode {
    QUEUE("queue"),
    TOPIC("topic");
    private final String value;
    PoohMode(String value) {
        this.value = value;
    }
    public String value() {
        return value;
    }
}
