package telcoventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telcoventas.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}