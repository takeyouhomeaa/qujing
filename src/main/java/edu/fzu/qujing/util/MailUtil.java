package edu.fzu.qujing.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Component
public class MailUtil {

    private static final String myEmailSMTPHost = "smtp.163.com";
    private static final String myEmailAccount = "18030389136@163.com";
    private static final String myEmailPassword = "HPGRPDOQKZAMRLLC";
    private static Properties properties;
    private static String receiveMailAccount;

    private static void init() {
        properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", myEmailSMTPHost);
        properties.setProperty("mail.smtp.auth", "true");
    }


    public static void sendToNoSSL(String receiveMailAccount, Integer id, String username,String encode) {
        init();
        Session session = Session.getDefaultInstance(properties);
        System.out.println(id);
        try {
            Transport transport = session.getTransport();
            String body = new MailUtil().getMailText(username, id,encode);
            MimeMessage mimeMessage = createMimeMessage(session, myEmailAccount, receiveMailAccount, body);
            transport.connect(myEmailAccount, myEmailPassword);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String body) {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myEmailAccount, "取经", "UTF-8"));
            message.setRecipient(MimeMessage.RecipientType.TO,
                    new InternetAddress(receiveMail, "取经用户", "UTF-8"));
            message.setSubject("激活您的帐号", "UTF-8");
            message.setContent(body, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return message;
    }

    private String getMailText(String username, Integer id,String encode) {
        StringBuilder mailText = new StringBuilder();
        mailText.append("<p>Email 地址验证");
        mailText.append(username);
        mailText.append("</p><br/>");
        mailText.append("<p>这封信是由 取经 发送的。<br/>" +
                "您收到这封邮件，是由于在 取经 进行了新用户注册，或用户修改 Email 使用 了这个邮箱地址。" +
                "如果您并没有访问过 取经，或没有进行上述操作，请忽 略这封邮件。您不需要退订或进行其他进一步的操作。<br/><br/>" +
                "----------------------------------------------------------------------<br/>" +
                "帐号激活说明<br/>" +
                "----------------------------------------------------------------------<p><br/>");
        mailText.append("验证码:");
        mailText.append(encode);
        mailText.append("<p>(请不要泄露上方的验证码,有效期5分钟)<br/>" +
                "感谢您的访问，祝您使用愉快！<br/>" +
                "此致</br>" +
                "取经管理 管理团队.<br/>");
        return mailText.toString();

    }

}
