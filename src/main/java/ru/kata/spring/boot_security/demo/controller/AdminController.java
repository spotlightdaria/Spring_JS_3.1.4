package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
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
    public String createUserForm(User user, Model model) {
        model.addAttribute(user);
        return "user-create";
    }

    @PostMapping("/user-create")
    public String createUser(User user) {
        appService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        appService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/user-update/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        User user = appService.getUserById(id);
        List<Role> listRoles = appService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);

        return "user-update";
    }

    @PostMapping("/user-update/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
 //       user.setId(user.getId());
        appService.saveUser(user);
        return "redirect:/admin";
    }


}
