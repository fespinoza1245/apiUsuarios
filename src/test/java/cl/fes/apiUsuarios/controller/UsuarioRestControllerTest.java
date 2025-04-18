package cl.fes.apiUsuarios.controller;


import cl.fes.apiUsuarios.dto.UsuarioCreatedDTO;
import cl.fes.apiUsuarios.dto.UsuarioEncontradoDTO;
import cl.fes.apiUsuarios.dto.UsuarioListDTO;
import cl.fes.apiUsuarios.model.Usuario;
import cl.fes.apiUsuarios.services.UsuarioService;
import cl.fes.apiUsuarios.util.Constantes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioRestControllerTest {

    private UsuarioService usuarioService;
    private UsuarioRestController controller;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);
        controller = new UsuarioRestController();
        controller.setUsuarioService(usuarioService);
    }

    @Test
    void listarUsuarios_OK() {
        List<UsuarioListDTO> listaMock = Arrays.asList(new UsuarioListDTO(), new UsuarioListDTO());
        when(usuarioService.buscarTodos()).thenReturn(listaMock);

        ResponseEntity<List<UsuarioListDTO>> response = controller.listarUsuarios();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void buscarUsuarioPorId_OK() {
        Long id = 1L;
        UsuarioEncontradoDTO dtoMock = new UsuarioEncontradoDTO();
        when(usuarioService.buscarPorId(id)).thenReturn(dtoMock);

        ResponseEntity<UsuarioEncontradoDTO> response = controller.buscarUsuarioPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void crearUsuario_OK() {
        Usuario usuario = new Usuario();
        UsuarioCreatedDTO dtoMock = new UsuarioCreatedDTO();

        when(usuarioService.crearUsuario(usuario)).thenReturn(dtoMock);

        ResponseEntity<UsuarioCreatedDTO> response = controller.crearUsuario(usuario);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void actualizarUsuario_OK() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        UsuarioCreatedDTO dtoMock = new UsuarioCreatedDTO();

        when(usuarioService.editarUsuario(usuario, id)).thenReturn(dtoMock);

        ResponseEntity<UsuarioCreatedDTO> response = controller.actualizarUsuario(usuario, id);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void eliminarUsuarioPorId_OK() {
        Long id = 1L;
        doNothing().when(usuarioService).eliminarUsuario(id);

        ResponseEntity<?> response = controller.eliminarUsuarioPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Constantes.MENSAJE_ELIMINAR, response.getBody());
    }
}