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
        String text;
        if (!HttpRequestType.POST.value().equals(req.httpRequestType())
                && !HttpRequestType.GET.value().equals(req.httpRequestType())) {
            throw new IllegalArgumentException("Unknown request type");
        }
        if (HttpRequestType.POST.value().equals(req.httpRequestType())) {
            var mapQueue = mapMap.get(req.getSourceName());
            if (null == mapQueue) {
                return new Resp("No Subscribers to post", StatusCodes.BAD_REQUEST_400.code());
            }
            for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry : mapQueue.entrySet()) {
                mapQueue.get(entry.getKey()).add(req.getParam());
            }
            return new Resp("OK", StatusCodes.OK_200.code());
        } else {
            mapMap.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            var mapQueue = mapMap.get(req.getSourceName());
            var nullIfRegistered = mapQueue.putIfAbsent(req.getParam(),
                    new ConcurrentLinkedQueue<>());
            var queue = mapMap.get(req.getSourceName()).get(req.getParam());
            text = queue.poll();
            if (nullIfRegistered == null) {
                return new Resp(req.getParam() + " subscribed", StatusCodes.CREATED_201.code());
            } else if (text == null) {
                return new Resp("", StatusCodes.NO_CONTENT_204.code());
            }
        }
        return new Resp(text, StatusCodes.OK_200.code());
    }
}
