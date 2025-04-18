package cl.fes.apiUsuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.fes.apiUsuarios.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Override
    List<Usuario> findAll();

    //Agregamos la validacion para buscar el correo agregado
    Optional<Usuario> findByEmail(String email);
}
