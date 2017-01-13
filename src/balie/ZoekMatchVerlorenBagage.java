/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balie;

import global.Home;
import global.MenuB;
import global.Mysql;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import login.PDFregister;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class ZoekMatchVerlorenBagage {
    
    private int customerId;
    private Stage primaryStage = new Stage();
    private int verlorenKofferID;
    private final Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    // Bagage info
    private String Bagagelabel;
    private String Kleur;
    private String Bijzonderhede;
    private String Merk;
    private String Softhard;
    private String Luchthavenaankomst;
    private String Luchthavengevonden;
    //klanten info
    private String Voornaam;
    private String Tussenvoegsel;
    private String Achternaam;
    private String Telefoonnummer;
    private String Email;
    private String Plaats;
    private String Land;
    private String Huisnr;
    private String Straat;
    private String Postcode;

    
   
    

    public void maakZoekString(int customerId, Stage primaryStage, int verlorenKofferID, String bagagelabel, String kleur, String hoogte,
            String lengte, String breedte, String luchthavenVertrek,
            String luchthavenAankomst, String bijzonderheden, String merk, String hardSoftCase) {
        
        this.primaryStage = primaryStage;
        this.verlorenKofferID = verlorenKofferID;
        this.customerId = customerId;
        
        String zoekOpBagagelabel = "SELECT COUNT(*) AS result FROM gevondenbagage WHERE bagagelabel = '" + bagagelabel + "'";
        String zoekCriteria = "SELECT * FROM gevondenbagage WHERE bagagelabel = '' ";
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
        zoekCriteria += " AND softhard = '" + hardSoftCase + "'";
        zoekCriteria += " AND (luchthavengevonden = '" + luchthavenAankomst + "' OR luchthavengevonden = '" + luchthavenVertrek + "' )";
        zoekCriteria += " AND status = 'notSolved'";
        System.out.println(zoekCriteria);
        zoekOpCriteria(zoekCriteria, zoekOpBagagelabel, bagagelabel);
    }

    private void zoekOpCriteria(String zoekCriteria, String zoekOpBagagelabel, String bagagelabel) {
        ArrayList<Integer> kofferIdResultaten = new ArrayList<>();
        Connection conn;
        try {
            System.out.println(zoekCriteria + "\n" + zoekOpBagagelabel + "\n" + bagagelabel);
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement stmt = (Statement) conn.createStatement();
            Statement stmt2 = (Statement) conn.createStatement();
            Statement stmt3 = (Statement) conn.createStatement();
            ResultSet matchOpBagagelabel = stmt.executeQuery(zoekOpBagagelabel);
            while (matchOpBagagelabel.next()) {
                if (matchOpBagagelabel.getInt("result") == 0) {
                    ResultSet matchendeKoffersResult = stmt2.executeQuery(zoekCriteria);
                    while (matchendeKoffersResult.next()) {
                        kofferIdResultaten.add(matchendeKoffersResult.getInt("gevondenkofferID"));
                    }
                } else {
                    ResultSet resultaatBagageLabel = stmt3.executeQuery("SELECT * FROM gevondenbagage WHERE bagagelabel = '" + bagagelabel + "'");
                    while (resultaatBagageLabel.next()) {
                        kofferIdResultaten.add(resultaatBagageLabel.getInt("gevondenkofferID"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        laatResultatenZien(kofferIdResultaten);
    }
    
    private TableView<Table> table = new TableView<>();
    private String kofferid, dlabel, kleur, dikte, lengte, breedte, luchthavengevonden, datum, softhard, bijzonderhede, merk;
    
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
        
        if(kofferIdResultaten.isEmpty()){
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            Button test = new Button();
            test.setText("JA");
            dialogVbox.getChildren().addAll(new Text("No match with lost lugage"), test);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();

            test.setOnAction((ActionEvent e) -> {
                Home home = new Home();
                home.start(primaryStage);
                dialog.close();
            });
        }else{
            try {
                //maak connectie met de database
                Connection conn;
                conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                Statement st = conn.createStatement();

                String selectString = "SELECT * FROM gevondenbagage WHERE gevondenKofferID = '" + kofferIdResultaten.get(0) + "'";
                for (int i = 1; i < kofferIdResultaten.size(); i++) {
                    selectString += "OR gevondenKofferID = '" + kofferIdResultaten.get(i) + "'";
                }
                System.out.println(selectString);
                ResultSet rs = st.executeQuery(selectString);
                ObservableList<Table> data = FXCollections.observableArrayList();
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
                    
                    data.add(new Table(kofferid, dlabel, kleur, dikte, lengte, 
                            breedte, luchthavengevonden, datum, softhard, bijzonderhede, merk));
                    table.setItems(data);
                }
            }
            catch (SQLException ed) {
                    
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
            
            


        
        
        table.getColumns().addAll(bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavengevondencol, datumcol, softhardcol, merkcol, bijzonderhedecol);

        Scene scene = new Scene(new Group(), 1200, 1000);
        primaryStage.setTitle("Table");
        
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
        
        match.setOnAction((ActionEvent e) -> {
            if(table.getSelectionModel().isEmpty() == false){
                Table selected = table.getSelectionModel().getSelectedItem();
                zetMatchInDatabase(selected.getGevondenkofferID());
            }
        });
        
        
        final VBox vbox = new VBox(root);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(matchesLabel, table, match);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
            
        }
    }
    
    private void zetMatchInDatabase(String gevondenKofferID){
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
            /*
             * JIORGOS mail naar klant dat koffer is gevonden en dat ie wordt opgestuurd naar zijn of haar afleveradres, dat afleveradres er dus ook even bij zetten
             * en pdf voor vliegtuig welk bagage stuk waar heen moet
            */
            
        Statement kofferinfo = conn.createStatement();
            String insertString2 = "SELECT * FROM verlorenbagage v LEFT JOIN customers c ON c.customersID = v.customersID LEFT JOIN afleveradres a ON a.VerlorenkofferID = v.verlorenkofferID WHERE v.verlorenkofferID == " +verlorenKofferID;
            ResultSet rs = kofferinfo.executeQuery(insertString2);
            while (rs.next()) {
                    this.Bagagelabel = rs.getString("bagagelabel");
                    this.Kleur = rs.getString("kleur");
                    this.Bijzonderhede = rs.getString("bijzonderhede");
                    this.Merk = rs.getString("merk");
                    this.Softhard = rs.getString("softhard");
                    this.Voornaam = rs.getString("voornaam");
                    this.Tussenvoegsel = rs.getString("tussenvoegsel");
                    this.Achternaam = rs.getString("achternaam");
                    this.Telefoonnummer = rs.getString("telefoonnummer");
                    this.Email = rs.getString("email");
                    this.Plaats = rs.getString("Plaats");
                    this.Land = rs.getString("Land");
                    this.Huisnr = rs.getString("Huisnumer");
                    this.Postcode = rs.getString("Postcode");
                    this.Straat = rs.getString("Straat"); 
                    this.Luchthavenaankomst = rs.getString("luchthavenaankomst");
                    }
            
            Statement kofferinfo2 = conn.createStatement();
            String insertString3 = "select * from gevondenbagage where gevondenkofferID =" + gevondenKofferID;
            ResultSet rs1 = kofferinfo.executeQuery(insertString3);
            while (rs1.next()) {
               this.Luchthavengevonden = rs1.getString("luchthavengevonden");
                    }
           

                 
        }catch (SQLException ed) {
                  
        }
        // pdf verloren kofffer match
           // Create a new empty document
        PDDocument document = new PDDocument();

        // Create a new blank page and add it to the document
        PDPage blankPage = new PDPage();
        document.addPage( blankPage );
         Connection conn;
         
        try {
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont font2 = PDType1Font.TIMES_ROMAN;
            
            // Create an instance of SimpleDateFormat used for formatting 
            // the string representation of date (month/day/year)
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            // Get the date today using Calendar object.
            java.util.Date today = Calendar.getInstance().getTime();        
            // Using DateFormat format method we can create a string 
            // representation of a date with the defined format.
            String reportDate = df.format(today);

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
            // headline
            contentStream.beginText();
            contentStream.setFont( font, 15 );
            contentStream.moveTextPositionByAmount( 75, 700 );
            contentStream.drawString( "Match found form" );
            contentStream.endText();
            
            //Datum 
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 175, 650 );
            contentStream.drawString( "Date and time: " );
            contentStream.endText();
            //luchthaven
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 150, 635 );
            contentStream.drawString( "Airport: " );
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
            contentStream.drawString( "Name: " );
            contentStream.endText();
            //Adres
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 560 );
            contentStream.drawString( "Address: " );
            contentStream.endText();
            //Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 152, 545 );
            contentStream.drawString( "Home town: " );
            contentStream.endText();
            //Postcode
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 165, 530 );
            contentStream.drawString( "Zip code: " );
            contentStream.endText();
            //Land
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 185, 515 );
            contentStream.drawString( "Country: " );
            contentStream.endText();
            //Telefoon
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 167, 500 );
            contentStream.drawString( "Telephone: " );
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
            contentStream.drawString( "Luggage label information: " );
            contentStream.endText();
            //Label nummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 140, 440 );
            contentStream.drawString( "Label number: " );
            contentStream.endText();
            //Bestemming
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 150, 425 );
            contentStream.drawString( "Destination: " );
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
            contentStream.moveTextPositionByAmount( 187, 365 );
            contentStream.drawString( "Type: " );
            contentStream.endText();
            //Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 185, 350);
            contentStream.drawString( "Brand: " );
            contentStream.endText();
            //Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 184, 335 );
            contentStream.drawString( "Colour: " );
            contentStream.endText();
            //Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 155, 320 );
            contentStream.drawString( "Brand: " );
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
            
            //input Datum
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 650 );
            contentStream.drawString( reportDate );
            contentStream.endText();
            //input luchthaven
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 635 );
            contentStream.drawString( this.luchthavengevonden );
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
            contentStream.drawString( this.Straat + " " + this.Huisnr );
            contentStream.endText();
            //input Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 545 );
            contentStream.drawString( this.Plaats );
            contentStream.endText();
            //input Postcode
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 530 );
            contentStream.drawString( this.Postcode );
            contentStream.endText();
            //input Land
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 515 );
            contentStream.drawString( this.Land );
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
            //input Bestemming
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 425 );
            contentStream.drawString( this.Luchthavenaankomst );
            contentStream.endText();
            //input Type
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 365 );
            contentStream.drawString( this.Softhard );
            contentStream.endText();
            //input Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 350 );
            contentStream.drawString( this.Merk );
            contentStream.endText();
            //input Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 335 );
            contentStream.drawString( this.Kleur );
            contentStream.endText();
            //input Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 320 );
            contentStream.drawString( this.Bijzonderhede );
            contentStream.endText();
            
            
            
            


            // Make sure that the content stream is closed:
            contentStream.close();

            document.save("Match bagage.pdf");
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(PDFregister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     

    public static class Table{
        private SimpleStringProperty gevondenkofferID, bagagelabel, kleur,
                dikte, lengte, breedte, luchthavengevonden, datum,
                softhard, bijzonderhede, merk;
        
        private Table(String gevondenkofferID, String bagagelabel, String kleur, String dikte, String lengte, String breedte, String luchthavengevonden, String datum, String softhard, String bijzonderhede, String merk) {
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
