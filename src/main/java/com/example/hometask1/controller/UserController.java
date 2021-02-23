package com.example.hometask1.controller;

import com.example.hometask1.model.User;
import com.example.hometask1.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Api("User Controller")
public class UserController {
    private final UserRepository userRepository;

    @ApiOperation(
            value =
                    "Adding new user in database")
    @PostMapping("/add")
    public String addUser(@RequestParam Map<String, String> map) {
        String password = map.get("password");
        String login = map.get("login");

        if (userRepository.findByLogin(login) != null) {
            return "redirect:/users?error=Custom ERROR";
        } else {
            User user = new User();
            user.setLogin(login);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
            return "redirect:/login";
        }
    }

    @GetMapping("/")
    public String registration() {
        return "registration";
    }
}
