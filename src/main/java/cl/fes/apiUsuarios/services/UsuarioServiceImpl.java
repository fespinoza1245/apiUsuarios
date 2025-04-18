package cl.fes.apiUsuarios.services;

import cl.fes.apiUsuarios.dto.UsuarioCreatedDTO;
import cl.fes.apiUsuarios.dto.UsuarioEncontradoDTO;
import cl.fes.apiUsuarios.dto.UsuarioListDTO;
import cl.fes.apiUsuarios.exceptions.ApiError;
import cl.fes.apiUsuarios.model.Usuario;
import cl.fes.apiUsuarios.repository.UsuarioRepository;
import cl.fes.apiUsuarios.util.Utilidades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${parametros.regexEmail}")
    private String regexEmail;

    @Value("${parametros.regexPassword}")
    private String regexPassword;

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Override
    public List<UsuarioListDTO> buscarTodos() throws ApiError {

        log.info("[INI][buscarTodos]");
        List<Usuario> usuarios = new ArrayList<>();
        try {
            if (!usuarioRepository.findAll().isEmpty()) {
                usuarios = usuarioRepository.findAll();
            } else {
                log.error("[ERR][buscarTodos] No hay usuarios registrados");
                throw new ApiError("La lista de usuarios esta vacia", HttpStatus.NOT_FOUND);
            }

            List<UsuarioListDTO> usuariosListDTO = new ArrayList<>();
            for (Usuario usuario : usuarios) {
                UsuarioListDTO usuarioListDTO = new UsuarioListDTO();
                usuarioListDTO.setId(usuario.getId().toString());
                usuarioListDTO.setNombre(usuario.getName());
                usuarioListDTO.setEmail(usuario.getEmail());
                usuarioListDTO.setPhones(Utilidades.concatenaTelefono(usuario.getPhones()));
                usuariosListDTO.add(usuarioListDTO);
            }
            log.info("[FIN_OK][buscarTodos] Usuarios encontrados: " + usuariosListDTO);
            return usuariosListDTO;
        }catch (Exception ex){
            log.error("[ERR][buscarTodos] Ocurrio un error al hacer la solicitud de usuarios" + ex.getMessage());
            throw new ApiError("No se logro traer la lista de usuarios", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public UsuarioEncontradoDTO buscarPorId(Long id) throws ApiError {
        log.info("[INI][buscarPorId] Id de usuario buscado: " + id);
        UsuarioEncontradoDTO usuarioEncontradoDTO = new UsuarioEncontradoDTO();
        Usuario usuario = new Usuario();
        try {
            if (!usuarioRepository.findById(id).isPresent()) {
                log.error("[ERR][buscarPorId] No se encontro el usuario buscado con ID: " + id);
                throw new ApiError("El usuario no fue encontrado", HttpStatus.NOT_FOUND);
            } else {
                usuario = usuarioRepository.findById(id).orElseThrow();
                usuarioEncontradoDTO.setId(usuario.getId().toString());
                usuarioEncontradoDTO.setNombre(usuario.getName());
                usuarioEncontradoDTO.setEmail(usuario.getEmail());
                usuarioEncontradoDTO.setCreated(usuario.getCreated().toString());
                if (usuario.isActive()) {
                    usuarioEncontradoDTO.setEstado("activo");
                } else {
                    usuarioEncontradoDTO.setEstado("desactivado");
                }
            }
            log.info("[FIN_OK][buscarPorId] Usuario encontrado: " + usuarioEncontradoDTO);
            return usuarioEncontradoDTO;
        }catch(Exception ex){
            log.error("[ERR][buscarPorId] Ocurrio un error al hacer la solicitud de usuario" + ex.getMessage());
            throw new ApiError("No se encontro el usuario buscado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UsuarioCreatedDTO crearUsuario(Usuario u) throws ApiError {
        log.info("[INI][crearUsuario] Usuario a crear: " + u);
        UsuarioCreatedDTO usuarioCreatedDTO = new UsuarioCreatedDTO();
        LocalDateTime now = LocalDateTime.now();
        try {
            if (usuarioRepository.findByEmail(u.getEmail()).isPresent()) {
                log.error("[ERR][crearUsuario] El correo ya está registrado: " + u.getEmail());
                throw new ApiError("El correo ya está registrado", HttpStatus.BAD_REQUEST);
            }
            if (!u.getEmail().matches(regexEmail)) {
                log.error("[ERR][crearUsuario] El correo no tiene el formato correcto : " + u.getEmail());
                throw new ApiError("El correo no tiene el formato correcto", HttpStatus.BAD_REQUEST);
            }
            if (!u.getPassword().matches(regexPassword)) {
                log.error("[ERR][crearUsuario] El password debe tener al menos 1 Mayuscula, 1 numero y 8 digitos minimo : " + u.getPassword());
                throw new ApiError("El password debe tener al menos 1 Mayuscula, 1 numero y 8 digitos minimo", HttpStatus.BAD_REQUEST);
            }
            //Seteamos los parametros que se pasan en el servicio
            u.setCreated(now);
            u.setModified(now);
            u.setLast_login(now);
            u.setToken(UUID.randomUUID().toString());
            u.setActive(true);
            //Guardamos el usuario
            usuarioRepository.save(u);

            //Llenamos el DTO que mostramos en la salida de este metodo
            usuarioCreatedDTO.setId(u.getId());
            usuarioCreatedDTO.setName(u.getName());
            usuarioCreatedDTO.setEmail(u.getEmail());
            usuarioCreatedDTO.setPhones(Utilidades.concatenaTelefono(u.getPhones()));
            usuarioCreatedDTO.setCreated(u.getCreated());
            usuarioCreatedDTO.setModified(u.getModified());
            usuarioCreatedDTO.setLast_login(u.getLast_login());
            usuarioCreatedDTO.setToken(u.getToken());
            usuarioCreatedDTO.setActive(u.isActive());

            log.info("[FIN_OK][crearUsuario] Usuario creado: " + usuarioCreatedDTO);
            return usuarioCreatedDTO;
        }catch (Exception ex){
            log.error("[ERR][crearUsuario] Ocurrio un error al hacer la solicitud de crear usuario" + ex.getMessage());
            throw new ApiError("No se pudo crear el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public UsuarioCreatedDTO editarUsuario(Usuario u , Long id) throws ApiError {
        log.info("[INI][editarUsuario] Usuario a editar: " + u + " id: " + id);
        UsuarioCreatedDTO usuarioCreatedDTO = new UsuarioCreatedDTO();
        LocalDateTime now = LocalDateTime.now();
        try {
                Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow();

                if(usuarioEncontrado == null){
                    log.error("[ERR][editarUsuario] El usuario no existe o id no encontrado : " + u.getEmail());
                    throw new ApiError("El usuario no existe o id no encontrado ", HttpStatus.NOT_FOUND);
                }
                if (!u.getEmail().matches(regexEmail)) {
                    log.error("[ERR][editarUsuario] El correo no tiene el formato correcto : " + u.getEmail());
                    throw new ApiError("El correo no tiene el formato correcto", HttpStatus.BAD_REQUEST);
                }
                if (!u.getPassword().matches(regexPassword)) {
                    log.error("[ERR][editarUsuario] El password debe tener al menos 1 Mayuscula, 1 numero y 8 digitos minimo : " + u.getPassword());
                    throw new ApiError("El password debe tener al menos 1 Mayuscula, 1 numero y 8 digitos minimo", HttpStatus.BAD_REQUEST);
                }
                //Actualizamos los parametros que se pasan en el servicio
                usuarioEncontrado.setId(id);
                usuarioEncontrado.setName(u.getName());
                usuarioEncontrado.setEmail(u.getEmail());
                usuarioEncontrado.setPassword(u.getPassword());
                usuarioEncontrado.setPhones(u.getPhones());
                usuarioEncontrado.setModified(now);
                usuarioEncontrado.isActive();
                //Guardamos el usuario
                usuarioRepository.save(usuarioEncontrado);
                //Llenamos el DTO que mostramos en la salida de este metodo
                usuarioCreatedDTO.setId(id);
                usuarioCreatedDTO.setName(usuarioEncontrado.getName());
                usuarioCreatedDTO.setEmail(usuarioEncontrado.getEmail());
                usuarioCreatedDTO.setPhones(Utilidades.concatenaTelefono(usuarioEncontrado.getPhones()));
                usuarioCreatedDTO.setCreated(usuarioEncontrado.getCreated());
                usuarioCreatedDTO.setModified(usuarioEncontrado.getModified());
                usuarioCreatedDTO.setLast_login(usuarioEncontrado.getLast_login());
                usuarioCreatedDTO.setToken(usuarioEncontrado.getToken());
                usuarioCreatedDTO.setActive(usuarioEncontrado.isActive());

            log.info("[FIN_OK][editarUsuario] Usuario editado: " + usuarioCreatedDTO);
            return usuarioCreatedDTO;
        }catch (Exception ex){
            log.error("[ERR][editarUsuario] Ocurrio un error al hacer la solicitud de actualizar usuario" + ex.getMessage());
            throw new ApiError("No se pudo actualizar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void eliminarUsuario(Long id) throws ApiError {
        log.info("[INI][eliminarUsuario] Usuario a eliminar: " + id);
        try {
            if (usuarioRepository.findById(id).isPresent()) {
                usuarioRepository.deleteById(id);
                log.info("[FIN_OK][eliminarUsuario] Usuario eliminado Ok con id: " + id);
            } else {
                log.error("[ERR][eliminarUsuario] No se encontro el usuario buscado con ID: " + id);
                throw new ApiError("No se encontro el Id para eliminar el Usuario", HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex){
            log.error("[ERR][eliminarUsuario] Ocurrio un error al hacer la solicitud de eliminar usuario" + ex.getMessage());
            throw new ApiError("No se pudo eliminar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDetails buscarPorUsuarioPorMail(String username) throws UsernameNotFoundException {
    	log.info("[INI][buscarPorUsuarioPorMail]");
        Usuario usuario = usuarioRepository.findByEmail(username)
        		.orElseThrow(() -> {
                    log.error("[ERR][buscarPorUsuarioPorMail] Usuario no encontrado con el email: " + username);
                    ApiError error = new ApiError("Usuario no encontrado con el email: " + username, HttpStatus.UNAUTHORIZED);
                    return new UsernameNotFoundException(error.getMessage());
                });
        log.info("[FIN_OK][buscarPorUsuarioPorMail] Usuario encontrado con email: " + username);
        return User.builder()
                .username(usuario.getEmail()) 
                .password(usuario.getPassword()) 
                .authorities(new ArrayList<>())
                .build();
    }
}
