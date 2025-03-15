package com.example.board.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model) {

        String apiUrl = "http://localhost:8080/api/users/" + email;
        UserDto userDto = restTemplate.getForObject(apiUrl, UserDto.class);

        if (userDto != null) {
            if (userDto.getPassword().equals(password)) {
                model.addAttribute("user", userDto);
                return "redirect:/home.html";
            } else {
                model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
                return "login.html";
            }
        } else {
            model.addAttribute("error", "등록되지 않은 이메일입니다.");
            return "login.html";
        }
    }
}
