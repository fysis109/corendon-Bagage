/*
 * In deze klasse wordt een match gezocht met een koffer die wordt meegegeven
 * Daarna worden alle matches getoond en kan er gekozen worden om een match te kiezen
 * Als dat gebeurd wordt er een email gestuurd naar de klant van wie de verloren koffer is
 * En wordt er een email gestuurd naar de betreffende luchthaven
 */
package balie;

import global.Home;
import global.MenuB;
import global.Mysql;
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
import java.util.ArrayList;
import java.util.Calendar;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import login.WachtwoordVergeten;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class ZoekMatchVerlorenBagage {
    
    private int customerId;
    private Stage primaryStage = new Stage();
    private int verlorenKofferID;
    
    //mysql connectie info
    private final Mysql MYSQL = new Mysql();
    private final String USERNAME = MYSQL.getUsername();
    private final String PASSWORD = MYSQL.getPassword();
    private final String CONN_STRING = MYSQL.getUrlmysql();
    
    public void maakZoekString(int customerId, Stage primaryStage, int verlorenKofferID, String bagagelabel, String kleur, String hoogte,
            String lengte, String breedte, String luchthavenVertrek,
            String luchthavenAankomst, String bijzonderheden, String merk, String hardSoftCase) {
        
        this.primaryStage = primaryStage;
        this.verlorenKofferID = verlorenKofferID;
        this.customerId = customerId;
        
        //Maakt 2 zoekstrings eentje om alleen te zoeken op bagagelabel en eentje om 
        //alleen te zoeken op kenmerken
        
        String zoekOpBagagelabel = "SELECT COUNT(*) AS result FROM gevondenbagage WHERE bagagelabel = '" + bagagelabel + "'";
        String zoekCriteria = "SELECT * FROM gevondenbagage WHERE softhard = '" + hardSoftCase + "'";
        if (!kleur.equals("Other")) {
            zoekCriteria += " AND (kleur = '" + kleur + "' OR kleur = 'Other' )";
        }
        if (!hoogte.equals("Unknown")) {
            zoekCriteria += " AND (dikte = '" + hoogte + "' OR dikte = 'Unknown' )";
        } 
        if (!lengte.equals("Unknown")) {
            zoekCriteria += " AND (lengte = '" + lengte + "' OR lengte = 'Unknown' )";
        }
        if (!breedte.equals("Unknown")) {
            zoekCriteria += " AND (breedte = '" + breedte + "' OR breedte = 'Unknown' )";
        } 
        if (!merk.equals("Other")) {
            zoekCriteria += " AND (merk = '" + merk + "' OR merk = 'Other' )";
        }
        zoekCriteria += " AND bagagelabel IS null ";
        zoekCriteria += " AND (luchthavengevonden = '" + luchthavenAankomst + "' OR luchthavengevonden = '" + luchthavenVertrek + "' )";
        zoekCriteria += " AND status = 'notSolved'";
        zoekOpCriteria(zoekCriteria, zoekOpBagagelabel, bagagelabel);
    }

    private void zoekOpCriteria(String zoekCriteria, String zoekOpBagagelabel, String bagagelabel) {
        //arraylist om alle resultaten in op te slaan
        ArrayList<Integer> kofferIdResultaten = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement stmt = (Statement) conn.createStatement();
            Statement stmt2 = (Statement) conn.createStatement();
            Statement stmt3 = (Statement) conn.createStatement();
            ResultSet matchOpBagagelabel = stmt.executeQuery(zoekOpBagagelabel);
            while (matchOpBagagelabel.next()) {
                //als er geen match is op bagagelabel worden alle koffers die aan de zoekcriteria toegevoed aan de array
                if (matchOpBagagelabel.getInt("result") == 0) {
                    ResultSet matchendeKoffersResult = stmt2.executeQuery(zoekCriteria);
                    while (matchendeKoffersResult.next()) {
                        kofferIdResultaten.add(matchendeKoffersResult.getInt("gevondenkofferID"));
                    }
                } 
                //als er een match is op bagagelabel wordt alleen die toegevoegd aan de array
                else {
                    ResultSet resultaatBagageLabel = stmt3.executeQuery("SELECT * FROM gevondenbagage WHERE bagagelabel = '" + bagagelabel + "'");
                    while (resultaatBagageLabel.next()) {
                        kofferIdResultaten.add(resultaatBagageLabel.getInt("gevondenkofferID"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        //Dan worden de resultaten naar een tableview omgezet
        laatResultatenZien(kofferIdResultaten);
    }
    
    private void laatResultatenZien(ArrayList<Integer> kofferIdResultaten) {
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        //grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        root.setCenter(grid);
        
        //Als er helemaal geen resultaten zijn is de arraylist leeg en komt er een popup dat er geen match is.
        if(kofferIdResultaten.isEmpty()){
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            Button homeButton = new Button();
            homeButton.setText("Home");
            dialogVbox.getChildren().addAll(new Text("No match with lost lugage\nthe luggage has been added to the database\nclick the button to go to the homepage"), homeButton);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();

            homeButton.setOnAction((ActionEvent e) -> {
                Home home = new Home();
                home.start(primaryStage);
                dialog.close();
            });
        }else{
            try {
                Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                Statement st = conn.createStatement();
                //Krijgt alle informatie van alle koffers in de arraylist uit de database
                String selectString = "SELECT * FROM gevondenbagage WHERE gevondenKofferID = '" + kofferIdResultaten.get(0) + "'";
                for (int i = 1; i < kofferIdResultaten.size(); i++) {
                    selectString += "OR gevondenKofferID = '" + kofferIdResultaten.get(i) + "'";
                }
                ResultSet rs = st.executeQuery(selectString);
                ObservableList<bagageStuk> data = FXCollections.observableArrayList();
                while (rs.next()) {
                    this.kofferid = rs.getString("gevondenKofferID");
                    this.dlabel = rs.getString("bagagelabel");
                    this.kleur = rs.getString("kleur");
                    this.dikte = rs.getString("dikte");
                    this.lengte = rs.getString("lengte");
                    this.breedte = rs.getString("breedte");
                    this.luchthavengevonden = rs.getString("luchthavengevonden");
                    this.datum = rs.getString("datum");
                    this.bijzonderhede = rs.getString("bijzonderhede");
                    this.merk = rs.getString("merk");
                    this.softhard = rs.getString("softhard");
                    
                    data.add(new bagageStuk(kofferid, dlabel, kleur, dikte, lengte, 
                            breedte, luchthavengevonden, datum, softhard, bijzonderhede, merk));
                    table.setItems(data);
                }
            }catch (SQLException ed) {
                System.out.println(ed);     
            }
            
            table.setEditable(true);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
            //Label voor boven de table
            Label matchesLabel = new Label("Matches:");
            matchesLabel.setFont(new Font("Arial", 20));
            
            //alle kolommen voor de table
            TableColumn gevondenkofferIDcol = new TableColumn("ID");
            gevondenkofferIDcol.setMinWidth(10);
            gevondenkofferIDcol.setCellValueFactory(
                    new PropertyValueFactory<>("gevondenkofferID"));

            TableColumn bagagelabelcol = new TableColumn("Label");
            bagagelabelcol.setMinWidth(100);
            bagagelabelcol.setCellValueFactory(
                    new PropertyValueFactory<>("bagagelabel"));

            TableColumn kleurcol = new TableColumn("Color");
            kleurcol.setMinWidth(100);
            kleurcol.setCellValueFactory(
                    new PropertyValueFactory<>("kleur"));

            TableColumn diktecol = new TableColumn("Height");
            diktecol.setMinWidth(100);
            diktecol.setCellValueFactory(
                    new PropertyValueFactory<>("dikte"));

            TableColumn lengtecol = new TableColumn("Length");
            lengtecol.setMinWidth(100);
            lengtecol.setCellValueFactory(
                    new PropertyValueFactory<>("lengte"));

            TableColumn breedtecol = new TableColumn("Width");
            breedtecol.setMinWidth(100);
            breedtecol.setCellValueFactory(
                    new PropertyValueFactory<>("breedte"));

            TableColumn luchthavengevondencol = new TableColumn("Found");
            luchthavengevondencol.setMinWidth(120);
            luchthavengevondencol.setCellValueFactory(
                    new PropertyValueFactory<>("luchthavengevonden"));
            
            TableColumn datumcol = new TableColumn("Date");
            datumcol.setMinWidth(100);
            datumcol.setCellValueFactory(
                    new PropertyValueFactory<>("datum"));

            TableColumn softhardcol = new TableColumn("Soft/Hard Case");
            softhardcol.setMinWidth(150);
            softhardcol.setCellValueFactory(
                    new PropertyValueFactory<>("softhard"));

            TableColumn bijzonderhedecol = new TableColumn("Characteristics");
            bijzonderhedecol.setMinWidth(120);
            bijzonderhedecol.setCellValueFactory(
                    new PropertyValueFactory<>("bijzonderhede"));

            TableColumn merkcol = new TableColumn("Brand");
            merkcol.setMinWidth(100);
            merkcol.setCellValueFactory(
                    new PropertyValueFactory<>("merk"));
            
            //voeg alle columns toe aan de tableview
            table.getColumns().addAll(bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavengevondencol, datumcol, softhardcol, merkcol, bijzonderhedecol);

            Scene scene = new Scene(new Group(), 1200, 1000);
            primaryStage.setTitle("Matches");

            //zet de grootte van de tabel aan de hand van de grootte van het scherm
            scene.widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                    table.setMinWidth(((double)newSceneWidth - 10));
                    table.setMaxWidth(((double)newSceneWidth - 10));
                }
            });
            scene.heightProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                    table.setMinHeight((double)newSceneHeight - 200);
                    table.setMaxHeight((double)newSceneHeight - 200);
                }
            });
            
            Button match = new Button("Match");
            Button geenMatch = new Button("No match");
            GridPane buttons = new GridPane();
            buttons.setVgap(10);
            buttons.setHgap(10);
            buttons.setPadding(new Insets(25, 25, 25, 25));
            buttons.add(match, 0, 0);
            buttons.add(geenMatch, 3, 0);
            
            //als er een match is en daar wordt ook op gedrukt dan gebeurd dit
            match.setOnAction((ActionEvent e) -> {
                if(table.getSelectionModel().isEmpty() == false){
                    bagageStuk selected = table.getSelectionModel().getSelectedItem();
                    zetMatchInDatabase(selected.getGevondenkofferID());
                }
            });
            
            //als er geenMatch button word geklikt gebeurd dit.
            geenMatch.setOnAction((ActionEvent e) -> {
                final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(primaryStage);
                    VBox dialogVbox = new VBox(20);
                    Button homeButton = new Button();
                    homeButton.setText("Home");
                    dialogVbox.getChildren().addAll(new Text("This luggage has been added to\nthe database click the\nbutton to go to the homepage"), homeButton);
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();

                    homeButton.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent e){
                        Home home = new Home();
                        home.start(primaryStage);
                        dialog.close();
                    }}); 
            });


            final VBox vbox = new VBox(root);
            vbox.setSpacing(10);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(matchesLabel, table, buttons);

            ((Group) scene.getRoot()).getChildren().addAll(vbox);
            scene.getStylesheets().add("global/Style2.css");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        }
    }
    
    private void zetMatchInDatabase(String gevondenKofferID){
        //Verloren koffer bagage strings
        String  bagagelabelVerloren = null, voornaam = null, tussenvoegsel = null, 
                achternaam = null, telefoonnummer = null, email = null, 
                plaats = null, land = null, huisnr = null, straat = null, postcode = null;
        
        //gevonden koffer bagage strings
        String bagageLabelGevonden = null, kleurGevonden = null, merkGevonden = null, hoogteGevonden = null,
                    lengteGevonden = null, breedteGevonden = null, luchthavenGevonden = null, softHardGevonden = null, bijzonderhedenGevonden = null;
            
        //eerst wordt de status van de koffer op pending gezet, dan wordt er een pdf
        //gemaakt die naar het vliegveld wordt gemaild, daarna wordt er nog een mailtje
        //naar de klant gestuurd.
        try{
            Connection conn;
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
            String insertString = "INSERT INTO opgelost (customersID, verlorenkofferID, gevondenkofferID, datum) "
                    + "values ('"+customerId+"', '"+verlorenKofferID+"', '"+gevondenKofferID+"', curdate())";
            st.execute(insertString);
            
            String updateStatusVerl = "UPDATE verlorenbagage SET status = 'pending' WHERE verlorenkofferID = '"+verlorenKofferID+"'";
            String updateStatusGev = "UPDATE gevondenbagage SET status = 'pending' WHERE gevondenkofferID = '"+gevondenKofferID+"'";
            st.execute(updateStatusVerl);
            st.execute(updateStatusGev);
            
            //alle informatie over de verloren bagage en de klant
            String insertString2 = "SELECT * FROM verlorenbagage v LEFT JOIN customers c ON c.customersID = v.customersID LEFT JOIN afleveradres a ON a.VerlorenkofferID = v.verlorenkofferID WHERE v.verlorenkofferID = '" +verlorenKofferID +"'";
            ResultSet rs = st.executeQuery(insertString2);
            while (rs.next()) {
                bagagelabelVerloren = rs.getString("bagagelabel");
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
            }
            
            //alle informatie over gevondenbagage
            Statement st2 = conn.createStatement();
            String selectGevondenKoffer = "select * from gevondenbagage where gevondenkofferID = '" + gevondenKofferID+ "'";
            ResultSet rs1 = st2.executeQuery(selectGevondenKoffer);
            while (rs1.next()) {
                bagageLabelGevonden = rs1.getString("bagagelabel");
                kleurGevonden = rs1.getString("kleur");
                merkGevonden =rs1.getString("merk");
                hoogteGevonden = rs1.getString("dikte");
                lengteGevonden = rs1.getString("lengte");
                breedteGevonden = rs1.getString("breedte");
                softHardGevonden = rs1.getString("softhard");
                bijzonderhedenGevonden = rs1.getString("bijzonderhede");
                luchthavenGevonden = rs1.getString("luchthavengevonden");
            }
            
            String stuurNaarLuchthaven = "";
            if(land.equals("Netherlands")){
                stuurNaarLuchthaven = "Schiphol, Amsterdam";
            }if(land.equals("Spain")){
                stuurNaarLuchthaven = "El prat, Barcelona";
            }if(land.equals("Turkey")){
                stuurNaarLuchthaven = "Atatürk, Istanbul";
            }
            
            if(bagageLabelGevonden == null){
                bagageLabelGevonden = "Doesn't exist";
            }
     
            //maak nieuw document aan
            PDDocument document = new PDDocument();

            // voeg een lege pdf pagina toe
            PDPage blankPage = new PDPage();
            document.addPage( blankPage );        
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont font2 = PDType1Font.TIMES_ROMAN;

            //zorgt voor de datum en tijd
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            java.util.Date today = Calendar.getInstance().getTime();        
            String reportDate = df.format(today);

            //contentStream slaat de informatie op die erin moet
            PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
            // headline
            contentStream.beginText();
            contentStream.setFont( font, 15 );
            contentStream.moveTextPositionByAmount( 75, 700 );
            contentStream.drawString( "DHL shipment form" );
            contentStream.endText();
            //Datum 
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 650 );
            contentStream.drawString( "Date: " + reportDate);
            contentStream.endText();
            //luchthaven waar bagage heen moet
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 635 );
            contentStream.drawString( "Send to airport: "  + stuurNaarLuchthaven );
            contentStream.endText();
            //Luchthaven waar het gevonden is
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 620 );
            contentStream.drawString( "From airport: "  + luchthavenGevonden );
            contentStream.endText();
            //begin reiziger informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 590 );
            contentStream.drawString( "Customer information: " );
            contentStream.endText();
            //Naam
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 575 );
            contentStream.drawString( "Name: " + voornaam + " " + tussenvoegsel + " " + achternaam);
            contentStream.endText();
            //Adres
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 560 );
            contentStream.drawString( "Address: " + straat + " " + huisnr );
            contentStream.endText();
            //Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 545 );
            contentStream.drawString( "Home town: " + plaats );
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
            contentStream.drawString( "Country: " + land);
            contentStream.endText();
            //Telefoon
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 500 );
            contentStream.drawString( "Phonenumber: " + telefoonnummer );
            contentStream.endText();
            //E-mail
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 485 );
            contentStream.drawString( "E-mail: " + email);
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
            contentStream.drawString( "Label number: " + bagageLabelGevonden);
            contentStream.endText(); 
            //Bagage informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 410 );
            contentStream.drawString( "Luggage information: " );
            contentStream.endText();
            //Type
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 395 );
            contentStream.drawString( "Type: " + softHardGevonden);
            contentStream.endText();
            //Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 380);
            contentStream.drawString( "Brand: " + merkGevonden);
            contentStream.endText();
            //Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 365 );
            contentStream.drawString( "Colour: " + kleurGevonden);
            contentStream.endText();
            //Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 350 );
            contentStream.drawString( "Dimensions (height x width x depth): "+ hoogteGevonden + ", "+ lengteGevonden +", " + breedteGevonden);
            contentStream.endText();
            //bijzonderheden
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 335 );
            contentStream.drawString( "Characteristics: "+bijzonderhedenGevonden);
            contentStream.endText();
            
            // Make sure that the content stream is closed:
            contentStream.close();
            
            //wordt gekeken of de map pdfopslaan bestaat, zo nee dan wordt deze aangemaakt op de computer.
            JFileChooser fr = new JFileChooser();
            FileSystemView fw = fr.getFileSystemView();
            Path test = Paths.get(fw.getDefaultDirectory() + "\\pdfopslaan" );
            if(!Files.exists(test)){
               File file = new File(fw.getDefaultDirectory() + "\\pdfopslaan");
               file.mkdir();
            }
            
            //pdf wordt opgeslagen in die map
            String opslaanAls = fw.getDefaultDirectory() + "\\pdfopslaan\\Match luggage " + gevondenKofferID + "matches with "+ verlorenKofferID+ ".pdf";
            document.save(opslaanAls);
            document.close();
            
            
            //mailtje naar de klant
            String body = "Dear Sir/Madam, \nYour luggage may have been found and will be sent to: "+stuurNaarLuchthaven+"\n Luggage label: "
                    + bagagelabelVerloren + "\nPlease visit the customer service of Corendon at "+ stuurNaarLuchthaven + " to verify it's your luggage \n\n"
                    + "Yours faithfully \nCorendon";
            WachtwoordVergeten sendMail = new WachtwoordVergeten();
            sendMail.sendEmail(WachtwoordVergeten.EMAIL_USER_NAME, WachtwoordVergeten.EMAIL_PASSWORD, email, "Luggage might be found", body, null);
            
            //mail naar de luchthaven met de pdf als bijlage
            //voor nu even onze eigen email gedaan omdat de mail applicatie anders
            //een foutmelding geeft
            String luchthavenEmail = "";
            if(luchthavenGevonden.equals("Schiphol, Amsterdam")){
                //luchthavenEmail = "schiphol@corendonbalie.com";
                luchthavenEmail = "tim-vlaar@hotmail.com";
            }if(luchthavenGevonden.equals("El prat, Barcelona")){
                //luchthavenEmail = "elprat@corendonbalie.com";
                luchthavenEmail = "tim-vlaar@hotmail.com";
            }if(luchthavenGevonden.equals("Atatürk, Istanbul")){
                //luchthavenEmail = "atatürk@corendonbalie.com";
                luchthavenEmail = "tim-vlaar@hotmail.com";
            }
            sendMail.sendEmail(WachtwoordVergeten.EMAIL_USER_NAME, WachtwoordVergeten.EMAIL_PASSWORD, luchthavenEmail, "Send luggage back", "Send this back", opslaanAls);
            
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        Button homeButton = new Button();
        homeButton.setText("Home");
        dialogVbox.getChildren().addAll(new Text("The customer has received an email\nthe airport will send the luggage back\nclick home to go to the\nhomepage"), homeButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        homeButton.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent e){
            Home home = new Home();
            home.start(primaryStage);
            dialog.close();
        }});
    }
    
    
    
    
    //deze klasse is nodig om de tableview te kunnen maken
    private TableView<bagageStuk> table = new TableView<>();
    private String kofferid, dlabel, kleur, dikte, lengte, breedte, luchthavengevonden, datum, softhard, bijzonderhede, merk;
    
    public static class bagageStuk{
        private SimpleStringProperty gevondenkofferID, bagagelabel, kleur,
                dikte, lengte, breedte, luchthavengevonden, datum,
                softhard, bijzonderhede, merk;
        
        private bagageStuk(String gevondenkofferID, String bagagelabel, String kleur, String dikte, String lengte, String breedte, String luchthavengevonden, String datum, String softhard, String bijzonderhede, String merk) {
            this.gevondenkofferID = new SimpleStringProperty(gevondenkofferID);
            this.bagagelabel = new SimpleStringProperty(bagagelabel);
            this.kleur = new SimpleStringProperty(kleur);
            this.dikte = new SimpleStringProperty(dikte);
            this.lengte = new SimpleStringProperty(lengte);
            this.breedte = new SimpleStringProperty(breedte);
            this.luchthavengevonden = new SimpleStringProperty(luchthavengevonden);
            this.datum = new SimpleStringProperty(datum);
            this.softhard = new SimpleStringProperty(softhard);
            this.bijzonderhede = new SimpleStringProperty(bijzonderhede);
            this.merk = new SimpleStringProperty(merk);
        }
    
    
        public String getGevondenkofferID() {
            return gevondenkofferID.get();
        }

        public void setGevondenkofferID(String gevondenkofferID) {
            this.gevondenkofferID.set(gevondenkofferID);
        }

        public String getBagagelabel() {
            return bagagelabel.get();
        }

        public void setBagagelabel(String gevondenkofferID) {
            this.bagagelabel.set(gevondenkofferID);
        }

        public String getKleur() {
            return kleur.get();
        }

        public void setKleur(String gevondenkofferID) {
            this.kleur.set(gevondenkofferID);
        }

        public String getDikte() {
            return dikte.get();
        }

        public void setDikte(String gevondenkofferID) {
            this.dikte.set(gevondenkofferID);
        }

        public String getLengte() {
            return lengte.get();
        }

        public void setLengte(String gevondenkofferID) {
            this.lengte.set(gevondenkofferID);
        }

        public String getBreedte() {
            return breedte.get();
        }

        public void setBreedte(String gevondenkofferID) {
            this.breedte.set(gevondenkofferID);
        }

        public String getLuchthavengevonden() {
            return luchthavengevonden.get();
        }

        public void setLuchthavengevonden(String gevondenkofferID) {
            this.luchthavengevonden.set(gevondenkofferID);
        }

        public String getDatum() {
            return datum.get();
        }

        public void setDatum(String gevondenkofferID) {
            this.datum.set(gevondenkofferID);
        }

        public String getSofthard() {
            return softhard.get();
        }

        public void setSofthard(String gevondenkofferID) {
            this.softhard.set(gevondenkofferID);
        }

        public String getBijzonderhede() {
            return bijzonderhede.get();
        }

        public void setBijzonderhede(String gevondenkofferID) {
            this.bijzonderhede.set(gevondenkofferID);
        }

        public String getMerk() {
            return merk.get();
        }

        public void setMerk(String merk) {
            this.merk.set(merk);
        }
    
    }
    

}
