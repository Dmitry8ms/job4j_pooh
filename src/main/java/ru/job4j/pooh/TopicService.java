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
        Resp result = new Resp("OK", StatusCodes.OK_200.code());
        if (HttpRequestType.POST.value().equals(req.httpRequestType())) {
            var mapQueue = mapMap.get(req.getSourceName());
            if (null != mapQueue) {
                for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry : mapQueue.entrySet()) {
                        mapQueue.get(entry.getKey()).add(req.getParam());
                }
            } else {
                result = new Resp("No Subscribers to post", StatusCodes.BAD_REQUEST_400.code());
            }
        } else if (HttpRequestType.GET.value().equals(req.httpRequestType())) {
            mapMap.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            var mapQueue = mapMap.get(req.getSourceName());
                    var nullIfRegistered = mapQueue.putIfAbsent(req.getParam(),
                    new ConcurrentLinkedQueue<>());
            var queue = mapMap.get(req.getSourceName()).get(req.getParam());
            var text = queue.poll();
            if (text != null) {
                result = new Resp(text, StatusCodes.OK_200.code());
            } else if (nullIfRegistered == null) {
                result = new Resp(req.getParam() + " subscribed", StatusCodes.CREATED_201.code());
            } else {
                result = new Resp("", StatusCodes.NO_CONTENT_204.code());
            }
        } else {
            throw new IllegalArgumentException("Unknown request type");
        }
        return result;
    }
}
