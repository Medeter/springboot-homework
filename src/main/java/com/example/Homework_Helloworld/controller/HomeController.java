package com.example.Homework_Helloworld.controller;

import com.example.Homework_Helloworld.model.MessageRequest;
import com.example.Homework_Helloworld.model.MessageResponse;
import com.example.Homework_Helloworld.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/kumpee")
    public MessageResponse kumpee(@RequestBody MessageRequest request) {
        return messageService.createWelcomeMessage(request);
    }
    
    @GetMapping("/kumpee")
    public MessageResponse getKumpee() {
        MessageRequest defaultRequest = new MessageRequest("Kumpee Kongsuk");
        return messageService.createWelcomeMessage(defaultRequest);
    }
}
