package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.ObjetivoAcademicoMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoResponse;
import com.bootcodeperu.admision_academica.application.service.ObjetivoAcademicoService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Carrera;
import com.bootcodeperu.admision_academica.domain.model.ObjetivoAcademico;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.CarreraRepository;
import com.bootcodeperu.admision_academica.domain.repository.ObjetivoAcademicoRepository;
import com.bootcodeperu.admision_academica.domain.repository.ProcesoAdmisionRepository;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjetivoAcademicoUseCase implements ObjetivoAcademicoService {

    private final ObjetivoAcademicoRepository objetivoRepository;
    private final CarreraRepository carreraRepository;
    private final ProcesoAdmisionRepository procesoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjetivoAcademicoMapper mapper;

    @Override
    public List<ObjetivoAcademicoResponse> listarTodos() {
        // Asumiendo que agregaste findAll() en tu ObjetivoAcademicoRepository y su Impl
        return objetivoRepository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public ObjetivoAcademicoResponse obtenerPorId(Long id) {
        // Asumiendo que agregaste findById(id) en tu Repositorio
        return objetivoRepository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Objetivo no encontrado"));
    }

    @Override
    @Transactional
    public ObjetivoAcademicoResponse actualizar(Long id, ObjetivoAcademicoRequest request) {
        ObjetivoAcademico objetivo = objetivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objetivo no encontrado"));

        Usuario usuario = usuarioRepository.findById(request.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Carrera carrera = carreraRepository.findById(request.idCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada"));

        mapper.updateEntity(request, objetivo);
        objetivo.setUsuario(usuario);
        objetivo.setCarrera(carrera);

        if (request.idProcesoAdmision() != null) {
            var proceso = procesoRepository.findById(request.idProcesoAdmision())
                    .orElseThrow(() -> new ResourceNotFoundException("Proceso no encontrado"));
            objetivo.setProcesoAdmision(proceso);
        } else {
            objetivo.setProcesoAdmision(null);
        }

        return mapper.toResponse(objetivoRepository.save(objetivo));
    }

    @Override
    @Transactional
    public void establecerNuevoObjetivo(Usuario usuario, Long idCarrera, Long idProcesoAdmision) {

        // 1. Validar que la carrera exista
        Carrera carrera = carreraRepository.findById(idCarrera)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con ID: " + idCarrera));

        // 2. Buscar si el alumno ya tenía objetivos activos (Historial)
        List<ObjetivoAcademico> objetivosAnteriores = objetivoRepository.findByUsuarioIdAndActivoTrue(usuario.getId());

        // 3. Desactivar los anteriores para mantener el historial limpio
        if (!objetivosAnteriores.isEmpty()) {
            for (ObjetivoAcademico obj : objetivosAnteriores) {
                obj.setActivo(false);
                obj.setObjetivoPrincipal(false);
            }
            objetivoRepository.saveAll(objetivosAnteriores);
        }

        // 4. Crear el nuevo objetivo
        ObjetivoAcademico nuevoObjetivo = new ObjetivoAcademico();
        nuevoObjetivo.setUsuario(usuario);
        nuevoObjetivo.setCarrera(carrera);
        nuevoObjetivo.setObjetivoPrincipal(true);
        nuevoObjetivo.setActivo(true);

        // 5. Si mandó un proceso de admisión lo enlazamos
        if (idProcesoAdmision != null) {
            var proceso = procesoRepository.findById(idProcesoAdmision)
                    .orElseThrow(() -> new ResourceNotFoundException("Proceso de Admisión no encontrado"));
            nuevoObjetivo.setProcesoAdmision(proceso);
            nuevoObjetivo.setPostulaProximoExamen(true);
        } else {
            nuevoObjetivo.setPostulaProximoExamen(false);
        }

        // 6. Guardar en BD
        objetivoRepository.save(nuevoObjetivo);
    }
}
