package login;

import java.sql.Connection;
import java.sql.DriverManager;
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
            "bruin"  
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
        Label TextMerkKoffer = new Label("Vul de merk van de koffer in: ");
        grid.add(TextMerkKoffer, 0, 3);       
        
        //drop down box merk koffer
        final ComboBox merkKofferComboBox = new ComboBox();
        merkKofferComboBox.getItems().addAll(
            "American Tourister",
            "Bric's",
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
            "overige"
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
            "niet bekend"
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
        Label TextLengteKoffer = new Label("Lengte van de koffer in cm: ");
        grid.add(TextLengteKoffer, 0, 5);       
        
        //drop down box hoogte koffer
        final ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
            "30cm-40cm",
            "40cm-50cm",
            "50cm-60cm",
            "60cm-70cm",
            "70cm-80cm",
            "niet bekend"
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
        Label TextBreedeKoffer = new Label("Breedte van de koffer in cm: ");
        grid.add(TextBreedeKoffer, 0, 6);       
        
        //drop down box hoogte koffer
        final ComboBox breedteKofferComboBox = new ComboBox();
        breedteKofferComboBox.getItems().addAll(
            "20cm-30cm",
            "30cm-40cm",
            "40cm-50cm",
            "50cm-60cm",
            "niet bekend"
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
        TextField Bijzonderheden = new TextField();
        grid.add(Bijzonderheden, 1, 1);
        
        
        //button voor registeren
        Button registreerInformatie = new Button("Registreer");
        grid.add(registreerInformatie, 1,9);
        
        //button event maken
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 10);

        registreerInformatie.setOnAction((ActionEvent e) -> {
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
                System.out.println(INSERTINFOQuary);

                // create the java statement
                Statement st = conn.createStatement();
                
                //push naar het database
                st.executeUpdate(INSERTINFOQuary);
                
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Button test = new Button();
                test.setText("JA");
                dialogVbox.getChildren().addAll(new Text("De koffers is succesvol toegevoegd in het database."), test);
                Scene dialogScene = new Scene(dialogVbox, 400, 300);
                dialog.setScene(dialogScene);
                dialog.show();
                
                test.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent e){
                    Home.start(primaryStage);
                    dialog.close(); 
                }});

                }catch (SQLException ed) {
                    System.err.println(ed);
                }
            
                System.out.println(dikteKoffer);
            }
        
        });
        
        //scene
        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}