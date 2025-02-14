package com.example.board.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final com.example.board.User.UserService userService;


    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        User user = userDto.toEntity();
        User savedUser = userService.registerUser(user);
        UserDto savedUserDto = UserDto.fromEntity(savedUser);
        return ResponseEntity.ok(savedUserDto);
    }


    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(u -> ResponseEntity.ok(UserDto.fromEntity(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(UserDto::fromEntity)
                .toList();
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto updatedUserDto) {
        User updatedUser = updatedUserDto.toEntity();
        Optional<User> updated = userService.updateUser(id, updatedUser);
        return updated.map(u -> ResponseEntity.ok(UserDto.fromEntity(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
