package cl.fes.apiUsuarios.controller;

import cl.fes.apiUsuarios.dto.LoginRequest;
import cl.fes.apiUsuarios.exceptions.ApiError;
import cl.fes.apiUsuarios.model.Usuario;
import cl.fes.apiUsuarios.repository.UsuarioRepository;
import cl.fes.apiUsuarios.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class LoginController {

	 @Autowired
	    private UsuarioRepository usuarioRepository;

	    @Autowired
	    private JwtUtil jwtUtil;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
	        this.usuarioRepository = usuarioRepository;
	    }

	    public void setJwtUtil(JwtUtil jwtUtil) {
	        this.jwtUtil = jwtUtil;
	    }

	    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
	        this.passwordEncoder = passwordEncoder;
	    }

	    @Operation(
	            summary = "Login para utilizar los servicios de usuario"
	            , description = "<h3>Endpoint encargado de el token de autorizacion para utilizar todos los servicios de usuarios.</h3>")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "200", description = "OK", content = {
	                    @Content(mediaType = "application/json", schema =
	                    @Schema(implementation = LoginRequest.class)) }) })
	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
	        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
		        .orElseThrow(() -> {
	                new ApiError("Usuario no encontrado con el email: " + request.getEmail(), HttpStatus.UNAUTHORIZED);
	                return new RuntimeException("Usuario no encontrado");
	            });

	        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
	            throw new RuntimeException("Credenciales inválidas");
	        }

	        String token = jwtUtil.generaToken(usuario.getEmail());
	        HashMap<String, String> respuesta = new HashMap<>();
	        respuesta.put("token", token);

	        return ResponseEntity.ok(respuesta);
	    }
}