/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

/**
 *
 * @author mikebuhrs
 */
public class Login extends Application {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "lol123";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/wsdatabase?autoReconnect=true&useSSL=false";

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Corendon Bagage");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

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

        //Username text
        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);

        //Text veld na Username
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        //Password: text
        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        //text veld na password + bullets
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);  

        //De Sign in button
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        //button event maken
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 6);

        //Enter ook
        btn.setDefaultButton(true);

        //Button event text
        btn.setOnAction((ActionEvent e) -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            if(pwBox.getText() == null || pwBox.getText().trim().isEmpty() || userTextField.getText() == null || userTextField.getText().trim().isEmpty()){
                actiontarget.setText("Password and/or username \ncan't be left open");
            }
            else{
            
            System.out.print(username + "\n" + password);
            Connection conn;
            try {
                conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                System.out.println("Connected!");
                Statement stmt = (Statement) conn.createStatement();
                username = "'" + username + "'";
               
                ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) AS total FROM users WHERE username = " + username);
                int count = 0;
                while(rs1.next()){
                    count = rs1.getInt("total");
                }
                System.out.println(count);
                ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = " + username);
                if(count > 0){
                    while (rs.next()) {
                        String pass = rs.getString("wachtwoord");
                        System.out.print(pass);
                        System.out.println(username);
                        if (pass.equals(password)) {
                            System.out.println("Je bent ingelogd!");
                            actiontarget.setText("");
                        } else {
                            actiontarget.setText("Wrong password or uername try again!");
                            System.out.println("Je bent niet inglogd");
                        }
                    }    
                }else{
                    actiontarget.setText("Wrong password or uername try again!");
                }
                

            } catch (SQLException ed) {
                System.err.println(ed);
            }
            
            }
        });

        Scene scene = new Scene(grid, 360, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
