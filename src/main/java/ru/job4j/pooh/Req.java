package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] lines = content.split("\\R");
        String[] parses = lines[0].split(" ");
        String[] words = parses[1].split("/");
        String httpRequestType = parses[0];
        String poohMode = words[1];
        String sourceName = words[2];
        String param = "";
        if ("POST".equals(httpRequestType)) {
            param = lines[lines.length - 1].trim();
        } else if ("GET".equals(httpRequestType) && "topic".equals(poohMode)) {
            if (words.length == 4) {
                param = words[3];
            } else {
                param = lines[lines.length - 1].trim();
            }
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
