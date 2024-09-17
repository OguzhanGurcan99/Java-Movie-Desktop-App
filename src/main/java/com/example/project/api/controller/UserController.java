package com.example.project.api.controller;

import com.example.project.api.model.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public User getUser(@RequestParam Integer id) {
        Optional user = userService.getUser(id);
        if (user.isPresent()){
            return (User) user.get();
        }
        return null;
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User newUser) { // JSON formatında kullanıcı bilgisi al
        List<User> addedList = userService.addUser(newUser.getName(), newUser.getAge(), newUser.getEmail());
        System.out.println( "Kullanıcı eklendi . Güncel kullanıcı sayısı : " + addedList.size());
    }
}
