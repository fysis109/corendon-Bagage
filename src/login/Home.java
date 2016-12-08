package login;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Home {

    //mysql connectie
    Mysql mysql = new Mysql();
    
    public Stage test;
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    
    public void start(Stage primaryStage) {

        

        GebruikerAanmaken gebruikerAanmaken = new GebruikerAanmaken();
        Login log = new Login();
        GebruikerAanpassen gebruikerAanpassen = new GebruikerAanpassen();
        GevKofferReg gevKofferReg = new GevKofferReg();

        VerlKofferReg verlKofferReg = new VerlKofferReg();



        // deze vijf regels om een homeknop aan te roepen
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        
                
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        // deze regel moet ook aangemaakt worden voor de homeknop
        root.setCenter(grid);

        Button buttonGebruikerAanmaken = new Button("Gebruiker aanmaken");
        Button buttonSignIn = new Button("Sign in");
        Button buttonGebruikerAanpassen = new Button("Gebruiker aanpassen");
        Button buttonGevondenKofferRegistreren = new Button("Gevonden bagage registreren");
        Button buttonVerlKofferReg = new Button("Verloren bagage registreren");
        
        if(Login.rol.equals("Admin")){
            root.getChildren().add(buttonGebruikerAanmaken);
            grid.add(buttonGebruikerAanmaken, 0, 6);
            root.getChildren().add(buttonGebruikerAanpassen);
            grid.add(buttonGebruikerAanpassen, 4, 6);
        }else{
            root.getChildren().add(buttonSignIn);
            grid.add(buttonSignIn, 4, 6);
            grid.add(buttonGevondenKofferRegistreren, 3,6);
            grid.add(buttonVerlKofferReg, 5,6);
            
        }
        
        buttonGebruikerAanpassen.setOnAction((ActionEvent e) -> {
            gebruikerAanpassen.star(primaryStage);
        });
        
        buttonSignIn.setOnAction((ActionEvent e) -> {
            log.start(primaryStage);
        });
        
        buttonGebruikerAanmaken.setOnAction((ActionEvent e) -> {
            gebruikerAanmaken.start(primaryStage);
        });
        
        buttonGevondenKofferRegistreren.setOnAction((ActionEvent e) -> {
            gevKofferReg.start(primaryStage);
        });
        
        buttonVerlKofferReg.setOnAction((ActionEvent e) -> {
            verlKofferReg.start(primaryStage);
        });
        
        
        // deze aanpassen van grid naar root..
        Scene scene = new Scene(root, 1200, 920);
        
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
    }
    
    public static void main(String[] args) {
       
    }

}