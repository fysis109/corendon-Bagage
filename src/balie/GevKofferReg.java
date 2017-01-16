package balie;

import global.Home;
import global.MenuB;
import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GevKofferReg {

    //Strings
    private String kofferKleur, merkKoffer, breedteKoffer, lengteKoffer,
            dikteKoffer, locatieKoffer, softHardCase;
    private int gevKofID;
 
    
    
    
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

                        String selectGevKofferID = "SELECT gevondenKofferID FROM gevondenbagage ORDER BY gevondenkofferID DESC LIMIT 1";                                      
                        ResultSet gevKofferIdRes = st.executeQuery(selectGevKofferID);
                        while(gevKofferIdRes.next()){
                            gevKofID = gevKofferIdRes.getInt("gevondenKofferID");
                        }
                        zoekBagage zoekbagage = new zoekBagage();
                        zoekbagage.maakZoekString(primaryStage, gevKofID, InputBagageNummer.getText().trim(), 
                                kofferKleur, dikteKoffer, lengteKoffer, breedteKoffer, 
                                locatieKoffer,Bijzonderheden.getText(), merkKoffer, softHardCase);
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
                        
                        //SQL query
                        String INSERTINFOQuary = "INSERT INTO gevondenbagage (kleur, dikte, lengte, breedte, luchthavengevonden, datum, bijzonderhede, merk, softhard, status)"
                                + "VALUES ('" + kofferKleur + "', '" + dikteKoffer + "', '" + lengteKoffer + "', '" + breedteKoffer + "', '" + locatieKoffer + "', CURDATE(), '" + Bijzonderheden.getText() + "', '" + merkKoffer + "', '" + softHardCase + "', 'notSolved' )";
                        st.executeUpdate(INSERTINFOQuary);
                        String selectGevKofferID = "SELECT gevondenKofferID FROM gevondenbagage ORDER BY gevondenkofferID DESC LIMIT 1" ;                                 
                        ResultSet gevKofferIdRes = st.executeQuery(selectGevKofferID);
                        while(gevKofferIdRes.next()){
                            gevKofID = gevKofferIdRes.getInt("gevondenKofferID");
                        }
                        zoekBagage zoekbagage = new zoekBagage();
                        zoekbagage.maakZoekString(primaryStage, gevKofID, null, 
                                kofferKleur, dikteKoffer, lengteKoffer, breedteKoffer, 
                                locatieKoffer, Bijzonderheden.getText(), merkKoffer, softHardCase);
                    } catch (SQLException ed) {
                        System.err.println(ed);
                    }        
                }
            }
        });

        //scene
        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register found luggage");
        scene.getStylesheets().add("global/Style2.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   
    

}

