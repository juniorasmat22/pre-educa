package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.carrera.CarreraRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.carrera.CarreraResponse;

import java.util.List;

public interface CarreraService {
    List<CarreraResponse> getAllCareers();
    List<CarreraResponse> getCareersByAreaId(Long areaId);
    CarreraResponse getCareerById(Long id);
    CarreraResponse createCareer(CarreraRequest request);
    CarreraResponse updateCareer(Long id, CarreraRequest request);
}
