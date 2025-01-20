package ohonovskiy.ua.buycrypto.service.util.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import ohonovskiy.ua.buycrypto.DTO.crypto.EmailSendRequest;
import ohonovskiy.ua.buycrypto.enums.EmailSendType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailService {
    private final String username = "yuriy.ohonovskiy@gmail.com";
    private final String password = "rqbt dwmf vxga hqzq";

    private final String SIGNATURE =
            """
            \n
            \n
            \n
            Best regards,
            BuyCrypto Team.
            +390 2813 100
            UK, London
            """;

    private final Map<EmailSendType, EmailTemplate> templates = new HashMap<>() {{
        put(EmailSendType.SUPPORT, new EmailTemplate(
                "New email from user!",
                "You're welcome, %s!",
                """
                Thank you for reaching out to us!
                We’ve received your message and will get back to you as soon as possible.
                Our team is here to assist you and typically responds within 3 business days.
                """ + SIGNATURE
        ));
        put(EmailSendType.NEWS, new EmailTemplate(
                "Exciting News from BuyCrypto!",
                "Hello %s, Check Out Our Latest Updates!",
                """
                We're thrilled to share some exciting news with you! Stay tuned for more updates.
                """ + SIGNATURE
        ));
    }};

    public void handleRequest(EmailSendRequest emailSendRequest, EmailSendType emailSendType) {
        sendEmail(emailSendRequest, emailSendType);
    }

    private void sendEmail(EmailSendRequest emailSendRequest, EmailSendType emailSendType) {
        EmailTemplate template = templates.get(emailSendType);
        if (template == null) {
            throw new IllegalArgumentException("Unsupported email type: " + emailSendType);
        }
        sendEmailUtil(emailSendRequest, template);
    }

    private void sendEmailUtil(EmailSendRequest emailSendRequest, EmailTemplate template) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailSendRequest.getEmail())
            );

            message.setSubject(String.format(template.getSubject(), emailSendRequest.getFullName()));

            String messageBody = template.getBody();
            if (template.isForTeam()) {
                messageBody = String.format(
                        "User message:\n%s\n\nUser email: %s\n%s",
                        emailSendRequest.getMessage(),
                        emailSendRequest.getEmail(),
                        SIGNATURE
                );
            }

            message.setText(messageBody);

            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Error occurred while sending email.");
        }
    }

    private static class EmailTemplate {
        private final String teamSubject;
        private final String userSubject;
        @Getter
        private final String body;

        public EmailTemplate(String teamSubject, String userSubject, String body) {
            this.teamSubject = teamSubject;
            this.userSubject = userSubject;
            this.body = body;
        }

        public String getSubject() {
            return userSubject;
        }

        public boolean isForTeam() {
            return teamSubject != null;
        }
    }
}