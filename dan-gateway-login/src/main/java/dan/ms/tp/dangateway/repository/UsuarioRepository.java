package dan.ms.tp.dangateway.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import dan.ms.tp.dangateway.model.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Long> {

    Mono<Usuario> findByUsername(String username);

}
