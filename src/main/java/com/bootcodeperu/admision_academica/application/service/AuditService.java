package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.audit.AuditResponse;

import java.util.List;

public interface AuditService {
    List<AuditResponse> getHistory(Object id, Class<?> entityClass);
}
