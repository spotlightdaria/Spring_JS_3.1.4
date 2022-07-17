package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        model.addAttribute("users", users);
        model.addAttribute("roles", appService.listRoles());
        model.addAttribute("user1", user);
        return "admin";
    }

    @PostMapping("/new")
    public String createUser(User user) {
        appService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("user") User user, @RequestParam("roles") Long id) {
        appService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        appService.removeUser(id);
        return "redirect:/admin";
    }

}
