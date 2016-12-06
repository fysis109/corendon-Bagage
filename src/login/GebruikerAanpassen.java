/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    //test
    public void star(Stage primaryStage) {
        
         // deze vijf regels om een homeknop aan te roepen
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        primaryStage.setTitle("Gebruiker aanpassen");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        root.setCenter(grid);
        //Plaatje linksonder
        Image logo = new Image("file:src/images/corendon_logo.jpg");
        ImageView imgpic = new ImageView();
        imgpic.setImage(logo);
        imgpic.setFitHeight(50);
        imgpic.setFitWidth(150);
        grid.add(imgpic, 0, 6);

        //Welkom + Letter type
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        int rij = 1;
        //Username text
        Label userName = new Label("Username:");
        grid.add(userName, 0, rij);

        //Text veld na Username
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, rij++);

        //newUsername text
        Label userName2 = new Label("New username:");
        grid.add(userName2, 0, rij);

        //Text veld na newUsername
        TextField userTextField2 = new TextField();
        grid.add(userTextField2, 1, rij++);

        //Password: text
        Label pw = new Label("Password:");
        grid.add(pw, 0, rij);

        //text veld na password + bullets
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, rij++);

        //newPassword: text
        Label pw2 = new Label("New password:");
        grid.add(pw2, 0, rij);

        //text veld na newpassword + bullets
        PasswordField pwBox2 = new PasswordField();
        grid.add(pwBox2, 1, rij++);

        //Rol: text
        Label rol = new Label("User role:");
        grid.add(rol, 0, rij);

        //text veld na Rol + bullets
        TextField rolnaam = new TextField();
        grid.add(rolnaam, 1, rij++);

        //Rolnew: text
        Label rol2 = new Label("User role:");
        grid.add(rol2, 0, rij);

        //text veld na rolnew + bullets
        TextField rolnaam2 = new TextField();
        grid.add(rolnaam2, 1, rij++);

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
                String username = userTextField.getText();
                String password = pwBox.getText();
                String gebruikersRol = rolnaam.getText();
                String username2 = userTextField2.getText();
                String password2 = pwBox2.getText();
                String gebruikersRol2 = rolnaam2.getText();
                if (pwBox.getText().trim().isEmpty() || pwBox2.getText().trim().isEmpty() || userTextField.getText().trim().isEmpty()) {
                    actiontarget.setText("Fields can't be left open");
                } else {
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
                                if (pass.equals(password)) {
                                    actiontarget.setText("");
                                    System.out.println("Gebruiker bestaat en kan aangepast worden");
                                    Statement stmt = (Statement) conn.createStatement();
                                    ResultSet rs1 = stmt.executeQuery("SELECT userID FROM users WHERE username = '" + username + "'");
                                    int userID = 0;
                                    while (rs1.next()) {
                                        userID = rs1.getInt("userID");
                                    }

                                    String sql = "UPDATE users SET username=?, wachtwoord=?, rol=? WHERE userID=?";
                                    PreparedStatement statement = conn.prepareStatement(sql);
                                    statement.setString(1, username2);
                                    statement.setString(2, password2);
                                    statement.setString(3, gebruikersRol2);
                                    statement.setInt(4, userID);
                                    int rowsUpdated = statement.executeUpdate();
                                    if (rowsUpdated > 0) {
                                        System.out.println("An existing user was updated successfully!");
                                    }

                                } else {
                                    actiontarget.setText("Wrong password or uername try again!");
                                    System.out.println("Gebruiker bestaat niet");
                                }
                            }
                        } else {
                            actiontarget.setText("Wrong password or username try again!");
                        }

                    } catch (SQLException ed) {
                        System.err.println(ed);
                    }

                }
            }
        });

        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
    }

}
