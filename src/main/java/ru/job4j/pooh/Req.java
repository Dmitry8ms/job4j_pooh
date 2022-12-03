package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;
    private final static int TOPICPARAM = 4;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String startingLine = parseStartingLine(content);
        String httpRequestType = parseMethod(startingLine);
        String poohMode = parseMode(startingLine);
        return new Req(httpRequestType, poohMode, parseTheme(startingLine),
                parseParam(content, httpRequestType, poohMode));
    }

    private static String parseStartingLine(String content) {
        String[] lines = content.split("\\R");
        return lines[0];
    }

    private static String parseParam(String content, String httpRequestType, String poohMode) {
        String[] lines = content.split("\\R");
        String[] parses = lines[0].split(" ");
        String[] words = parses[1].split("/");
        String param = lines[lines.length - 1].trim();
        if ((HttpRequestType.GET.value().equals(httpRequestType) && PoohMode.QUEUE.value().equals(poohMode))) {
            param = "";
        }
        if (words.length == TOPICPARAM) {
            param = words[words.length - 1];
        }
        return param;
    }

    private static String parseMethod(String startingLine) {
        String[] chunks = startingLine.split(" ");
        return chunks[0];
    }

    private static String parseMode(String startingLine) {
        String[] chunks = startingLine.split(" ");
        String[] words = chunks[1].split("/");
        return words[1];
    }

    private static String parseTheme(String startingLine) {
        String[] chunks = startingLine.split(" ");
        String[] words = chunks[1].split("/");
        return words[2];
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
