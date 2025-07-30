package com.example.Homework_Helloworld.controller;

import com.example.Homework_Helloworld.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/kumpee")
    public String kumpee() {
        return messageService.getWelcomeMessage();
    }
}
