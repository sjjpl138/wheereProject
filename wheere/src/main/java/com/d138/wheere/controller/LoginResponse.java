package com.d138.wheere.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse<K, T>  {
    private K member;
    private T busList;
}
