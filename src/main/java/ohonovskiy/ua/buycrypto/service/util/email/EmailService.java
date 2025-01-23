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
                "You're welcome, %s!",
                """
                Thank you for reaching out to us!
                We’ve received your message and will get back to you as soon as possible.
                Our team is here to assist you and typically responds within 3 business days.
                """ + SIGNATURE,
                "New email from user!",
                """
                 User message:
                 %s
                 """ + SIGNATURE
        ));
        put(EmailSendType.NEWS, new EmailTemplate(
                "Hello! Check Out Our Latest Updates!",
                """
                We're thrilled to share some exciting news with you! Stay tuned for more updates.
                %s
                """ + SIGNATURE
        ));
    }};

    public void handleRequest(EmailSendRequest emailSendRequest) {

        EmailSendType sendType = emailSendRequest.getEmailSendType();

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

            if(sendType.equals(EmailSendType.SUPPORT)) {
                message.setSubject(String.format(templates.get(sendType).getTeamHeader()));

                message.setText(
                        String.format(
                                templates.get(sendType).getTeamBody(),
                                emailSendRequest.getMessage()
                        )
                );

                Transport.send(message);

                message.setSubject(
                        String.format(
                                templates.get(sendType).getUserHeader(),
                                emailSendRequest.getFullName()
                        )
                );

                message.setText(templates.get(sendType).getUserBody());

                Transport.send(message);

            } else if (sendType.equals(EmailSendType.NEWS)) {
                message.setSubject(templates.get(sendType).getUserHeader());

                message.setText(
                        String.format(
                                templates.get(sendType).getUserBody(),
                                emailSendRequest.getMessage()
                        )
                );

                Transport.send(message);
            }

        } catch (MessagingException e) {
            System.err.println("Error occurred while sending email.");
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class EmailTemplate {
    private String userHeader;
    private String userBody;
    private String teamHeader;
    private String teamBody;

    public EmailTemplate(String userHeader, String userBody) {
        this.userHeader = userHeader;
        this.userBody = userBody;
    }
}