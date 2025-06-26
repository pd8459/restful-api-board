package com.example.board.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class SignupController {

    private final RestTemplate restTemplate;

    public SignupController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(String email, String password, String nickname, Model model) {
        UserDto userDto = new UserDto(email, password, nickname);
        String apiUrl = "http://localhost:8080/api/users";
        UserDto response = restTemplate.postForObject(apiUrl, userDto, UserDto.class);

        if (response != null) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "회원가입 실패");
            return "signup";
        }
    }
}
