package global;

import admin.GebruikerAanmaken;
import admin.GebruikersTable;
import balie.BagageAanpassen;
import balie.GevKofferReg;
import balie.GevondenBagageAanpassen;
import balie.KlantenAanpassen;
import balie.VerlKofferReg;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import login.Login;
import manager.ManagerStartScherm;

public class Home {

    public void start(Stage primaryStage) {

        //Objects aanmaken
        GebruikerAanmaken gebruikerAanmaken = new GebruikerAanmaken();
        GevKofferReg gevKofferReg = new GevKofferReg();
        VerlKofferReg verlKofferReg = new VerlKofferReg();
        ManagerStartScherm managerStartScherm = new ManagerStartScherm();
        KlantenAanpassen klantenAanpassen = new KlantenAanpassen();
        GebruikersTable gebruikersTable = new GebruikersTable();
        VernietigBagage vernietigBagage = new VernietigBagage();
        BagageAanpassen bagageAanpassen = new BagageAanpassen();
        GevondenBagageAanpassen gevBagAanpassen = new GevondenBagageAanpassen();

        //Hier wordt de menubar bovenin aangemaakt
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        //gridpane aangemaakt
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        root.setCenter(grid);

        //Alle Buttons
        Button buttonGebruikerAanmaken = new Button("Create new user");
       
        buttonGebruikerAanmaken.setMinWidth(175);
        Button buttonGebruikersTable = new Button("Adjust user");
        buttonGebruikersTable.setMinWidth(175);
        Button buttonGevondenKofferRegistreren = new Button("Register found luggage");
        buttonGevondenKofferRegistreren.setMinWidth(175);
        Button buttonVerlKofferReg = new Button("Register lost luggage");
        buttonVerlKofferReg.setMinWidth(175);
        Button buttonKlantenAanpassen = new Button("Adjust customers");
        buttonKlantenAanpassen.setMinWidth(175);
        Button buttonStatistics = new Button("Statistics");
        buttonStatistics.setMinWidth(175);
        Button buttonVerwijderBagage = new Button("Update destroyed luggage");
        buttonVerwijderBagage.setMinWidth(175);
        Button buttonBagageAanpassen = new Button("Adjust lost luggage");
        buttonBagageAanpassen.setMinWidth(175);
        Button buttonGevBagageAanpassen = new Button("Adjust found luggage");
        buttonGevBagageAanpassen.setMinWidth(175);

        //kijkt welke buttons er op de homepage moeten afhankeijk van de rol 
        switch (Login.rol) {
            case "SuperAdmin":
                grid.add(buttonGevondenKofferRegistreren, 0, 1);
                grid.add(buttonVerlKofferReg, 0, 0);
                grid.add(buttonGebruikersTable, 1, 0);
                grid.add(buttonGebruikerAanmaken, 1, 1);
                grid.add(buttonKlantenAanpassen, 2,0);
                grid.add(buttonStatistics,2,1);
                grid.add(buttonVerwijderBagage,3,0);
                grid.add(buttonBagageAanpassen, 3,1);
                grid.add(buttonGevBagageAanpassen, 3, 2);
                break;
            case "Admin":
                grid.add(buttonGevondenKofferRegistreren, 0, 0);
                grid.add(buttonVerlKofferReg, 0, 1);
                grid.add(buttonGebruikersTable, 1, 0);
                grid.add(buttonGebruikerAanmaken, 1, 1);
                grid.add(buttonKlantenAanpassen, 2,0);
                grid.add(buttonStatistics,2,1);
                grid.add(buttonVerwijderBagage,3,0);
                grid.add(buttonBagageAanpassen, 3,1);
                grid.add(buttonGevBagageAanpassen, 3, 2);
                break;
            case "Manager":
                grid.add(buttonStatistics, 0, 0);
                grid.add(buttonKlantenAanpassen, 0,1);
                grid.add(buttonGevondenKofferRegistreren, 1,0);
                grid.add(buttonVerlKofferReg, 1,1);
                grid.add(buttonBagageAanpassen, 2,0);
                grid.add(buttonGevBagageAanpassen, 2, 1);
                break;
            default:
                grid.add(buttonKlantenAanpassen, 0,0);
                grid.add(buttonGevondenKofferRegistreren, 1,0);
                grid.add(buttonVerlKofferReg, 1,1);
                grid.add(buttonBagageAanpassen, 2,0);
                grid.add(buttonGevBagageAanpassen, 2, 1);
                break;
        }

        //alle ActionEvent handlers van de buttons
        buttonGebruikersTable.setOnAction((ActionEvent e) -> {
            gebruikersTable.start(primaryStage);
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
        
        buttonVerwijderBagage.setOnAction((ActionEvent e) ->{
           vernietigBagage.start(primaryStage);
         
        });
        
        buttonBagageAanpassen.setOnAction((ActionEvent e) ->{
           bagageAanpassen.start(primaryStage); 
        });
        
        buttonGevBagageAanpassen.setOnAction((ActionEvent e) -> {
            gevBagAanpassen.start(primaryStage);
        });

        Scene scene = new Scene(root, 1220, 920);
        primaryStage.setTitle("Home");
        scene.getStylesheets().add("global/Style.css");
        primaryStage.setScene(scene);
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.show();
    }
    
}
