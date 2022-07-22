package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.AppService;

@Controller

public class MainController {
    private final AppService appService;

    @Autowired
    public MainController(AppService userService) {
        this.appService = userService;
    }

    @GetMapping(value = "/")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/user")
    public String user() {
        return "userInfo";
    }

    @GetMapping(value = "/admin")
    public String admin() {
        return "admin";
    }


}
