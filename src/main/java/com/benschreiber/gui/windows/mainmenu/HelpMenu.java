package com.benschreiber.gui.windows.mainmenu;

import com.benschreiber.gui.FXController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class HelpMenu extends FXController implements Initializable {

    @FXML
    TextField userEmail, emailHeader;

    @FXML
    TextArea supportMessage;

    @FXML
    Label requestSentLabel;

    private static final String EZQUIZ_EMAIL = "ezquiz.help@gmail.com";
    private static final String EZQUIZ_PASS = "ezquiz123";

    public void sendEmailClicked() {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EZQUIZ_EMAIL, EZQUIZ_PASS);
            }
        });

        Message message = prepareMessage(session);
        if (message != null) {

            try {
                Transport.send(message);
                requestSentLabel.setText("Support successfully requested.");

            } catch (MessagingException e) {
                requestSentLabel.setText("Could not send email.");
                e.printStackTrace();
            }

        } else {
            requestSentLabel.setText("Could not send email.");
        }

    }

    private Message prepareMessage(Session session) {
        Message message  = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(EZQUIZ_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail.getText()));
            message.setSubject(emailHeader.getText());
            message.setText(userEmail + "\n" + supportMessage.getText());
            return message;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userEmail.setFocusTraversable(false);
        emailHeader.setFocusTraversable(false);
        supportMessage.setFocusTraversable(false);

    }
}
