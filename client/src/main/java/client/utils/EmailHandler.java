package client.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailHandler {
    //TODO: Handle properties not found
    private JavaMailSender javaMailSender;
    private String userEmail;
    private boolean isConfigured;
    private String host;
    private int port;
    private String userName;
    private String password;
    private boolean smtpAuth;
    private boolean smtpStarttlsEnable;
    private ConfigUtils configUtils;

    /**
     * Constructor
     */
    public EmailHandler() {
        isConfigured = false;
        configUtils = new ConfigUtils();
        this.javaMailSender = createJavaMailSender();
    }

    /**
     * Constructor with a javaMailSender object
     */
    public EmailHandler(JavaMailSender javaMailSender) {
        isConfigured = false;
        this.javaMailSender = javaMailSender;
    }

    /**
     * Create a JavaMailSender object
     * @return JavaMailSender object
     */
    private JavaMailSender createJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties properties = configUtils.easyLoadProperties();
        readProperties(properties);
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

    public void readProperties(Properties properties) {
        host = properties.getProperty("spring.mail.host");
        String strPort = properties.getProperty("spring.mail.port");
        if (strPort != null && !strPort.isEmpty()){
            try{
                port = Integer.parseInt(properties.getProperty("spring.mail.port"));
            }catch (NumberFormatException e) {
                isConfigured = false;
                return;
            }
        }
        userName = properties.getProperty("spring.mail.username");
        userEmail = userName;
        password = properties.getProperty("spring.mail.password");
        String strSmtpAuth = properties.getProperty("spring.mail.properties.mail.smtp.auth");
        if (strSmtpAuth != null && !strSmtpAuth.isEmpty()){
            smtpAuth = strSmtpAuth.equals("true");
        }
        String strSmtpStarttlsEnable = properties
                .getProperty("spring.mail.properties.mail.smtp.starttls.enable");
        if (strSmtpStarttlsEnable != null && !strSmtpStarttlsEnable.isEmpty()){
            smtpStarttlsEnable = strSmtpStarttlsEnable.equals("true");
        }
        if (host != null && !host.isEmpty() && port != -1 && userName != null
                && !userName.isEmpty() && password != null && !password.isEmpty()){
            isConfigured = true;
        }
    }
    /**
     * Send a test email
     * @return True if the email was sent successfully, false otherwise
     */
    public boolean sendTestEmail() {
        return sendEmail(userEmail, "Test email", "This is a test email");
    }
    /**
     * Send an email
     * @param to Email recipient
     * @param subject Email subject
     * @param text Email text
     * @return True if the email was sent successfully, false otherwise
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
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Error sending email");
            return false;
        }
    }
    /**
     * Check if the email handler is configured
     * @return True if the email handler is configured, false otherwise
     */
    public boolean isConfigured() {
        return isConfigured;
    }
}