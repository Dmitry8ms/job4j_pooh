package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> mapQueue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text;
        if (!HttpRequestType.POST.value().equals(req.httpRequestType())
               && !HttpRequestType.GET.value().equals(req.httpRequestType())) {
            throw new IllegalArgumentException("Unknown request type");
        }
        if (HttpRequestType.POST.value().equals(req.httpRequestType())) {
            mapQueue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            mapQueue.get(req.getSourceName()).add(req.getParam());
            return new Resp("OK", StatusCodes.OK_200.code());
        } else {
            mapQueue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            var queue = mapQueue.get(req.getSourceName());
            text = queue.poll();
            if (text == null) {
                return new Resp("", StatusCodes.NO_CONTENT_204.code());
            }
        }
        return new Resp(text, StatusCodes.OK_200.code());
    }
}
