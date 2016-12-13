package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.*;
import javafx.scene.paint.Color;
import javax.mail.*;
import javax.mail.internet.*;

public class WachtwoordVergeten {
    
    //mysql connectie
    Mysql mysql = new Mysql();
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    
    //Dit is voor de email reset
    private static final String EMAIL_USER_NAME = "is1092016";
    private static final String EMAIL_PASSWORD = "Pulsar11";
    
    public void start(Stage primaryStage) {

    //import java class
    Login Login = new Login();
    
    //grind
    primaryStage.setTitle("Wachtwoord vergeten");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));
    
    //Titel    
    Label scenetitle = new Label("Wachtwoord vergeten.");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
    scenetitle.setAlignment(Pos.BOTTOM_CENTER);
    grid.add(scenetitle, 0, 0);
  
    //Username text
    Label eMailAdress = new Label("E-mail adres:");
    grid.add(eMailAdress, 0, 1);

    //Text veld na Username
    TextField rawEMailTextField = new TextField();
    grid.add(rawEMailTextField, 1, 1);

    //Password: text
    Label rawGebruikersNaam = new Label("username:");
    grid.add(rawGebruikersNaam, 0, 2);

    //Text veld na Username
    TextField rawUsername = new TextField();
    grid.add(rawUsername, 1, 2);
    
    //button wahtwoord vergeten
    Button buttonWachtwoordVergeten = new Button("Forgot password");
    HBox bwvbox = new HBox(10);
    bwvbox.setAlignment(Pos.BOTTOM_CENTER);
    bwvbox.getChildren().add(buttonWachtwoordVergeten);
    grid.add(bwvbox, 0, 4);
    
    //button event maken
    final Text actiontarget = new Text();
    actiontarget.setFill(Color.FIREBRICK);
    grid.add(actiontarget, 1, 5);
    
    //button wachtwoord vergeten actie
    buttonWachtwoordVergeten.setOnAction(new EventHandler<ActionEvent>() {
        private String[] test;
        @Override
        public void handle(ActionEvent e) {
            
            if(rawUsername.getText() == null || rawEMailTextField.getText().trim().isEmpty() || rawUsername.getText() == null || rawEMailTextField.getText().trim().isEmpty()){
                    actiontarget.setText("email address and/or username \ncan't be left open");
                } else {
                    
                    //haal data uit text field
                    String username = rawUsername.getText();
                    String emailadress = rawEMailTextField.getText();
                    System.out.println("geladen");
                    Connection conn;
                    try {
                        
                        //maak connectie met het database
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        
                        //SQL query
                        String query = "SELECT * FROM users WHERE username LIKE '"+username+"'";
                        System.out.println(query);

                        // create the java statement
                        Statement st = conn.createStatement();
                        // execute the query, and get a java resultset
                        ResultSet databaseResponse = st.executeQuery(query);

                        
                        while (databaseResponse.next())
                        {   
                            //database response verwerken
                            String DBEmailadress = databaseResponse.getString("email");
                            String customerId = databaseResponse.getString("userID");
                            
                            //kijk of mail adres het zelfde is als in het database staat
                            if(emailadress.equals(DBEmailadress)){
                                
                                //creat nieuw cijfer matig wachtwoord.
                                String randomGetal = "";
                                for(int i = 0; i < 8; i++){
                                    randomGetal += String.valueOf((int) (Math.random()* ((10-1)+1)));
                                }
                                
                                //update quary
                                String updateQuary = "UPDATE users SET wachtwoord='"+randomGetal+"' WHERE userID='"+customerId+"'";
                                st.executeUpdate(updateQuary);
                                
                                String RECIPIENT = emailadress;
                                
                                String from = EMAIL_USER_NAME;
                                String pass = EMAIL_PASSWORD;
                                String[] to = { RECIPIENT };
                                String subject = "Corendon wachtwoord reset.";
                                String body = "Welkom. Hier bij is een nieuwe wachtwoord. Wachtwoord is:"+randomGetal;

                                //send mail
                                sendFromGMail(from, pass, to, subject, body);
                                
                                //
                                actiontarget.setText("We send you the mail if username and email is correct.");
                            } else {
                                actiontarget.setText("We send you the mail if username and email is correct.");
                            }
                        }
                    } catch (SQLException ed) {
                        System.err.println(ed);
                    }
                }
            //Login.start(primaryStage);*/
        }
    });
    
    //show interface
    Scene scene = new Scene(grid, 1200, 920);
    primaryStage.setScene(scene);
    primaryStage.show();
    }
    
    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        /*catch (AddressException ae) {
            ae.printStackTrace();
        }*/
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}