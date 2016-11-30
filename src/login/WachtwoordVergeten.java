package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WachtwoordVergeten {
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
  
    //text gebeid
    Label text = new Label("Neem contact op met uw manager voor een wachtwoord reset.");
    text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(text, 0, 3);
    
    //button wahtwoord vergeten
    Button buttonWachtwoordVergeten = new Button("Forgot password");
    HBox bwvbox = new HBox(10);
    bwvbox.setAlignment(Pos.BOTTOM_CENTER);
    bwvbox.getChildren().add(buttonWachtwoordVergeten);
    grid.add(bwvbox, 0, 4);
    buttonWachtwoordVergeten.setOnAction(new EventHandler<ActionEvent>() {
        private String[] test;
        @Override
        public void handle(ActionEvent e) {
            Login.start(primaryStage);
        }
    });
    
    //show interface
    Scene scene = new Scene(grid, 1200, 920);
    primaryStage.setScene(scene);
    primaryStage.show();
    }
}
