package balie;

import global.Home;
import global.MenuB;
import global.Mysql;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GevKofferReg {

    //Strings
    private String kofferKleur, merkKoffer, breedteKoffer, lengteKoffer,
            dikteKoffer, locatieKoffer, softHardCase;
    private int gevKofID;
    private int[] bagageIdArray;

    //mysql connectie
    Mysql mysql = new Mysql();
    Home Home = new Home();

    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    

    public void start(Stage primaryStage) {

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

        //Titel    
        Label scenetitle = new Label("Register found luggage");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        scenetitle.setAlignment(Pos.BOTTOM_CENTER);
        grid.add(scenetitle, 0, 0, 2, 1);

        //bagage nummer text
        Label TextKofferNummer = new Label("Luggage label:");
        grid.add(TextKofferNummer, 0, 1);

        //input veld bagage nummer
        TextField InputBagageNummer = new TextField();
        InputBagageNummer.setPrefWidth(225);
        grid.add(InputBagageNummer, 1, 1);

        //text kleur koffer
        Label TextKleurKoffer = new Label("Color: ");
        grid.add(TextKleurKoffer, 0, 2);

        //drop down box kleur koffer
        final ComboBox kleurKofferComboBox = new ComboBox();
        kleurKofferComboBox.getItems().addAll(
                "Blue", "Yellow", "Black", "Gray", "Brown", "Other"
        );
        kleurKofferComboBox.setPrefWidth(225);

        //update string merk koffer 
        kleurKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                kofferKleur = t1;
            }
        });

        //add drop down box kleur in scene
        grid.add(kleurKofferComboBox, 1, 2);

        //text merk koffer
        Label TextMerkKoffer = new Label("Brand: ");
        grid.add(TextMerkKoffer, 0, 3);

        //drop down box merk koffer
        final ComboBox merkKofferComboBox = new ComboBox();
        merkKofferComboBox.getItems().addAll(
            "American Tourister", "Eastpak", "March", "Porsche Design", "Rimowa",
            "Samsonite", "Swiss Wenger", "Ted Baker", "The North Face", 
            "Tumi", "Victorinox", "Other"
        );
        merkKofferComboBox.setPrefWidth(225);

        //add koffer merk in scene
        grid.add(merkKofferComboBox, 1, 3);

        //update string merk koffer 
        merkKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                merkKoffer = t1;
            }
        });

        //text hoogte koffer
        Label TextHoogteKoffer = new Label("Height of luggage:");
        grid.add(TextHoogteKoffer, 0, 4);

        //drop down box hoogte koffer
        final ComboBox dikteKofferComboBox = new ComboBox();
        dikteKofferComboBox.getItems().addAll(
                "10cm-15cm", "15cm-20cm", "20cm-25cm", "25cm-30cm",
                "35cm-40cm", "40cm-45cm", "Unknown"
        );
        dikteKofferComboBox.setPrefWidth(225);

        //add mark koffer in scene
        grid.add(dikteKofferComboBox, 1, 4);

        //update string lengte koffer 
        dikteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                dikteKoffer = t1;
            }
        });

        //text lengte koffer
        Label TextLengteKoffer = new Label("Length of luggage:");
        grid.add(TextLengteKoffer, 0, 5);

        //drop down box hoogte koffer
        final ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
            "30cm-40cm", "40cm-50cm", "50cm-60cm",
            "60cm-70cm", "70cm-80cm", "Unknown"
        );
        lengteKofferComboBox.setPrefWidth(225);

        //add mark koffer in scene
        grid.add(lengteKofferComboBox, 1, 5);

        //update string lengte koffer 
        lengteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                lengteKoffer = t1;
            }
        });

        //text dikte koffer
        Label TextBreedeKoffer = new Label("Width of luggage:");
        grid.add(TextBreedeKoffer, 0, 6);

        //drop down box hoogte koffer
        final ComboBox breedteKofferComboBox = new ComboBox();
        breedteKofferComboBox.getItems().addAll(
                "20cm-30cm", "30cm-40cm", "40cm-50cm", "50cm-60cm", "Unknown"
        );
        breedteKofferComboBox.setPrefWidth(225);

        //add mark koffer in scene
        grid.add(breedteKofferComboBox, 1, 6);

        //update string dikte koffer
        breedteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                breedteKoffer = t1;
            }
        });

        //label softhard case
        Label softHardCaseLabel = new Label("Soft or hard case:");
        grid.add(softHardCaseLabel, 0, 7);

        //comboBox softhard
        ComboBox softHardCaseComboBox = new ComboBox();
        softHardCaseComboBox.getItems().addAll(
                "Soft", "Hard"
        );
        softHardCaseComboBox.setPrefWidth(225);
        grid.add(softHardCaseComboBox, 1, 7);

        softHardCaseComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                softHardCase = t1;
            }
        });

        //text dikte koffer
        Label TextlocatieKoffer = new Label("Airport found:");
        grid.add(TextlocatieKoffer, 0, 8);

        //locatie koffer drop down
        ComboBox locatieKofferComboBox = new ComboBox();
        locatieKofferComboBox.getItems().addAll(
                "Schiphol, Amsterdam", "El Prat, Barcelona", "Atatürk, Istanbul"
        );
        locatieKofferComboBox.setPrefWidth(225);

        //add locatie koffer in scene
        grid.add(locatieKofferComboBox, 1, 8);

        //update string waarvan de koffer is gevonden
        locatieKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                locatieKoffer = t1;
            }
        });

        //bagage nummer text
        Label TextBijzonderheden = new Label("Characteristics: ");
        grid.add(TextBijzonderheden, 0, 9);

        //input veld bagage nummer
        TextArea Bijzonderheden = new TextArea();
        Bijzonderheden.setMaxSize(225, 50);
        grid.add(Bijzonderheden, 1, 9);

        //button voor registeren
        Button registreerInformatie = new Button("Check for matches");
        grid.add(registreerInformatie, 1, 10);

        //button event maken
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 11);

        
        
        registreerInformatie.setOnAction(new EventHandler<ActionEvent>() {
            @Override

            public void handle(ActionEvent e) {
                //maak connectie met het database
                if (!InputBagageNummer.getText().trim().isEmpty()) 
                    {
                        Connection conn;
                    try {
                        //maak connectie met het database
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        Statement st = conn.createStatement();
                        ResultSet bagagelabelExistsCheck = st.executeQuery("SELECT COUNT(*) AS total FROM gevondenbagage WHERE bagagelabel = '"+InputBagageNummer.getText().trim()+"'");
                        int count = 0;
                        while(bagagelabelExistsCheck.next()){
                            count = bagagelabelExistsCheck.getInt("total");
                        }
                        if(count == 0){
                        String INSERTINFOQuary = "INSERT INTO `corendonbagagesystem`.`gevondenbagage` (`bagagelabel`, `kleur`, `dikte`, `lengte`, `breedte`, `luchthavengevonden`, `datum`, `bijzonderhede`, `merk`, `softhard`, status)"
                            + "VALUES ('" + InputBagageNummer.getText() + "','" + kofferKleur + "', '" + dikteKoffer + "', '" + lengteKoffer + "', '" + breedteKoffer + "', '" + locatieKoffer + "', CURDATE(), '" + Bijzonderheden.getText() + "', '" + merkKoffer + "', '" + softHardCase + "', 'notSolved')";
                        
                        st.executeUpdate(INSERTINFOQuary);

                        String selectGevKofferID = "SELECT gevondenKofferID from gevondenbagage where bagagelabel = '"+InputBagageNummer.getText()+"' AND kleur = '"+kofferKleur+"' AND dikte = '"+dikteKoffer+"' AND lengte = '"+lengteKoffer+"' AND breedte = '"+breedteKoffer+"'"
                                + " AND luchthavengevonden = '"+ locatieKoffer + "' AND datum = CURDATE() AND bijzonderhede = '"+Bijzonderheden.getText()+"' AND merk = '"+merkKoffer+"' AND softhard = '"+softHardCase+"'";                                      
                        ResultSet gevKofferIdRes = st.executeQuery(selectGevKofferID);
                        while(gevKofferIdRes.next()){
                            gevKofID = gevKofferIdRes.getInt("gevondenKofferID");
                        }
                        String BagageNummer = InputBagageNummer.getText();
                        zoekBagage zoekBagage = new zoekBagage(BagageNummer, kofferKleur, merkKoffer, breedteKoffer, lengteKoffer, dikteKoffer, locatieKoffer, softHardCase);
                        bagageIdArray = zoekBagage.check();
                        createTable(bagageIdArray, primaryStage);
                        }else{
                            actiontarget.setText("Luggage label already exists");
                        }
                    } catch (SQLException ed) {
                        System.err.println(ed);
                    }
                    
                }else if (kofferKleur == null || breedteKoffer == null || lengteKoffer == null || dikteKoffer == null 
                        || locatieKoffer == null || softHardCase == null || merkKoffer == null) {
                    actiontarget.setText("You can't leave anything open.");
                }else{
                        Connection conn;
                    try {
                        //maak connectie met het database
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        Statement st = conn.createStatement();
                        ResultSet bagagelabelExistsCheck = st.executeQuery("SELECT COUNT(*) AS total FROM gevondenbagage WHERE bagagelabel = '"+InputBagageNummer.getText().trim()+"'");
                        int count = 0;
                        while(bagagelabelExistsCheck.next()){
                            count = bagagelabelExistsCheck.getInt("total");
                        }
                        if(count == 0){
                            //SQL query
                            String INSERTINFOQuary = "INSERT INTO `corendonbagagesystem`.`gevondenbagage` (`bagagelabel`, `kleur`, `dikte`, `lengte`, `breedte`, `luchthavengevonden`, `datum`, `bijzonderhede`, `merk`, `softhard`)"
                                    + "VALUES ('" + InputBagageNummer.getText() + "','" + kofferKleur + "', '" + dikteKoffer + "', '" + lengteKoffer + "', '" + breedteKoffer + "', '" + locatieKoffer + "', CURDATE(), '" + Bijzonderheden.getText() + "', '" + merkKoffer + "', '" + softHardCase + "')";String selectGevKofferID = "SELECT gevondenKofferID from gevondenbagage where bagagelabel = '"+InputBagageNummer.getText()+"' AND kleur = '"+kofferKleur+"' AND dikte = '"+dikteKoffer+"' AND lengte = '"+lengteKoffer+"' AND breedte = '"+breedteKoffer+"'"
                                        + " AND luchthavengevonden = '"+ locatieKoffer + "' AND datum = CURDATE() AND bijzonderhede = '"+Bijzonderheden.getText()+"' AND merk = '"+merkKoffer+"' AND softhard = '"+softHardCase+"'";                                      
                            ResultSet gevKofferIdRes = st.executeQuery(selectGevKofferID);
                            while(gevKofferIdRes.next()){
                                gevKofID = gevKofferIdRes.getInt("gevondenKofferID");
                                System.out.println(gevKofID);
                            }
                            //push naar het database
                            st.executeUpdate(INSERTINFOQuary);

                            String BagageNummer = InputBagageNummer.getText();
                            zoekBagage zoekBagage = new zoekBagage(BagageNummer, kofferKleur, merkKoffer, breedteKoffer, lengteKoffer, dikteKoffer, locatieKoffer, softHardCase);
                            bagageIdArray = zoekBagage.check();
                            createTable(bagageIdArray, primaryStage);
                        }else{
                            actiontarget.setText("Luggage label already exists");
                        }
                    } catch (SQLException ed) {
                        System.err.println(ed);
                    }        
                }
            }
        });

        //scene
        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register found luggage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Person> table = new TableView<Person>();
    private String kofferid, dlabel, kleur, dikte, lengte, breedte, luchthavengevonden, luchthavenaankomst, datum, softhard, bijzonderhede, merk;

    private void createTable(int[] bagageIdArray, Stage primaryStage) {

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

        Connection conn;
        if (bagageIdArray[0] != 0) {
            try {
                //maak connectie met het database
                conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

                //Person[] person = new Person[bagageIdArray.length];
                Statement st = conn.createStatement();

                String selectString = "SELECT * FROM verlorenbagage WHERE verlorenkofferID = '" + bagageIdArray[0] + "'";
                for (int i = 1; i < bagageIdArray.length; i++) {

                    selectString += "OR verlorenkofferID = '" + bagageIdArray[i] + "'";
                }

                ResultSet databaseResponse2 = st.executeQuery(selectString);

                ObservableList<Person> data = FXCollections.observableArrayList();

                while (databaseResponse2.next()) {

                    //database response verwerken
                    this.kofferid = databaseResponse2.getString("verlorenkofferID");
                    this.dlabel = databaseResponse2.getString("bagagelabel");
                    this.kleur = databaseResponse2.getString("kleur");
                    this.dikte = databaseResponse2.getString("dikte");
                    this.lengte = databaseResponse2.getString("lengte");
                    this.breedte = databaseResponse2.getString("breedte");
                    this.luchthavengevonden = databaseResponse2.getString("luchthavenvertrokken");
                    this.luchthavenaankomst = databaseResponse2.getString("luchthavenaankomst");
                    this.datum = databaseResponse2.getString("datum");
                    this.softhard = databaseResponse2.getString("softhard");
                    this.bijzonderhede = databaseResponse2.getString("bijzonderheden");
                    this.merk = databaseResponse2.getString("merk");

                    data.add(new Person(kofferid, dlabel, kleur, dikte, lengte, breedte, luchthavengevonden, luchthavenaankomst, datum, softhard, bijzonderhede, merk));

                    table.setItems(data);

                }
            } catch (SQLException ed) {
                System.err.println(ed);
            }

            table.setEditable(true);
            
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

            table.getColumns().addAll(gevondenkofferIDcol, bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavengevondencol, luchthavenaankomstcol, datumcol, softhardcol, merkcol, bijzonderhedecol);

            Scene scene = new Scene(new Group(), 1250, 920);
            primaryStage.setTitle("Table");
            
            
            Button match = new Button("Match");
        
            match.setOnAction((ActionEvent e) -> {
                if(table.getSelectionModel().isEmpty() == false){
                    Person selected = table.getSelectionModel().getSelectedItem();
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
        } else {
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

            test.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    Home home = new Home();
                    home.start(primaryStage);
                    dialog.close();

                }
            });
        }

    }
    
    private void zetMatchInDatabase(String verlorenKofferID){
        Connection conn;
        int klantID = 0;
        try {
            //maak connectie met het database
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
                        
            String selectKlantID = "SELECT customersID from verlorenbagage where verlorenkofferID = '"+verlorenKofferID+"'";
            ResultSet resultKlantID = st.executeQuery(selectKlantID);
            while(resultKlantID.next()){
                klantID = resultKlantID.getInt("customersID");
            }
            
            String insertOpgelost = "INSERT INTO opgelost (customersID, verlorenkofferID, gevondenkofferID, datum) "
                    + "values ('"+klantID+"', '"+verlorenKofferID+"', '"+gevKofID+"', curdate())";
            st.execute(insertOpgelost);
            
            String updateStatusVerl = "UPDATE verlorenbagage SET status = 'pending' WHERE verlorenkofferID = '"+verlorenKofferID+"'";
            String updateStatusGev = "UPDATE gevondenbagage SET status = 'pending' WHERE gevondenkofferID = '"+gevKofID+"'";
            System.out.println(updateStatusGev);
            st.execute(updateStatusVerl);
            st.execute(updateStatusGev);
            
            /* Jiorgos hier een mailtje naar de klant dat er een match is.
                en pdf voor vliegveld om terug te sturen
            */
        
        } catch (SQLException ed) {
            
        }
    }

    public static class Person {

        private SimpleStringProperty gevondenkofferID, bagagelabel, kleur,
                dikte, lengte, breedte, luchthavengevonden, luchthavenaankomst, datum,
                softhard, bijzonderhede, merk;

        private Person(String gevondenkofferID, String bagagelabel, String kleur, String dikte, String lengte, String breedte, String luchthavengevonden, String luchthavenaankomst, String datum, String softhard, String bijzonderhede, String merk) {
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
