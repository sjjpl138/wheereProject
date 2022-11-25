package com.d138.wheere.repository.SSE;

import com.d138.wheere.service.SSE.NotificationService.Notification;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
@Log4j2
public class EmitterRepository {

    private Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        log.info(emitters);
        return sseEmitter;
    }

    public void deleteById(String emitterId) {
        if (!emitters.isEmpty()) {
            emitters.remove(emitterId);
        }
    }

    public Map<String, SseEmitter> findAllStartWithById (String emitterId) {
        return emitters.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(emitterId))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue)
                );
    }

    public void saveEventCache(String key, Object event) {
        eventCache.put(key,  event);
    }

    public Map<String, Object> findAllEventCacheWithId(String emitterId) {
        return emitters.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(emitterId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteAllEmitterWithId(String emitterId) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(emitterId)){
                emitters.remove(key);
            }
        });
    }

    public void deleteAllEventCacheStartWithId(String emitterId) {
        eventCache.forEach((key, emitter) -> {
            if (key.startsWith(emitterId)){
                emitters.remove(key);
            }
        });
    }

}
