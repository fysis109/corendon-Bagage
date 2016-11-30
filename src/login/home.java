package login;

import com.mysql.jdbc.log.Log;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Home {

    //mysql connectie
    Mysql mysql = new Mysql();
    
    //private mqsql
    private final String USERNAME = mysql.username();
    private final String PASSWORD = mysql.password();
    private final String CONN_STRING = mysql.urlmysql();
    
    public void start(Stage primaryStage, String rol) {
        
        //import java class
        GebruikerAanmaken gebruikerAanmaken = new GebruikerAanmaken();
        Login log = new Login();
        GebruikerAanpassen gebruikerAanpassen = new GebruikerAanpassen();
        System.out.println("bla");
        
        
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

        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {  
    }

}
