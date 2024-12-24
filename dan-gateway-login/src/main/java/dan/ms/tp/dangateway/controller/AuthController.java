package dan.ms.tp.dangateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dan.ms.tp.dangateway.dto.AuthRequest;
import dan.ms.tp.dangateway.dto.AuthResponse;
import dan.ms.tp.dangateway.model.Usuario;
import dan.ms.tp.dangateway.servicios.AuthService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return userService.loginUser(authRequest)
        .map(authResponse -> ResponseEntity.ok(authResponse)) // Envuelve en ResponseEntity después de resolver el Mono
        .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null))); // Maneja errores
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signup(@RequestBody Usuario user) {
    return userService.registerUser(user)
            .map(savedUser -> ResponseEntity.ok("User signed up successfully"));
    }

}
