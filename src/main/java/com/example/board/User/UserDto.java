package com.example.board.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String nickname;

    // 엔티티 -> DTO 변환 (Response용)
    public static UserDto fromEntity(User user) {
        return new UserDto(user.getEmail(), user.getPassword(), user.getNickname());
    }

    // DTO -> 엔티티 변환 (Request용)
    public User toEntity() {
        return new User(null, this.email, this.password, this.nickname);
    }
}
