package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WachtwoordVergeten {
    public void start(Stage primaryStage) {

    //import java class
    Login Log = new Login();
    
    //grind
    primaryStage.setTitle("Corendon Bagage");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    
    
    
    
    
    
    
    
    //button
    Button btn = new Button("Sign in");

    StackPane root = new StackPane();
    root.getChildren().add(btn);

    btn.setOnAction(new EventHandler<ActionEvent>() {
        private String[] test;
        @Override
        public void handle(ActionEvent e) {
            Log.start(primaryStage);
        }
    });

    Scene scene = new Scene(root, 1200, 920);

    primaryStage.setTitle("Home scherm");
    primaryStage.setScene(scene);
    primaryStage.show();
    
    //text
    Text scenetitle = new Text("Welcome");
    
    //button
    Button buttonWachtwoordVergeten = new Button("Forgot password");
    HBox bwvbox = new HBox(10);
    bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
    bwvbox.getChildren().add(buttonWachtwoordVergeten);
    grid.add(bwvbox, 1, 3);
    //Login.start(primaryStage);
    }
}
