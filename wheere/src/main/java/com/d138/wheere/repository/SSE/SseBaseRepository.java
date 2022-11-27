package com.d138.wheere.repository.SSE;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface SseBaseRepository {
    Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    Map<String, Object> eventCache = new ConcurrentHashMap<>();

    SseEmitter save(String id, SseEmitter sseEmitter);
    void deleteById(String emitterId);
    Map<String, SseEmitter> findAllStartWithById (String emitterId);
    void saveEventCache(String key, Object event);
    Map<String, Object> findAllEventCacheWithId(String emitterId);
    void deleteAllEmitterWithId(String emitterId);
    void deleteAllEventCacheStartWithId(String emitterId);

}
