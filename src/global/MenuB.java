/*
 * Maakt een menubar die bovenin elk scherm toegevoegd kan worden.
 */
package global;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import login.Login;


public class MenuB {
    
    public MenuBar createMenuB(Stage primaryStage){
        
    MenuBar menuBar = new MenuBar();
    Home home = new Home();
    Login login = new Login();
    Profile profiel = new Profile();
        
    // File menu - new, save, exit
    Menu homeB = new Menu("Options");
    Menu options = new Menu("Options");
    MenuItem homePage = new MenuItem("Go to homepage");
    MenuItem logout = new MenuItem("Logout");
    MenuItem exit = new MenuItem("Exit application");
    
    //Plaatje linksonder
        Image logo = new Image("file:src/images/corendon_logo.jpg");
        ImageView imgpic = new ImageView();
        imgpic.setImage(logo);
        imgpic.setFitHeight(50);
        imgpic.setFitWidth(150);
    //button event handelers
    exit.setOnAction(actionEvent -> Platform.exit());
    homePage.setOnAction((ActionEvent e) -> {
        home.start(primaryStage);
    });
    logout.setOnAction((ActionEvent e) -> {
        login.start(primaryStage);
    });
    //toevoegen actions bij options
    homeB.getItems().addAll(homePage, logout,
        new SeparatorMenuItem(), exit);

    //help button
    Menu help = new Menu("Help");
    MenuItem userManual = new MenuItem("Open user manual");
    help.getItems().add(userManual);
    
    //prile button
    Label menuLabel = new Label("Profile");
    Menu profile = new Menu();
    profile.setGraphic(menuLabel);
    menuLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event){
          profiel.star(primaryStage);  
        }
    });
    //handleiding toevoegen bij open user manual
    userManual.setOnAction((ActionEvent e) -> {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("src/images/Handleiding.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered fSor PDFs
            }
        }
    });
    
    //alles toevoegen bij menubar
    menuBar.getMenus().addAll(homeB, help, profile);
        
    return menuBar;
    
    }
    
}
