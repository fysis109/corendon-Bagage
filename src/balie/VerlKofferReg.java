/*
 * In deze klasse wordt eerst een klant geregistreerd. Daarna is het mogelijk om zijn
 * koffer te registreren. Dan wordt er een pdf van gemaakt en geopend. Dan wordt er 
 * gekeken of er matches zijn. 
 */
package balie;

import global.MenuB;
import global.Mysql;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class VerlKofferReg {

    private final Mysql MYSQL = new Mysql();
    private final String USERNAME = MYSQL.getUsername();
    private final String PASSWORD = MYSQL.getPassword();
    private final String CONN_STRING = MYSQL.getUrlmysql();
    private int customerID;
    
    //private Strings nodig voor het invullen van de informatie
    private String kleurEntry, merkEntry, hoogteEntry, lengteEntry, breedteEntry, luchthavenVertrekEntry,
            luchthavenAankomstEntry, countryEntry, hardSoftCaseEntry;
    
   

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
        
        //alle labels en textfields voor het registreren van een klant
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
        
        
        //eerst wordt er gekeken alles is ingevuld, daarna of de klant al bestaat
        //als de klant al bestaat wordt daar mee verder gegaan, anders wordt er een nieuwe klant aangemaakt
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
                try {
                    Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
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
        
        //nadat de klant is toegevoegd door naar bagagetoevoegen
        verderNaarBagage.setOnAction((ActionEvent event) -> {
            BagageToevoegen(primaryStage);
        });

        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register Customer");
        scene.getStylesheets().add("global/Style2.css");
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
        
        //alle labels, comboboxen en textfield voor het registreren van verloren bagage
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
            kleurEntry = (String) kleurKofferComboBox.getSelectionModel().getSelectedItem();
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
            merkEntry = (String) merkKofferComboBox.getSelectionModel().getSelectedItem();
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
            hoogteEntry = (String) hoogteKofferComboBox.getSelectionModel().getSelectedItem();
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
            lengteEntry = (String) lengteKofferComboBox.getSelectionModel().getSelectedItem();
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
            breedteEntry = (String) breedteKofferComboBox.getSelectionModel().getSelectedItem();
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
            hardSoftCaseEntry = (String) hardSoftCaseComboBox.getSelectionModel().getSelectedItem();
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

        //alle labels en textfields voor het registreren van het afleveradres
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
        
        /*
         * Er wordt gekeken of alles is ingevuld, dan wordt gekeken of het bagagelabel al bestaat.
         * Dan wordt de bagage toegevoegd aan de database en het afleveradres ook.
         * Dan wordt de pdf gemaakt en daarna wordt er gezocht of er matches zijn.
        */
        
        registreerBagage.setOnAction((ActionEvent e) -> {
            try {
                Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                String bagagelabel = bagageLabelEntry.getText();
                String city = plaatsEntry.getText();
                String straat = straatEntry.getText();
                String bijzonderheden = bijzonderhedenEntry.getText();
                if (bijzonderheden.equals("null")) {
                    bijzonderheden = null;
                }
                String toevoeging = huisnummerToevoeging.getText();
                String postcode = postcodeEntry.getText();
                Statement stmt = (Statement) conn.createStatement();
                if (countryEntry == null || bagageLabelEntry.getText().trim().isEmpty() || city == null || straat == null || huisnummerEntry.getText().trim().isEmpty() || postcodeEntry.getText().trim().isEmpty() || merkEntry == null
                        || kleurEntry == null || hoogteEntry == null || lengteEntry == null || breedteEntry == null || luchthavenAankomstEntry == null || luchthavenVertrekEntry == null || hardSoftCaseEntry == null) {
                    actiontarget.setText("All fields must be\nfilled in except\nCharacteristics"); 
                }else if(luchthavenAankomstEntry.equals(luchthavenVertrekEntry)){
                    actiontarget.setText("Airport of departure \ncan't be the same as\naiport of arrival");
                } else {
                    ResultSet bagagelabelExistsCheck = stmt.executeQuery("SELECT COUNT(*) AS total FROM verlorenbagage WHERE bagagelabel = '"+bagagelabel+"'");
                    int count = 0;
                    while(bagagelabelExistsCheck.next()){
                        count = bagagelabelExistsCheck.getInt("total");
                    }
                    if(count == 0){
                        String insert = "INSERT INTO verlorenbagage (bagagelabel, kleur, dikte, lengte, breedte, luchthavenvertrokken, luchthavenaankomst, datum, bijzonderheden, merk, customersID, softhard, status)"
                                + "VALUES ('" + bagagelabel + "','" + kleurEntry + "','" + hoogteEntry + "','" + lengteEntry + "','" + breedteEntry + "','" + luchthavenVertrekEntry + "','" + luchthavenAankomstEntry
                                + "',curdate(),'" + bijzonderheden + "','" + merkEntry + "', '" + customerID + "', '" + hardSoftCaseEntry + "', 'notSolved')";
                        stmt.execute(insert);
                        actiontarget.setFill(Color.GREEN);
                        actiontarget.setText("Lugage added");
                        ResultSet result1232 = stmt.executeQuery("SELECT verlorenkofferID FROM verlorenbagage WHERE bagagelabel = '" + bagagelabel + "' AND kleur = '" + kleurEntry + "' AND dikte = '" + hoogteEntry + "' AND lengte ='"
                                + lengteEntry + "' AND breedte = '" + breedteEntry + "' AND luchthavenvertrokken = '" + luchthavenVertrekEntry + "' AND luchthavenaankomst = '" + luchthavenAankomstEntry
                                + "' AND datum = curdate() AND bijzonderheden = '" + bijzonderheden + "' AND merk = '" + merkEntry + "' AND customersID = '" + customerID + "'");

                        int verlorenKofferID = 0;
                        while (result1232.next()) {
                            verlorenKofferID = result1232.getInt("verlorenkofferID");
                        }

                        stmt.execute("INSERT INTO afleveradres (verlorenkofferID, Land, Straat, Huisnummer, Toevoeging, Postcode, Plaats) VALUES "
                                + "('" + verlorenKofferID + "','" + countryEntry + "','" + straat + "','" + Integer.valueOf(huisnummerEntry.getText()) + "','" + toevoeging + "','" + postcode + "','" + city + "')");

                        maakPDF(verlorenKofferID);
                        ZoekMatchVerlorenBagage zoekMatchVerlorenBagage = new ZoekMatchVerlorenBagage();
                        zoekMatchVerlorenBagage.maakZoekString(customerID ,primaryStage, verlorenKofferID, bagagelabel, kleurEntry, hoogteEntry, lengteEntry, breedteEntry, 
                                luchthavenVertrekEntry, luchthavenAankomstEntry, bijzonderheden, merkEntry, hardSoftCaseEntry);
                    }else{
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Luggage label already exists");
                    }          
                }
            } catch (SQLException ed) {
                System.out.println(ed);
            } 
        });

        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register Lost Lugage");
        scene.getStylesheets().add("global/Style2.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    private void maakPDF(int verlorenBagageID){
        //alle strings nodig voor het maken van de pdf
        String bagagelabel = null, kleur = null, bijzonderhede = null, merk = null, 
                softhard = null, luchthavenaankomst = null, voornaam = null,
                tussenvoegsel = null, achternaam = null, telefoonnummer = null, 
                email = null, plaats = null, land = null, huisnr = null, straat = null, postcode = null;
        
        //alle informatie die nodig is wordt uit de database gehaald
        try{
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement kofferinfo = conn.createStatement();
            String insertString2 = "SELECT * FROM verlorenbagage v LEFT JOIN customers c ON c.customersID = v.customersID LEFT JOIN afleveradres a ON a.VerlorenkofferID = v.verlorenkofferID WHERE v.verlorenkofferID = '" +verlorenBagageID+" '" ;
            ResultSet rs = kofferinfo.executeQuery(insertString2);
            while (rs.next()) {
                bagagelabel = rs.getString("bagagelabel");
                kleur = rs.getString("kleur");
                bijzonderhede = rs.getString("bijzonderheden");
                merk = rs.getString("merk");
                softhard = rs.getString("softhard");
                voornaam = rs.getString("voornaam");
                tussenvoegsel = rs.getString("tussenvoegsel");
                achternaam = rs.getString("achternaam");
                telefoonnummer = rs.getString("telefoonnummer");
                email = rs.getString("email");
                plaats = rs.getString("Plaats");
                land = rs.getString("Land");
                huisnr = rs.getString("Huisnummer");
                postcode = rs.getString("Postcode");
                straat = rs.getString("Straat"); 
                luchthavenaankomst = rs.getString("luchthavenaankomst");
            }

           
            PDDocument document = new PDDocument();

            // Maak een pagina in het pdf bestand
            PDPage blankPage = new PDPage();
            document.addPage( blankPage );
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont font2 = PDType1Font.TIMES_ROMAN;
            
            //hier krijgen we de datum en de tijd
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            java.util.Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);

            //contentStream slaat de informatie op die erin moet
            PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
            
            contentStream.setLeading(14.5f);
            // headline
            contentStream.beginText();
            contentStream.setFont( font, 15 );
            contentStream.moveTextPositionByAmount( 75, 700 );
            contentStream.drawString( "Lost luggage registration form" );
            contentStream.endText();
            
            //Datum 
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 650 );
            contentStream.drawString( "Date: " + reportDate);
            contentStream.endText();
            
            //luchthaven
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 635 );
            contentStream.drawString( "Airport: " + luchthavenaankomst);
            contentStream.endText();
            
            //begin reiziger informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 590 );
            contentStream.drawString( "Traveler information: " );
            contentStream.endText();
            //Naam
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 575 );
            contentStream.drawString( "Name: " + voornaam + " " + tussenvoegsel + " " + achternaam  );
            contentStream.endText();
            //Adres
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 560 );
            contentStream.drawString( "Address: " + straat + " " + huisnr);
            contentStream.endText();
            //Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 545 );
            contentStream.drawString( "Home town: " + plaats);
            contentStream.endText();
            //Postcode
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 530 );
            contentStream.drawString( "Zip code: " + postcode );
            contentStream.endText();
            //Land
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 515 );
            contentStream.drawString( "Country: " + land );
            contentStream.endText();
            //Telefoon
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 500 );
            contentStream.drawString( "Telephone: " + telefoonnummer);
            contentStream.endText();
            //E-mail
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 485 );
            contentStream.drawString( "E-mail: " + email );
            contentStream.endText();
            
            //bagagelabel informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 455 );
            contentStream.drawString( "Luggage label information: " );
            contentStream.endText();
            //Label nummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 440 );
            contentStream.drawString( "Label number: " + bagagelabel);
            contentStream.endText();
            //Bestemming
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 425 );
            contentStream.drawString( "Destination: " + luchthavenaankomst);
            contentStream.endText();
            
            //Bagage informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 380 );
            contentStream.drawString( "Luggage information: " );
            contentStream.endText();
            //Type
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 365 );
            contentStream.drawString( "Type: " + softhard );
            contentStream.endText();
            //Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 350);
            contentStream.drawString( "Brand: " + merk);
            contentStream.endText();
            //Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 335 );
            contentStream.drawString( "Colour: " + kleur);
            contentStream.endText();
            //Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 320 );
            contentStream.drawString( "Characteristics: " + bijzonderhede);
            contentStream.endText();
            
            //handtekening reiziger
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 220 );
            contentStream.drawString( "Autograph traveler: " );
            contentStream.endText();
            //Handtekening klantenservice
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 140 );
            contentStream.drawString( "Autograph customer service: " );
            contentStream.endText();
            
            contentStream.close();

            //hier wordt een map aangemaakt als die nog niet bestand in mijn documenten
            JFileChooser fr = new JFileChooser();
            FileSystemView fw = fr.getFileSystemView();            
            Path test = Paths.get(fw.getDefaultDirectory() + "\\pdfopslaan");
            if(!Files.exists(test)){
               File file = new File(fw.getDefaultDirectory() + "\\pdfopslaan");
               file.mkdir();
            }
            
            //dan wordt de pdf opgeslagen in die map
            String opslaanAls = fw.getDefaultDirectory() + "\\pdfopslaan\\Luggage in database " + bagagelabel + ".pdf";
            document.save(opslaanAls);
            document.close();
            
            //dan wordt het pdf bestand geopend
            File myFile = new File(fw.getDefaultDirectory() + "\\pdfopslaan\\Luggage in database " + bagagelabel + ".pdf");
            Desktop.getDesktop().open(myFile);
            
        } catch (SQLException | IOException ed) {
            System.out.println(ed);
        }
    }
}
