package com.vijay.sec01.controller;

import com.vijay.sec01.dto.CustomerDto;
import com.vijay.sec01.dto.UserDto;
import com.vijay.sec01.exception.UserNotFoundException;
import com.vijay.sec01.model.User;
import com.vijay.sec01.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @RequestMapping
    public Flux<UserDto> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(
                        ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                );
    }
    @PostMapping
    public Mono<UserDto> createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }
    @PostMapping("/add")
    public Mono<ResponseEntity<String>> createUsers(@RequestBody UserDto userDto) {
        return userService.createUsers(userDto)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error creating user: " + e.getMessage())));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserDto userDetails) {
        return userService.updateUser(id, userDetails)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .map(ResponseEntity::ok)
                .onErrorResume(UserNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUsers(@PathVariable Long id) {
        return userService.deleteUsers(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .onErrorResume(
                        UserNotFoundException.class,
                        e -> Mono.just(ResponseEntity.notFound().build())
                );
    }





}
