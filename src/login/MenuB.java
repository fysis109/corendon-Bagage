/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author Joljin Verwest
 */
public class MenuB {
    
    public MenuBar createMenuB(){
        
        MenuBar menuBar = new MenuBar();
        
    // File menu - new, save, exit
    Menu fileMenu = new Menu("File");
    MenuItem newMenuItem = new MenuItem("New");
    MenuItem saveMenuItem = new MenuItem("Save");
    MenuItem exitMenuItem = new MenuItem("Exit");
    exitMenuItem.setOnAction(actionEvent -> Platform.exit());

    fileMenu.getItems().addAll(newMenuItem, saveMenuItem,
        new SeparatorMenuItem(), exitMenuItem);

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

    menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);
        
    return menuBar;
    
    }
    
}
