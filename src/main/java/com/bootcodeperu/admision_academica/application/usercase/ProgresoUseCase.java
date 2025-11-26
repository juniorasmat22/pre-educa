package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.controller.dto.progresotema.ProgresoTemaResponse;
import com.bootcodeperu.admision_academica.application.service.ProgresoService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.ProgresoTemaRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgresoUseCase implements ProgresoService {

	private final ProgresoTemaRepository progresoTemaRepository;
	private final UsuarioRepository usuarioRepository;
	private final TemaRepository temaRepository;
	private final ModelMapper modelMapper;

	@Override
	@Transactional
	public ProgresoTemaResponse actualizarPuntajePromedio(Long usuarioId, Long temaId, Double nuevoPuntaje) {

		// 1. Verificar existencia
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));
		Tema tema = temaRepository.findById(temaId)
				.orElseThrow(() -> new ResourceNotFoundException("Tema", "id", temaId));

		// 2. Obtener o crear registro de progreso
		ProgresoTema progreso = progresoTemaRepository.findByUsuarioIdAndTemaId(usuarioId, temaId)
				.orElseGet(() -> {
					ProgresoTema nuevoProgreso = new ProgresoTema();
					nuevoProgreso.setUsuario(usuario);
					nuevoProgreso.setTema(tema);
					return nuevoProgreso;
				});

		// 3. Recalcular el promedio
		progreso.setNumeroIntentos(progreso.getNumeroIntentos() + 1);
		progreso.setSumaPuntajes(progreso.getSumaPuntajes() + nuevoPuntaje);

		// Fórmula del nuevo promedio: Suma Total / Número de Intentos
		double nuevoPromedio = progreso.getSumaPuntajes() / progreso.getNumeroIntentos();
		progreso.setPuntajePromedio(nuevoPromedio);

		// 4. Guardar
		return modelMapper.map(progreso, ProgresoTemaResponse.class);
	}

	@Override
	public List<ProgresoTemaResponse> getProgresoByUsuario(Long usuarioId) {
		List<ProgresoTema> progresoList = progresoTemaRepository.findAllByUsuarioId(usuarioId);

		// Mapear la lista de Entidades a una lista de DTOs usando streams y ModelMapper
		return progresoList.stream()
				.map(progreso -> modelMapper.map(progreso, ProgresoTemaResponse.class))
				.collect(Collectors.toList());
	}
}
