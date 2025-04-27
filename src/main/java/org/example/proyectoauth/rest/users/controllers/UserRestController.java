package org.example.proyectoauth.rest.users.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.config.security.SecurityUtils;
import org.example.proyectoauth.pageresponse.PageResponse;
import org.example.proyectoauth.rest.users.dto.UserInfoResponseDto;
import org.example.proyectoauth.rest.users.dto.UserProfileUpdateDto;
import org.example.proyectoauth.rest.users.dto.UserRequestDto;
import org.example.proyectoauth.rest.users.dto.UserResponseDto;
import org.example.proyectoauth.rest.users.model.User;
import org.example.proyectoauth.rest.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.example.proyectoauth.config.security.SecurityUtils.getCurrentUsername;

@RestController
@Slf4j
@RequestMapping("${api.version}/users")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Users", description = "Endpoint para gestionar los usuarios del sistema")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponseDto>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        log.info("[ADMIN: {}] solicitó todos los usuarios con filtros", getCurrentUsername());

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<UserResponseDto> pageResult = userService.findAll(username, email, isActive, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponseDto> getById(@PathVariable("id") Long id) {
        log.info("[ADMIN: {}] obtuvo usuario con ID: {}", getCurrentUsername(), id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> postUser(@Valid @RequestBody UserRequestDto user) {
        log.info("[ADMIN: {}] creó un nuevo usuario: {}", getCurrentUsername(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> putUser(@PathVariable("id") Long id, @RequestBody UserRequestDto user) {
        log.info("[ADMIN: {}] actualizó usuario con ID: {}", getCurrentUsername(), id);
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        log.info("[ADMIN: {}] eliminó usuario con ID: {}", getCurrentUsername(), id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfoResponseDto> meProfile(@AuthenticationPrincipal User user) {
        log.info("[USER: {}] solicitó su perfil", getCurrentUsername());
        return ResponseEntity.ok(userService.findById(user.getId()));
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> meProfileUpdate(@AuthenticationPrincipal User user,
                                                           @RequestBody UserProfileUpdateDto userDto) {
        log.info("[USER: {}] actualizó su perfil", getCurrentUsername());
        return ResponseEntity.ok(userService.updateProfile(user.getId(), userDto));
    }


    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> meProfileDelete(@AuthenticationPrincipal User user) {
        log.info("[USER: {}] eliminó su perfil", getCurrentUsername());
        userService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
