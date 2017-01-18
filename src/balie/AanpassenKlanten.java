/*
 * Hiermee kan je de klant informatie veranderen in de database
 */
package balie;

import global.MenuB;
import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AanpassenKlanten {

    //connectie met de database
    private Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();

    public void AanpassenKlanten(Stage primaryStage, String voornaam, String achternaam, String tussenvoegsel2, String telefoon,
            String eMail, String gebDatum, String customersID) {

        //Menubar aan de bovenkant
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        //Gridpane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        root.setCenter(grid);

        int rij = 1;

        //Alle labels en textfields
        Text scenetitle = new Text("Customer Information");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label lastName = new Label("Last name:");
        grid.add(lastName, 0, rij);

        TextField lastNameEntry = new TextField(achternaam);
        grid.add(lastNameEntry, 1, rij++);

        Label tussenVoegsel = new Label("Insertion:");
        grid.add(tussenVoegsel, 0, rij);

        TextField tussenVoegselEntry = new TextField(tussenvoegsel2);
        grid.add(tussenVoegselEntry, 1, rij++);

        Label firstName = new Label("First name:");
        grid.add(firstName, 0, rij);

        TextField firstNameEntry = new TextField(voornaam);
        grid.add(firstNameEntry, 1, rij++);

        Label geboorteDatum = new Label("Date of birth yyyy-mm-dd:");
        grid.add(geboorteDatum, 0, rij);

        TextField geboorteDatumEntry = new TextField(gebDatum);
        grid.add(geboorteDatumEntry, 1, rij++);

        Label phonenumber = new Label("Phonenumber:");
        grid.add(phonenumber, 0, rij);

        TextField phonenumberEntry = new TextField(String.valueOf(telefoon));
        grid.add(phonenumberEntry, 1, rij++);

        Label email = new Label("Email:");
        grid.add(email, 0, rij);

        TextField emailEntry = new TextField(eMail);
        grid.add(emailEntry, 1, rij++);

        Button registreerKlant = new Button("Adjust user");
        registreerKlant.setMinWidth(100);
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(registreerKlant);
        grid.add(bwvbox, 1, rij++);

        Button goBack = new Button("Go back");
        goBack.setMinWidth(100);
        HBox bwvbox1 = new HBox(10);
        bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox1.getChildren().add(goBack);
        grid.add(bwvbox1, 1, rij++);

        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, rij);

        //eventhandler om terug naar het overzicht van klanten te gaan
        goBack.setOnAction((ActionEvent e) -> {
            KlantenAanpassen klantenAanpassen = new KlantenAanpassen();
            klantenAanpassen.start(primaryStage);
        });

        //eventhandler voor de button pas klant aan
        registreerKlant.setOnAction((ActionEvent e) -> {
            String lastName1 = lastNameEntry.getText();
            String firstName1 = firstNameEntry.getText();
            String phoneNumber = phonenumberEntry.getText();
            String email1 = emailEntry.getText();
            String tussenvoegsel = tussenVoegselEntry.getText();
            String geb_datum = geboorteDatumEntry.getText();
            //kijkt of alle velden zijn ingevuld
            if (lastNameEntry.getText().trim().isEmpty() || firstNameEntry.getText().trim().isEmpty()
                    || geboorteDatumEntry.getText().trim().isEmpty() || emailEntry.getText().trim().isEmpty()
                    || phonenumberEntry.getText().trim().isEmpty()) {
                actiontarget.setText("Lastname, Firstname,\ndate of birth, phonenumber\n and email can't\n be left open");
            } else {
                Connection conn;
                try {
                    conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                    Statement stmt = (Statement) conn.createStatement();
                    //hier wordt de query aangemaakt en uitgevoerd
                    String update = "UPDATE customers SET voornaam = '" + firstName1 + "', achternaam ='" + lastName1 + "', tussenvoegsel ="
                            + "'" + tussenvoegsel + "', telefoonnummer = '" + phoneNumber + "', email = '" + email1 + "', geb_datum = '" + geb_datum + "'"
                            + "where customersID = '" + customersID + "' ";
                    stmt.execute(update);
                    actiontarget.setFill(Color.GREEN);
                    actiontarget.setText("Customer adjusted");
                } catch (SQLException ed) {
                    System.out.println(ed);
                }
            }
        });

        //laat het scherm ook daadwerkelijk zien
        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register Customer");
        scene.getStylesheets().add("global/Style2.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
