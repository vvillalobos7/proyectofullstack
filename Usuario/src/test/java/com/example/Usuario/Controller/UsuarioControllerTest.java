package com.example.Usuario.Controller;

import com.example.Usuario.Model.Usuario;
import com.example.Usuario.Service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "Juan", "juan@mail.com", "1234", null, null, null);
    }

    @Test
    void testObtenerUsuarios_conDatos() throws Exception {
        when(usuarioService.getUsuarios()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testObtenerUsuarios_sinDatos() throws Exception {
        when(usuarioService.getUsuarios()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/usuario"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerUsuarioPorId_existente() throws Exception {
        when(usuarioService.getUsuarioPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testObtenerUsuarioPorId_noExiste() throws Exception {
        when(usuarioService.getUsuarioPorId(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/usuario/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearUsuario() throws Exception {
        Usuario nuevo = new Usuario(null, "Pedro", "pedro@mail.com", "abcd", null, null, null);
        Usuario creado = new Usuario(2L, "Pedro", "pedro@mail.com", "abcd", null, null, null);

        when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(creado);

        mockMvc.perform(post("/api/v1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idusuario").value(2))
                .andExpect(jsonPath("$.nombre").value("Pedro"));
    }

    @Test
    void testActualizarUsuario_existente() throws Exception {
        Usuario actualizado = new Usuario(1L, "Juan Modificado", "juan@mod.com", "xyz", null, null, null);

        when(usuarioService.updateUsuario(eq(1L), any(Usuario.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Modificado"));
    }

    @Test
    void testActualizarUsuario_noExiste() throws Exception {
        when(usuarioService.updateUsuario(eq(1L), any(Usuario.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/v1/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarUsuario_existente() throws Exception {
        doNothing().when(usuarioService).deleteUsuario(1L);

        mockMvc.perform(delete("/api/v1/usuario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarUsuario_noExiste() throws Exception {
        doThrow(new RuntimeException()).when(usuarioService).deleteUsuario(1L);

        mockMvc.perform(delete("/api/v1/usuario/1"))
                .andExpect(status().isNotFound());
    }
}