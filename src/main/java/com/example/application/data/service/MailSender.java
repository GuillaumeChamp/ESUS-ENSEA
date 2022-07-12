package com.example.application.data.service;

import com.example.application.data.entity.Request;
import com.example.application.data.entity.User;
import com.example.application.data.generator.TextConverter;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@SuppressWarnings("SpellCheckingInspection")
public class MailSender {

    private static final BasicTextEncryptor encryptor= new BasicTextEncryptor();
    private static String APP_ADRESS;
    private static Session session;
    private static String aurion;
    private static String etude;
    private static String moodle;
    private static String cas;
    private static String dev;
    private static String ri;
    private static String fip;

    /**
     * Create the session
     */
    private static void init(){
        String password;
        String tls;
        String host;
        String port;
        String authState;
        try {
            Properties log = new Properties();
            try{
                log.load(new FileInputStream("./drive/resources/mailconf.properties"));
            }catch (FileNotFoundException e){
                log.load(MailSender.class.getResourceAsStream("/META-INF/resources/mailconf.properties"));
            }
            encryptor.setPassword(log.getProperty("pass"));
            APP_ADRESS = log.getProperty("mail");
            aurion = log.getProperty("aurion");
            etude = log.getProperty("etude");
            dev = log.getProperty("dev");
            moodle = log.getProperty("moodle");
            ri = log.getProperty("ri");
            cas = log.getProperty("cas");
            fip = log.getProperty("fip");
            host = log.getProperty("host","smtp.office365.com");
            port = log.getProperty("port","587");
            authState = log.getProperty("auth","true");
            tls = log.getProperty("starttls","true");
            String encodedpass = log.getProperty("encoded");
            password =  encryptor.decrypt(
                    encodedpass
            );
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port",port);
        properties.put("mail.smtp.auth",authState);
        properties.put("mail.smtp.starttls.enable",tls);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(APP_ADRESS,password);
            }
        };
        session = Session.getInstance(properties,auth);
    }

    /**
     * Send a mail with a pattern
     * @param user the one who generate the request
     */
    public static void TestMail(User user){
        if(session==null) init();
        String to;
        String subject;
        String message = "Message de l'Application Esus \n --------------------------------------------------------------\n";
        to = dev;
        subject = "testmail";
        message = message.concat("\nEssaie de l'envoie automatique par "+ user.getUsername());
        message = message + "\n --------------------------------------------------------------\n";
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(APP_ADRESS));
            mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(to)));
            mail.setSubject(subject);
            mail.setText(message);

            Transport.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void accountCreation(String user,String password,String email){
        if(session==null) init();
        String message = TextConverter.ConvertFile("mail/welcome.txt");
        if (message.contains("USER")) message = message.replace("USER",user);
        else message = message.concat("user : " + user);
        if (message.contains("PASSWORD")) message = message.replace("PASSWORD",password);
        else message = message.concat("password : " + password);
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(APP_ADRESS));
            mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mail.setSubject("Welcome at ENSEA");
            mail.setText(message);
            Transport.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void recover(String email,String password){
        if(session==null) init();
        String message = "Your password have been set to \""+password+"\"\n Log you and change it fast.";
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(APP_ADRESS));
            mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mail.setSubject("Recover Password");
            mail.setText(message);
            Transport.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void InformRequestSucess(Request request){
        if(session==null) init();

        String content = TextConverter.ConvertFile("mail/requestAccepted.txt");

        String email = request.getStudent().getEmail();
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(APP_ADRESS));
            mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mail.setSubject("You are no longer locked");
            mail.setText(content);
            Transport.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void InformRequestFailure(Request request,String motif){
        if(session==null) init();
        String content = TextConverter.ConvertFile("mail/requestRejected.txt");
        if (content.contains("MOTIF")) content = content.replace("MOTIF",motif);
        else content = content.concat(motif);
        String email = request.getStudent().getEmail();
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(APP_ADRESS));
            mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mail.setSubject("We need more information about your acceptance");
            mail.setText(content);
            Transport.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendAnswer(User student,String question,String answerValue){
        if(session==null) init();
        String content = "The student "+ student.getStudent().getFirstName()+ " "+student.getStudent().getLastName().toUpperCase()+" answer to the question :" +question
                +"\nThe answer is : " + answerValue;
        String email = dev;
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(APP_ADRESS));
            mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mail.setSubject(question);
            mail.setText(content);
            Transport.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a mail with a file to the service
     * @param service who receive
     * @param file attached file
     */
    public static void sendService(String service,File file){
        if(session==null) init();
        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(APP_ADRESS));
            switch (service){
                case "aurion" :
                    mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(aurion)));
                    mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(cas)));
                    break;
                case "moodle" :
                    mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(moodle)));
                    break;
                case "etude" :
                    mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(etude)));
                    break;
                case "fip" :
                    mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(fip)));
            }
            mail.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(ri)));
            mail.setSubject("Nouveaux entrants");

            MimeBodyPart part = new MimeBodyPart();
            part.setText(TextConverter.ConvertFile("mail/"+service+".txt"));
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(part);
            DataSource source = new FileDataSource(file.getPath());
            part = new MimeBodyPart();
            part.setFileName(file.getName());
            part.setDataHandler(new DataHandler(source));

            multipart.addBodyPart(part);

            mail.setContent(multipart);
            Transport.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
