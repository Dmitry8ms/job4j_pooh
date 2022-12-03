package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> mapQueue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("OK", StatusCodes.OK_200.code());
        if (HttpRequestType.POST.value().equals(req.httpRequestType())) {
            mapQueue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            mapQueue.get(req.getSourceName()).add(req.getParam());
        } else if (HttpRequestType.GET.value().equals(req.httpRequestType())) {
            mapQueue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            var queue = mapQueue.get(req.getSourceName());
            var text = queue.poll();
            if (text != null) {
                result = new Resp(text, StatusCodes.OK_200.code());
            } else {
                result = new Resp("", StatusCodes.NO_CONTENT_204.code());
            }
        } else {
            throw new IllegalArgumentException("Unknown request type");
        }
        return result;
    }
}
