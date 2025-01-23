package ohonovskiy.ua.buycrypto.service.util.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ohonovskiy.ua.buycrypto.DTO.util.email.EmailSendRequest;
import ohonovskiy.ua.buycrypto.enums.EmailSendType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailService {

    private static final String USERNAME = "yuriy.ohonovskiy@gmail.com";
    private static final String PASSWORD = "rqbt dwmf vxga hqzq";
    private static final String SIGNATURE = """
            \n\n\n
            Best regards,
            BuyCrypto Team.
            +390 2813 100
            UK, London
            """;

    private final Map<EmailSendType, EmailTemplate> templates;

    public EmailService() {
        templates = new EnumMap<>(EmailSendType.class);
        templates.put(EmailSendType.SUPPORT, new EmailTemplate(
                "You're welcome, %s!",
                """
                Thank you for reaching out to us!
                We’ve received your message and will get back to you as soon as possible.
                Our team is here to assist you and typically responds within 3 business days.
                """ + SIGNATURE,
                "New email from %s!",
                """
                User message:
                %s
                
                User email:
                %s
                """ + SIGNATURE
        ));
        templates.put(EmailSendType.NEWS, new EmailTemplate(
                "Hello! Check Out Our Latest Updates!",
                """
                We're thrilled to share some exciting news with you! Stay tuned for more updates.
                %s
                """ + SIGNATURE
        ));
    }

    public void handleRequest(EmailSendRequest emailSendRequest) {
        try {
            Session session = createEmailSession();
            EmailSendType sendType = emailSendRequest.getEmailSendType();
            EmailTemplate template = templates.get(sendType);

            if (template != null) {
                switch (sendType.getScope()) {
                    case USERS_ONLY -> sendEmail(session, emailSendRequest.getEmail(),
                            String.format(template.getUserHeader(), emailSendRequest.getFullName()),
                            String.format(template.getUserBody(), emailSendRequest.getMessage()));

                    case SUPPORT_ONLY -> sendEmail(session, USERNAME, String.format(template.getTeamHeader(), emailSendRequest.getFullName()),
                            String.format(template.getTeamBody(), emailSendRequest.getMessage(), emailSendRequest.getEmail()));

                    case BOTH -> {
                        sendEmail(session, emailSendRequest.getEmail(),
                                String.format(template.getUserHeader(), emailSendRequest.getFullName()),
                                String.format(template.getUserBody(), emailSendRequest.getMessage()));
                        sendEmail(session, USERNAME, String.format(template.getTeamHeader(), emailSendRequest.getFullName()),
                                String.format(template.getTeamBody(), emailSendRequest.getMessage(), emailSendRequest.getEmail()));
                    }
                }
            }
        } catch (MessagingException e) {
            System.err.println("Error occurred while sending email: " + e.getMessage());
        }
    }


    private Session createEmailSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    private void sendEmail(Session session, String recipient, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class EmailTemplate {
        private String userHeader;
        private String userBody;
        private String teamHeader;
        private String teamBody;

        public EmailTemplate(String userHeader, String userBody) {
            this.userHeader = userHeader;
            this.userBody = userBody;
        }
    }
}
