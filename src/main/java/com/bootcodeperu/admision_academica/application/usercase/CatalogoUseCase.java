package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.application.controller.dto.catalogo.AreaCatalogoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.catalogo.CarreraCatalogoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.catalogo.CatalogoResponse;
import com.bootcodeperu.admision_academica.application.service.CatalogoService;
import com.bootcodeperu.admision_academica.domain.repository.UniversidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoUseCase implements CatalogoService {

    // Reusamos el repositorio de Universidad que ya tiene la consulta optimizada (JOIN FETCH)
    private final UniversidadRepository universidadRepository;

    @Override
    public List<CatalogoResponse> obtenerCatalogoCompleto() {
        var universidades = universidadRepository.obtenerEstructuraActiva();

        return universidades.stream().map(u -> new CatalogoResponse(
                u.getId(),
                u.getNombre(),
                u.getSiglas(),
                u.getAreas().stream().map(a -> new AreaCatalogoResponse(
                        a.getId(),
                        a.getNombre(),
                        a.getCarreras().stream().map(c -> new CarreraCatalogoResponse(
                                c.getId(),
                                c.getNombre(),
                                c.getPuntajeMinimoHistorico()
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }
}