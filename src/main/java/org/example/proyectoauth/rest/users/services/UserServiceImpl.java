package org.example.proyectoauth.rest.users.services;

import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.rest.users.dto.UserInfoResponseDto;
import org.example.proyectoauth.rest.users.dto.UserProfileUpdateDto;
import org.example.proyectoauth.rest.users.dto.UserRequestDto;
import org.example.proyectoauth.rest.users.dto.UserResponseDto;
import org.example.proyectoauth.rest.users.exceptions.UserNotFound;
import org.example.proyectoauth.rest.users.exceptions.UsernameOrEmailExists;
import org.example.proyectoauth.rest.users.mapper.UserMapper;
import org.example.proyectoauth.rest.users.model.User;
import org.example.proyectoauth.rest.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación de la interfaz UserService que gestiona las operaciones relacionadas con usuarios.
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    /**
     * Constructor de UserServiceImpl.
     *
     * @param userRepository     Repositorio de usuarios.
     * @param userMapper         Mapper para convertir entre DTOs y entidades de usuario.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Obtiene una página de usuarios filtrados opcionalmente por nombre de usuario, correo electrónico y estado de activación.
     *
     * @param username Nombre de usuario opcional para filtrar.
     * @param email    Dirección de correo electrónico opcional para filtrar.
     * @param isActive Estado de activación opcional para filtrar.
     * @param pageable Objeto Pageable para la paginación y ordenación de los resultados.
     * @return Página de UserResponseDto que cumple con los criterios de filtrado.
     */
    @Override
    public Page<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isActive, Pageable pageable) {
        Specification<User> specUsername = ((root, query, criteriaBuilder) ->
                username.map(us -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + us.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<User> specEmail = ((root, query, criteriaBuilder) ->
                email.map(em -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + em.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<User> specIsActive = ((root, query, criteriaBuilder) ->
                isActive.map(is -> criteriaBuilder.equal(root.get("isActive"), is))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<User> criterial = Specification.where(specUsername)
                .and(specEmail)
                .and(specIsActive);

        return userRepository.findAll(criterial,pageable).map(userMapper::toUserResponse);
    }

    /**
     * Obtiene la información detallada de un usuario por su identificador único.
     *
     * @param id Identificador único del usuario.
     * @return UserInfoResponseDto que contiene la información detallada del usuario.
     */
    @Override
    public UserInfoResponseDto findById(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFound(" id " + id));
        return userMapper.toUserInfoResponse(user);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    /**
     * Guarda un nuevo usuario en el sistema.
     *
     * @param userRequestDto Datos del usuario a ser guardado.
     * @return UserResponseDto que contiene la información del usuario guardado.
     */
    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        log.info("Guardando usuario: " + userRequestDto);

        userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(userRequestDto.getUsername(), userRequestDto.getEmail())
                .ifPresent(user -> {
                    throw new UsernameOrEmailExists(user.getUsername() + "-" + user.getEmail());
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRequestDto)));
    }

    /**
     * Actualiza la información de un usuario existente por su identificador único.
     *
     * @param id             Identificador único del usuario a actualizar.
     * @param userRequestDto Nuevos datos del usuario.
     * @return UserResponseDto que contiene la información del usuario actualizado.
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponseDto update(Long id, UserRequestDto userRequestDto) {
        log.info("Actualizando usuario: " + userRequestDto);
        var userfound = userRepository.findById(id).orElseThrow(() -> new UserNotFound("id " + id));

        userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(userRequestDto.getUsername(), userRequestDto.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new UsernameOrEmailExists(user.getUsername() + "-" + user.getEmail());
                    }
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userfound,userRequestDto,id)));
    }
    /**
     * Actualiza el perfil de un usuario existente por su identificador único.
     *
     * @param id Identificador único del usuario a actualizar.
     * @param dto Nuevos datos del perfil del usuario.
     * @return UserResponseDto que contiene la información del usuario actualizado.
     */
    public UserResponseDto updateProfile(Long id, UserProfileUpdateDto dto) {
        log.info("Actualizando perfil de usuario con ID: {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("id " + id));

        // Validar si el username o email ya están en uso por otro
        userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(dto.getUsername(), dto.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new UsernameOrEmailExists(existingUser.getUsername() + "-" + existingUser.getEmail());
                    }
                });

        // Actualiza solo los campos permitidos
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        return userMapper.toUserResponse(userRepository.save(user));
    }


    /**
     * Elimina un usuario por su identificador único.
     *
     * @param id Identificador único del usuario a eliminar.
     */
    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Desactivando usuario con ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("id " + id));

        user.setIsActive(false);
        userRepository.save(user); //desactivamos el usuario de forma lógica, digamos que lo baneamos
    }
}
