package ru.kata.spring.boot_security.demo.service;

import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findByUsername(String username);

    List<User> getAll();

    void add(User user);

    void delete(User user);

    User getUserById(Long id);
    User convertToUser(UserDTO userDTO);

    void update(User user);
}
