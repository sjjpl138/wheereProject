package com.d138.wheere.repository;

import com.d138.wheere.domain.BusState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class BusNumDirDTO {

    private String busNum;
    private BusState dir;

    public BusNumDirDTO(String busNum, BusState dir) {
        this.busNum = busNum;
        this.dir = dir;
    }
}
