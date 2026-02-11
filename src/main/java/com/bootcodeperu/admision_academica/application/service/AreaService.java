package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.page.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService {
    PageResponse<AreaResponse> getAreasPaged(String search, boolean includeInactive, Pageable pageable);

    List<AreaResponse> getAllAreas();

    AreaResponse findAreaById(Long id);

    AreaResponse createArea(AreaRequest request);

    AreaResponse updateArea(Long id, AreaRequest request);

    void deleteArea(Long id); // Borrado lógico
}