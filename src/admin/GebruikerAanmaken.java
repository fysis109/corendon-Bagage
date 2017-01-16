/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import java.sql.Connection;
import java.sql.DriverManager;
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
import global.Encrypt;
import global.MenuB;
import global.Mysql;

/**
 *
 * @author Joljin Verwest 
 */
public class GebruikerAanmaken {
    
    Mysql mysql = new Mysql();
     
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    String gebruikersRol;
    
    
    //test
    public void start(Stage primaryStage) {
        
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);
        
        primaryStage.setTitle("Gebruiker aanmaken");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        root.setCenter(grid);
        
        //Welkom + Letter type
        Text scenetitle = new Text("Make a new user");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        
        
        int rij = 1;
        //Username text
        Label userName = new Label("Username:");
        grid.add(userName, 0, rij);

        //Text veld na Username
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, rij++);
        
        //Username text
        Label userName2 = new Label("Username:");
        grid.add(userName2, 0, rij);

        //Text veld na Username
        TextField userTextField2 = new TextField();
        grid.add(userTextField2, 1, rij++);

        //Password: text
        Label pw = new Label("Password:");
        grid.add(pw, 0, rij);

        //text veld na password + bullets
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, rij++);  
        
        //Password: text
        Label pw2 = new Label("Password:");
        grid.add(pw2, 0, rij);

        //text veld na password + bullets
        PasswordField pwBox2 = new PasswordField();
        grid.add(pwBox2, 1, rij++);
        
         //Password: text
        Label mail = new Label("Emailadres:");
        grid.add(mail, 0, rij);

        //text veld na password + bullets
        TextField mailbox = new TextField();
        grid.add(mailbox, 1, rij++);
        
         //Password: text
        Label mail2 = new Label("Emailadres:");
        grid.add(mail2, 0, rij);

        //text veld na password + bullets
        TextField mailbox2= new TextField();
        grid.add(mailbox2, 1, rij++);
        
        Label rol = new Label("Rol");
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
        //De Sign in 
        Button btn = new Button("Make user");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, rij++);

        //button event maken
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, rij++);

        //Plaatje linksonder
        Image logo = new Image("file:src/images/corendon_logo.jpg");
        ImageView imgpic = new ImageView(logo);
        imgpic.setImage(logo);
        imgpic.setFitHeight(50);
        imgpic.setFitWidth(150);
        grid.add(imgpic, 1, rij++ , 1 , 1);
        
        Button btn5 = new Button();
        btn5.setGraphic(new ImageView(logo));
        
        //Enter ook
        btn.setDefaultButton(true);

        //Button event text
        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println(gebruikersRol);
                String username = userTextField.getText();
                String password = pwBox.getText();
                String username2 = userTextField2.getText();
                String password2 = pwBox2.getText();
                String mail = mailbox.getText();
                String mail2 = mailbox2.getText();
                if(pwBox.getText().trim().isEmpty() || pwBox2.getText().trim().isEmpty() ||
                   userTextField.getText().trim().isEmpty()|| userTextField2.getText().trim().isEmpty()||
                   mailbox.getText().trim().isEmpty()||mailbox2.getText().trim().isEmpty()||gebruikersRol == null){
                   actiontarget.setText("Fields can't be left open");
                } else {
                    System.out.print(username + "\n" + password);
                    actiontarget.setText("");
                    Connection conn;
                    if(!username.equals(username2) || !password.equals(password2) || !mail.equals(mail2)){
                        System.out.println("Username, password and/or mailadress are not the same");
                        actiontarget.setText("Username, password and/or\n mailadress are not the same");
                    }else{
                    
                        try {
                            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                            System.out.println("Connected!");
                            Statement stmt = (Statement) conn.createStatement();
                            
                            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) AS total FROM users WHERE username = '" + username+ "'");
                            int count = 0;
                            while(rs2.next()){
                               count = rs2.getInt("total");
                            }
                            if(count == 0){
                                Encrypt encrypt = new Encrypt();
                                try {
                                    password = encrypt.createHash(password);
                                } catch (Encrypt.CannotPerformOperationException ex) {
                                   
                                }
                                String insert="INSERT INTO users (username, wachtwoord, rol, email) VALUES('"+username+"','"+password+"','"
                                        +gebruikersRol+"','"+mail+"')";
                                stmt.execute(insert);
                                actiontarget.setFill(Color.GREEN);
                                actiontarget.setText("User has been added");
                                System.out.println("Gebruiker toegevoegd");
                            }else{
                                actiontarget.setText("Username already exists");
                                System.out.println("bestaat al");}
                        } catch (SQLException ed) {
                            System.err.println(ed);
                        }
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
    
