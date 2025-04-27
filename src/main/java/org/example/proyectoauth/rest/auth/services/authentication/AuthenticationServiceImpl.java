package org.example.proyectoauth.rest.auth.services.authentication;

import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.rest.auth.dto.JwtAuthResponseDto;
import org.example.proyectoauth.rest.auth.dto.UserSignInRequest;
import org.example.proyectoauth.rest.auth.dto.UserSignUpRequest;
import org.example.proyectoauth.rest.auth.exceptions.AuthSignInInvalid;
import org.example.proyectoauth.rest.auth.exceptions.AuthSignInStatusException;
import org.example.proyectoauth.rest.auth.exceptions.UserInvalidPasswords;
import org.example.proyectoauth.rest.auth.repositories.AuthRepository;
import org.example.proyectoauth.rest.auth.services.jwt.JwtService;
import org.example.proyectoauth.rest.users.exceptions.UsernameOrEmailExists;
import org.example.proyectoauth.rest.users.model.Role;
import org.example.proyectoauth.rest.users.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementación del servicio de autenticación.
 *
 * Proporciona los métodos para registrar y autenticar usuarios,
 * generar tokens JWT y validar credenciales usando Spring Security.
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor con inyección de dependencias necesarias para la autenticación.
     *
     * @param authRepository Repositorio de usuarios.
     * @param passwordEncoder Codificador de contraseñas.
     * @param jwtService Servicio para generar tokens JWT.
     * @param authenticationManager Administrador de autenticación de Spring.
     */
    public AuthenticationServiceImpl(AuthRepository authRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param signUpRequest Datos del usuario a registrar.
     * @return Token JWT generado para el nuevo usuario.
     * @throws UserInvalidPasswords Si las contraseñas no coinciden.
     * @throws UsernameOrEmailExists Si ya existe un usuario o email registrado.
     */
    @Override
    public JwtAuthResponseDto signUp(UserSignUpRequest signUpRequest) {
        log.info("[AUTH] Registro de nuevo usuario con username: {}", signUpRequest.getUsername());

        if (!signUpRequest.getPassword().equals(signUpRequest.getPasswordComprobacion())) {
            throw new UserInvalidPasswords("Las contraseñas no coinciden");
        }

        // Verificar si ya existe ese username
        authRepository.findByUsername(signUpRequest.getUsername()).ifPresent(user -> {
            throw new UsernameOrEmailExists("El nombre de usuario '" + user.getUsername() + "' ya está registrado");
        });

        // Verificar si ya existe ese email
        authRepository.findByEmail(signUpRequest.getEmail()).ifPresent(user -> {
            throw new UsernameOrEmailExists("El correo electrónico '" + user.getEmail() + "' ya está registrado");
        });

        User user = User.builder()

                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .roles(Set.of(Role.USER))
                .build();

        User savedUser = authRepository.save(user);
        String jwt = jwtService.generateToken(savedUser);

        return JwtAuthResponseDto.builder().token(jwt).build();
    }


    /**
     * Autentica un usuario existente y genera un token JWT.
     *
     * @param signInRequest Credenciales del usuario.
     * @return Token JWT si las credenciales son válidas.
     * @throws AuthSignInInvalid Si las credenciales son incorrectas.
     */
    @Override
    public JwtAuthResponseDto signIn(UserSignInRequest signInRequest) {
        log.info("[AUTH] Intento de inicio de sesión para: {}", signInRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);

            log.info("[AUTH] Inicio de sesión exitoso para: {}", userDetails.getUsername());

            return JwtAuthResponseDto.builder().token(jwt).build();

        } catch (DisabledException e) {
            log.warn("[AUTH] Usuario desactivado: {}", signInRequest.getUsername());
            throw new AuthSignInStatusException("Tu cuenta está desactivada. Contacta con un administrador.", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.warn("[AUTH] Fallo en inicio de sesión para {}: {}", signInRequest.getUsername(), e.getMessage());
            throw new AuthSignInStatusException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }
    }

}
