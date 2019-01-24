package com.sipios.bank.controller;

import com.sipios.bank.model.Chat;
import com.sipios.bank.model.Message;
import com.sipios.bank.model.User;
import com.sipios.bank.repository.ChatRepository;
import com.sipios.bank.repository.MessageRepository;
import com.sipios.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sipios.bank.config.MvcConfig.adminPassword;
import static com.sipios.bank.config.MvcConfig.adminUsername;
import static java.lang.Math.toIntExact;

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
        user.getChats().forEach(chat -> {
            chat.getMessages().removeIf(message -> message.getDate().toInstant().plusSeconds(60L).isBefore(Instant.now()));
        } );
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
        message1.setFromAdvisor(false);
        messageRepository.save(message1);

        Collection<Message> messages = chat.getMessages();
        messages.add(message1);

        List<String> advisorMessages = new ArrayList<String>();
        advisorMessages.add("Bonjour, comment puis-je vous aider aujourd'hui ?");
        advisorMessages.add("Je comprends parfaitement, mais malheureusement notre système informatique ne me permet actuellement pas de procéder à cette action pour votre compte. Je ne manquerai pas d'en faire part à mes responsables.");
        advisorMessages.add("Ce qu'il est possible de faire pour avancer, ce serait de nous envoyer par courrier recommandé un justificatif de domicile ainsi qu'une preuve de virement accompagnée de la signature du directeur de votre agence.");
        advisorMessages.add("J'ai peur qu'il ne soit possible de faire le nécessaire qu'en agence. Auriez-vous la possibilité de passer en agence voir votre conseiller? Il a des disponibilités le mercredi 6/03 de 15h à 15h30.");
        advisorMessages.add("[WARNING] Vous avez atteint le nombre maximal d'échanges avec votre conseiller");

        if(message.contains("<script>") && message.contains("</script>") && message.contains("document.cookie")) {
            Message successMessage = new Message();
            successMessage.setChat(chat);
            successMessage.setFromAdvisor(true);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders reqHeaders = new HttpHeaders();
            reqHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("username", adminUsername);
            map.add("password", adminPassword);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, reqHeaders);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/se-connecter", request, String.class);
            HttpHeaders headers = responseEntity.getHeaders();
            if (headers.getFirst("Location").contains("login-error")) {
                successMessage.setValue("Error");
            } else {
                successMessage.setValue("Cookie: " + headers.getFirst("Set-Cookie"));
            }
            messages.add(successMessage);
            messageRepository.save(successMessage);
        } else if (!message.isEmpty()) {
            Message successMessage = new Message();
            successMessage.setChat(chat);
            successMessage.setFromAdvisor(true);

            long advisorMessagesCount = messages.stream().filter(m -> m.getFromAdvisor()).count();
            String chosenMessage = advisorMessagesCount < 4 ? advisorMessages.get(toIntExact(advisorMessagesCount)) : advisorMessages.get(4);
            successMessage.setValue(chosenMessage);
            messages.add(successMessage);
            messageRepository.save(successMessage);
        }

        chat.setMessages(messages);
        chatRepository.save(chat);

        return "redirect:/user/"+ userId +"/chat?chatId=" + id;
    }
}
