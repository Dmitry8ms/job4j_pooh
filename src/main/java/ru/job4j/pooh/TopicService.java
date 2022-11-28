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
                result = new Resp("No Subscribers", "400");
            }
        }
        return result;
    }
}
