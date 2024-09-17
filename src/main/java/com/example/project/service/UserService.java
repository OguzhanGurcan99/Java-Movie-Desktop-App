package com.example.project.service;

import com.example.project.api.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> userList;

    public UserService() {
        userList = new ArrayList<>();
        User user1 = new User(1,"oguzhan",25, "oguzhan@gmail.com");
        User user2 = new User(2,"burak",25, "burak@gmail.com");
        User user3 = new User(3,"ali",25, "ali@gmail.com");
        User user4 = new User(4,"veli",25, "veli@gmail.com");
        User user5 = new User(5,"mert",25, "mert@gmail.com");

        userList.addAll(Arrays.asList(user1,user2,user3,user4,user5));
    }

    public Optional<User> getUser(Integer id) {
            Optional optional = Optional.empty();
        for (User user : userList) {
            if (id == user.getId()) {
                optional = Optional.of(user);
                return optional;
            }
        }
        return optional;
    }

    public List<User> addUser(String name, int age , String email) {
        int newId = userList.size() + 1;
        User newUser = new User(newId,name,age,email);
        userList.add(newUser);
        return  userList;
    }
}
