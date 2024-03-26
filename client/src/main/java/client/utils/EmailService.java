package client.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailService {
    //TODO: Handle properties not found
    private JavaMailSender javaMailSender;
    private String userEmail;

    /**
     * Constructor
     */
    public EmailService() {
        this.javaMailSender = createJavaMailSender();
    }

    /**
     * Create a JavaMailSender object
     * @return JavaMailSender object
     */
    private JavaMailSender createJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        String host = "";
        int port = 0;
        String userName = "";
        String password = "";
        boolean smtpAuth = false;
        boolean smtpStarttlsEnable = false;
        try {
            inputStream = new FileInputStream("splitty.properties");
            properties.load(inputStream);
            host = properties.getProperty("spring.mail.host");
            port = Integer.parseInt(properties.getProperty("spring.mail.port"));
            userName = properties.getProperty("spring.mail.username");
            password = properties.getProperty("spring.mail.password");
            smtpAuth = Boolean.parseBoolean
                    (properties.getProperty("spring.mail.properties.mail.smtp.auth"));
            smtpStarttlsEnable = Boolean.parseBoolean
                    (properties.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the input stream if it's not null
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(userName);
        mailSender.setPassword(password);
        userEmail = userName;
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", smtpStarttlsEnable);

        return mailSender;
    }

    /**
     * Send a test email
     */
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(userEmail);
        message.setTo(userEmail);
        message.setSubject("Test email");
        message.setText("This is a test email");
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}