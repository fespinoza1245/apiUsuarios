package cl.fes.apiUsuarios.controller;

import cl.fes.apiUsuarios.dto.UsuarioCreatedDTO;
import cl.fes.apiUsuarios.dto.UsuarioEncontradoDTO;
import cl.fes.apiUsuarios.dto.UsuarioListDTO;
import cl.fes.apiUsuarios.exceptions.ApiError;
import cl.fes.apiUsuarios.model.Usuario;
import cl.fes.apiUsuarios.services.UsuarioService;
import cl.fes.apiUsuarios.util.Constantes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Listar todos los usuarios"
            , description = "<h3>Endpoint encargado de listar todos los usuarios desde la base de datos</h3>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UsuarioListDTO.class)) }) })
    @GetMapping(value = "listar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioListDTO>> listarUsuarios() throws ApiError {
        List<UsuarioListDTO> listaUsuarios = new ArrayList<>();
        try {
            listaUsuarios = usuarioService.buscarTodos();
            return new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
        }catch (RuntimeException ex){
            throw new ApiError("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Buscar usuario por Id"
            , description = "<h3>Endpoint encargado de buscar usuario pasando la Id en path del endpoint</h3>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UsuarioEncontradoDTO.class)) }) })
    @GetMapping(value = "buscar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioEncontradoDTO> buscarUsuarioPorId(@PathVariable Long id) throws ApiError {
        UsuarioEncontradoDTO usuarioEncontradoDTO = new UsuarioEncontradoDTO();
        try {
            usuarioEncontradoDTO = usuarioService.buscarPorId(id);
            return new ResponseEntity<>(usuarioEncontradoDTO, HttpStatus.OK);
        }catch (RuntimeException ex){
            throw new ApiError("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Crear un usuario en la BD"
            , description = "<h3>Endpoint encargado de crear usuario</h3>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UsuarioCreatedDTO.class)) }) })
    @PostMapping(value = "crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioCreatedDTO> crearUsuario(@RequestBody Usuario u) throws ApiError{
        UsuarioCreatedDTO nuevoUsuario = new UsuarioCreatedDTO();
        try{
            nuevoUsuario = usuarioService.crearUsuario(u);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            throw new ApiError("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Actualizar un usuario en la BD"
            , description = "<h3>Endpoint encargado de actualizar usuario pasando el usuario y el Id en el path del endpoint</h3>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UsuarioCreatedDTO.class)) }) })
    @PutMapping(value = "actualizar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioCreatedDTO> actualizarUsuario(@RequestBody Usuario u, @PathVariable Long id) throws ApiError{
        UsuarioCreatedDTO usuarioModificado = new UsuarioCreatedDTO();
        try{
            usuarioModificado = usuarioService.editarUsuario(u, id);
            return new ResponseEntity<>(usuarioModificado, HttpStatus.OK);
        } catch (RuntimeException ex) {
            throw new ApiError("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Eliminar un usuario de la BD por su Id"
            , description = "<h3>Endpoint encargado de eliminar un usuario pasando rl Id en el path del endpoint</h3>")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json") }) 
    @DeleteMapping(value = "eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> eliminarUsuarioPorId(@PathVariable Long id) throws ApiError {

        try{
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<String>(Constantes.MENSAJE_ELIMINAR,HttpStatus.OK);
        } catch (RuntimeException ex) {
            throw new ApiError("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

}
