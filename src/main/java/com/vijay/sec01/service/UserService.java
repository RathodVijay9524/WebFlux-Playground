package com.vijay.sec01.service;

import com.vijay.sec01.dto.UserDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserService {
    Flux<UserDto> getAllUsers();

    Mono<UserDto> getUserById(Long id);

    Mono<UserDto> createUser(UserDto userDto);

    Mono<String> createUsers(UserDto userDto);

    Mono<UserDto> updateUser(Long id, UserDto userDetails);

    Mono<String> deleteUser(Long id);
    Mono<Void> deleteUsers(Long id);

}
