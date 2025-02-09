package com.example.board.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입 (Create)
    public User registerUser(User user) {
        // 비밀번호 암호화 로직을 추가할 수 있습니다.
        return userRepository.save(user);
    }

    // 이메일로 사용자 조회 (Read)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 모든 사용자 조회 (Read)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 사용자 정보 수정 (Update)
    @Transactional
    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setNickname(updatedUser.getNickname());
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    // 사용자 삭제 (Delete)
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
