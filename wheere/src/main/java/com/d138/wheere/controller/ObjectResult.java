package com.d138.wheere.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ObjectResult<T> {
    private T response;
}