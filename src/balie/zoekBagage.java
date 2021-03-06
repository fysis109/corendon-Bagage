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

public class zoekBagage {
    
    //mysql informatie
    private Mysql MYSQL = new Mysql();
    private final String USERNAME = MYSQL.getUsername();
    private final String PASSWORD = MYSQL.getPassword();
    private final String CONN_STRING = MYSQL.getUrlmysql();   
    
    private int gevKofID;
    
    public void maakZoekString(Stage primaryStage, int gevondenKofferID, String bagageLabel, 
            String kleur, String hoogte, String lengte, String breedte, String luchthaven, 
            String bijzonderheden, String merk, String softhard){
        
        String zoekBagage = null;
        gevKofID = gevondenKofferID;
        
        //maakt een zoekstring als er een bagagelabel is ingevuld bij de verloren bagage dan zoekt ie alleen daarop
        //anders alleen op de kenmerken
        if(bagageLabel != null){
            zoekBagage = "SELECT verlorenkofferID FROM verlorenbagage WHERE bagagelabel = '"+bagageLabel+"'";
        }else{
            zoekBagage = "SELECT verlorenkofferID FROM verlorenbagage WHERE (luchthavenvertrokken = '"+luchthaven+"' OR luchthavenaankomst = '"+luchthaven+"')";
            if(!kleur.equals("Other")){
                zoekBagage += " AND (kleur = '" + kleur + "' OR kleur = 'Other' )";
            }
            if (!hoogte.equals("Unknown")) {
                zoekBagage += " AND (dikte = '" + hoogte + "' OR dikte = 'Unknown' )";
            }
             if (!lengte.equals("Unknown")) {
                zoekBagage += " AND (lengte = '" + lengte + "' OR lengte = 'Unknown' )";
            }
            if (!breedte.equals("Unknown")) {
                zoekBagage += " AND (breedte = '" + breedte + "' OR breedte = 'Unknown' )";
            } 
            if (!merk.equals("Other")) {
                zoekBagage += " AND (merk = '" + merk + "' OR merk = 'Other' )";
            } 
            zoekBagage += " AND softhard = '"+softhard+"'";            
        }
        zoekBagage += " AND status = 'notSolved' ";
        zoekEnShow(primaryStage, zoekBagage);
    }
    
    private void zoekEnShow(Stage primaryStage, String zoekBagage){
        ArrayList<Integer> kofferIdResultaten = new ArrayList<>();
        try{
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(zoekBagage);
            while(rs.next()){
                kofferIdResultaten.add(rs.getInt("verlorenkofferID"));
            }
        }catch(SQLException ed){
            System.out.println(ed);
        }
        laatResultatenZien(primaryStage, kofferIdResultaten);
    }
    
    
    private TableView<bagageStuk> table = new TableView<bagageStuk>();
   
    
    private void laatResultatenZien(Stage primaryStage, ArrayList<Integer> kofferIdResultaten){
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
        
        //als er een match is en dus is de array niet leeg, dan worden die allemaal getoond op het scherm
        if(!kofferIdResultaten.isEmpty()) {
            try {
                Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                Statement st = conn.createStatement();
                String selectString = "SELECT * FROM verlorenbagage WHERE verlorenkofferID = '" + kofferIdResultaten.get(0) + "'";
                for (int i = 1; i < kofferIdResultaten.size(); i++) {
                    selectString += "OR verlorenkofferID = '" + kofferIdResultaten.get(i) + "'";
                }

                ResultSet databaseResponse2 = st.executeQuery(selectString);
                ObservableList<bagageStuk> data = FXCollections.observableArrayList();
                while (databaseResponse2.next()) {

                    //database response verwerken
                    String kofferid = databaseResponse2.getString("verlorenkofferID");
                    String dlabel = databaseResponse2.getString("bagagelabel");
                    String kleur = databaseResponse2.getString("kleur");
                    String dikte = databaseResponse2.getString("dikte");
                    String lengte = databaseResponse2.getString("lengte");
                    String breedte = databaseResponse2.getString("breedte");
                    String luchthavengevonden = databaseResponse2.getString("luchthavenvertrokken");
                    String luchthavenaankomst = databaseResponse2.getString("luchthavenaankomst");
                    String datum = databaseResponse2.getString("datum");
                    String softhard = databaseResponse2.getString("softhard");
                    String bijzonderhede = databaseResponse2.getString("bijzonderheden");
                    String merk = databaseResponse2.getString("merk");

                    data.add(new bagageStuk(kofferid, dlabel, kleur, dikte, lengte, breedte, luchthavengevonden, luchthavenaankomst, datum, softhard, bijzonderhede, merk));

                    table.setItems(data);

                }
            } catch (SQLException ed) {
                System.err.println(ed);
            }

            
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

            TableColumn luchthavengevondencol = new TableColumn("Departure");
            luchthavengevondencol.setMinWidth(120);
            luchthavengevondencol.setCellValueFactory(
                    new PropertyValueFactory<>("luchthavengevonden"));

            TableColumn luchthavenaankomstcol = new TableColumn("Arrival");
            luchthavenaankomstcol.setMinWidth(120);
            luchthavenaankomstcol.setCellValueFactory(
                    new PropertyValueFactory<>("luchthavenaankomst"));

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

            Scene scene = new Scene(new Group(), 1250, 920);
            primaryStage.setTitle("Table");
            
            table.getColumns().addAll(gevondenkofferIDcol, bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavengevondencol, luchthavenaankomstcol, datumcol, softhardcol, merkcol, bijzonderhedecol);

            table.setEditable(true);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
            //veranderd de grootte van de tableview aan de hand van de grootte van het scherm
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
            
            match.setOnAction((ActionEvent e) -> {
                if(table.getSelectionModel().isEmpty() == false){
                    bagageStuk selected = table.getSelectionModel().getSelectedItem();
                    zetMatchInDatabase(selected.getGevondenkofferID(), primaryStage);
                }
            });
            
            geenMatch.setOnAction((ActionEvent e) -> {
            final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Button test = new Button();
                test.setText("Home");
                dialogVbox.getChildren().addAll(new Text("This luggage has been added to\nthe database click the\nbutton to go to the homepage"), test);
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
                
                test.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent e){
                    Home home = new Home();
                    home.start(primaryStage);
                    dialog.close();
                }}); 
            });

            VBox vbox = new VBox(root);
            vbox.setSpacing(10);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(matchesLabel, table, buttons);

            ((Group) scene.getRoot()).getChildren().addAll(vbox);
            scene.getStylesheets().add("global/Style2.css");
            primaryStage.setScene(scene);
            primaryStage.show();
        } 
        //als er dus geen matches zijn
        else {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            Button test = new Button();
            test.setText("Home");
            dialogVbox.getChildren().addAll(new Text("No match with lost lugage\nthe luggage has been added to the database\nclick the button to go to the homepage"), test);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();

            test.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    Home home = new Home();
                    home.start(primaryStage);
                    dialog.close();

                }
            });
        }

    }
    
    
    private void zetMatchInDatabase(String verlorenKofferID, Stage primaryStage){
        int klantID = 0;
        
        //Verloren koffer bagage strings
        String  bagagelabelVerloren = null, voornaam = null, tussenvoegsel = null, 
                achternaam = null, telefoonnummer = null, email = null, 
                plaats = null, land = null, huisnr = null, straat = null, postcode = null;
        
        //gevonden koffer bagage strings
        String bagageLabelGevonden = null, kleurGevonden = null, merkGevonden = null, hoogteGevonden = null,
                    lengteGevonden = null, breedteGevonden = null, luchthavenGevonden = null, softHardGevonden = null, bijzonderhedenGevonden = null;
        
        try {
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
            
            //informatie over de eigenaar van de verlorenkoffer
            String selectKlantID = "SELECT customersID FROM verlorenbagage WHERE verlorenkofferID = '"+verlorenKofferID+"'";
            ResultSet resultKlantID = st.executeQuery(selectKlantID);
            while(resultKlantID.next()){
                klantID = resultKlantID.getInt("customersID");
            }
            
            String insertOpgelost = "INSERT INTO opgelost (customersID, verlorenkofferID, gevondenkofferID, datum) "
                    + "values ('"+klantID+"', '"+verlorenKofferID+"', '"+gevKofID+"', curdate())";
            st.execute(insertOpgelost);
            
            String updateStatusVerl = "UPDATE verlorenbagage SET status = 'pending' WHERE verlorenkofferID = '"+verlorenKofferID+"'";
            String updateStatusGev = "UPDATE gevondenbagage SET status = 'pending' WHERE gevondenkofferID = '"+gevKofID+"'";
            st.execute(updateStatusVerl);
            st.execute(updateStatusGev);
            
            //alle informatie over de verloren koffer en klant
            Statement kofferinfo = conn.createStatement();
            String insertString2 = "SELECT * FROM verlorenbagage v LEFT JOIN customers c ON c.customersID = v.customersID LEFT JOIN afleveradres a ON a.VerlorenkofferID = v.verlorenkofferID WHERE v.verlorenkofferID = '" +verlorenKofferID +"'";
            ResultSet rs = kofferinfo.executeQuery(insertString2);
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
            
            //alle informatie over de gevonden koffer en de klant
            String insertString3 = "select * from gevondenbagage where gevondenkofferID = '" + gevKofID+ "'";
            ResultSet rs1 = kofferinfo.executeQuery(insertString3);
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
     
            //nieuw pdf document wordt aangemaakt
            PDDocument document = new PDDocument();
            PDPage blankPage = new PDPage();
            document.addPage( blankPage );
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont font2 = PDType1Font.TIMES_ROMAN;
            
            //zorgt voor de tijd en datum
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
            contentStream.drawString( "Phonenumber " + telefoonnummer );
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
            String opslaanAls = fw.getDefaultDirectory() + "\\pdfopslaan\\Match luggage " + gevKofID + "matches with "+ verlorenKofferID+ ".pdf";
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
        } catch (SQLException | IOException ex) {
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

    
    //deze klasse is nodig om de tableview te maken
    public static class bagageStuk {

        private SimpleStringProperty gevondenkofferID, bagagelabel, kleur,
                dikte, lengte, breedte, luchthavengevonden, luchthavenaankomst, datum,
                softhard, bijzonderhede, merk;

        private bagageStuk(String gevondenkofferID, String bagagelabel, String kleur, String dikte, String lengte, String breedte, String luchthavengevonden, String luchthavenaankomst, String datum, String softhard, String bijzonderhede, String merk) {
            this.gevondenkofferID = new SimpleStringProperty(gevondenkofferID);
            this.bagagelabel = new SimpleStringProperty(bagagelabel);
            this.kleur = new SimpleStringProperty(kleur);
            this.dikte = new SimpleStringProperty(dikte);
            this.lengte = new SimpleStringProperty(lengte);
            this.breedte = new SimpleStringProperty(breedte);
            this.luchthavengevonden = new SimpleStringProperty(luchthavengevonden);
            this.luchthavenaankomst = new SimpleStringProperty(luchthavenaankomst);
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

        public String getLuchthavenaankomst() {
            return luchthavenaankomst.get();
        }

        public void setLuchthavenaankomst(String luchthavenaankomst) {
            this.luchthavenaankomst.set(luchthavenaankomst);
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