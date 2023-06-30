package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class OldAdminController {
    private final UserServiceImp userService;
    private final RoleServiceImp roleService;

    @Autowired
    public OldAdminController(UserServiceImp userService, RoleServiceImp roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public String welcomePrint(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.getAll());
        User user = userService.findByUsername(principal.getName());
        model.put("roles", roleService.getAll());
        model.addAttribute("user", user);
        return "users";
    }

    @GetMapping(value = "/add")
    public String showUserForm(ModelMap model) {
        model.put("Title", "Add new user");
        User newUser = new User();
        newUser.setRoles(new HashSet<>());
        model.put("user", newUser);
        model.put("roles", roleService.getAll());
        return "add";
    }

    @PostMapping(value = "/add")
    public String addUser(@RequestParam String name, @RequestParam String surname,
                          @RequestParam int age, @RequestParam String password, @RequestParam List<Long> roles, ModelMap model) {
        User newUser = new User(name, surname, age, password, roleService.getRolesById(roles));
        model.put("users", newUser);
        userService.add(newUser);
        return "redirect:/admin";
    }
    @DeleteMapping()
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(userService.getUserById(id));
        return "redirect:/admin";
    }
    @GetMapping(value = "/edit")
    public String showEditUserForm(@RequestParam Long id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        model.put("roles", roleService.getAll());
        model.put("Title", "Edit user");
        return "add";
    }
    @PatchMapping()
    public String editUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

}
