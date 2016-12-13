/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author tim
 */
public class VerlKofferReg {

    Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    private int customerID;
    private String kleur, merk, hoogte, lengte, breedte, luchthavenVertrekEntry, luchthavenAankomstEntry, countryEntry;

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
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(registreerKlant);
        grid.add(bwvbox, 1, rij++);

        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 10);

        Button verderNaarBagage = new Button("Register Lugage");

        registreerKlant.setOnAction(new EventHandler<ActionEvent>() {
            private String[] test;

            @Override
            public void handle(ActionEvent e) {
                String lastName = lastNameEntry.getText();
                String firstName = firstNameEntry.getText();
                String phoneNumber = phonenumberEntry.getText();
                String email = emailEntry.getText();
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
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM customers WHERE voornaam = '" + firstName + "' AND achternaam = '" + lastName + "' AND tussenvoegsel = '" + tussenvoegsel + "' AND geb_datum = '" + geb_datum + "'");
                        int count = 0;
                        while (rs.next()) {
                            count = rs.getInt("total");
                            System.out.println(count);
                        }
                        if (count == 0) {
                            String insert = "insert into customers (voornaam, achternaam, tussenvoegsel, telefoonnummer, email, geb_datum) "
                                    + "VALUES ('" + firstName + "','" + lastName + "', '" + tussenvoegsel + "' , '" + phoneNumber + "', '" + email + "','" + geb_datum + "' )";
                            stmt.execute(insert);
                            actiontarget.setFill(Color.GREEN);
                            actiontarget.setText("Klant Toegevoegd");
                        } else {
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Klant bestaat al,\nklik op ga verder\nom aan deze klant\neen koffer toe\nte voegen");
                        }

                        ResultSet rs2 = stmt.executeQuery("SELECT customersID FROM customers WHERE voornaam = '" + firstName + "' AND achternaam = '" + lastName + "' AND tussenvoegsel = '" + tussenvoegsel + "' AND geb_datum = '" + geb_datum + "'");
                        while (rs2.next()) {
                            customerID = rs2.getInt("customersID");
                        }
                        System.out.println(customerID);

                        HBox bwvbox1 = new HBox(10);
                        bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
                        bwvbox1.getChildren().add(verderNaarBagage);
                        grid.add(bwvbox1, 1, 10);

                    } catch (SQLException ed) {
                        System.out.println(ed);
                    }
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
        kleurKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                kleur = t1;
            }
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
        merkKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                merk = t1;
            }
        });

        Label hoogteKoffer = new Label("Height of luggage:");
        grid.add(hoogteKoffer, 0, rij);

        ComboBox hoogteKofferComboBox = new ComboBox();
        hoogteKofferComboBox.getItems().addAll(
                "10cm-15cm",
                "15cm-20cm",
                "20cm-25cm",
                "25cm-30cm",
                "35cm-40cm",
                "40cm-45cm",
                "Unknown"
        );
        hoogteKofferComboBox.setPrefWidth(225);
        grid.add(hoogteKofferComboBox, 1, rij++, 2, 1);

        //eventhandler hoogtekoffercombobox
        hoogteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                hoogte = t1;
            }
        });

        Label lengteKoffer = new Label("Length of luggage:");
        grid.add(lengteKoffer, 0, rij);

        ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
                "30cm-40cm",
                "40cm-50cm",
                "50cm-60cm",
                "60cm-70cm",
                "70cm-80cm",
                "Unknown"
        );
        lengteKofferComboBox.setPrefWidth(225);
        grid.add(lengteKofferComboBox, 1, rij++, 2, 1);

        lengteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                lengte = t1;
            }
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
        breedteKofferComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                breedte = t1;
            }
        });

        Label luchthavenVertrek = new Label("Airport of departure:");
        grid.add(luchthavenVertrek, 0, rij);

        ComboBox vliegveldVertrekComboBox = new ComboBox();
        vliegveldVertrekComboBox.getItems().addAll(
                "Schiphol, Amsterdam",
                "El Prat, Barcelona",
                "Atatürk, Istanbul"
        );
        vliegveldVertrekComboBox.setPrefWidth(225);
        grid.add(vliegveldVertrekComboBox, 1, rij++, 2, 1);

        //eventhandler luchthavenvertrek
        vliegveldVertrekComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                luchthavenVertrekEntry = t1;
            }
        });

        Label luchthavenAankomst = new Label("Airport of arrival:");
        grid.add(luchthavenAankomst, 0, rij);

        ComboBox vliegveldAankomstComboBox = new ComboBox();
        vliegveldAankomstComboBox.getItems().addAll(
                "Schiphol, Amsterdam",
                "El Prat, Barcelona",
                "Atatürk, Istanbul"
        );
        vliegveldAankomstComboBox.setPrefWidth(225);
        grid.add(vliegveldAankomstComboBox, 1, rij++, 2, 1);

        //eventhandler vliegveld aankomst
        vliegveldAankomstComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                luchthavenAankomstEntry = t1;
            }
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
                "Netherlands",
                "Spain",
                "Turkey"
        );
        countryComboBox.setPrefWidth(225);
        grid.add(countryComboBox, 1, rij++, 2, 1);

        countryComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                countryEntry = t1;
            }
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
                if (countryEntry == null || bagageLabelEntry.getText().trim().isEmpty() || city == null || straat == null || huisnummerEntry.getText().trim().isEmpty() || postcodeEntry.getText().trim().isEmpty() || merk == null
                        || kleur == null || hoogte == null || lengte == null || breedte == null || luchthavenAankomstEntry == null || luchthavenVertrekEntry == null) {
                    actiontarget.setText("All fields must be filled in except Characteristics");
                } else {
                    System.out.println(bagagelabel + kleur + merk + hoogte + lengte + breedte + luchthavenVertrekEntry + luchthavenAankomstEntry + bijzonderheden + countryEntry + city + straat + huisnummer + toevoeging + postcode);
                    Statement stmt = (Statement) conn.createStatement();
                    String insert = "INSERT INTO verlorenbagage (bagagelabel, kleur, dikte, lengte, breedte, luchthavenvertrokken, luchthavenaankomst, datum, bijzonderheden, merk, customersID)"
                            + "VALUES ('" + bagagelabel + "','" + kleur + "','" + hoogte + "','" + lengte + "','" + breedte + "','" + luchthavenVertrekEntry + "','" + luchthavenAankomstEntry
                            + "',curdate(),'" + bijzonderheden + "','" + merk + "', '" + customerID + "')";
                    stmt.execute(insert);
                    actiontarget.setFill(Color.GREEN);
                    actiontarget.setText("Lugage added");
                    //Select de verlorenkoffer id en koppel deze aan de customer
                    


                    /*String select = "SELECT verlorenkofferID FROM verlorenbagage WHERE bagagelabel = '"+ bagagelabel +"' AND kleur = '"+kleur+"' AND dikte = '"+hoogte+"' AND lengte ='"
                            + lengte+"' AND breedte = '"+breedte+"' AND luchthavenvertrokken = '"+luchthavenVertrekEntry+ "' AND luchthavenaankomst = '"+luchthavenAankomstEntry+
                                    "' AND datum = curdate() AND bijzonderheden = '" + bijzonderheden+"' AND merk =' "+merk+"'";*/
                    System.out.println("test voor execute");

                    ResultSet result1232 = stmt.executeQuery("SELECT verlorenkofferID FROM verlorenbagage WHERE bagagelabel = '" + bagagelabel + "' AND kleur = '" + kleur + "' AND dikte = '" + hoogte + "' AND lengte ='"

                            + lengte + "' AND breedte = '" + breedte + "' AND luchthavenvertrokken = '" + luchthavenVertrekEntry + "' AND luchthavenaankomst = '" + luchthavenAankomstEntry
                            + "' AND datum = curdate() AND bijzonderheden = '" + bijzonderheden + "' AND merk = '" + merk + "' AND customersID = '" + customerID + "'");

                    int verlorenKofferID = 0;

                    while (result1232.next()) {
                        verlorenKofferID = result1232.getInt("verlorenkofferID");
                    }

                    stmt.execute("INSERT INTO afleveradres (verlorenkofferID, Land, Straat, Huisnummer, Toevoeging, Postcode, Plaats) VALUES "
                            + "('" + verlorenKofferID + "','" + countryEntry + "','" + straat + "','" + huisnummer + "','" + toevoeging + "','" + postcode + "','" + city + "')");

                }
            } catch (SQLException ed) {
                System.out.println(ed);
            }
        });
        
        

        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Register Lost Lugage");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
