package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.audit.AuditResponse;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AuditMapper {

    @Mapping(target = "usuario", expression = "java(getAuthor(change))")
    @Mapping(target = "fecha", expression = "java(getCommitDate(change))")
    @Mapping(target = "tipoCambio", expression = "java(change.getClass().getSimpleName())")
    @Mapping(target = "propiedad", expression = "java(getPropertyName(change))")
    @Mapping(target = "valorAnterior", expression = "java(getLeftValue(change))")
    @Mapping(target = "valorNuevo", expression = "java(getRightValue(change))")
    public abstract AuditResponse toResponse(Change change);

    public abstract List<AuditResponse> toResponseList(List<Change> changes);

    // Métodos de apoyo para extraer datos de Javers
    protected String getAuthor(Change change) {
        return change.getCommitMetadata()
                .map(cm -> cm.getAuthor())
                .orElse("SYSTEM");
    }

    protected String getCommitDate(Change change) {
        return change.getCommitMetadata()
                .map(cm -> cm.getCommitDate().toString())
                .orElse("");
    }

    protected String getPropertyName(Change change) {
        if (change instanceof ValueChange vc) return vc.getPropertyName();
        return "";
    }

    protected Object getLeftValue(Change change) {
        if (change instanceof ValueChange vc) return vc.getLeft();
        return null;
    }

    protected Object getRightValue(Change change) {
        if (change instanceof ValueChange vc) return vc.getRight();
        return null;
    }
}