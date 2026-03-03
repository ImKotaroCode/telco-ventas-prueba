package telcoventas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import telcoventas.config.JwtService;
import telcoventas.config.UserDetailsServiceImpl;
import telcoventas.dto.request.LoginRequest;
import telcoventas.dto.response.LoginResponse;
import telcoventas.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        UserDetails ud = userDetailsService.loadUserByUsername(req.getUsername());
        String rol = usuarioRepository.findByUsername(req.getUsername()).orElseThrow().getRol().name();

        String token = jwtService.generateToken(ud, rol);
        return new LoginResponse(token, req.getUsername(), rol);
    }
}