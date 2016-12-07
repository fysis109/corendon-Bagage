/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

/**
 *
 * @author Jiorgos
 */
import java.sql.Connection;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class Databasescherm extends Application {
      Mysql mysql = new Mysql();
     Connection con;
    
    
    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data =
        FXCollections.observableArrayList(
            new Person("001","34GF4" ,"Rood","GF","GF","GF", "Portugal","20160101", "Hardcase","g" ),
            new Person("002","34GF4" ,"Geel","GF","GF","GF","Turkije","20160101", "Hardcase","g"),
            new Person("003","34GF4" ,"Zwart","GF","GF","GF","Nederland","20160101","Softcase","g" ),
            new Person("004","34GF4" ,"Bruin","GF","GF","GF", "Griekenland","20160101", "Softcase","g"),
            new Person("005","34GF4" ,"Zwart","GF","GF","GF","Turkije","20160101", "Hardcase","g" )
        );
   
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(1950);
        stage.setHeight(500);
 
        final Label label = new Label("Gevonden Koffers");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn gevondenkofferIDcol = new TableColumn("id");
        gevondenkofferIDcol.setMinWidth(100);
        gevondenkofferIDcol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("id"));
 
        TableColumn bagagelabelcol = new TableColumn("bagagelabel");
        bagagelabelcol.setMinWidth(100);
        bagagelabelcol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("bagagelabel"));
 
        TableColumn kleurcol = new TableColumn("kleur");
        kleurcol.setMinWidth(200);
        kleurcol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("kleur"));
        
        TableColumn diktecol = new TableColumn("dikte");
        diktecol.setMinWidth(200);
        diktecol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("dikte"));
        
        TableColumn lengtecol = new TableColumn("lengte");
        lengtecol.setMinWidth(200);
        lengtecol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lengte"));
        
        TableColumn breedtecol = new TableColumn("breedte");
        breedtecol.setMinWidth(200);
        breedtecol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("breedte"));
        
        TableColumn luchthavengevondencol = new TableColumn("luchthavengevonden");
        luchthavengevondencol.setMinWidth(200);
        luchthavengevondencol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("luchthavengevonden"));
        
        TableColumn datumcol = new TableColumn("datum");
        datumcol.setMinWidth(200);
        datumcol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("datum"));
        
        TableColumn softhardcol = new TableColumn("soft/hard");
        softhardcol.setMinWidth(200);
        softhardcol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("soft/hard"));
        
        TableColumn bijzonderhedecol = new TableColumn("bijzonderhede");
        bijzonderhedecol.setMinWidth(200);
        bijzonderhedecol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("bijzonderhede"));
 
        table.setItems(data);
        table.getColumns().addAll(gevondenkofferIDcol, bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavengevondencol, datumcol, softhardcol, bijzonderhedecol);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    public static class Person {
 
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
 
        public void setbagagelabel(String gevondenkofferID) {
            this.bagagelabel.set(gevondenkofferID);
        }
 
        public String getkleur() {
            return kleur.get();
        }
 
        public void setkleur(String gevondenkofferID) {
            this.kleur.set(gevondenkofferID);
        }
         public String getdikte() {
            return dikte.get();
        }
 
        public void setdikte(String gevondenkofferID) {
            this.dikte.set(gevondenkofferID);
        }
        
        public String getlengte() {
            return lengte.get();
        }
 
        public void setlengte(String gevondenkofferID) {
            this.lengte.set(gevondenkofferID);
        }
        
        public String getbreedte() {
            return breedte.get();
        }
 
        public void setbreedte(String gevondenkofferID) {
            this.breedte.set(gevondenkofferID);
        }
        
        public String getluchthavengevonden() {
            return luchthavengevonden.get();
        }
 
        public void setluchthavengevonden(String gevondenkofferID) {
            this.luchthavengevonden.set(gevondenkofferID);
        }
        
        public String getdatum() {
            return datum.get();
        }
 
        public void setdatum(String gevondenkofferID) {
            this.datum.set(gevondenkofferID);
        }
        
        public String getsofthard() {
            return softhard.get();
        }
 
        public void setsofthard(String gevondenkofferID) {
            this.softhard.set(gevondenkofferID);
        }
        
        public String getbijzonderhede() {
            return bijzonderhede.get();
        }
 
        public void setbijzonderhede(String gevondenkofferID) {
            this.bijzonderhede.set(gevondenkofferID);
        }
        
        }
    }
