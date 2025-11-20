package com.bootcodeperu.admision_academica.adapter.util;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.PermisoRepository;
import com.bootcodeperu.admision_academica.domain.repository.RolRepository;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
    	/*
        // --- 1. Crear Permiso de Contenido ---
        Permiso createContent = permisoRepository.save(new Permiso("CONTENIDO_CREATE"));
        Permiso readContent = permisoRepository.save(new Permiso("CONTENIDO_READ"));
        
        // --- 2. Crear Roles ---
        
        // Rol Administrador
        Rol adminRole = new Rol();
        adminRole.setNombre("ROLE_ADMIN");
        adminRole.setPermisos(Set.of(createContent, readContent)); // El ADMIN puede crear y leer contenido
        rolRepository.save(adminRole);
        
        // Rol Estudiante (Puede leer contenido y realizar simulacros)
        Rol estudianteRole = new Rol();
        estudianteRole.setNombre("ROLE_ESTUDIANTE");
        estudianteRole.setPermisos(Set.of(readContent));
        rolRepository.save(estudianteRole);

        // --- 3. Crear Usuario Administrador ---
        if (usuarioRepository.findByEmail("admin@unt.edu.pe").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin Inicial");
            admin.setEmail("admin@unt.edu.pe");
            admin.setPasswordHash(passwordEncoder.encode("admin123")); // Cambiar en prod
            admin.setRol(adminRole);
            usuarioRepository.save(admin);
            System.out.println(">>> Usuario Admin Creado: admin@unt.edu.pe / admin123");
        }
        */
    }
}