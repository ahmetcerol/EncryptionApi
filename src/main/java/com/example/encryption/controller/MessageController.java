package com.example.encryption.controller;

import com.example.encryption.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private EncryptionService encryptionService;

    private final Path filePath = Path.of("messages.txt");
    // Şifrelenmiş mesajı içeren JSON nesnesi
    public static class MessageResponse {
        private final String encryptedMessage;

        public MessageResponse(String encryptedMessage) {
            this.encryptedMessage = encryptedMessage;
        }

        public String getEncryptedMessage() {
            return encryptedMessage;
        }
    }
    @PostMapping("/send")
    public MessageResponse sendMessage(@RequestBody String message) throws Exception {
        String encryptedMessage = encryptionService.encrypt(message);
        System.out.println(encryptedMessage);
        Files.write(filePath, (encryptedMessage + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        // Geri dönen şifrelenmiş mesajı içeren bir JSON nesnesi
        return new MessageResponse(encryptedMessage);
    }


    @GetMapping("/receive")
    public List<String> receiveMessages() throws IOException {
        List<String> encryptedMessages = Files.readAllLines(filePath);
        for (int i = 0; i < encryptedMessages.size(); i++) {
            try {
                String decryptedMessage = encryptionService.decrypt(encryptedMessages.get(i));
                encryptedMessages.set(i, decryptedMessage);
            } catch (Exception e) {
                // Handle decryption error
            }
        }
        return encryptedMessages;
    }

}
