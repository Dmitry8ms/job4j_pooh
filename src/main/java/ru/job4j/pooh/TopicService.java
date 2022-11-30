package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> mapMap =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("OK", "201");
        if ("POST".equals(req.httpRequestType())) {
            var mapQueue = mapMap.get(req.getSourceName());
            if (null != mapQueue) {
                for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry : mapQueue.entrySet()) {
                        mapQueue.get(entry.getKey()).add(req.getParam());
                }
            } else {
                result = new Resp("No Subscribers to post", "400");
            }
        } else if ("GET".equals(req.httpRequestType())) {
            mapMap.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            var mapQueue = mapMap.get(req.getSourceName());
                    var nullIfRegistered = mapQueue.putIfAbsent(req.getParam(),
                    new ConcurrentLinkedQueue<>());
            var queue = mapMap.get(req.getSourceName()).get(req.getParam());
            var text = queue.poll();
            if (text != null) {
                result = new Resp(text, "200");
            } else if (nullIfRegistered == null) {
                result = new Resp(req.getParam() + " subscribed", "201");
            } else {
                result = new Resp("", "204");
            }
        } else {
            throw new IllegalArgumentException("Unknown request type");
        }
        return result;
    }
}
