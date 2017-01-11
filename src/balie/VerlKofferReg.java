/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balie;

import global.MenuB;
import global.Mysql;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.PDFregister;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class VerlKofferReg {

    Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    private int customerID;
    private String kleur, merk, hoogte, lengte, breedte, luchthavenVertrekEntry,
            luchthavenAankomstEntry, countryEntry, hardSoftCase;
    // Bagage info
    private String Bagagelabel;
    private String Kleur;
    private String Lengte;
    private String Dikte;
    private String Breedte;
    private String Luchthavengevonden;
    private String Bijzonderhede;
    private String Merk;
    private String Softhard;
    //klanten info
    private String Voornaam;
    private String Tussenvoegsel;
    private String Achternaam;
    private String Telefoonnummer;
    private String Email;

    public void start(Stage primaryStage) {

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
        root.setCenter(grid);

        int rij = 1;

        Text scenetitle = new Text("Customer Information");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label lastName = new Label("Last name:");
        grid.add(lastName, 0, rij);

        TextField lastNameEntry = new TextField();
        grid.add(lastNameEntry, 1, rij++);

        Label tussenVoegsel = new Label("Insertion:");
        grid.add(tussenVoegsel, 0, rij);

        TextField tussenVoegselEntry = new TextField();
        grid.add(tussenVoegselEntry, 1, rij++);

        Label firstName = new Label("First name:");
        grid.add(firstName, 0, rij);

        TextField firstNameEntry = new TextField();
        grid.add(firstNameEntry, 1, rij++);

        Label geboorteDatum = new Label("Date of birth yyyy-mm-dd:");
        grid.add(geboorteDatum, 0, rij);

        TextField geboorteDatumEntry = new TextField();
        grid.add(geboorteDatumEntry, 1, rij++);

        Label phonenumber = new Label("Phonenumber:");
        grid.add(phonenumber, 0, rij);

        TextField phonenumberEntry = new TextField();
        grid.add(phonenumberEntry, 1, rij++);

        Label email = new Label("Email:");
        grid.add(email, 0, rij);

        TextField emailEntry = new TextField();
        grid.add(emailEntry, 1, rij++);

        Button registreerKlant = new Button("Register customer");
        registreerKlant.setPrefWidth(150);
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(registreerKlant);
        grid.add(bwvbox, 1, rij++);

        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, rij++);

        Button verderNaarBagage = new Button("Register Lugage");
        verderNaarBagage.setPrefWidth(150);

        registreerKlant.setOnAction((ActionEvent e) -> {
            String lastName1 = lastNameEntry.getText();
            String firstName1 = firstNameEntry.getText();
            String phoneNumber = phonenumberEntry.getText();
            String email1 = emailEntry.getText();
            String tussenvoegsel = tussenVoegselEntry.getText();
            String geb_datum = geboorteDatumEntry.getText();
            if (lastNameEntry.getText().trim().isEmpty() || firstNameEntry.getText().trim().isEmpty() || geboorteDatumEntry.getText().trim().isEmpty()
                    || emailEntry.getText().trim().isEmpty() || phonenumberEntry.getText().trim().isEmpty()) {
                actiontarget.setText("Lastname, Firstname,\ndate of birth, phonenumber\n and email can't\n be left open");
            } else {
                Connection conn;
                try {
                    conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                    Statement stmt = (Statement) conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM customers WHERE voornaam = '" + firstName1 + "' AND achternaam = '" + lastName1 + "' AND tussenvoegsel = '" + tussenvoegsel + "' AND geb_datum = '" + geb_datum + "'");
                    int count = 0;
                    while (rs.next()) {
                        count = rs.getInt("total");
                    }
                    if (count == 0) {
                        String insert = "insert into customers (voornaam, achternaam, tussenvoegsel, telefoonnummer, email, geb_datum) "
                                + "VALUES ('" + firstName1 + "','" + lastName1 + "', '" + tussenvoegsel + "' , '" + phoneNumber + "', '" + email1 + "','" + geb_datum + "' )";
                        stmt.execute(insert);
                        actiontarget.setFill(Color.GREEN);
                        actiontarget.setText("Customer added");
                    } else {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Customer already exists,\nclick register luggage to\ncontinue");
                    }
                    ResultSet rs2 = stmt.executeQuery("SELECT customersID FROM customers WHERE voornaam = '" + firstName1 + "' AND achternaam = '" + lastName1 + "' AND tussenvoegsel = '" + tussenvoegsel + "' AND geb_datum = '" + geb_datum + "'");
                    while (rs2.next()) {
                        customerID = rs2.getInt("customersID");
                    }
                    HBox bwvbox1 = new HBox(10);
                    bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
                    bwvbox1.getChildren().add(verderNaarBagage);
                    grid.add(bwvbox1, 1, 10);
                } catch (SQLException ed) {
                    System.out.println(ed);
                }
            }
        });

        verderNaarBagage.setOnAction((ActionEvent event) -> {
            BagageToevoegen(primaryStage);
        });

        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register Customer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void BagageToevoegen(Stage primaryStage) {

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
        root.setCenter(grid);

        int rij = 1;

        Text scenetitle = new Text("Register lost Lugage");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label bagageLabel = new Label("Lugagelabelnumber:");
        grid.add(bagageLabel, 0, rij);

        TextField bagageLabelEntry = new TextField();
        bagageLabelEntry.setMaxWidth(225);
        grid.add(bagageLabelEntry, 1, rij++, 2, 1);

        Label kleurKoffer = new Label("Color:");
        grid.add(kleurKoffer, 0, rij);

        ComboBox kleurKofferComboBox = new ComboBox();
        kleurKofferComboBox.getItems().addAll(
                "Blue", "Yellow", "Black", "Gray", "Brown", "Other"
        );
        kleurKofferComboBox.setPrefWidth(225);
        grid.add(kleurKofferComboBox, 1, rij++, 2, 1);

        //eventhandler kleurKofferComboBox
        kleurKofferComboBox.setOnAction((event) -> {
            kleur = (String) kleurKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label merkKoffer = new Label("Brand:");
        grid.add(merkKoffer, 0, rij);

        ComboBox merkKofferComboBox = new ComboBox();
        merkKofferComboBox.getItems().addAll(
                "American Tourister", "Eastpak", "March", "Porsche Design", "Rimowa",
                "Samsonite", "Swiss Wenger", "Ted Baker", "The North Face",
                "Tumi", "Victorinox", "Other"
        );
        merkKofferComboBox.setPrefWidth(225);
        grid.add(merkKofferComboBox, 1, rij++, 2, 1);

        //eventhandler merkkoffercombobox      
        merkKofferComboBox.setOnAction((event) -> {
            merk = (String) merkKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label hoogteKoffer = new Label("Height of luggage:");
        grid.add(hoogteKoffer, 0, rij);

        ComboBox hoogteKofferComboBox = new ComboBox();
        hoogteKofferComboBox.getItems().addAll(
                "10cm-15cm", "15cm-20cm", "20cm-25cm", "25cm-30cm",
                "35cm-40cm", "40cm-45cm", "Unknown"
        );
        hoogteKofferComboBox.setPrefWidth(225);
        grid.add(hoogteKofferComboBox, 1, rij++, 2, 1);

        //eventhandler hoogtekoffercombobox
        hoogteKofferComboBox.setOnAction((event) -> {
            hoogte = (String) hoogteKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label lengteKoffer = new Label("Length of luggage:");
        grid.add(lengteKoffer, 0, rij);

        ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
                "30cm-40cm", "40cm-50cm", "50cm-60cm",
                "60cm-70cm", "70cm-80cm", "Unknown"
        );
        lengteKofferComboBox.setPrefWidth(225);
        grid.add(lengteKofferComboBox, 1, rij++, 2, 1);

        //eventhandler lengte combobox
        lengteKofferComboBox.setOnAction((event) -> {
            lengte = (String) lengteKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label breedteKoffer = new Label("Width of luggage:");
        grid.add(breedteKoffer, 0, rij);

        ComboBox breedteKofferComboBox = new ComboBox();
        breedteKofferComboBox.getItems().addAll(
                "20cm-30cm", "30cm-40cm", "40cm-50cm", "50cm-60cm", "Unknown"
        );
        breedteKofferComboBox.setPrefWidth(225);
        grid.add(breedteKofferComboBox, 1, rij++, 2, 1);

        //eventhandler breedtekoffercombobox      
        breedteKofferComboBox.setOnAction((event) -> {
            breedte = (String) breedteKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label hardSoftCaseLabel = new Label("Soft/hard case:");
        grid.add(hardSoftCaseLabel, 0, rij);

        ComboBox hardSoftCaseComboBox = new ComboBox();
        hardSoftCaseComboBox.getItems().addAll(
                "Soft", "Hard"
        );
        hardSoftCaseComboBox.setPrefWidth(225);
        grid.add(hardSoftCaseComboBox, 1, rij++, 2, 1);

        //eventhandler hardSoftCaseComboBox
        hardSoftCaseComboBox.setOnAction((event) -> {
            hardSoftCase = (String) hardSoftCaseComboBox.getSelectionModel().getSelectedItem();
        });

        Label luchthavenVertrek = new Label("Airport of departure:");
        grid.add(luchthavenVertrek, 0, rij);

        ComboBox vliegveldVertrekComboBox = new ComboBox();
        vliegveldVertrekComboBox.getItems().addAll(
                "Schiphol, Amsterdam", "El Prat, Barcelona", "Atatürk, Istanbul"
        );
        vliegveldVertrekComboBox.setPrefWidth(225);
        grid.add(vliegveldVertrekComboBox, 1, rij++, 2, 1);

        //eventhandler luchthavenvertrek
        vliegveldVertrekComboBox.setOnAction((event) -> {
            luchthavenVertrekEntry = (String) vliegveldVertrekComboBox.getSelectionModel().getSelectedItem();
        });

        Label luchthavenAankomst = new Label("Airport of arrival:");
        grid.add(luchthavenAankomst, 0, rij);

        ComboBox vliegveldAankomstComboBox = new ComboBox();
        vliegveldAankomstComboBox.getItems().addAll(
                "Schiphol, Amsterdam", "El Prat, Barcelona", "Atatürk, Istanbul"
        );
        vliegveldAankomstComboBox.setPrefWidth(225);
        grid.add(vliegveldAankomstComboBox, 1, rij++, 2, 1);

        //eventhandler vliegveld aankomst
        vliegveldAankomstComboBox.setOnAction((event) -> {
            luchthavenAankomstEntry = (String) vliegveldAankomstComboBox.getSelectionModel().getSelectedItem();
        });

        Label bijzonderhedenLabel = new Label("Characteristics: ");
        grid.add(bijzonderhedenLabel, 0, rij);

        TextArea bijzonderhedenEntry = new TextArea();
        bijzonderhedenEntry.setMaxSize(225, 50);
        grid.add(bijzonderhedenEntry, 1, rij++, 2, 2);

        rij++;

        Text scenetitle1 = new Text("Delivery address");
        scenetitle1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle1, 0, rij++, 2, 1);

        Label country = new Label("Country:");
        grid.add(country, 0, rij);

        ComboBox countryComboBox = new ComboBox();
        countryComboBox.getItems().addAll(
                "Netherlands", "Spain", "Turkey"
        );
        countryComboBox.setPrefWidth(225);
        grid.add(countryComboBox, 1, rij++, 2, 1);

        countryComboBox.setOnAction((event) -> {
            countryEntry = (String) countryComboBox.getSelectionModel().getSelectedItem();
        });

        Label plaats = new Label("City/town:");
        grid.add(plaats, 0, rij);

        TextField plaatsEntry = new TextField();
        plaatsEntry.setMaxWidth(225);
        grid.add(plaatsEntry, 1, rij++, 2, 1);

        Label straatLabel = new Label("Street");
        grid.add(straatLabel, 0, rij);

        TextField straatEntry = new TextField();
        straatEntry.setMaxWidth(225);
        grid.add(straatEntry, 1, rij++, 2, 1);

        Label huisnummerLabel = new Label("Street number / addition");
        grid.add(huisnummerLabel, 0, rij);

        TextField huisnummerEntry = new TextField();
        huisnummerEntry.setMaxWidth(107.5);
        grid.add(huisnummerEntry, 1, rij);

        TextField huisnummerToevoeging = new TextField();
        huisnummerToevoeging.setMaxWidth(107.5);
        grid.add(huisnummerToevoeging, 2, rij++);

        Label postcodeLabel = new Label("Zipcode");
        grid.add(postcodeLabel, 0, rij);

        TextField postcodeEntry = new TextField();
        postcodeEntry.setMaxWidth(225);
        grid.add(postcodeEntry, 1, rij++, 2, 1);

        Button registreerBagage = new Button("Register Lugage");
        HBox bwvbox1 = new HBox(10);
        bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox1.getChildren().add(registreerBagage);
        grid.add(bwvbox1, 2, rij++);

        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, rij, 2, 1);

        registreerBagage.setOnAction((ActionEvent e) -> {
            Connection conn;
            try {
                conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                String bagagelabel = bagageLabelEntry.getText();
                String city = plaatsEntry.getText();
                String straat = straatEntry.getText();
                String bijzonderheden = bijzonderhedenEntry.getText();
                if (bijzonderheden.equals("null")) {
                    bijzonderheden = null;
                }
                int huisnummer = 0;
                if (huisnummerEntry.getText() == null) {
                    huisnummer = Integer.valueOf(huisnummerEntry.getText());
                }
                String toevoeging = huisnummerToevoeging.getText();
                String postcode = postcodeEntry.getText();
                Statement stmt = (Statement) conn.createStatement();
                if (countryEntry == null || bagageLabelEntry.getText().trim().isEmpty() || city == null || straat == null || huisnummerEntry.getText().trim().isEmpty() || postcodeEntry.getText().trim().isEmpty() || merk == null
                        || kleur == null || hoogte == null || lengte == null || breedte == null || luchthavenAankomstEntry == null || luchthavenVertrekEntry == null || hardSoftCase == null) {
                    actiontarget.setText("All fields must be\nfilled in except\nCharacteristics"); 
                } else {
                    ResultSet bagagelabelExistsCheck = stmt.executeQuery("SELECT COUNT(*) AS total FROM verlorenbagage WHERE bagagelabel = '"+bagagelabel+"'");
                    int count = 0;
                    while(bagagelabelExistsCheck.next()){
                        count = bagagelabelExistsCheck.getInt("total");
                    }
                    if(count == 0){
                    String insert = "INSERT INTO verlorenbagage (bagagelabel, kleur, dikte, lengte, breedte, luchthavenvertrokken, luchthavenaankomst, datum, bijzonderheden, merk, customersID, softhard, status)"
                            + "VALUES ('" + bagagelabel + "','" + kleur + "','" + hoogte + "','" + lengte + "','" + breedte + "','" + luchthavenVertrekEntry + "','" + luchthavenAankomstEntry
                            + "',curdate(),'" + bijzonderheden + "','" + merk + "', '" + customerID + "', '" + hardSoftCase + "', 'notSolved')";
                    stmt.execute(insert);
                    actiontarget.setFill(Color.GREEN);
                    actiontarget.setText("Lugage added");
                    ResultSet result1232 = stmt.executeQuery("SELECT verlorenkofferID FROM verlorenbagage WHERE bagagelabel = '" + bagagelabel + "' AND kleur = '" + kleur + "' AND dikte = '" + hoogte + "' AND lengte ='"
                            + lengte + "' AND breedte = '" + breedte + "' AND luchthavenvertrokken = '" + luchthavenVertrekEntry + "' AND luchthavenaankomst = '" + luchthavenAankomstEntry
                            + "' AND datum = curdate() AND bijzonderheden = '" + bijzonderheden + "' AND merk = '" + merk + "' AND customersID = '" + customerID + "'");

                    int verlorenKofferID = 0;
                    
                    Statement kofferinfo = conn.createStatement();
            String insertString2 = "SELECT * FROM verlorenbagage,customers WHERE gevondenkofferID =" + verlorenKofferID +  "AND customersID =" + customerID;
            ResultSet rs = kofferinfo.executeQuery(insertString2);
            while (rs.next()) {
                    this.Bagagelabel = rs.getString("bagagelabel");
                    this.Kleur = rs.getString("kleur");
                    this.Lengte = rs.getString("lengte");
                    this.Dikte = rs.getString("dikte");
                    this.Breedte = rs.getString("breedte");
                    this.Luchthavengevonden = rs.getString("luchthavengevonden");
                    this.Bijzonderhede = rs.getString("bijzonderhede");
                    this.Merk = rs.getString("merk");
                    this.Softhard = rs.getString("softhard");
                    this.Voornaam = rs.getString("voornaam");
                    this.Tussenvoegsel = rs.getString("tussenvoegsel");
                    this.Achternaam = rs.getString("achternaam");
                    this.Telefoonnummer = rs.getString("telefoonnummer");
                    this.Email = rs.getString("email");
                    }

                    while (result1232.next()) {
                        verlorenKofferID = result1232.getInt("verlorenkofferID");
                    }

                    stmt.execute("INSERT INTO afleveradres (verlorenkofferID, Land, Straat, Huisnummer, Toevoeging, Postcode, Plaats) VALUES "
                            + "('" + verlorenKofferID + "','" + countryEntry + "','" + straat + "','" + huisnummer + "','" + toevoeging + "','" + postcode + "','" + city + "')");
                    /*
                     * JIORGOS, pdf dat de koffer in de database is gezet en met welke gegevens allemaal.
                    */
                    
                    
                    
                    ZoekMatchVerlorenBagage zoekMatchVerlorenBagage = new ZoekMatchVerlorenBagage();
                    zoekMatchVerlorenBagage.maakZoekString(customerID ,primaryStage, verlorenKofferID, bagagelabel, kleur, hoogte, lengte, breedte, 
                            luchthavenVertrekEntry, luchthavenAankomstEntry, bijzonderheden, merk, hardSoftCase);
                                                               
                    }else{
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Luggage label already exists");
                    }
                }
            } catch (SQLException ed) {
                System.out.println(ed);
            }
            // pdf verloren kofffer match
           // Create a new empty document
        PDDocument document = new PDDocument();

        // Create a new blank page and add it to the document
        PDPage blankPage = new PDPage();
        document.addPage( blankPage );

        try {
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont font2 = PDType1Font.TIMES_ROMAN;

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
            // headline
            contentStream.beginText();
            contentStream.setFont( font, 15 );
            contentStream.moveTextPositionByAmount( 75, 700 );
            contentStream.drawString( "Vermiste bagage registratie formulier" );
            contentStream.endText();
            
            //Datum 
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 175, 650 );
            contentStream.drawString( "Datum: " );
            contentStream.endText();
            //tijd
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 187, 635 );
            contentStream.drawString( "Tijd: " );
            contentStream.endText();
            //luchthaven
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 150, 620 );
            contentStream.drawString( "Luchthaven: " );
            contentStream.endText();
            
            //begin reiziger informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 590 );
            contentStream.drawString( "Reiziger informatie: " );
            contentStream.endText();
            //Naam
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 575 );
            contentStream.drawString( "Naam: " );
            contentStream.endText();
            //Adres
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 560 );
            contentStream.drawString( "Adres: " );
            contentStream.endText();
            //Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 152, 545 );
            contentStream.drawString( "Woonplaats: " );
            contentStream.endText();
            //Postcode
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 165, 530 );
            contentStream.drawString( "Postcode: " );
            contentStream.endText();
            //Land
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 185, 515 );
            contentStream.drawString( "Land: " );
            contentStream.endText();
            //Telefoon
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 167, 500 );
            contentStream.drawString( "Telefoon: " );
            contentStream.endText();
            //E-mail
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 178, 485 );
            contentStream.drawString( "E-mail: " );
            contentStream.endText();
            
            //bagagelabel informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 455 );
            contentStream.drawString( "Bagage label informatie: " );
            contentStream.endText();
            //Label nummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 140, 440 );
            contentStream.drawString( "Label nummer: " );
            contentStream.endText();
            //Vluchtnummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 138, 425 );
            contentStream.drawString( "Vluchtnummer: " );
            contentStream.endText();
            //Bestemming
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 150, 410 );
            contentStream.drawString( "Bestemming: " );
            contentStream.endText();
            
            //Bagage informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 380 );
            contentStream.drawString( "Bagage informatie: " );
            contentStream.endText();
            //Type
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 187, 365 );
            contentStream.drawString( "Type: " );
            contentStream.endText();
            //Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 185, 350);
            contentStream.drawString( "Merk: " );
            contentStream.endText();
            //Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 184, 335 );
            contentStream.drawString( "Kleur: " );
            contentStream.endText();
            //Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 155, 320 );
            contentStream.drawString( "Kenmerken: " );
            contentStream.endText();
            
            //handtekening reiziger
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 220 );
            contentStream.drawString( "Handtekening reiziger: " );
            contentStream.endText();
            //Handtekening klantenservice
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 140 );
            contentStream.drawString( "Handtekening klantenservice: " );
            contentStream.endText();
            
            //input Datum
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 650 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input tijd
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 635 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input luchthaven
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 620 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Naam
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 575 );
            contentStream.drawString( this.Voornaam + " " + this.Tussenvoegsel + " " + this.Achternaam );
            contentStream.endText();
            //input Adres
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 560 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 545 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Postcode
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 530 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Land
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 515 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Telefoon
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 500 );
            contentStream.drawString( this.Telefoonnummer );
            contentStream.endText();
            //input E-mail
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 485 );
            contentStream.drawString( this.Email );
            contentStream.endText();
            //input Label nummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 440 );
            contentStream.drawString(this.Bagagelabel );
            //input Vluchtnummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 425 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Bestemming
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 410 );
            contentStream.drawString( "tekst" );
            contentStream.endText();
            //input Type
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 365 );
            contentStream.drawString( hardSoftCase );
            contentStream.endText();
            //input Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 350 );
            contentStream.drawString( merk );
            contentStream.endText();
            //input Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 335 );
            contentStream.drawString( kleur );
            contentStream.endText();
            //input Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 320 );
            contentStream.drawString( this.Bijzonderhede );
            contentStream.endText();
            
            
            
            


            // Make sure that the content stream is closed:
            contentStream.close();

            document.save("Gevonden bagage.pdf");
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(PDFregister.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        });

        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register Lost Lugage");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
