package com.vijay.sec01.service;

import com.vijay.sec01.dto.UserDto;
import com.vijay.sec01.exception.UserNotFoundException;
import com.vijay.sec01.model.User;
import com.vijay.sec01.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Flux<UserDto> getAllUsers() {
        return userRepository.findAll()
                .map(user -> {
                    UserDto userDto=new UserDto();
                    BeanUtils.copyProperties(user,userDto);
                    return userDto;
                });
    }

    @Override
    public Mono<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDto userDto=new UserDto();
                    BeanUtils.copyProperties(user,userDto);
                    return userDto;
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("User not found with id: {}", id);
                    return Mono.error(new UserNotFoundException("User Not Found"));
                }));
    }

    @Override
    public Mono<UserDto> createUser(UserDto userDto) {
        User user=new User();
        BeanUtils.copyProperties(userDto, user);
        return userRepository.save(user)
                .map(createdUser -> {
                    BeanUtils.copyProperties(createdUser,userDto);
                  return userDto;
                });
    }

    @Override
    public Mono<UserDto> updateUser(Long id, UserDto userDetails) {
        return userRepository.findById(id) // Find existing user by ID
                .switchIfEmpty(Mono.error(new UserNotFoundException("User Not Found"))) // Handle user not found
                .flatMap(existingUser -> {
                    BeanUtils.copyProperties(userDetails, existingUser, "id"); // Copy properties, excluding ID
                    return userRepository.save(existingUser); // Save updated user
                }).map(user ->{
                    BeanUtils.copyProperties(user,userDetails);
                    return userDetails;
                }); // Convert to UserDto
    }

    @Override
    public Mono<String> deleteUser(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User Not Found..!!")))
                .flatMap(user -> userRepository.delete(user).thenReturn("User deleted successfully"));
    }

    @Override
    public Mono<Void> deleteUsers(Long id) {
        return userRepository.findById(id) // Find existing user by ID
                .switchIfEmpty(Mono.error(new UserNotFoundException("User Not Found"))) // Handle user not found
                .flatMap(userRepository::delete);
    }

    @Override
    public Mono<String> createUsers(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return userRepository.save(user)
                .map(createdUser -> {
                    BeanUtils.copyProperties(createdUser, userDto);
                    return "User registered successfully: " + userDto.toString();
                });
    }

}
