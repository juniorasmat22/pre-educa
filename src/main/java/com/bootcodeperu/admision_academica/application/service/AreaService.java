package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;

import java.util.List;

public interface AreaService {
    List<AreaResponse> getAllAreas();
    AreaResponse findAreaById(Long id);
    AreaResponse createArea(AreaRequest request);
    AreaResponse updateArea(Long id, AreaRequest request);
}