/*
 * In deze klasse wordt alle gevonden bagage getoond en daarna kan je deze
 * aanpassen, daarna wordt er gekeken of er matches zijn.
 */
package balie;


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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GevondenBagageAanpassen {
    
    private final Mysql MYSQL = new Mysql();
    private final String USERNAME = MYSQL.getUsername();
    private final String PASSWORD = MYSQL.getPassword();
    private final String CONN_STRING = MYSQL.getUrlmysql();
    private TableView<BagageStuk> table = new TableView<>();
    private String kleurEntry, merkEntry, hoogteEntry, lengteEntry, breedteEntry, 
                luchthavenGevondenEntry, hardSoftEntry, statusEntry;
    
    
    public void start(Stage primaryStage){
        
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
        
        //Alle gevonden bagage wordt uit de database gehaald en toegevoegd aan 
        //de tableview
        try{
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM gevondenbagage WHERE status = 'pending' OR status = 'notSolved' ORDER BY status DESC");
            ObservableList<BagageStuk> data = FXCollections.observableArrayList();
            while(rs.next()){
                String kofferid = rs.getString("gevondenkofferID");
                String dlabel = rs.getString("bagagelabel");
                String kleur = rs.getString("kleur");
                String dikte = rs.getString("dikte");
                String lengte = rs.getString("lengte");
                String breedte = rs.getString("breedte");
                String luchthavenvertrokken = rs.getString("luchthavengevonden");
                String datum = rs.getString("datum");
                String bijzonderheden = rs.getString("bijzonderhede");
                String merk = rs.getString("merk");
                String softhard = rs.getString("softhard");
                String status = rs.getString("status");
                data.add(new BagageStuk(kofferid, dlabel, kleur, dikte, lengte, 
                            breedte, luchthavenvertrokken, 
                            datum, softhard, bijzonderheden, merk, status));
                table.setItems(data);
            }    
        }catch (SQLException ed){
            System.out.println(ed);
        }
    
    
        Scene scene = new Scene(new Group(), 1200, 1000);
        primaryStage.setTitle("All lost luggage");
        
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        Label matchesLabel = new Label("All lost lugage: ");
        matchesLabel.setFont(new Font("Arial", 20));
        
        //alle tablecolumns
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

        TableColumn luchthavenvertrokkencol = new TableColumn("Airport found");
        luchthavenvertrokkencol.setMinWidth(120);
        luchthavenvertrokkencol.setCellValueFactory(
                new PropertyValueFactory<>("luchthavenvertrokken"));
        
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
                new PropertyValueFactory<>("bijzonderheden"));

        TableColumn merkcol = new TableColumn("Brand");
        merkcol.setMinWidth(100);
        merkcol.setCellValueFactory(
                new PropertyValueFactory<>("merk"));
        
        TableColumn statuscol = new TableColumn("Status");
        statuscol.setMinWidth(100);
        statuscol.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        
        table.getColumns().addAll(bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavenvertrokkencol, datumcol, softhardcol, merkcol, bijzonderhedecol, statuscol);
        
        //veranderd de grootte van de table aan de hand van het grootte van het scherm
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
        
        
        Button pasAan = new Button("Adjust luggage");
        Button pasStatusAan = new Button("Adjust status");
        GridPane buttons = new GridPane();
        buttons.setVgap(10);
        buttons.setHgap(10);
        buttons.setPadding(new Insets(25, 25, 25, 25));
        buttons.add(pasAan, 0, 0);
        buttons.add(pasStatusAan, 3, 0);
        
        //verandert de beschikbaarheid van de buttons aan de hand van de status van het bagagestuk dat is geselecteerd
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if(table.getSelectionModel().getSelectedItem() != null) 
                {    
                    BagageStuk selected = table.getSelectionModel().getSelectedItem();
                    if("pending".equals(selected.getStatus())){
                        pasAan.setDisable(true);
                        pasStatusAan.setDisable(false);
                    }else{
                        pasAan.setDisable(false);
                        pasStatusAan.setDisable(true);
                    }
                }
            }
        });
        
        //verander de gegevens van het geselecteerde bagagestuk
        pasAan.setOnAction((ActionEvent e) -> {
            if(table.getSelectionModel().isEmpty() == false){
                BagageStuk selected = table.getSelectionModel().getSelectedItem();
                if(selected.getBagagelabel() == null){
                    pasBagageAan(primaryStage, selected.getVerlorenkofferID(), null, 
                        selected.getKleur(), selected.getDikte(), selected.getLengte(), selected.getBreedte(),
                        selected.getLuchthavenvertrokken(), selected.getBijzonderheden(), selected.getMerk(), selected.getSofthard()
                        );
                }else{
                pasBagageAan(primaryStage, selected.getVerlorenkofferID(), selected.getBagagelabel(), 
                        selected.getKleur(), selected.getDikte(), selected.getLengte(), selected.getBreedte(),
                        selected.getLuchthavenvertrokken(), selected.getBijzonderheden(), selected.getMerk(), selected.getSofthard()
                        );
                }
            }
        });
        
        //pas de status aan van het geselecteerde bagagestuk
        pasStatusAan.setOnAction((ActionEvent e ) -> {
            if(table.getSelectionModel().isEmpty() == false){
                BagageStuk selected = table.getSelectionModel().getSelectedItem();
                pasStatusAan(primaryStage, selected.getVerlorenkofferID(), selected.getStatus(), selected.getBagagelabel());
            }    
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
    
    private void pasBagageAan(Stage primaryStage, String gevondenkofferID, 
                    String bagageLabel, String kleur, String dikte, String lengte, 
                    String breedte, String luchthavenGevonden, 
                    String bijzonderheden, String merk, String softhard){
        
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
        
        //alle labels, comboboxen en textfields
        Text scenetitle = new Text("Adjust lost Lugage");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label bagageLabelLabel = new Label("Lugagelabelnumber:");
        grid.add(bagageLabelLabel, 0, rij);

        TextField bagageLabelEntry = new TextField(bagageLabel);
        bagageLabelEntry.setMaxWidth(225);
        grid.add(bagageLabelEntry, 1, rij++, 2, 1);

        Label kleurKofferLabel = new Label("Color:");
        grid.add(kleurKofferLabel, 0, rij);

        ComboBox kleurKofferComboBox = new ComboBox();
        kleurKofferComboBox.getItems().addAll(
                "Blue", "Yellow", "Black", "Gray", "Brown", "Other"
        );
        kleurKofferComboBox.setPrefWidth(225);
        kleurKofferComboBox.getSelectionModel().select(kleur);
        grid.add(kleurKofferComboBox, 1, rij++, 2, 1);

        //eventhandler kleurKofferComboBox
        kleurKofferComboBox.setOnAction((event) -> {
            kleurEntry = (String) kleurKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label merkKofferLabel = new Label("Brand:");
        grid.add(merkKofferLabel, 0, rij);

        ComboBox merkKofferComboBox = new ComboBox();
        merkKofferComboBox.getItems().addAll(
                "American Tourister", "Eastpak", "March", "Porsche Design", "Rimowa",
                "Samsonite", "Swiss Wenger", "Ted Baker", "The North Face",
                "Tumi", "Victorinox", "Other"
        );
        merkKofferComboBox.setPrefWidth(225);
        merkKofferComboBox.getSelectionModel().select(merk);
        grid.add(merkKofferComboBox, 1, rij++, 2, 1);

        //eventhandler merkkoffercombobox      
        merkKofferComboBox.setOnAction((event) -> {
            merkEntry = (String) merkKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label hoogteKofferLabel = new Label("Height of luggage:");
        grid.add(hoogteKofferLabel, 0, rij);

        ComboBox hoogteKofferComboBox = new ComboBox();
        hoogteKofferComboBox.getItems().addAll(
                "10cm-15cm", "15cm-20cm", "20cm-25cm", "25cm-30cm",
                "35cm-40cm", "40cm-45cm", "Unknown"
        );
        hoogteKofferComboBox.setPrefWidth(225);
        hoogteKofferComboBox.getSelectionModel().select(dikte);
        grid.add(hoogteKofferComboBox, 1, rij++, 2, 1);

        //eventhandler hoogtekoffercombobox
        hoogteKofferComboBox.setOnAction((event) -> {
            hoogteEntry = (String) hoogteKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label lengteKofferLabel = new Label("Length of luggage:");
        grid.add(lengteKofferLabel, 0, rij);

        ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
                "30cm-40cm", "40cm-50cm", "50cm-60cm",
                "60cm-70cm", "70cm-80cm", "Unknown"
        );
        lengteKofferComboBox.setPrefWidth(225);
        lengteKofferComboBox.getSelectionModel().select(lengte);
        grid.add(lengteKofferComboBox, 1, rij++, 2, 1);

        //eventhandler lengte combobox
        lengteKofferComboBox.setOnAction((event) -> {
            lengteEntry = (String) lengteKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label breedteKofferLabel = new Label("Width of luggage:");
        grid.add(breedteKofferLabel, 0, rij);

        ComboBox breedteKofferComboBox = new ComboBox();
        breedteKofferComboBox.getItems().addAll(
                "20cm-30cm", "30cm-40cm", "40cm-50cm", "50cm-60cm", "Unknown"
        );
        breedteKofferComboBox.setPrefWidth(225);
        breedteKofferComboBox.getSelectionModel().select(breedte);
        grid.add(breedteKofferComboBox, 1, rij++, 2, 1);

        //eventhandler breedtekoffercombobox      
        breedteKofferComboBox.setOnAction((event) -> {
            breedteEntry = (String) breedteKofferComboBox.getSelectionModel().getSelectedItem();
        });

        Label hardSoftCaseLabel = new Label("Soft/hard case:");
        grid.add(hardSoftCaseLabel, 0, rij);

        ComboBox hardSoftCaseComboBox = new ComboBox();
        hardSoftCaseComboBox.getItems().addAll(
                "Soft", "Hard"
        );
        hardSoftCaseComboBox.setPrefWidth(225);
        hardSoftCaseComboBox.getSelectionModel().select(softhard);
        grid.add(hardSoftCaseComboBox, 1, rij++, 2, 1);

        //eventhandler hardSoftCaseComboBox
        hardSoftCaseComboBox.setOnAction((event) -> {
            hardSoftEntry = (String) hardSoftCaseComboBox.getSelectionModel().getSelectedItem();
        });
        
        Label luchthavenAankomstLabel = new Label("Airport found");
        grid.add(luchthavenAankomstLabel, 0, rij);

        ComboBox vliegveldAankomstComboBox = new ComboBox();
        vliegveldAankomstComboBox.getItems().addAll(
                "Schiphol, Amsterdam", "El Prat, Barcelona", "Atatürk, Istanbul"
        );
        vliegveldAankomstComboBox.setPrefWidth(225);
        vliegveldAankomstComboBox.getSelectionModel().select(luchthavenGevonden);
        grid.add(vliegveldAankomstComboBox, 1, rij++, 2, 1);

        //eventhandler vliegveld aankomst
        vliegveldAankomstComboBox.setOnAction((event) -> {
            luchthavenGevondenEntry = (String) vliegveldAankomstComboBox.getSelectionModel().getSelectedItem();
        });

        Label bijzonderhedenLabel = new Label("Characteristics: ");
        grid.add(bijzonderhedenLabel, 0, rij);

        TextArea bijzonderhedenEntry = new TextArea(bijzonderheden);
        bijzonderhedenEntry.setMaxSize(225, 50);
        grid.add(bijzonderhedenEntry, 1, rij++, 2, 2);
        
        rij++;
        
        Button updateEnCheckMatch = new Button("Update and check for matches");
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(updateEnCheckMatch);
        grid.add(bwvbox, 1, rij++);
        
        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, rij++, 2, 1);
        
        zoekBagage zoekBagage = new zoekBagage();
        
        //verander de gegevens in de database en check of er een match is
        updateEnCheckMatch.setOnAction((ActionEvent e) -> {
            if(kleurEntry == null){kleurEntry = kleur;}
            if(hoogteEntry == null){hoogteEntry = dikte;}
            if(lengteEntry == null){lengteEntry = lengte;}
            if(breedteEntry == null){breedteEntry = breedte;}
            if(luchthavenGevondenEntry == null){luchthavenGevondenEntry = luchthavenGevonden;}
            if(merkEntry == null){merkEntry = merk;}
            if(hardSoftEntry == null){hardSoftEntry = softhard;}
            
            try{
                Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                Statement stmt = (Statement) conn.createStatement();
                //als het bagagelabel al leeg was en leeg is gelaten
                if(bagageLabelEntry.getText() == null || bagageLabelEntry.getText().isEmpty()){
                    String updateString = "UPDATE gevondenbagage SET kleur = '"+kleurEntry+"', dikte = '"+hoogteEntry+"', bagagelabel = null, "
                                        + "lengte = '"+lengteEntry+"', breedte = '"+ breedteEntry + "', luchthavengevonden = '"+luchthavenGevondenEntry
                                        +"', bijzonderhede = '"+bijzonderhedenEntry.getText()+ "', merk = '"+ merkEntry+ "', softhard = '"+ hardSoftEntry +"' WHERE gevondenkofferID = '"+ gevondenkofferID+"'" ;
                        stmt.execute(updateString);
                        zoekBagage.maakZoekString(primaryStage, Integer.parseInt(gevondenkofferID), 
                                        null, kleurEntry, hoogteEntry, lengteEntry, 
                                        breedteEntry, luchthavenGevondenEntry, bijzonderhedenEntry.getText(), 
                                        merkEntry, hardSoftEntry);
                }
                // als het bagagelabel null was en veranderd is naar een echt bagagelabel.
                else if(!bagageLabelEntry.getText().trim().isEmpty() && bagageLabel == null || !bagageLabel.equals(bagageLabelEntry.getText().trim()) ){
                    ResultSet bagagelabelExistsCheck = stmt.executeQuery("SELECT COUNT(*) AS total FROM gevondenbagage WHERE bagagelabel = '"+bagageLabelEntry.getText().trim()+"'");
                    int count = 0;
                    while(bagagelabelExistsCheck.next()){
                        count = bagagelabelExistsCheck.getInt("total");
                    }
                    if(count == 0){
                        String updateString = "UPDATE gevondenbagage SET bagagelabel = '"+bagageLabelEntry.getText().trim()+"', kleur = '"+kleurEntry+"', dikte = '"+hoogteEntry+"', "
                                + "lengte = '"+lengteEntry+"', breedte = '"+ breedteEntry + "', luchthavengevonden = '"+luchthavenGevondenEntry
                                +"', bijzonderhede = '"+bijzonderhedenEntry.getText()+ "', merk = '"+ merkEntry+ "', softhard = '"+ hardSoftEntry +"' WHERE gevondenkofferID = '"+ gevondenkofferID+"'" ;
                        stmt.execute(updateString);
                        zoekBagage.maakZoekString(primaryStage, Integer.parseInt(gevondenkofferID), 
                                bagageLabelEntry.getText(), kleurEntry, hoogteEntry, lengteEntry, 
                                breedteEntry, luchthavenGevondenEntry, bijzonderhedenEntry.getText(), 
                                merkEntry, hardSoftEntry);
                    }
                }
                //als het bagagelabel al bestond en niet is aangepast
                else{
                    String updateString = "UPDATE gevondenbagage SET kleur = '"+kleurEntry+"', dikte = '"+hoogteEntry+"', "
                                        + "lengte = '"+lengteEntry+"', breedte = '"+ breedteEntry + "', luchthavengevonden = '"+luchthavenGevondenEntry
                                        +"', bijzonderhede = '"+bijzonderhedenEntry.getText()+ "', merk = '"+ merkEntry+ "', softhard = '"+ hardSoftEntry +"' WHERE gevondenkofferID = '"+ gevondenkofferID+"'" ;
                        stmt.execute(updateString);
                        zoekBagage.maakZoekString(primaryStage, Integer.parseInt(gevondenkofferID), 
                                        bagageLabelEntry.getText(), kleurEntry, hoogteEntry, lengteEntry, 
                                        breedteEntry, luchthavenGevondenEntry, bijzonderhedenEntry.getText(), 
                                        merkEntry, hardSoftEntry);
                }
            }catch (SQLException ed) {
                System.out.println(ed);
            }
            
        });
        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Adjust found luggage");
        scene.getStylesheets().add("global/Style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    //veranderd de status van de koffer en de bijbehorende match
    private void pasStatusAan(Stage primaryStage, String verlorenKofferID, String status, String bagageLabel){
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
        
        //voegt het bagagelabel en combobox voor de status toe aan de grid 
        Text scenetitle = new Text("Adjust status");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        Label bagageLabelLabel = new Label("Lugagelabelnumber:");
        grid.add(bagageLabelLabel, 0, 1);
        
        Label bagage = new Label(bagageLabel);
        grid.add(bagage, 1,1);
        
        Label statusLabel = new Label("Status");
        grid.add(statusLabel, 0, 2);
        
        ComboBox statusComboBox = new ComboBox();
        statusComboBox.getItems().addAll("solved" , "pending", "not solved");
        statusComboBox.setPrefWidth(150);
        statusComboBox.getSelectionModel().select(status);
        
        statusComboBox.setOnAction((event) -> {
            statusEntry = (String) statusComboBox.getSelectionModel().getSelectedItem();
        });
        grid.add(statusComboBox, 1, 2);
        
        Button pasAan = new Button("Adjust status");
        HBox bwvbox1 = new HBox(10);
        bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox1.getChildren().add(pasAan);
        grid.add(bwvbox1, 1, 3);
        
        Button back = new Button("Go back");
        back.setMinWidth(150);
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(back);
        grid.add(bwvbox, 1, 4);
        
        Text actiontarget = new Text();
        actiontarget.setFill(Color.GREEN);
        grid.add(actiontarget, 1, 5);
        
        pasAan.setOnAction((ActionEvent e) -> {
            if(statusEntry == null){statusEntry = status;}
            if(statusEntry.equals("not solved")){statusEntry = "notSolved";}
           
                try { 
                    int bijbehorendeKoffer = 0;
                    Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT verlorenkofferID FROM opgelost WHERE gevondenkofferID = '"+verlorenKofferID+"'");
                    while(rs.next()){
                        bijbehorendeKoffer = rs.getInt("verlorenkofferID");
                    }
                    stmt.execute("UPDATE verlorenbagage SET status = '"+statusEntry+"' WHERE verlorenkofferID = '"+bijbehorendeKoffer+"'");
                    stmt.execute("UPDATE gevondenbagage SET status = '"+statusEntry+"' WHERE gevondenkofferID = '"+verlorenKofferID+"'");
                    if(statusEntry.equals("notSolved")){
                        stmt.execute("DELETE FROM opgelost WHERE gevondenkofferID = '"+verlorenKofferID+"'");
                    }
                    actiontarget.setText("Status updated");
                }
                catch (SQLException ed) {
                System.out.println(ed);
            }
        });
        
        //ga terug naar het overzicht van de bagage
        back.setOnAction((ActionEvent e) -> {
           GevondenBagageAanpassen gevondenBagageAanpassen = new GevondenBagageAanpassen();
           gevondenBagageAanpassen.start(primaryStage);
        });
        
        Scene scene = new Scene(root, 1200, 920);
        primaryStage.setTitle("Adjust status");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    //deze klasse wordt gebruikt om een tableview te kunnen maken
    public static class BagageStuk{
        private SimpleStringProperty verlorenkofferID, bagagelabel, kleur, dikte, lengte, breedte, 
                    luchthavenvertrokken, datum, softhard, bijzonderheden, 
                     merk, status;
        
        private BagageStuk(String gevondenkofferID, String bagagelabel, String kleur, String dikte, String lengte, String breedte, String luchthavenvertrokken, String datum, String softhard, String bijzonderhede, String merk, String status) {
            this.verlorenkofferID = new SimpleStringProperty(gevondenkofferID);
            this.bagagelabel = new SimpleStringProperty(bagagelabel);
            this.kleur = new SimpleStringProperty(kleur);
            this.dikte = new SimpleStringProperty(dikte);
            this.lengte = new SimpleStringProperty(lengte);
            this.breedte = new SimpleStringProperty(breedte);
            this.luchthavenvertrokken = new SimpleStringProperty(luchthavenvertrokken);
            this.datum = new SimpleStringProperty(datum);
            this.softhard = new SimpleStringProperty(softhard);
            this.bijzonderheden = new SimpleStringProperty(bijzonderhede);
            this.merk = new SimpleStringProperty(merk);
            this.status = new SimpleStringProperty(status);
        }
    
    
        public String getVerlorenkofferID() {
            return verlorenkofferID.get();
        }

        public void setVerlorenkofferID(String verlorenKofferID) {
            this.verlorenkofferID.set(verlorenKofferID);
        }

        public String getBagagelabel() {
            return bagagelabel.get();
        }

        public void setBagagelabel(String bagageLabel) {
            this.bagagelabel.set(bagageLabel);
        }

        public String getKleur() {
            return kleur.get();
        }

        public void setKleur(String kleur) {
            this.kleur.set(kleur);
        }

        public String getDikte() {
            return dikte.get();
        }

        public void setDikte(String dikte) {
            this.dikte.set(dikte);
        }

        public String getLengte() {
            return lengte.get();
        }

        public void setLengte(String lengte) {
            this.lengte.set(lengte);
        }

        public String getBreedte() {
            return breedte.get();
        }

        public void setBreedte(String breedte) {
            this.breedte.set(breedte);
        }

        public String getLuchthavenvertrokken() {
            return luchthavenvertrokken.get();
        }

        public void setLuchthavenvertrokken(String luchthavenVertrokken) {
            this.luchthavenvertrokken.set(luchthavenVertrokken);
        }

        public String getDatum() {
            return datum.get();
        }

        public void setDatum(String datum) {
            this.datum.set(datum);
        }

        public String getSofthard() {
            return softhard.get();
        }

        public void setSofthard(String softHard) {
            this.softhard.set(softHard);
        }

        public String getBijzonderheden() {
            return bijzonderheden.get();
        }

        public void setBijzonderheden(String bijzonderheden) {
            this.bijzonderheden.set(bijzonderheden);
        }

        public String getMerk() {
            return merk.get();
        }

        public void setMerk(String merk) {
            this.merk.set(merk);
        }
        
        public String getStatus(){
            return status.get();
        }
        
        public void setStatus(String status){
            this.status.set(status);
        }
    
    }
    
    
}
