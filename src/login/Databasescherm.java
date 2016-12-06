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
 
    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data =
        FXCollections.observableArrayList(
            new Person("23123", "Rood", "Hardcase", "Portugal"),
            new Person("31244", "Geel", "Hardcase", "Turkije"),
            new Person("431231", "Zwart", "Softcase", "Nederland"),
            new Person("31234", "Bruin", "Softcase", "Griekenland"),
            new Person("31234", "Zwart", "Hardcase", "Turkije")
        );
   
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(600);
        stage.setHeight(500);
 
        final Label label = new Label("Gevonden Koffers");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn firstNameCol = new TableColumn("Tan code");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));
 
        TableColumn lastNameCol = new TableColumn("Kleur");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));
 
        TableColumn emailCol = new TableColumn("Soft/Hard-case");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));
        
        TableColumn landCol = new TableColumn("Land");
        landCol.setMinWidth(200);
        landCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("Land"));
 
        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol,landCol);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    public static class Person {
 
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
        private final SimpleStringProperty land;
 
        private Person(String fName, String lName, String email, String land) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
            this.land = new SimpleStringProperty(land);
        }
 
        public String getFirstName() {
            return firstName.get();
        }
 
        public void setFirstName(String fName) {
            firstName.set(fName);
        }
 
        public String getLastName() {
            return lastName.get();
        }
 
        public void setLastName(String fName) {
            lastName.set(fName);
        }
 
        public String getEmail() {
            return email.get();
        }
 
        public void setEmail(String fName) {
            email.set(fName);
        }
         public String getLand() {
            return land.get();
        }
 
        public void setLand(String fName) {
            land.set(fName);
        }
    }
} 