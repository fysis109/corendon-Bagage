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
import java.util.ArrayList;
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
    private String Lengte;
    private String Dikte;
    private String Breedte;
    private String Luchthavengevonden;
    private String Bijzonderhede;
    private String Merk;
    private String Softhard;
    
   
    

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
            String insertString2 = "SELECT * FROM gevondenbagage,customers WHERE gevondenkofferID =" + verlorenKofferID +  "AND customersID =" + this.customerId;
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
            contentStream.drawString( "Teks" );
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
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input E-mail
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 485 );
            contentStream.drawString( "Teks" );
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
            contentStream.drawString( this.Luchthavengevonden );
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

            document.save("Gevonden bagage.pdf");
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
