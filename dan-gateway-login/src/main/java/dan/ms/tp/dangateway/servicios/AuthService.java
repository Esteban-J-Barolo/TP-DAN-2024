package dan.ms.tp.dangateway.servicios;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dan.ms.tp.dangateway.dto.AuthRequest;
import dan.ms.tp.dangateway.dto.AuthResponse;
import dan.ms.tp.dangateway.model.Usuario;
import dan.ms.tp.dangateway.repository.UsuarioRepository;
import dan.ms.tp.dangateway.seguridad.JWTUtil;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.usuarioRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
    }

    public Mono<Usuario> registerUser(Usuario user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usuarioRepository.save(user);
    }

    public Mono<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Mono<AuthResponse> loginUser(AuthRequest authRequest){

        Mono<Usuario> usuario = findByUsername(authRequest.getUsername());

        return usuario
                .map(userDetails -> {
                    if (passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
                        return new AuthResponse(jwtUtil.generateToken(authRequest.getUsername()));
                    } else {
                        throw new BadCredentialsException("Invalid username or password");
                    }
                }).switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username or password")));
    }

}
