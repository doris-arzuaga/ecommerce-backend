package com.arquitectura.ecommerce.service;

import com.arquitectura.ecommerce.dto.LoginRequest;
import com.arquitectura.ecommerce.dto.RegistroRequest;
import com.arquitectura.ecommerce.dto.UsuarioDTO;
import com.arquitectura.ecommerce.model.Usuario;

public interface UsuarioService {

    UsuarioDTO registrarUsuario(RegistroRequest registroRequest);

    UsuarioDTO login(LoginRequest loginRequest);

    UsuarioDTO obtenerUsuarioPorId(Long id);

    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);

    void eliminarUsuario(Long id);

    Usuario buscarPorEmail(String email);

    Usuario buscarPorId(Long id);
}
