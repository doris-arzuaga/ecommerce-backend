package com.arquitectura.ecommerce.dto;

import com.arquitectura.ecommerce.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;

    private Usuario.Rol rol;

    // Método para verificar si es admin (para conveniencia)
    public boolean isAdmin() {
        return rol == Usuario.Rol.ADMIN;
    }
}