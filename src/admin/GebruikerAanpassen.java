/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import global.Encrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import global.MenuB;
import global.Mysql;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joljin Verwest
 */
public class GebruikerAanpassen {

    Mysql mysql = new Mysql();

    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    String gebruikersRol = null;
    //test
    public void star(Stage primaryStage, String userName, String wachtwoord, String role, String mail, String userID) {
        
         // deze vijf regels om een homeknop aan te roepen
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        primaryStage.setTitle("Adjust users");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        root.setCenter(grid);
        

        //Welkom + Letter type
        Text scenetitle = new Text("Adjust user");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 10, 1);

        int rij = 1;
        //Username text
        Label username = new Label("Username:");
        grid.add(username, 0, rij);

        //Text veld na Username
        TextField userTextField = new TextField(userName);
        grid.add(userTextField, 1, rij++);

        //Password: text
        Label pw = new Label("New password:");
        grid.add(pw, 0, rij);

        //text veld na password + bullets
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, rij++);

        //newPassword: text
        Label pw2 = new Label("Confirm new password:");
        grid.add(pw2, 0, rij);

        //text veld na newpassword + bullets
        PasswordField pwBox2 = new PasswordField();
        grid.add(pwBox2, 1, rij++);
        
        Label emailLabel = new Label("New emailaddress");
        grid.add(emailLabel, 0, rij);

        //text veld na password + bullets
        TextField emailText = new TextField(mail);
        grid.add(emailText, 1, rij++);

        //newPassword: text
        Label emailconLabel = new Label("Confirm new emailaddress:");
        grid.add(emailconLabel, 0, rij);

        //text veld na newpassword + bullets
        TextField emailconText = new TextField(mail);
        grid.add(emailconText, 1, rij++);

        Connection conn;
        
        Label rol = new Label("Rol:");
        grid.add(rol, 0, rij);
        
        final ComboBox rollen = new ComboBox();
        rollen.getItems().addAll(
        "Admin",
        "Balie",
        "Manager"
                                   );
        rollen.setPrefWidth(150);
        grid.add(rollen , 1 , rij++);
        
        rollen.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                gebruikersRol = t1;                
            }}); 
        
        
        
        //Plaatje linksonder
        Image logo = new Image("file:src/images/corendon_logo.jpg");
        ImageView imgpic = new ImageView();
        imgpic.setImage(logo);
        imgpic.setFitHeight(50);
        imgpic.setFitWidth(150);
        grid.add(imgpic, 0, 6);
        
        //De Sign in 
        Button btn = new Button("Adjust user");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, rij++);

        //button event maken
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, rij++);

        //Enter ook
        btn.setDefaultButton(true);

        //Button event text
        btn.setOnAction(new EventHandler<ActionEvent>() {
            private String[] test;

            @Override
            public void handle(ActionEvent e) {
                System.out.println(gebruikersRol);
                String username = userTextField.getText();
                String password = pwBox.getText();
                String password2 = pwBox2.getText();
                String email = emailText.getText();
                String emailcon = emailconText.getText();
                if (gebruikersRol == null )
                {
                    actiontarget.setText("Role can't be left open");
                } else {
                    Encrypt encrypt = new Encrypt();
                    try {
                        Connection conn;
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

                        Statement stmt2 = (Statement) conn.createStatement();
                        Statement stmt3 = (Statement) conn.createStatement();
                        ResultSet rs2 = stmt2.executeQuery("SELECT COUNT(*) AS total FROM users WHERE username = '" + username + "'");
                        int count = 0;
                        while (rs2.next()) {
                            count = rs2.getInt("total");
                        }
                        System.out.println(count);
                        ResultSet rs3 = stmt3.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
                        if (count > 0) {
                            while (rs3.next()) {
                                String pass = rs3.getString("wachtwoord");
                                if (password.equals(password2) && email.equals(emailcon)) {
                                    actiontarget.setText("");
                                    System.out.println("Gebruiker bestaat en kan aangepast worden");
                                    Statement stmt = (Statement) conn.createStatement();
                                    ResultSet rs1 = stmt.executeQuery("SELECT userID FROM users WHERE username = '" + username + "'");
                                    int userID = 0;
                                    while (rs1.next()) {
                                        userID = rs1.getInt("userID");
                                    }
                                    try {
                                        password2 = encrypt.createHash(password2);
                                    } catch (Encrypt.CannotPerformOperationException ex) {
                                        Logger.getLogger(GebruikerAanpassen.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String sql = "UPDATE users SET username=?, wachtwoord=?, email=?, rol=? WHERE userID=?";
                                    PreparedStatement statement = conn.prepareStatement(sql);
                                    statement.setString(1, username);
                                    statement.setString(2, password2);
                                    statement.setString(3, emailcon);
                                    statement.setString(4, gebruikersRol);
                                    statement.setInt(5, userID);
                                    
                                    int rowsUpdated = statement.executeUpdate();
                                    if (rowsUpdated > 0) {
                                        actiontarget.setText("An existing user was updated successfully!");
                                    }

                                } else {
                                    actiontarget.setText("Password or emailaddress fields \nare not the same");
                                }
                            }
                        } else {
                            actiontarget.setText("Password or emailaddress fields \nare not the same");
                        }

                    } catch (SQLException ed) {
                        Encrypt.CannotPerformOperationException ex;
                        System.err.println(ed);
                    }

                }
            }
        });

        Scene scene = new Scene(root, 1200, 920);
        scene.getStylesheets().add("global/Style2.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
    }

}
