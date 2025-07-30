package com.example.Homework_Helloworld.service;

import com.example.Homework_Helloworld.model.MessageRequest;
import com.example.Homework_Helloworld.model.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public MessageResponse createWelcomeMessage(MessageRequest request) {
        String message = "Hello " + request.getName();
        return new MessageResponse(message);
    }
}
