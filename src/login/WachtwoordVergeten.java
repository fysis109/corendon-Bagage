package login;

import global.Encrypt;
import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class WachtwoordVergeten {

    //mysql connectie
    Mysql mysql = new Mysql();

    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();

    //Dit is om in te loggen op de mail
    public static final String EMAIL_USER_NAME = "is1092016";
    public static final String EMAIL_PASSWORD = "Pulsar11";

    public void start(Stage primaryStage) {

        //import java class
        Login login = new Login();

        //grind
        primaryStage.setTitle("Wachtwoord vergeten");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Titel    
        Label scenetitle = new Label("Forgot your password");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        grid.add(scenetitle, 0, 0, 2, 1);

        Text info = new Text("Insert your email and username and you will\nreceive an email with a new password");
        info.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        grid.add(info, 0, 1, 2, 1);

        //email text
        Label eMailAdress = new Label("E-mail address:");
        grid.add(eMailAdress, 0, 2);

        //Text veld na email
        TextField rawEMailTextField = new TextField();
        grid.add(rawEMailTextField, 1, 2);

        //Password: text
        Label rawGebruikersNaam = new Label("Username:");
        grid.add(rawGebruikersNaam, 0, 3);

        //Text veld na Username
        TextField rawUsername = new TextField();
        grid.add(rawUsername, 1, 3);

        //button wahtwoord vergeten
        Button buttonWachtwoordVergeten = new Button("Forgot password");
        buttonWachtwoordVergeten.setPrefWidth(140);
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(buttonWachtwoordVergeten);
        grid.add(bwvbox, 1, 4);

        //button terug naar home
        Button terugNaarHome = new Button("Back to login");
        terugNaarHome.setPrefWidth(140);
        HBox bwvbox1 = new HBox(10);
        bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox1.getChildren().add(terugNaarHome);
        grid.add(bwvbox1, 1, 5);

        //Text die in beeld komt als niet alle velden zijn ingevuld
        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 6);

        //button wachtwoord vergeten actie
        buttonWachtwoordVergeten.setOnAction((ActionEvent e) -> {
            if (rawEMailTextField.getText().trim().isEmpty()
                    || rawEMailTextField.getText().trim().isEmpty()) {
                actiontarget.setText("Email address and/or username \ncan't be left open");
            } else {
                String username = rawUsername.getText();
                String emailadress = rawEMailTextField.getText();
                Connection conn;
                try {
                    //maak connectie met de database
                    conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                    String query = "SELECT * FROM users WHERE username LIKE '" + username + "'";
                    Statement st = conn.createStatement();
                    ResultSet databaseResponse = st.executeQuery(query);

                    while (databaseResponse.next()) {
                        String DBEmailadress = databaseResponse.getString("email");
                        String customerId = databaseResponse.getString("userID");
                        //kijk of mail adres het zelfde is als in het database staat
                        if (emailadress.equals(DBEmailadress)) {
                            actiontarget.setText("If you entered the right\nusername and email an email\nhas been sent.");
                            //creat nieuw cijfer matig wachtwoord.
                            String randomGetal = "";
                            for (int i = 0; i < 8; i++) {
                                randomGetal += String.valueOf((int) (Math.random() * ((10 - 1) + 1)));
                            }
                            Encrypt encrypt = new Encrypt();
                            //voer het nieuwe wachtwoord in, in de database
                            String updateQuary;
                            try {
                                updateQuary = "UPDATE users SET wachtwoord='" + encrypt.createHash(String.valueOf(randomGetal)) + "' WHERE userID='" + customerId + "'";
                                st.executeUpdate(updateQuary);
                            } catch (Encrypt.CannotPerformOperationException ex) {
                            }
                            String RECIPIENT = emailadress;
                            String from = EMAIL_USER_NAME;
                            String pass = EMAIL_PASSWORD;
                            String[] to = {RECIPIENT};
                            String subject = "Corendon password reset.";
                            String body = "Hello, here is your new password: " + randomGetal;
                            //send mail
                            sendEmail(from, pass, emailadress, subject, body, null);
                        } else {
                            actiontarget.setText("If you entered the right\nusername and email an email\nhas been sent.");
                        }
                    }
                } catch (SQLException ed) {
                    System.err.println(ed);
                }
            }
        });
        
        terugNaarHome.setOnAction((ActionEvent e) -> {
           login.start(primaryStage); 
        });

        Scene scene = new Scene(grid, 1200, 920);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    
    public void sendEmail(String username, String password, String to, String subject, String body, String filePath){
    

    Properties props = new Properties();
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

    try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        
        if(filePath != null){
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            Multipart multipart = new MimeMultipart();

            messageBodyPart = new MimeBodyPart();
            String fileName = "DHL form.pdf";
           
            
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
        }
        Transport.send(message);
        

    } catch (MessagingException e) {
        e.printStackTrace();
    }
  }
    }
    
    

