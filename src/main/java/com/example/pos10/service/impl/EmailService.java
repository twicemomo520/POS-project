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
        message.setFrom("密碼重置信件<ru8ru812345@gmail.com>"); 
        mailSender.send(message);
    }
    
    // 訂位確認信
    public void sendReservationConfirmationEmail(String to, String customerName, String reservationDate, String reservationTime, int people) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("訂位建立成功");

        String text = "訂位建立成功\n\n" +
                      customerName + "您好，\n\n" +
                      "提醒您已成功建立一筆即將到來的訂位。\n\n" +
                      "訂位保留時間為 10 分鐘，遲到即取消，還請留意時間！\n" +
                      "如需取消，請致電店家。\n\n" +
                      "訂位日期: " + reservationDate + "\n" +
                      "訂位時間: " + reservationTime + "\n" +
                      "訂位人數: " + people + " 人\n\n" +
                      "感謝您的光臨！";

        message.setText(text);
        message.setFrom("訂位確認<com2100927@gmail.com>");  // 替換為你的發件人電子郵件
        mailSender.send(message);
    }
    
    // 訂位提醒信
    public void sendReminderEmail(String to, String customerName, String reservationDate, String reservationTime, int people) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("訂位提醒");

        String text = "訂位提醒\n\n" +
                      customerName + "您好，\n\n" +
                      "提醒您的訂位即將到來！\n\n" +
                      "訂位日期: " + reservationDate + "\n" +
                      "訂位時間: " + reservationTime + "\n" +
                      "訂位人數: " + people + " 人\n\n" +
                      "期待您的到來！";

        message.setText(text);
        message.setFrom("訂位提醒<com2100927@gmail.com>");
        mailSender.send(message);
    }
}


