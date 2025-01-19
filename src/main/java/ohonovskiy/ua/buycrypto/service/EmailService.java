package ohonovskiy.ua.buycrypto.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import ohonovskiy.ua.buycrypto.DTO.EmailSendRequest;
import ohonovskiy.ua.buycrypto.enums.EmailType;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    private final String username = "yuriy.ohonovskiy@gmail.com";
    private final String password = "rqbt dwmf vxga hqzq";

    private String TEAM_SUBJECT = "New email from user!";
    private String USER_SUBJECT = "You're welcome "; // appending full name later
    private String YOUR_MESSAGE_ACCEPTED =
            """
                    Thank you for reaching out to us!
                    We’ve received your message and will get back to you as soon as possible.
                    Our team is here to assist you and typically responds within 3 business days.
            """;

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


    public void handleRequest(EmailSendRequest emailSendRequest) {
        sendEmail(emailSendRequest, EmailType.SEND_TO_TEAM);
        sendEmail(emailSendRequest, EmailType.SEND_TO_USER);
    }

    private void sendEmail(EmailSendRequest emailSendRequest, EmailType emailType) {

        boolean sendToTeam = emailType == EmailType.SEND_TO_TEAM;

        StringBuilder messageText = new StringBuilder();

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
                    InternetAddress.parse(sendToTeam ? username : emailSendRequest.getEmail())
            );

            message.setSubject(sendToTeam ? TEAM_SUBJECT : USER_SUBJECT + emailSendRequest.getFullName() + "!");

            messageText
                    .append(sendToTeam ?
                            "User message:\n"
                                    + emailSendRequest.getMessage()
                                    + "\n\nUser email: "
                                    + emailSendRequest.getEmail()
                            : YOUR_MESSAGE_ACCEPTED)
                    .append(SIGNATURE);

            message.setText(messageText.toString());

            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Error occurred while sending email.");
        }
    }
}
