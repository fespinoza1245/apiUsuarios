package cl.fes.apiUsuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.fes.apiUsuarios.model.Telefono;

public interface TelefonoRepository extends JpaRepository<Telefono, Long> {
}
