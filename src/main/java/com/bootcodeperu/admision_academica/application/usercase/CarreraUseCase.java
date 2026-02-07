package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.CarreraMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.carrera.CarreraRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.carrera.CarreraResponse;
import com.bootcodeperu.admision_academica.application.service.CarreraService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.model.Carrera;
import com.bootcodeperu.admision_academica.domain.repository.AreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarreraUseCase implements CarreraService {
    private final CarreraRepository carreraRepository;
    private final AreaRepository areaRepository;
    private final CarreraMapper carreraMapper;
    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponse> getAllCareers() {
        return carreraRepository.findAll().stream()
                .map(carreraMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponse> getCareersByAreaId(Long areaId) {
        return carreraRepository.findByAreaId(areaId).stream()
                .map(carreraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponse getCareerById(Long id) {
        return carreraRepository.findById(id)
                .map(carreraMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no econtrada con ID: " + id));
    }

    @Override
    @Transactional
    public CarreraResponse createCareer(CarreraRequest request) {
        if (carreraRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("El nombre de la carrera ya existe: " + request.nombre());
        }

        Area area = areaRepository.findById(request.areaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + request.areaId()));

        Carrera carrera = new Carrera();
        carrera.setNombre(request.nombre());
        carrera.setArea(area);

        return carreraMapper.toResponse(carreraRepository.save(carrera));
    }

    @Override
    @Transactional
    public CarreraResponse updateCareer(Long id, CarreraRequest request) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no econtrada con ID: " + id));

        Area area = areaRepository.findById(request.areaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + request.areaId()));

        carrera.setNombre(request.nombre());
        carrera.setArea(area);

        return carreraMapper.toResponse(carreraRepository.save(carrera));
    }
}
