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

    @GetMapping(value = "/subscribe/{dId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SseEmitter subscribe(@PathVariable String dId,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

            SseEmitter emitter = notificationService.subscribe(dId, lastEventId);

            return emitter;
        }
}