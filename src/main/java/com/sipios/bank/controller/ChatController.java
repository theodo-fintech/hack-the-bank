package com.sipios.bank.controller;

import com.sipios.bank.model.Chat;
import com.sipios.bank.model.Message;
import com.sipios.bank.model.User;
import com.sipios.bank.repository.ChatRepository;
import com.sipios.bank.repository.MessageRepository;
import com.sipios.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ChatController {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}/chat")
    public String chat(
        Model model,
        @PathVariable Long userId,
        @RequestParam(required = false) Long chatId
    ) {
        User user = userRepository.getOne(userId);
        if (chatId != null) {
            user.setChats(user.getChats().stream().filter(chat -> Objects.equals(chat.getId(), chatId)).collect(Collectors.toList()));
        }
        model.addAttribute("user", user);

        return "chat";
    }

    @PostMapping("/chat/{id}")
    public String newMessage(
        Model model,
        @PathVariable Long id,
        @RequestParam("message") String message,
        @RequestParam("userId") Long userId
    ) {
        Chat chat = chatRepository.getOne(id);

        Message message1 = new Message();
        message1.setValue(message);
        message1.setChat(chat);
        messageRepository.save(message1);


        Collection<Message> messages = chat.getMessages();
        messages.add(message1);
        chat.setMessages(messages);
        chatRepository.save(chat);

        return "redirect:/user/"+ userId +"/chat?chatId=" + id;
    }
}
