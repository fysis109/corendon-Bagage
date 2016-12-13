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

    public void start(Stage primaryStage) {

        //Objects aanmaken
        GebruikerAanmaken gebruikerAanmaken = new GebruikerAanmaken();
        Login log = new Login();
        GebruikerAanpassen gebruikerAanpassen = new GebruikerAanpassen();
        GevKofferReg gevKofferReg = new GevKofferReg();
        VerlKofferReg verlKofferReg = new VerlKofferReg();
        ManagerStartScherm managerStartScherm = new ManagerStartScherm();
        KlantenAanpassen klantenAanpassen = new KlantenAanpassen();

        //Hier wordt de menubar bovenin aangemaakt
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        //gridpaneaangemaakt
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        root.setCenter(grid);

        //Alle Buttons
        Button buttonGebruikerAanmaken = new Button("Gebruiker aanmaken");
        buttonGebruikerAanmaken.setMaxWidth(220);
        Button buttonGebruikerAanpassen = new Button("Gebruiker aanpassen");
        buttonGebruikerAanpassen.setMaxWidth(220);
        Button buttonGevondenKofferRegistreren = new Button("Gevonden bagage registreren");
        buttonGevondenKofferRegistreren.setMaxWidth(220);
        Button buttonVerlKofferReg = new Button("Verloren bagage registreren");
        buttonVerlKofferReg.setMaxWidth(220);
        Button buttonKlantenAanpassen = new Button("Adjust customers");
        buttonVerlKofferReg.setMaxWidth(220);
        Button buttonStatistics = new Button("Statistics");
        buttonStatistics.setMaxWidth(220);

        //kijkt welke buttons er op de homepage moeten afhankeijk van de rol 
        switch (Login.rol) {
            case "Admin":
                grid.add(buttonGebruikerAanmaken, 0, 0);
                grid.add(buttonGebruikerAanpassen, 1, 0);
                grid.add(buttonGevondenKofferRegistreren, 0, 1);
                grid.add(buttonVerlKofferReg, 1, 1);
                break;
            case "Manager":
                grid.add(buttonStatistics, 0, 0);
                break;
            default:
                grid.add(buttonKlantenAanpassen, 7, 6 );
                grid.add(buttonGevondenKofferRegistreren, 3, 6);
                grid.add(buttonVerlKofferReg, 5, 6);
                break;
        }

        //alle ActionEvent handlers van de buttons
        buttonGebruikerAanpassen.setOnAction((ActionEvent e) -> {
            gebruikerAanpassen.star(primaryStage);
        });

        buttonGebruikerAanmaken.setOnAction((ActionEvent e) -> {
            gebruikerAanmaken.start(primaryStage);
        });

        buttonGevondenKofferRegistreren.setOnAction((ActionEvent e) -> {
            gevKofferReg.start(primaryStage);
        });
        buttonKlantenAanpassen.setOnAction((ActionEvent e) -> {
            klantenAanpassen.start(primaryStage);
        });
        
        buttonVerlKofferReg.setOnAction((ActionEvent e) -> {
            verlKofferReg.start(primaryStage);
        });

        buttonStatistics.setOnAction((ActionEvent e) -> {
            managerStartScherm.start(primaryStage);
        });

        Scene scene = new Scene(root, 1220, 920);
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
    }

}
