package com.tibame.peterparker.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class OrderMailService {

    // 設定傳送郵件:至收信人的Email信箱,Email主旨,Email內容
    public void sendMail(String to, String mailText) {
        try {
            // 設定使用SSL連線至 Gmail smtp Server
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            final String myGmail = "bliu69108@gmail.com";
            final String myGmail_password = "ncom exxb bbln oybb";
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myGmail, myGmail_password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myGmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // 設定信中的主旨
            String mailTitle = mailTitle();
            message.setSubject(mailTitle);
            // 設定信中的內容
            message.setText(mailText);

            Transport.send(message);
            System.out.println("郵件傳送成功!");
        } catch (MessagingException e) {
            System.out.println("郵件傳送失敗!");
            e.printStackTrace();
        }
    }

    private String mailTitle() {
        String mailTitle = "[PeterParker] 訂單完成通知";
        return mailTitle;
    }




}
