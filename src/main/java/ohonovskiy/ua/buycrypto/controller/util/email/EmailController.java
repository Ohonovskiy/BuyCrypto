package ohonovskiy.ua.buycrypto.controller.util.email;

import ohonovskiy.ua.buycrypto.DTO.crypto.EmailSendRequest;
import ohonovskiy.ua.buycrypto.service.util.email.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/send-email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public String sendEmail(@RequestBody EmailSendRequest emailSendRequest) {
        emailService.handleRequest(emailSendRequest);
        return "redirect:/";
    }
}
