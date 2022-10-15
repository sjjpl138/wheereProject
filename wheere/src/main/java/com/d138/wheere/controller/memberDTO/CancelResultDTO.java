package com.d138.wheere.controller.memberDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelResultDTO {
    private Long uid;
    private Long rid;
    private boolean isCancelled;

    public CancelResultDTO(Long uid, Long rid, boolean isCancelled) {
        this.uid = uid;
        this.rid = rid;
        this.isCancelled = isCancelled;
    }
}
