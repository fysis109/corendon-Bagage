package login;

import global.Mysql;
import global.Encrypt;
import global.Home;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login extends Application {

    
    
    
    
    //Aanmaken van objecten
    Home home = new Home();
    WachtwoordVergeten wachtwoordVergeten = new WachtwoordVergeten();

    //mysql connectie
    Mysql mysql = new Mysql();

    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    //gebruikersrol die wordt geset op het moment van inloggen
    public static String rol;
    public static String userName;
    public static String voornaam;
    public static String achternaam;
    public static String email;

    @Override
    public void start(Stage primaryStage) {
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Plaatje linksonder
        Image logo = new Image("file:src/images/corendon_logo.png");
        ImageView imgpic = new ImageView();
        imgpic.setImage(logo);
        imgpic.setFitHeight(90);
        imgpic.setFitWidth(250);
        grid.add(imgpic, 0, 6);

        //Welkom + Letter type
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        //Username text
        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);

        //Text veld na Username
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        //Password text
        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        //text veld na password
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        //De Sign in button
        Button btn = new Button("Sign in");
        btn.setPrefWidth(150);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 3);

        //Registreert ook een enter
        btn.setDefaultButton(true);

        //Text die een foutmelding geeft
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 6);

        //wachtwoord vergeten
        Button buttonWachtwoordVergeten = new Button("Forgot password");
        buttonWachtwoordVergeten.setPrefWidth(150);
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(buttonWachtwoordVergeten);
        grid.add(bwvbox, 1, 4);

        //button ActionEvent wachtwoord vergeten
        buttonWachtwoordVergeten.setOnAction((ActionEvent e) -> {
            wachtwoordVergeten.start(primaryStage);
        });
        
        Encrypt a = new Encrypt();
        try{
        System.out.println("Hash voor wachtwoord 1: "+a.createHash("1"));
        }catch(Encrypt.CannotPerformOperationException ex){
            
        }
        //button ActionEvent inloggen
        btn.setOnAction((ActionEvent e) -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            //kijkt of allebei de velden zijn ingevuld
            if (pwBox.getText().trim().isEmpty() || userTextField.getText().trim().isEmpty()) {
                actiontarget.setText("Password and/or username \ncan't be left open");
            } else {
                Connection conn;
                try {
                    conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                    Statement stmt = (Statement) conn.createStatement();
                    //kijkt of er wel een user bestaat met deze username
                    ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) AS total FROM users WHERE username = '" + username + "'");
                    int count = 0;
                    while (rs1.next()) {
                        count = rs1.getInt("total");
                    }
                    //zoja vraag wachtwoord en de rol op
                    if (count > 0) {
                        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
                        while (rs.next()) {
                            String pass = rs.getString("wachtwoord");
                            rol = rs.getString("rol");
                            userName = rs.getString("username");
                            voornaam = rs.getString("voornaam");
                            achternaam = rs.getString("achternaam");
                            email = rs.getString("email");
                            
                            Encrypt encrypt = new Encrypt();
                            if (encrypt.verifyPassword(password, pass)) {
                                home.start(primaryStage);
                            } else {
                                actiontarget.setText("Wrong password or username try again!");
                            }
                        }
                    } else {
                        actiontarget.setText("Wrong password or username try again!");
                    }
                } catch (SQLException ed) {
                    System.err.println(ed);
                } 
                //2 catches voor het encrypten
                catch (Encrypt.CannotPerformOperationException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Encrypt.InvalidHashException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        primaryStage.setTitle("Corendon Bagage");
        Scene scene = new Scene(grid, 1200, 920);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("global/Style2.css");
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}
