package cl.fes.apiUsuarios.controller;


import cl.fes.apiUsuarios.dto.LoginRequest;
import cl.fes.apiUsuarios.model.Usuario;
import cl.fes.apiUsuarios.repository.UsuarioRepository;
import cl.fes.apiUsuarios.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private UsuarioRepository usuarioRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private LoginController loginController;

    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        jwtUtil = mock(JwtUtil.class);
        passwordEncoder = mock(PasswordEncoder.class);

        loginController = new LoginController();
        loginController.setUsuarioRepository(usuarioRepository);
        loginController.setJwtUtil(jwtUtil);
        loginController.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void login_UsuarioValido_TokenDevuelto() {
        String email = "usuario@ejemplo.com";
        String password = "miClave123";
        String hashedPassword = "$2a$10$simuladoParaTest"; // hash simulado
        String fakeToken = "jwt.token.simulado";

        Usuario mockUsuario = new Usuario();
        mockUsuario.setEmail(email);
        mockUsuario.setPassword(hashedPassword);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(mockUsuario));
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);
        when(jwtUtil.generaToken(email)).thenReturn(fakeToken);

        ResponseEntity<?> response = loginController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof HashMap);
        assertEquals(fakeToken, ((HashMap<?, ?>) response.getBody()).get("token"));
    }

    @Test
    void login_UsuarioNoExiste_Error() {
        String email = "noexiste@ejemplo.com";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("password");

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            loginController.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void login_PasswordInvalida_Error() {
        String email = "usuario@ejemplo.com";
        String password = "passwordIncorrecta";

        Usuario mockUsuario = new Usuario();
        mockUsuario.setEmail(email);
        mockUsuario.setPassword("$2a$10$hashvalido");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(mockUsuario));
        when(passwordEncoder.matches(password, mockUsuario.getPassword())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            loginController.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Credenciales inválidas"));
    }
}
