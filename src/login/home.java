package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class home {

    //mysql connectie
    mysql mysql = new mysql();
    
    //private mqsql
    private final String USERNAME = mysql.username();
    private final String PASSWORD = mysql.password();
    private final String CONN_STRING = mysql.urlmysql();
    
    public void start(Stage primaryStage) {
        
        //import java class
        Login log = new Login();
        
        Button btn = new Button("Sign in");
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            private String[] test;
            @Override
            public void handle(ActionEvent e) {
                log.start(primaryStage);
            }
        });
        
        Scene scene = new Scene(root, 1200, 920);

        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {  
    }

}
