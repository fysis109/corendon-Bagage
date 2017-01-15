/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import login.Login;

/**
 *
 * @author Joljin Verwest
 */
public class MenuB {
    
    public MenuBar createMenuB(Stage primaryStage){
        
    MenuBar menuBar = new MenuBar();
    Home home = new Home();
    Login login = new Login();
        
    // File menu - new, save, exit
    Menu homeB = new Menu("Options");
    Menu options = new Menu("Options");
    MenuItem homePage = new MenuItem("Go to homepage");
    MenuItem logout = new MenuItem("Logout");
    MenuItem exit = new MenuItem("Exit application");
    
    exit.setOnAction(actionEvent -> Platform.exit());
    homePage.setOnAction((ActionEvent e) -> {
        home.start(primaryStage);
        });
        logout.setOnAction((ActionEvent e) -> {
        login.start(primaryStage);
        });
    
    homeB.getItems().addAll(homePage, logout,
        new SeparatorMenuItem(), exit);

    Menu webMenu = new Menu("Help");
    MenuItem userManual = new CheckMenuItem("Open user manual");
    webMenu.getItems().add(userManual);
    
    userManual.setOnAction((ActionEvent e) -> {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("src/images/Handleiding.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    });
    
    menuBar.getMenus().addAll(homeB, webMenu);
        
    return menuBar;
    
    }
    
}
