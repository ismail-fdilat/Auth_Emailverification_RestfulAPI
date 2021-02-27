package com.user.registration.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {


    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;


    @Override
    @Async
    public void send(String to,String email) {
           try {
               MimeMessage mimeMessage = javaMailSender.createMimeMessage();
               MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
               messageHelper.setText(email,true);
               messageHelper.setTo(to);
               messageHelper.setSubject("confirmation of the email");
               messageHelper.setFrom("isfdilat@gmail.com");
               javaMailSender.send(mimeMessage);
}catch(MessagingException e){
    logger.error("failed to send email",e);
    throw new IllegalStateException ("failed to send email");

}
    }
}
