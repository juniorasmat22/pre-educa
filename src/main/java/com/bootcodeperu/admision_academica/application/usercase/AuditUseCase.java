package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.AuditMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.audit.AuditResponse;
import com.bootcodeperu.admision_academica.application.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditUseCase implements AuditService {
    private final Javers javers;
    private final AuditMapper auditMapper;

    @Override
    public List<AuditResponse> getHistory(Object id, Class<?> entityClass) {
        // Consultamos los cambios a Javers
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id, entityClass)
                .withChildValueObjects();
        List<Change> changes = javers.findChanges(jqlQuery.build());
        // Mapeamos los cambios técnicos a nuestra respuesta limpia para el front
        return auditMapper.toResponseList(changes);
    }
}