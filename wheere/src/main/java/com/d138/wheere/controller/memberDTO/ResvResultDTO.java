package com.d138.wheere.controller.memberDTO;

import com.d138.wheere.domain.Reservation;
import com.d138.wheere.domain.ReservationState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class ResvResultDTO {
    private String  uId;
    private Long rId;
    @JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "yyyy-MM-dd")
    private LocalDate rTime;

    private String rStart;
    private String rEnd;
    private boolean rIsPaid;
    private String bNumber;
    @Enumerated(EnumType.STRING)
    private ReservationState rState;


    public static  ResvResultDTO createResResult(String uId, boolean isPaid, Reservation resv) {
        return new ResvResultDTO(uId, resv.getId(), resv.getReservationDate(), resv.getStartPoint(), resv.getEndPoint(), isPaid, resv.getBus().getBusNumber(), resv.getReservationState());
    }
}
