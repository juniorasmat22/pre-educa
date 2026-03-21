package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoResponse;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

import java.util.List;

public interface ObjetivoAcademicoService {
    ObjetivoAcademicoResponse actualizar(Long id, ObjetivoAcademicoRequest request);

    ObjetivoAcademicoResponse obtenerPorId(Long id);

    List<ObjetivoAcademicoResponse> listarTodos();

    /**
     * Establece o actualiza el objetivo académico principal del usuario.
     * Desactiva los objetivos anteriores para mantener el historial.
     * * @param usuario El usuario registrado
     *
     * @param idCarrera         El ID de la carrera a la que postulará
     * @param idProcesoAdmision El ID del proceso de admisión (Opcional, puede ser null)
     */
    void establecerNuevoObjetivo(Usuario usuario, Long idCarrera, Long idProcesoAdmision);
}
