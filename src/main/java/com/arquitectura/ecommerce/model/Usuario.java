package com.arquitectura.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"ordenes"})
@EqualsAndHashCode(exclude = {"ordenes"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String password;

    private String direccion;

    private String telefono;

    // Agregamos un campo para el rol
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.USUARIO; // Valor por defecto

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Orden> ordenes = new HashSet<>();

    // Enum para los roles
    public enum Rol {
        ADMIN, USUARIO
    }

    // Métodos helper para mantener la consistencia bidireccional
    public void agregarOrden(Orden orden) {
        ordenes.add(orden);
        orden.setUsuario(this);
    }

    public void eliminarOrden(Orden orden) {
        ordenes.remove(orden);
        orden.setUsuario(null);
    }
}