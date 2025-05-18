package com.arquitectura.ecommerce.controller;

import com.arquitectura.ecommerce.dto.LoginRequest;
import com.arquitectura.ecommerce.dto.RegistroRequest;
import com.arquitectura.ecommerce.dto.UsuarioDTO;
import com.arquitectura.ecommerce.model.Usuario;
import com.arquitectura.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@Valid @RequestBody RegistroRequest registroRequest) {
        return new ResponseEntity<>(usuarioService.registrarUsuario(registroRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(usuarioService.login(loginRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuarioDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rol/{id}")
    public ResponseEntity<Map<String, Boolean>> verificarRolAdmin(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAdmin", usuario.getRol() == Usuario.Rol.ADMIN);
        return ResponseEntity.ok(response);
    }
}