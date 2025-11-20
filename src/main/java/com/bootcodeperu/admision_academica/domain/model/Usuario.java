package com.bootcodeperu.admision_academica.domain.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
public class Usuario implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash; // Almacenamos el hash de la contraseña

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areaPostulacion")
    private Area areaPostulacion;

    private String carreraDeseada;

    private LocalDateTime fechaRegistro = LocalDateTime.now();
    @Column(nullable = false)
    private boolean isAccountNonExpired = true;

    @Column(nullable = false)
    private boolean isAccountNonLocked = true;

    @Column(nullable = false)
    private boolean isCredentialsNonExpired = true;

    @Column(nullable = false)
    private boolean isEnabled = true;
 // RELACIÓN CON ROLES (NUEVA RELACIÓN)
    // Usamos EAGER para la relación de roles para que Spring Security la cargue automáticamente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRol")
    private Rol rol; 

    // ----------------------------------------------------------------
    // MÉTODOS UserDetails (IMPLEMENTACIÓN ACTUALIZADA)
    // ----------------------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve los permisos asociados al rol del usuario
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (this.rol != null) {
            // 1. Agregar el rol mismo (Ej: ROLE_ESTUDIANTE)
            authorities.add(new SimpleGrantedAuthority(this.rol.getNombre()));
            
            // 2. Agregar los permisos específicos del rol
            this.rol.getPermisos().forEach(permiso -> 
                authorities.add(new SimpleGrantedAuthority(permiso.getNombre()))
            );
        }
        return authorities;
    }
 // ... otros métodos UserDetails usan los nuevos campos persistentes

    @Override public String getPassword() { return this.passwordHash; }

    @Override public String getUsername() { return this.email; }

    @Override public boolean isAccountNonExpired() { return this.isAccountNonExpired; }

    @Override public boolean isAccountNonLocked() { return this.isAccountNonLocked; }

    @Override public boolean isCredentialsNonExpired() { return this.isCredentialsNonExpired; }

    @Override public boolean isEnabled() { return this.isEnabled; }
   
}
