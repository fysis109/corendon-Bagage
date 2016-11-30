package login;

import static java.awt.SystemColor.menu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class home {

    //mysql connectie
    mysql mysql = new mysql();
    
    //private mqsql
    private final String USERNAME = mysql.username();
    private final String PASSWORD = mysql.password();
    private final String CONN_STRING = mysql.urlmysql();
    
    public void start(Stage primaryStage, String rol) {
        
        //import java class
        GebruikerAanmaken gebruikerAanmaken = new GebruikerAanmaken();
        Login log = new Login();
        GebruikerAanpassen gebruikerAanpassen = new GebruikerAanpassen();
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        
        
        Button btn = new Button("Gebruiker aanmaken");
        Button btn1 = new Button("Sign in");
        Button btn2 = new Button("Gebruiker aanpassen");
        
        StackPane root = new StackPane();
        if(rol.equals("admin")){
            root.getChildren().add(btn);
            grid.add(btn, 0, 6);
            root.getChildren().add(btn2);
            grid.add(btn2, 4, 6);
        }else{
            root.getChildren().add(btn1);
            grid.add(btn1, 4, 6);
        }
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            private String[] test;
            @Override
            public void handle(ActionEvent e) {
                
                gebruikerAanpassen.star(primaryStage);
            }
        });
        
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            private String[] test;
            @Override
            public void handle(ActionEvent e) {
                log.start(primaryStage);
            }
        });
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            private String[] test;
            @Override
            public void handle(ActionEvent e) {
                gebruikerAanmaken.start(primaryStage);
            }
        });
        
        Scene scene = new Scene(grid, 1200, 920);

        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
    }

}
