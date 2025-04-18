package cl.fes.apiUsuarios.services;

import cl.fes.apiUsuarios.dto.UsuarioCreatedDTO;
import cl.fes.apiUsuarios.dto.UsuarioEncontradoDTO;
import cl.fes.apiUsuarios.dto.UsuarioListDTO;
import cl.fes.apiUsuarios.exceptions.ApiError;
import cl.fes.apiUsuarios.model.Usuario;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UsuarioService {
    public List<UsuarioListDTO> buscarTodos() throws ApiError;
    public UsuarioEncontradoDTO buscarPorId(Long id) throws ApiError;
    public UsuarioCreatedDTO crearUsuario(Usuario u) throws ApiError;
    public UsuarioCreatedDTO editarUsuario(Usuario u, Long id) throws ApiError;
    public void eliminarUsuario(Long id) throws ApiError;
    public UserDetails buscarPorUsuarioPorMail(String username) throws UsernameNotFoundException;
}
