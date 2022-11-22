package com.d138.wheere.repository.bus.query;

import com.d138.wheere.domain.BusState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class BusNumDirDTO {

    private String bNumber;
    private BusState bDir;
}
