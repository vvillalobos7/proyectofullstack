package com.example.Usuario.Service;

import com.example.Usuario.Model.Usuario;
import com.example.Usuario.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioEjemplo = new Usuario(1L, "Juan", "Pérez", "juan@mail.com", "1234", "999999999", 2L);
    }

    @Test
    void testGetUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        List<Usuario> usuarios = usuarioService.getUsuarios();

        assertEquals(1, usuarios.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testGetUsuarioPorId_Existe() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        Usuario result = usuarioService.getUsuarioPorId(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void testGetUsuarioPorId_NoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.getUsuarioPorId(1L);
        });

        assertEquals("Usuario no encontrado con ID: 1", ex.getMessage());
    }

    @Test
    void testSaveUsuario() {
        String passwordCodificada = "hashedPassword";
        when(passwordEncoder.encode("1234")).thenReturn(passwordCodificada);

        Usuario usuarioParaGuardar = new Usuario(null, "Juan", "Pérez", "juan@mail.com", "1234", "999999999", 2L);
        Usuario usuarioGuardado = new Usuario(1L, "Juan", "Pérez", "juan@mail.com", passwordCodificada, "999999999", 2L);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        Usuario result = usuarioService.saveUsuario(usuarioParaGuardar);

        assertEquals(passwordCodificada, result.getPassword());
        verify(passwordEncoder).encode("1234");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_Existe() {
        Usuario actualizado = new Usuario(null, "Pedro", "Soto", "pedro@mail.com", "abcd", "111111111", 3L);
        Usuario existente = new Usuario(1L, "Juan", "Pérez", "juan@mail.com", "1234", "999999999", 2L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("abcd")).thenReturn("encodedPwd");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(new Usuario(1L, "Pedro", "Soto", "pedro@mail.com", "encodedPwd", "111111111", 3L));

        Usuario result = usuarioService.updateUsuario(1L, actualizado);

        assertEquals("Pedro", result.getNombre());
        assertEquals("encodedPwd", result.getPassword());
        assertEquals("111111111", result.getTelefono());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_NoExiste() {
        Usuario actualizado = new Usuario(null, "Pedro", "Soto", "pedro@mail.com", "abcd", "111111111", 3L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.updateUsuario(1L, actualizado);
        });

        assertTrue(ex.getMessage().contains("Usuario no encontrado con ID: 1"));
    }

    @Test
    void testDeleteUsuario_Existe() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteUsuario(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void testDeleteUsuario_NoExiste() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.deleteUsuario(1L);
        });

        assertTrue(ex.getMessage().contains("Usuario no encontrado con ID: 1"));
    }

    @Test
    void testLogin_Exitoso() {
        when(usuarioRepository.findByCorreo("juan@mail.com")).thenReturn(Optional.of(usuarioEjemplo));
        when(passwordEncoder.matches("1234", "1234")).thenReturn(true);

        Usuario result = usuarioService.login("juan@mail.com", "1234");

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(passwordEncoder).matches("1234", "1234");
    }

    @Test
    void testLogin_CorreoNoExiste() {
        when(usuarioRepository.findByCorreo("desconocido@mail.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.login("desconocido@mail.com", "1234");
        });

        assertEquals("Correo no registrado", ex.getMessage());
    }

    @Test
    void testLogin_ContraseñaIncorrecta() {
        when(usuarioRepository.findByCorreo("juan@mail.com")).thenReturn(Optional.of(usuarioEjemplo));
        when(passwordEncoder.matches("wrongpass", "1234")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.login("juan@mail.com", "wrongpass");
        });

        assertEquals("Contraseña incorrecta", ex.getMessage());
    }
}