package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AppService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppService appService;

    @Autowired
    public AdminController(AppService userService) {
        this.appService = userService;
    }

    @GetMapping()
    public String findAll(Model model) {
        List<User> users = appService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/user-create")
    public String createUserForm(User user) {
        return "user-create";
    }

    @PostMapping("/user-create")
    public String createUser(User user) {
        appService.saveUser(user);
        return "redirect:/admin/admin";
    }

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        appService.removeUser(id);
        return "redirect:/admin/admin";
    }

    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = appService.getUserById(id);
        model.addAttribute("user", user);
        return "user-update";
    }

    @PostMapping("/user-update")
    public String updateUser(User user) {
        appService.saveUser(user);
        return "redirect:/admin/admin";
    }
}
