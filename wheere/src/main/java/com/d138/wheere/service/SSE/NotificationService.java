package com.d138.wheere.service.SSE;

import com.d138.wheere.repository.SSE.EmitterRepository;
import com.d138.wheere.repository.SSE.SseRatingRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@Log4j2
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository; // 버스 기사 - 서버 SSE
    private final SseRatingRepository sseRatingRepository; // 일반 사용자 - 서버 SSE

    //버스 기사 로그인 시  구독 요청
    public SseEmitter subscribe(String dId, String lastEventId) {
        // 1
        String id = dId + "_" + System.currentTimeMillis();

        // 2
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));
        emitter.onError((e) -> emitterRepository.deleteById(id));

        // 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventName = "503 에러 방지용 이벤트";
        EventDTO eventDTO = new EventDTO("EventStream Created.", dId);
        sendToClient(emitter, id, eventDTO, eventName, 1);

        // 4
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheWithId(String.valueOf(dId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue(), "미수신한 이벤트 목록", 1));
        }

        return emitter;
    }

    //일반 사용자에서 서버로 sse 구독 요청
    public SseEmitter subscribeRating(String uId, String lastEventId) {
        // 1
        String id = uId + "_" + System.currentTimeMillis();

        // 2
        SseEmitter emitter = sseRatingRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> sseRatingRepository.deleteById(id));
        emitter.onTimeout(() -> sseRatingRepository.deleteById(id));
        emitter.onError((e) -> sseRatingRepository.deleteById(id));

        // 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventName = "503 에러 방지용 이벤트";
        RatingSSESendDTO ratingSSESendDTO = new RatingSSESendDTO("EventStream Created.", uId);
        sendToClient(emitter, id, ratingSSESendDTO, eventName, 2);

        // 4
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = sseRatingRepository.findAllEventCacheWithId(String.valueOf(uId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue(), "미수신한 이벤트 목록", 2));
        }

        return emitter;
    }

    // 3
    private void sendToClient(SseEmitter emitter, String key, Object data, String name, int option) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(key)
                        .name(name)
                        .data(data, MediaType.APPLICATION_JSON)
                        .reconnectTime(0));

            } catch (Exception e) {
                if (option == 1)
                    emitterRepository.deleteById(key);
                else sseRatingRepository.deleteById(key);

                System.out.println(e.getMessage());
//                throw new RuntimeException("연결 오류!");
            }
        });
    }

    //  사용자 버스 예약 완료 후 사용자에게 버스 평점 요청
    public void sendByUserForRating(String uId , Long rId) {

        EventRatingDTO eventRatingDTO = createEventRatingDTO(rId);
        Map<String, SseEmitter> sseEmitters = sseRatingRepository.findAllStartWithById(uId);

        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    sseRatingRepository.saveEventCache(key, eventRatingDTO);
                    // 데이터 전송
                    sendToClient(emitter, key, eventRatingDTO,  "평점 요청", 2);
                    log.info("[resvId: " + rId + "]: " + eventRatingDTO);
                }
        );

    }

    // 사용자 예약 이벤트 발생시 해당 버스 기사에게 데이터 전송
    public void send(String  dId, Long rId, String startPoint, String  endPoint, LocalDate rTime ) {
        Notification notification = createNotification(rId, startPoint, endPoint, rTime);

        //버스 기사에 대한 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(dId);

        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, notification,  "사용자에 의한 예약 발생", 1);
                    log.info("[driverId: " + dId + "]: " + notification);
                }
        );
    }

    private Notification createNotification(Long rId, String startPoint, String endPoint, LocalDate rTime) {
        return Notification.builder()
                .rId(rId)
                .startPoint(startPoint)
                .endPoint(endPoint)
                .rTime(rTime)
                .build();
    }

    private EventRatingDTO createEventRatingDTO(Long rId) {
        return EventRatingDTO.builder()
                .rId(rId)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    static  class EventRatingDTO {
        private Long rId;
    }

    @Data
    @AllArgsConstructor
    static class EventDTO {
        private String dId;
        private String comment;
    }

    @Data
    @AllArgsConstructor
    static class RatingSSESendDTO {
        private String uId;
        private String comment;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Notification {
        private Long rId;
        private String startPoint;
        private String endPoint;
        private LocalDate rTime;
    }

}