/*
 * Hiermee kan je gebruikers aanmaken in en toevoegen in de database
 */
package admin;

import global.Encrypt;
import global.MenuB;
import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GebruikerAanmaken {
    
    //mysqil informatie
    private final Mysql MYSQL = new Mysql();
    private final String USERNAME = MYSQL.getUsername();
    private final String PASSWORD = MYSQL.getPassword();
    private final String CONN_STRING = MYSQL.getUrlmysql();
    private String gebruikersRol;
    
    
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
        
        //voegt rollen toe in combobox
        ComboBox rollen = new ComboBox();
        rollen.getItems().addAll(
            "Admin", "Balie", "SuperAdmin", "Manager"
        );
        rollen.setPrefWidth(150);
        grid.add(rollen , 1 , rij++);
        
        rollen.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                gebruikersRol = t1;                
            }});  
        
        //De Sign in 
        Button createUser = new Button("Make user");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(createUser);
        grid.add(hbBtn, 1, rij++);

        //button event maken
        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, rij++);

        //Enter ook
        createUser.setDefaultButton(true);

        //Button event text
        createUser.setOnAction((ActionEvent e) -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            String username2 = userTextField2.getText();
            String password2 = pwBox2.getText();
            String mail1 = mailbox.getText();
            String mail3 = mailbox2.getText();
            if (pwBox.getText().trim().isEmpty() || pwBox2.getText().trim().isEmpty() ||
                    userTextField.getText().trim().isEmpty()|| userTextField2.getText().trim().isEmpty()||
                    mailbox.getText().trim().isEmpty()||mailbox2.getText().trim().isEmpty()||gebruikersRol == null) {
                actiontarget.setFill(Color.RED);
                actiontarget.setText("Fields can't be left open");
            } else { 
                actiontarget.setText("");
                if (!username.equals(username2) || !password.equals(password2) || !mail1.equals(mail3)) {
                    System.out.println("Username, password and/or mailadress are not the same");
                    actiontarget.setFill(Color.RED);
                    actiontarget.setText("Username, password and/or\n mailadress are not the same");
                } else {
                    try {
                        Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        System.out.println("Connected!");
                        Statement stmt = (Statement) conn.createStatement();
                        ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) AS total FROM users WHERE username = '" + username+ "'");
                        int count = 0;
                        while(rs2.next()){
                            count = rs2.getInt("total");
                        }
                        if (count == 0) {
                            Encrypt encrypt = new Encrypt();
                            try {
                                password = encrypt.createHash(password);
                            } catch (Encrypt.CannotPerformOperationException ex) {
                                System.out.println(ex);
                            }
                            String insert = "INSERT INTO users (username, wachtwoord, rol, email) VALUES('"+username+"','"+password+"','"
                                    +gebruikersRol+"','" + mail1 + "')";
                            stmt.execute(insert);
                            actiontarget.setFill(Color.GREEN);
                            actiontarget.setText("User has been added");
                        } else {
                            actiontarget.setFill(Color.RED);
                            actiontarget.setText("Username already exists");
                        }
                    }catch (SQLException ed) {  
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

}
    
