/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import global.Home;
import global.MenuB;
import global.Mysql;

public class ZoekMatchVerlorenBagage {
    
    private int customerId;
    private Stage primaryStage = new Stage();
    private int verlorenKofferID;
    private final Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();

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
        } else {
            zoekCriteria += " AND kleur = 'Other'";
        }
        if (!hoogte.equals("Unknown")) {
            zoekCriteria += " AND (dikte = '" + hoogte + "' OR dikte = 'Unknown' )";
        } else {
            zoekCriteria += " AND dikte = 'Unknown'";
        }
        if (!lengte.equals("Unknown")) {
            zoekCriteria += " AND (lengte = '" + lengte + "' OR lengte = 'Unknown' )";
        } else {
            zoekCriteria += " AND lengte = 'Unknown'";
        }
        if (!breedte.equals("Unknown")) {
            zoekCriteria += " AND (breedte = '" + breedte + "' OR breedte = 'Unknown' )";
        } else {
            zoekCriteria += " AND breedte = 'Unknown'";
        }
        if (!merk.equals("Other")) {
            zoekCriteria += " AND (merk = '" + merk + "' OR merk = 'Other' )";
        } else {
            zoekCriteria += " AND merk = 'Other'";
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
            table.setMinHeight(800);
            table.setMaxHeight(800);
            
            
            
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
            
            
        }catch (SQLException ed) {
                  
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
