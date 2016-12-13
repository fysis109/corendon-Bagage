package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class GevKofferReg {
    
    //Strings
    String InputBagageNummer = null;
    String kofferKleur = null;
    String merkKoffer = null;
    String breedteKoffer = null;
    String lengteKoffer = null;
    String dikteKoffer = null;
    String locatieKoffer = null; 
    
    //mysql connectie
    Mysql mysql = new Mysql();
    Home Home = new Home();
    
    //tableview person
    private TableView<Person> table = new TableView<Person>();
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    public static String rol;    
    
    public void start(Stage primaryStage) {
       
        
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);
        
        //grind
        primaryStage.setTitle("Gevonden koffer.");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        root.setCenter(grid);
        
        //Titel    
        Label scenetitle = new Label("Gevonden koffer.");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        scenetitle.setAlignment(Pos.BOTTOM_CENTER);
        grid.add(scenetitle, 0, 0);

        //bagage nummer text
        Label TextKofferNummer = new Label("Vul het bagagae nummer in: ");
        grid.add(TextKofferNummer, 0, 1);

        //input veld bagage nummer
        TextField InputBagageNummer = new TextField();
        grid.add(InputBagageNummer, 1, 1);

        //text kleur koffer
        Label TextKleurKoffer = new Label("Vul de kleur van de koffer in: ");
        grid.add(TextKleurKoffer, 0, 2);
        
        //drop down box kleur koffer
        final ComboBox kleurKofferComboBox = new ComboBox();
         kleurKofferComboBox.getItems().addAll(
            "blauw",
            "geel",
            "zwart",
            "grijs",
            "bruin",
            "other"
        );

        //update string merk koffer 
        kleurKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                kofferKleur = t1;
            }    
        });    
         
        //add drop down box kleur in scene
        grid.add(kleurKofferComboBox, 1, 2);
        
        //text merk koffer
        Label TextMerkKoffer = new Label("Fill the Luggage tag brand in: ");
        grid.add(TextMerkKoffer, 0, 3);       
        
        //drop down box merk koffer
        final ComboBox merkKofferComboBox = new ComboBox();
        merkKofferComboBox.getItems().addAll(
            "American Tourister",
            "Eastpak ",
            "March",
            "Porsche Design",
            "Rimowa",
            "Samsonite",
            "Swiss Wenger",
            "Ted Baker",
            "The North Face",
            "Tumi",
            "Victorinox",
            "other"
        );
         
        //add koffer merk in scene
        grid.add(merkKofferComboBox, 1, 3);

        //update string merk koffer 
        merkKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                merkKoffer = t1;
            }    
        });        
        
        //text hoogte koffer
        Label TextHoogteKoffer = new Label("Hoogte van de koffer in cm: ");
        grid.add(TextHoogteKoffer, 0, 4);       
        
        //drop down box hoogte koffer
        final ComboBox dikteKofferComboBox = new ComboBox();
        dikteKofferComboBox.getItems().addAll(
            "10cm-15cm",
            "15cm-20cm",
            "20cm-25cm",
            "25cm-30cm",
            "35cm-40cm",
            "40cm-45cm",
            "unknown"
        );
         
        //add mark koffer in scene
        grid.add(dikteKofferComboBox, 1, 4);       
        
        //update string lengte koffer 
        dikteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                dikteKoffer = t1;
            }    
        });
        
        //text lengte koffer
        Label TextLengteKoffer = new Label("Length of Luggage tag in cm: ");
        grid.add(TextLengteKoffer, 0, 5);       
        
        //drop down box hoogte koffer
        final ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
            "30cm-40cm",
            "40cm-50cm",
            "50cm-60cm",
            "60cm-70cm",
            "70cm-80cm",
            "unknown"
        );
         
        //add mark koffer in scene
        grid.add(lengteKofferComboBox, 1, 5); 
        
        //update string lengte koffer 
        lengteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                lengteKoffer = t1;
            }    
        });
        
        //text dikte koffer
        Label TextBreedeKoffer = new Label("Height of case in cm: ");
        grid.add(TextBreedeKoffer, 0, 6);       
        
        //drop down box hoogte koffer
        final ComboBox breedteKofferComboBox = new ComboBox();
        breedteKofferComboBox.getItems().addAll(
            "20cm-30cm",
            "30cm-40cm",
            "40cm-50cm",
            "50cm-60cm",
            "unknown"
        );
         
        //add mark koffer in scene
        grid.add(breedteKofferComboBox, 1, 6); 
        
        //update string dikte koffer
        breedteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                breedteKoffer = t1;
            }    
        });
        
        //text dikte koffer
        Label TextlocatieKoffer = new Label("Locatie waar de koffer is gevonden: ");
        grid.add(TextlocatieKoffer, 0, 7);          
        
        //locatie koffer drop down
        ComboBox locatieKofferComboBox = new ComboBox();
        locatieKofferComboBox.getItems().addAll(
            "Schiphol, Amsterdam",
            "El Prat, Barcelona",
            "Atatürk, Istanbul" 
        );
        
        //add locatie koffer in scene
        grid.add(locatieKofferComboBox, 1, 7); 
        
        //update string waarvan de koffer is gevonden
        locatieKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                locatieKoffer = t1;
            }    
        });
        
        //bagage nummer text
        Label TextBijzonderheden = new Label("Zijn er bijzondere dingen: ");
        grid.add(TextBijzonderheden, 0, 8);

        //input veld bagage nummer
        TextArea Bijzonderheden = new TextArea();
        Bijzonderheden.setMaxSize (255, 50);
        grid.add(Bijzonderheden, 1, 8);
        
        
        //button voor registeren
        Button registreerInformatie = new Button("Registreer");
        grid.add(registreerInformatie, 1,9);
        
        //button event maken
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 10);

        registreerInformatie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //registreerInformatie.star(primaryStage);
                
                //maak connectie met het database
                if(InputBagageNummer.getText() == null || kofferKleur == null || breedteKoffer == null || lengteKoffer == null || dikteKoffer == null || locatieKoffer == null){
                    actiontarget.setText("You can leave anything open.");
                } else {
                    Connection conn;
                    try {
                        //maak connectie met het database
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        
                        //SQL query
                        String INSERTINFOQuary = "INSERT INTO `corendonbagagesystem`.`gevondenbagage` (`bagagelabel`, `kleur`, `dikte`, `lengte`, `breedte`, `luchthavengevonden`, `datum`, `bijzonderhede`)"+
                                "VALUES ('"+InputBagageNummer.getText()+"','"+kofferKleur+"', '"+dikteKoffer+"', '"+lengteKoffer+"', '"+breedteKoffer+"', '"+locatieKoffer+"', CURDATE(), '"+Bijzonderheden.getText()+"')";
                        
                        // create the java statement
                        Statement st = conn.createStatement();
                        
                        //push naar het database
                        st.executeUpdate(INSERTINFOQuary);
                        
                        String BagageNummer = InputBagageNummer.getText();
                        zoekBagage zoekBagage = new zoekBagage(BagageNummer, kofferKleur, merkKoffer, breedteKoffer, lengteKoffer, dikteKoffer, locatieKoffer);
                        int[] bagageID = zoekBagage.check();
                        
                        //new Stage
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(primaryStage);
                        
                        //nieuwe scene
                        Scene scene = new Scene(new Group());
                        dialog.setTitle("Table View Sample");
                        dialog.setWidth(600);
                        dialog.setHeight(500);
                        table.setEditable(true);
                        
                        //koffer id
                        TableColumn gevondenkofferIDcol = new TableColumn("id");
                        gevondenkofferIDcol.setMinWidth(100);
                        gevondenkofferIDcol.setCellValueFactory(
                        new PropertyValueFactory<>("gevondenkofferID"));
                        
                        //bagage label
                        TableColumn bagagelabelcol = new TableColumn("bagagelabel");
                        bagagelabelcol.setMinWidth(100);
                        bagagelabelcol.setCellValueFactory(
                        new PropertyValueFactory<>("bagagelabel"));
                        
                        //kleur
                        TableColumn kleurcol = new TableColumn("kleur");
                        kleurcol.setMinWidth(200);
                        kleurcol.setCellValueFactory(
                        new PropertyValueFactory<>("kleur"));
                        
                        //hoogte
                        TableColumn diktecol = new TableColumn("hoogte");
                        diktecol.setMinWidth(200);
                        diktecol.setCellValueFactory(
                        new PropertyValueFactory<>("dikte"));
                        
                        //lengte
                        TableColumn lengtecol = new TableColumn("lengte");
                        lengtecol.setMinWidth(200);
                        lengtecol.setCellValueFactory(
                        new PropertyValueFactory<>("lengte"));
                        
                        //breedte
                        TableColumn breedtecol = new TableColumn("breedte");
                        breedtecol.setMinWidth(200);
                        breedtecol.setCellValueFactory(
                        new PropertyValueFactory<>("breedte"));
                        
                        //luchthavengevonden
                        TableColumn luchthavengevondencol = new TableColumn("luchthavengevonden");
                        luchthavengevondencol.setMinWidth(200);
                        luchthavengevondencol.setCellValueFactory(
                        new PropertyValueFactory<>("luchthavengevonden"));
                        
                        //datum
                        TableColumn datumcol = new TableColumn("datum");
                        datumcol.setMinWidth(200);
                        datumcol.setCellValueFactory(
                        new PropertyValueFactory<>("datum"));
                        
                        //soft hard
                        TableColumn softhardcol = new TableColumn("soft/hard");
                        softhardcol.setMinWidth(200);
                        softhardcol.setCellValueFactory(
                        new PropertyValueFactory<>("soft/hard"));
                        
                        //bijzonderheden
                        TableColumn bijzonderhedecol = new TableColumn("bijzonderhede");
                        bijzonderhedecol.setMinWidth(200);
                        bijzonderhedecol.setCellValueFactory(
                        new PropertyValueFactory<>("bijzonderhede"));
                        
                        //data array list
                        ObservableList<Person> data = FXCollections.observableArrayList();
                        
                        //for loop
                        for(int i = 0; i < bagageID.length; i++){
                            System.out.println(bagageID[i]);
                            ResultSet databaseResponse = st.executeQuery("SELECT * FROM verlorenbagage WHERE bagagelabel ='"+bagageID[i]+"'");
                            while (databaseResponse.next()){
                                String verlorenkofferID = databaseResponse.getString("verlorenkofferID");
                                String bagagelabel = databaseResponse.getString("bagagelabel");
                                String kleur = databaseResponse.getString("kleur");
                                String dikte = databaseResponse.getString("dikte");
                                String lengte = databaseResponse.getString("lengte");
                                String breedte = databaseResponse.getString("breedte");
                                String luchthavenvertrokken = databaseResponse.getString("luchthavenvertrokken");
                                String datum = databaseResponse.getString("datum");
                                String softhard = databaseResponse.getString("softhard");
                                String bijzonderhede = databaseResponse.getString("bijzonderhede");
                                
                                
                                data.add(new Person(verlorenkofferID,bagagelabel,kleur,dikte,lengte,breedte,luchthavenvertrokken,datum,softhard,bijzonderhede));
                             
                                table.setItems(data);
                                table.getColumns().addAll(gevondenkofferIDcol, bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavengevondencol, datumcol, softhardcol, bijzonderhedecol);
                            }
                        }
                        
                        //final
                        final VBox vbox = new VBox();
                        vbox.setSpacing(5);
                        vbox.setPadding(new Insets(10, 0, 0, 10));
                        //vbox.getChildren().add(table);
                        
                        ((Group) scene.getRoot()).getChildren().addAll(table);
                        
                        dialog.setScene(scene);
                        
                        //button
                        Button test = new Button();
                        test.setText("JA");
                        vbox.getChildren().add(test);
                        dialog.show();
                        System.out.println();
                        test.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent e){
                            Home.start(primaryStage);
                            dialog.close();
                        }});
                    }catch (SQLException ed) {
                        System.err.println(ed);
                    }
                }
            }
        });
        
        //scene
        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static class Person {
 
        //Alle SimpleStringProperty
        private final SimpleStringProperty gevondenkofferID;
        private final SimpleStringProperty bagagelabel;
        private final SimpleStringProperty kleur;
        private final SimpleStringProperty dikte;
        private final SimpleStringProperty lengte;
        private final SimpleStringProperty breedte;
        private final SimpleStringProperty luchthavengevonden;
        private final SimpleStringProperty datum;
        private final SimpleStringProperty softhard;
        private final SimpleStringProperty bijzonderhede;
        
        //Person
        private Person(String gevondenkofferID, String bagagelabel, String kleur, String dikte, String lengte,String breedte,String luchthavengevonden, String datum, String softhard, String bijzonderhede) {
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
        }
        
        public String getgevondenkofferID() {
            return gevondenkofferID.get();
        }
 
        public void setgevondenkofferID(String gevondenkofferID) {
            this.gevondenkofferID.set(gevondenkofferID);
        }
 
        public String getbagagelabel() {
            return bagagelabel.get();
        }
 
        public void setbagagelabel(String bagagelabel) {
            this.bagagelabel.set(bagagelabel);
        }
 
        public String getkleur() {
            return kleur.get();
        }
 
        public void setkleur(String kleur) {
            this.kleur.set(kleur);
        }
         public String getdikte() {
            return dikte.get();
        }
 
        public void setdikte(String dikte) {
            this.dikte.set(dikte);
        }
        
        public String getlengte() {
            return lengte.get();
        }
 
        public void setlengte(String lengte) {
            this.lengte.set(lengte);
        }
        
        public String getbreedte() {
            return breedte.get();
        }
 
        public void setbreedte(String breedte) {
            this.breedte.set(breedte);
        }
        
        public String getluchthavengevonden() {
            return luchthavengevonden.get();
        }
 
        public void setluchthavengevonden(String luchthavengevonden) {
            this.luchthavengevonden.set(luchthavengevonden);
        }
        
        public String getdatum() {
            return datum.get();
        }
 
        public void setdatum(String datum) {
            this.datum.set(datum);
        }
        
        public String getsofthard() {
            return softhard.get();
        }
 
        public void setsofthard(String softhard) {
            this.softhard.set(softhard);
        }
        
        public String getbijzonderhede() {
            return bijzonderhede.get();
        }
 
        public void setbijzonderhede(String bijzonderhede) {
            this.bijzonderhede.set(bijzonderhede);
        }
    }
}