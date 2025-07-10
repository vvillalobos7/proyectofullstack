package com.example.Usuario.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Usuario.Model.Usuario;
import com.example.Usuario.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public Usuario saveUsuario(Usuario nuevo) {
        nuevo.setPassword(passwordEncoder.encode(nuevo.getPassword()));
        return usuarioRepository.save(nuevo);
    }

    @Transactional
    public Usuario updateUsuario(Long id, Usuario usuarioActualizado) {
        Usuario existente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        existente.setNombre(usuarioActualizado.getNombre());
        existente.setApellido(usuarioActualizado.getApellido());
        existente.setCorreo(usuarioActualizado.getCorreo());
        existente.setTelefono(usuarioActualizado.getTelefono());
        existente.setIdperfil(usuarioActualizado.getIdperfil());

        if (!usuarioActualizado.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        return usuarioRepository.save(existente);
    }

    @Transactional
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // Login
    public Usuario login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Correo no registrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }
}