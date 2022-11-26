package com.d138.wheere.controller.SSE;


import com.d138.wheere.service.SSE.NotificationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * SSE 구독
     */

    @GetMapping(value = "/subscribe/{dId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeForResv(@PathVariable String dId,
                                       @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        SseEmitter emitter = notificationService.subscribe(dId, lastEventId);
        System.out.println("==================================");
        System.out.println("SSE_dId = " + dId);
        System.out.println("==================================");


        return emitter;
    }

    @GetMapping(value = "/subscribe/user/{uId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeForRating(@PathVariable String uId,
                                         @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        SseEmitter sseEmitter = notificationService.subscribeRating(uId, lastEventId);
        System.out.println("==================================");
        System.out.println("SSE_uId = " + uId);
        System.out.println("==================================");

        return sseEmitter;
    }
}