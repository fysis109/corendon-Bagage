/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

import balie.GevKofferReg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import login.Login;

/**
 *
 * @author Joljin Verwest
 */
public class MenuB {
    
    GevKofferReg GVR = new GevKofferReg();
    
    public MenuBar createMenuB(Stage primaryStage){
        
        MenuBar menuBar = new MenuBar();
        Home home = new Home();
        Login login = new Login();
        
    // File menu - new, save, exit
    Menu homeB = new Menu("Options");
    Menu options = new Menu("Options");
    Button btn = new Button("test");
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

    Menu webMenu = new Menu("Web");
    CheckMenuItem htmlMenuItem = new CheckMenuItem("HTML");
    htmlMenuItem.setSelected(true);
    webMenu.getItems().add(htmlMenuItem);

    CheckMenuItem cssMenuItem = new CheckMenuItem("CSS");
    cssMenuItem.setSelected(true);
    webMenu.getItems().add(cssMenuItem);

    Menu sqlMenu = new Menu("SQL");
    ToggleGroup tGroup = new ToggleGroup();
    RadioMenuItem mysqlItem = new RadioMenuItem("MySQL");
    mysqlItem.setToggleGroup(tGroup);

    RadioMenuItem oracleItem = new RadioMenuItem("Oracle");
    oracleItem.setToggleGroup(tGroup);
    oracleItem.setSelected(true);

    sqlMenu.getItems().addAll(mysqlItem, oracleItem,
        new SeparatorMenuItem());

    Menu tutorialManeu = new Menu("Tutorial");
    tutorialManeu.getItems().addAll(
        new CheckMenuItem("Java"),
        new CheckMenuItem("JavaFX"),
        new CheckMenuItem("Swing"));

    sqlMenu.getItems().add(tutorialManeu);

    menuBar.getMenus().addAll(homeB, webMenu, sqlMenu);
        
    return menuBar;
    
    }
    
}
