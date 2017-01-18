/*
 * In deze klasse kan een gevonden koffer worden geregistreerd
*/

package balie;

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
    private final Mysql MYSQL = new Mysql();
    private final String USERNAME = MYSQL.getUsername();
    private final String PASSWORD = MYSQL.getPassword();
    private final String CONN_STRING = MYSQL.getUrlmysql();
    

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

        //Alle labels, comboboxen en textfields worden gemaakt en toegevoegd aan de grid 
        Label scenetitle = new Label("Register found luggage");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        scenetitle.setAlignment(Pos.BOTTOM_CENTER);
        grid.add(scenetitle, 0, 0, 2, 1);

        Label textKofferNummer = new Label("Luggage label:");
        grid.add(textKofferNummer, 0, 1);

        TextField inputBagageNummer = new TextField();
        inputBagageNummer.setPrefWidth(225);
        grid.add(inputBagageNummer, 1, 1);

        Label textKleurKoffer = new Label("Color: ");
        grid.add(textKleurKoffer, 0, 2);

        ComboBox kleurKofferComboBox = new ComboBox();
        kleurKofferComboBox.getItems().addAll(
                "Blue", "Yellow", "Black", "Gray", "Brown", "Other"
        );
        kleurKofferComboBox.setPrefWidth(225);

        kleurKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                kofferKleur = t1;
            }
        });
        grid.add(kleurKofferComboBox, 1, 2);

        Label textMerkKoffer = new Label("Brand: ");
        grid.add(textMerkKoffer, 0, 3);

        ComboBox merkKofferComboBox = new ComboBox();
        merkKofferComboBox.getItems().addAll(
            "American Tourister", "Eastpak", "March", "Porsche Design", "Rimowa",
            "Samsonite", "Swiss Wenger", "Ted Baker", "The North Face", 
            "Tumi", "Victorinox", "Other"
        );
        merkKofferComboBox.setPrefWidth(225);
        grid.add(merkKofferComboBox, 1, 3);
 
        merkKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                merkKoffer = t1;
            }
        });

        Label textHoogteKoffer = new Label("Height of luggage:");
        grid.add(textHoogteKoffer, 0, 4);

        ComboBox dikteKofferComboBox = new ComboBox();
        dikteKofferComboBox.getItems().addAll(
                "10cm-15cm", "15cm-20cm", "20cm-25cm", "25cm-30cm",
                "35cm-40cm", "40cm-45cm", "Unknown"
        );
        dikteKofferComboBox.setPrefWidth(225);
        grid.add(dikteKofferComboBox, 1, 4);

        dikteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                dikteKoffer = t1;
            }
        });

        Label textLengteKoffer = new Label("Length of luggage:");
        grid.add(textLengteKoffer, 0, 5);

        ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
            "30cm-40cm", "40cm-50cm", "50cm-60cm",
            "60cm-70cm", "70cm-80cm", "Unknown"
        );
        lengteKofferComboBox.setPrefWidth(225);
        grid.add(lengteKofferComboBox, 1, 5);

        lengteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                lengteKoffer = t1;
            }
        });

        Label textBreedeKoffer = new Label("Width of luggage:");
        grid.add(textBreedeKoffer, 0, 6);
        
        ComboBox breedteKofferComboBox = new ComboBox();
        breedteKofferComboBox.getItems().addAll(
                "20cm-30cm", "30cm-40cm", "40cm-50cm", "50cm-60cm", "Unknown"
        );
        breedteKofferComboBox.setPrefWidth(225);
        grid.add(breedteKofferComboBox, 1, 6);

        breedteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                breedteKoffer = t1;
            }
        });

        Label softHardCaseLabel = new Label("Soft or hard case:");
        grid.add(softHardCaseLabel, 0, 7);

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

        Label textlocatieKoffer = new Label("Airport found:");
        grid.add(textlocatieKoffer, 0, 8);

        ComboBox locatieKofferComboBox = new ComboBox();
        locatieKofferComboBox.getItems().addAll(
                "Schiphol, Amsterdam", "El Prat, Barcelona", "Atatürk, Istanbul"
        );
        locatieKofferComboBox.setPrefWidth(225);
        grid.add(locatieKofferComboBox, 1, 8);

        locatieKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                locatieKoffer = t1;
            }
        });

        Label textBijzonderheden = new Label("Characteristics: ");
        grid.add(textBijzonderheden, 0, 9);

        TextArea bijzonderheden = new TextArea();
        bijzonderheden.setMaxSize(225, 50);
        grid.add(bijzonderheden, 1, 9);

        Button registreerInformatie = new Button("Check for matches");
        grid.add(registreerInformatie, 1, 10);

        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 11);
        
        /*
         * Eerst wordt er gekeken of het bagalabel is ingevuld, zo ja dan wordt
         * er gekeken of het bagagelabel al bestaat. Dan wordt de bagage ingevoerd in de
         * database en daarna wordt er gekeken of er matches zijn.
         * Als het bagagelabel leeg is gelaten wordt er gekeken of de rest van de
         * velden wel zijn ingevuld, dan wordt de bagage ingevoerd in de database
         * en wordt er ook gekeken of er een match is.
        */
        registreerInformatie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!inputBagageNummer.getText().trim().isEmpty()) 
                    {
                    try {
                        //maak connectie met het database
                        Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        Statement st = conn.createStatement();
                        ResultSet bagagelabelExistsCheck = st.executeQuery("SELECT COUNT(*) AS total FROM gevondenbagage WHERE bagagelabel = '"+inputBagageNummer.getText().trim()+"'");
                        int count = 0;
                        while(bagagelabelExistsCheck.next()){
                            count = bagagelabelExistsCheck.getInt("total");
                        }
                        if(count == 0){
                        String insertQuery = "INSERT INTO `corendonbagagesystem`.`gevondenbagage` (`bagagelabel`, `kleur`, `dikte`, `lengte`, `breedte`, `luchthavengevonden`, `datum`, `bijzonderhede`, `merk`, `softhard`, status)"
                            + "VALUES ('" + inputBagageNummer.getText() + "','" + kofferKleur + "', '" + dikteKoffer + "', '" + lengteKoffer + "', '" + breedteKoffer + "', '" + locatieKoffer + "', CURDATE(), '" + bijzonderheden.getText() + "', '" + merkKoffer + "', '" + softHardCase + "', 'notSolved')";
                        st.executeUpdate(insertQuery);
                        //krijgt de laatste gevondenKofferId terug
                        String selectGevKofferID = "SELECT gevondenKofferID FROM gevondenbagage ORDER BY gevondenkofferID DESC LIMIT 1";                                      
                        ResultSet gevKofferIdRes = st.executeQuery(selectGevKofferID);
                        while(gevKofferIdRes.next()){
                            gevKofID = gevKofferIdRes.getInt("gevondenKofferID");
                        }
                        zoekBagage zoekbagage = new zoekBagage();
                        zoekbagage.maakZoekString(primaryStage, gevKofID, inputBagageNummer.getText().trim(), 
                                kofferKleur, dikteKoffer, lengteKoffer, breedteKoffer, 
                                locatieKoffer,bijzonderheden.getText(), merkKoffer, softHardCase);
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
                    try {
                        Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        Statement st = conn.createStatement();
                        //SQL query
                        String insertQuary = "INSERT INTO gevondenbagage (kleur, dikte, lengte, breedte, luchthavengevonden, datum, bijzonderhede, merk, softhard, status)"
                                + "VALUES ('" + kofferKleur + "', '" + dikteKoffer + "', '" + lengteKoffer + "', '" + breedteKoffer + "', '" + locatieKoffer + "', CURDATE(), '" + bijzonderheden.getText() + "', '" + merkKoffer + "', '" + softHardCase + "', 'notSolved' )";
                        st.executeUpdate(insertQuary);
                        //krijgt de laatst ingevoerde gevondenKofferId terug
                        String selectGevKofferID = "SELECT gevondenKofferID FROM gevondenbagage ORDER BY gevondenkofferID DESC LIMIT 1" ;                                 
                        ResultSet gevKofferIdRes = st.executeQuery(selectGevKofferID);
                        while(gevKofferIdRes.next()){
                            gevKofID = gevKofferIdRes.getInt("gevondenKofferID");
                        }
                        zoekBagage zoekbagage = new zoekBagage();
                        zoekbagage.maakZoekString(primaryStage, gevKofID, null, 
                                kofferKleur, dikteKoffer, lengteKoffer, breedteKoffer, 
                                locatieKoffer, bijzonderheden.getText(), merkKoffer, softHardCase);
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

