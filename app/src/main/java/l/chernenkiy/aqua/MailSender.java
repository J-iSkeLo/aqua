package l.chernenkiy.aqua;

import androidx.appcompat.app.AppCompatActivity;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender extends AppCompatActivity {

    public static void sendMail(String content) throws MessagingException {

            final String username = "aqua.m.kharkov.fish@gmail.com";
            final String password = "2506AquaFishKharkov";

            Properties properties = new Properties();

            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.starttls.enable", true);
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            String emailTo = "jiskelo91@gmail.com";
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject("Новый заказ!");
            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}

