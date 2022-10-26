package com.d138.wheere.service;

import com.d138.wheere.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
}
