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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class Databasescherm extends Application {
      Mysql mysql = new Mysql();
      Connection conn;
      private final String USERNAME = mysql.getUsername();
      private final String PASSWORD = mysql.getPassword();
      private final String CONN_STRING = mysql.getUrlmysql();
      private String kofferid;
      private String dlabel;
      private String kleur;
      private String dikte;
      private String lengte;
      private String breedte;
      private String luchthavengevonden;
      private String datum;
      private String softhard;
      private String bijzonderhede;
      private int idnumber = 0;
      private int idcounter;
      
      
             
      
      
      
      
    
    
    private TableView<Person> table = new TableView<Person>();
    
    
   
    public static void main(String[] args) {
        
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        try{           
                        //maak connectie met het database
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        // querry voor aantal koffers
                        String query1 = "select count(*) AS count from gevondenbagage";
                        System.out.println(query1);
                        
                        // create the java statement
                        Statement st1 = conn.createStatement();
                        
                        // execute the query, and get a java resultset
                        ResultSet databaseResponse = st1.executeQuery(query1);
                        while (databaseResponse.next())
                        { 
                             this.idcounter = databaseResponse.getInt("count");
                            
                        }
                        Person[] person = new Person[this.idcounter];
                        
                       
                        
                            String query2 =  "SELECT\n" +
                                             "(@testid := @testid + 1) AS rowNumber , gevondenkofferID, bagagelabel,kleur,dikte,lengte,breedte,luchthavengevonden,datum,softhardcase,bijzonderhede\n" +
                                             "FROM gevondenbagage AS t\n" +
                                             "CROSS JOIN (SELECT @testid := 0) AS dummy\n" +
                                             "ORDER BY gevondenkofferID ;";
                        System.out.println(query2);
                            Statement st2 = conn.createStatement();
                            ResultSet databaseResponse2 = st2.executeQuery(query2);
                            
                        ObservableList<Person> data = FXCollections.observableArrayList();
                        
                        while (databaseResponse2.next())
                        {   
                            
                                
                            //database response verwerken
                            this.kofferid = databaseResponse2.getString("gevondenkofferID");
                            this.dlabel = databaseResponse2.getString("bagagelabel");
                            this.kleur = databaseResponse2.getString("kleur");
                            this.dikte = databaseResponse2.getString("dikte");
                            this.lengte = databaseResponse2.getString("lengte");
                            this.breedte = databaseResponse2.getString("breedte");
                            this.luchthavengevonden = databaseResponse2.getString("luchthavengevonden");
                            this.datum = databaseResponse2.getString("datum");
                            this.softhard = databaseResponse2.getString("softhardcase");
                            this.bijzonderhede = databaseResponse2.getString("bijzonderhede");
                            this.idnumber = databaseResponse2.getInt("rowNumber");
                           

                             
                             
                             data.add(new Person(kofferid,dlabel,kleur,dikte,lengte,breedte,luchthavengevonden,datum,softhard,bijzonderhede));
                             
                             table.setItems(data);
                            
                        }
        }
                        catch (SQLException ed) {
                        System.err.println(ed);
                        }
        
        
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(1900);
        stage.setHeight(400);
 
        final Label label = new Label("Gevonden Koffers");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn gevondenkofferIDcol = new TableColumn("id");
        gevondenkofferIDcol.setMinWidth(10);
        gevondenkofferIDcol.setCellValueFactory(
                new PropertyValueFactory<>("gevondenkofferID"));
 
        TableColumn bagagelabelcol = new TableColumn("bagagelabel");
        bagagelabelcol.setMinWidth(100);
        bagagelabelcol.setCellValueFactory(
                new PropertyValueFactory<>("bagagelabel"));
 
        TableColumn kleurcol = new TableColumn("kleur");
        kleurcol.setMinWidth(200);
        kleurcol.setCellValueFactory(
                new PropertyValueFactory<>("kleur"));
        
        TableColumn diktecol = new TableColumn("dikte");
        diktecol.setMinWidth(200);
        diktecol.setCellValueFactory(
                new PropertyValueFactory<>("dikte"));
        
        TableColumn lengtecol = new TableColumn("lengte");
        lengtecol.setMinWidth(200);
        lengtecol.setCellValueFactory(
                new PropertyValueFactory<>("lengte"));
        
        TableColumn breedtecol = new TableColumn("breedte");
        breedtecol.setMinWidth(200);
        breedtecol.setCellValueFactory(
                new PropertyValueFactory<>("breedte"));
        
        TableColumn luchthavengevondencol = new TableColumn("luchthavengevonden");
        luchthavengevondencol.setMinWidth(200);
        luchthavengevondencol.setCellValueFactory(
                new PropertyValueFactory<>("luchthavengevonden"));
        
        TableColumn datumcol = new TableColumn("datum");
        datumcol.setMinWidth(200);
        datumcol.setCellValueFactory(
                new PropertyValueFactory<>("datum"));
        
        TableColumn softhardcol = new TableColumn("soft/hard");
        softhardcol.setMinWidth(200);
        softhardcol.setCellValueFactory(
                new PropertyValueFactory<>("softhard"));
        
        TableColumn bijzonderhedecol = new TableColumn("bijzonderhede");
        bijzonderhedecol.setMinWidth(200);
        bijzonderhedecol.setCellValueFactory(
                new PropertyValueFactory<>("bijzonderhede"));
          
        
        
 
       
        table.getColumns().addAll(gevondenkofferIDcol, bagagelabelcol, kleurcol, diktecol, lengtecol, breedtecol, luchthavengevondencol, datumcol, softhardcol, bijzonderhedecol);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(10);
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
        
        }
    }
