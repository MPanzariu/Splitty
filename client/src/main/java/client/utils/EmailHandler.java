package client.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailHandler {
    //TODO: Handle properties not found
    private JavaMailSender javaMailSender;
    private String userEmail;
    private boolean isConfigured;

    /**
     * Constructor
     */
    public EmailHandler() {
        isConfigured = false;
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
        String host = null;
        int port = -1;
        String userName = null;
        String password = null;
        boolean smtpAuth = false;
        boolean smtpStarttlsEnable = false;
        try {
            inputStream = new FileInputStream("splitty.properties");
            properties.load(inputStream);
            host = properties.getProperty("spring.mail.host");
            String strPort = properties.getProperty("spring.mail.port");
            if (strPort != null && !strPort.isEmpty()){
                port = Integer.parseInt(properties.getProperty("spring.mail.port"));
            }
            userName = properties.getProperty("spring.mail.username");
            password = properties.getProperty("spring.mail.password");
            String strSmtpAuth = properties.getProperty("spring.mail.properties.mail.smtp.auth");
            if (strSmtpAuth != null && !strSmtpAuth.isEmpty()){
                smtpAuth = Boolean.parseBoolean
                        (properties.getProperty("spring.mail.properties.mail.smtp.auth"));
            }
            String strSmtpStarttlsEnable = properties.getProperty("spring.mail.properties.mail.smtp.starttls.enable");
            if (strSmtpStarttlsEnable != null && !strSmtpStarttlsEnable.isEmpty()){
                smtpStarttlsEnable = Boolean.parseBoolean
                        (properties.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
            }
            if (host != null && port != -1 && userName != null && password != null) {
                isConfigured = true;
            }
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
        if (isConfigured){
            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setUsername(userName);
            mailSender.setPassword(password);
            userEmail = userName;
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", smtpAuth);
            props.put("mail.smtp.starttls.enable", smtpStarttlsEnable);
        }
        return mailSender;
    }

    /**
     * Send a test email
     */
    public boolean sendTestEmail() {
        return sendEmail(userEmail, "Test email", "This is a test email");
    }

    /**
     * Send an email
     * @param to Email recipient
     * @param subject Email subject
     * @param text Email text
     */
    public boolean sendEmail(String to, String subject, String text) {
        if (!isConfigured) {
            return false;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(userEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            //This needs to be run on a new thread as the application freezes otherwise
            Thread thread = new Thread(() -> javaMailSender.send(message));
            thread.start();
        } catch (Exception e) {
            System.out.println("Error sending email");
            return false;
        }
        return true;
    }

    /**
     * Check if the email handler is configured
     * @return True if the email handler is configured, false otherwise
     */
    public boolean isConfigured() {
        return isConfigured;
    }
}