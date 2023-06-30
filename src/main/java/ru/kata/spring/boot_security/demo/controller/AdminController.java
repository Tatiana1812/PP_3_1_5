package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;


import java.util.List;

@Controller
@RequestMapping("api/admin")
public class AdminController {
    private final UserServiceImp userService;
    private final RoleServiceImp roleService;

    @Autowired
    public AdminController(UserServiceImp userService, RoleServiceImp roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok().body(users);
    }
//    @PostMapping("/add")
//    public ResponseEntity<HttpStatus> addUser(@RequestBody UserDTO userDTO) {
//        userService.add(userService.convertToUser(userDTO));
//        return ResponseEntity.ok().build();
//    }
    @PostMapping()
    public ResponseEntity<HttpStatus> addUser(@RequestBody User user) {
        userService.add(user);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody User user) {
        userService.delete(user);
        return ResponseEntity.ok().build();
    }
    @PatchMapping()
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok().build();
    }
//    @DeleteMapping(value = "/delete")
//    public ResponseEntity<HttpStatus> deleteUser(@RequestBody UserDTO userDTO) {
//        userService.delete(userService.convertToUser(userDTO));
//        return ResponseEntity.ok().build();
//    }
//    @PatchMapping(value = "/edit")
//    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDTO userDTO) {
//        userService.update(userService.convertToUser(userDTO));
//        return ResponseEntity.ok().build();
//    }
    @GetMapping()
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAll();
        return ResponseEntity.ok().body(roles);
    }
}