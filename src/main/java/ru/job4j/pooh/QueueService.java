package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> mapQueue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("OK", "201");
        if ("POST".equals(req.httpRequestType())) {
            mapQueue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            mapQueue.get(req.getSourceName()).add(req.getParam());
        } else if ("GET".equals(req.httpRequestType())) {
            mapQueue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            var queue = mapQueue.get(req.getSourceName());
            var text = queue.peek();
            if (text != null) {
                result = new Resp(text, "200");
            } else {
                result = new Resp("", "204");
            }
        } else {
            throw new IllegalArgumentException("Unknown request type");
        }
        return result;
    }
}
