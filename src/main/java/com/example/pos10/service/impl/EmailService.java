package com.example.pos10.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("密碼重設驗證碼");
        message.setText("您的驗證碼為：" + code);
        message.setFrom("密碼重置信件<ru8ru812345@gmail.com>");  // 確保郵件的發件人
        mailSender.send(message);
    }
}

