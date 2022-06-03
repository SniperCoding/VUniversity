package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MailUtil {

    // 注入spring发送邮件的类
    @Autowired
    private JavaMailSender mailSender;

    // 获取你的用户名
    @Value("${spring.mail.username}")
    private String from;


    /**
     * 发送HTML邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendHtmlMail(String to, String subject, String content) throws Exception {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true表示构建一个可以带附件的邮件对象
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);  //  true表示支持 Html
            mailSender.send(message);
    }

    /**
     * 发送验证码到指定邮箱中
     *
     * @param to 收件人地址
     * @return 6位邮箱验证码
     */
    public String sendHtmlMail(String to) throws Exception {
        String code = randomString();
        String content = "您的注册验证码为：" + code + ",五分钟内有效，请勿泄漏！";
        sendHtmlMail(to, "VUniversity邮箱验证码", content);
        return code;
    }

    /**
     * 生成6位随机字符串
     *
     * @return
     */
    private static String randomString() {
        String result = "";
        for (int i = 0; i < 6; i++) {
            int intVal = (int) (Math.random() * 26 + 97);
            result = result + (char) intVal;
        }
        return result;
    }

    /**
     * 判断邮箱是否合法
     *
     * @param mail 待验证的邮箱
     * @return
     */
    public boolean isEmail(String mail) {
        if (mail == null) {
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(mail);
        boolean result = m.matches() ? true : false;
        return result;
    }
}
