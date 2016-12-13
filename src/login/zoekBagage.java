package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class zoekBagage {
    
    //Strings
    private final String BagageNummer; 
    private final String kofferKleur;
    private final String merkKoffer;
    private final String breedteKoffer;
    private final String lengteKoffer;
    private final String dikteKoffer;
    private final String locatieKoffer;
    
    //mysql connectie
    Mysql mysql = new Mysql();
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    public static String rol;    
    
    zoekBagage (String BagageNummer, String kofferKleur, String merkKoffer, String breedteKoffer, String lengteKoffer, String dikteKoffer, String locatieKoffer){
    
        //variable verwerken
        this.BagageNummer = BagageNummer;
        this.kofferKleur = kofferKleur;
        this.merkKoffer = merkKoffer;
        this.breedteKoffer = breedteKoffer;
        this.lengteKoffer = lengteKoffer;
        this.dikteKoffer = dikteKoffer;
        this.locatieKoffer = locatieKoffer;
    }
    
    int[] check(){
        Connection conn;
        try {

            //maak connectie met het database
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            
            //create the java statement
            Statement st = conn.createStatement();
            
            //if else
            System.out.println(BagageNummer);
            if (!BagageNummer.trim().isEmpty()){
                
                // execute the query, and get a java resultset
                ResultSet databaseResponse = st.executeQuery("SELECT * FROM verlorenbagage WHERE bagagelabel ='"+BagageNummer+"'");
                while (databaseResponse.next()){
                    
                    //database response verwerken
                    String DBBagageNummer = databaseResponse.getString("bagagelabel");
                    
                    //kijk of het bagage nummer het zelfde is
                    if(BagageNummer.equals(DBBagageNummer)){

                        //database response verwerken
                        int[] gevondenkofferID = {databaseResponse.getInt("verlorenkofferID")};

                        //koffer id terug geven
                        return gevondenkofferID;
                            //SELECT * FROM verlorenbagage WHERE kleur = '"+kofferKleur+"' AND merk = '"+merkKoffer+"' AND breedte='"+breedteKoffer+"' AND lengte ='"+lengteKoffer+"' AND dikte = '"+dikteKoffer+"'" 
                    }
                }            
            } else {
                
                //quary string maken
                //String ZoekenKenmerkenQuary = "SELECT * FROM verlorenbagage WHERE";

                //kijk of er 1 of meer kenmerken zijn ingevuld
                if ("overige kleur".equals(kofferKleur) && "overige".equals(merkKoffer) && "niet bekend".equals(breedteKoffer) && "niet bekend".equals(lengteKoffer) && "niet bekend".equals(dikteKoffer)){
                    System.out.println("Vul aub speciekere informatie");
                } else {
                    int [] zoekenOpkenmerkenCallback = {zoekenOpKenmerken()};
                    return zoekenOpkenmerkenCallback;
                }
            }
        } catch (SQLException ed) {
            System.err.println(ed);
        }
        int [] arry3 = {00};
        return arry3;
    };
    
    
    int zoekenOpKenmerken(){
        
        //
        Connection conn;
        try {
            //maak connectie met het database
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            
            //create the java statement
            Statement st = conn.createStatement();
            
            //quary string maken
            String ZoekenKenmerkenQuary = "SELECT * FROM verlorenbagage WHERE";
            
            //kijk of er 1 of meer kenmerken zijn ingevuld
            if ("other".equals(kofferKleur) && "other".equals(merkKoffer) && "unknown".equals(breedteKoffer) && "unknown".equals(lengteKoffer) && "unknown".equals(dikteKoffer)){
                System.out.println("Vul aub speciekere informatie");
            } else {
                //alle kenmerken door gaan om te kijken we info handig is om te gebruiken.
                
                //koffer kleur
                if(!"other".equals(kofferKleur)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND kleur='"+kofferKleur+"'";
                    } else {
                        ZoekenKenmerkenQuary += " kleur='"+kofferKleur+"'";
                    }
                }
                
                //merkKoffer
                if(!"other".equals(merkKoffer)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND merk='"+merkKoffer+"'";
                    } else {
                        ZoekenKenmerkenQuary += " merk='"+merkKoffer+"'";
                    }
                }
                
                //breedteKoffer
                if(!"unknown".equals(breedteKoffer)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND breedte='"+breedteKoffer+"'";
                    } else {
                        ZoekenKenmerkenQuary += " breedte='"+breedteKoffer+"'";
                    }
                }

                //lengteKoffer
                if(!"unknown".equals(lengteKoffer)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND lengte='"+lengteKoffer+"'";
                    } else {
                        ZoekenKenmerkenQuary += " lengte='"+lengteKoffer+"'";
                    }
                }
                
                //dikteKoffer
                if(!"unknown".equals(dikteKoffer)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND dikte='"+dikteKoffer+"'";
                    } else {
                        ZoekenKenmerkenQuary += " dikte='"+dikteKoffer+"'";
                    }
                }
                
                
                
                System.out.println(ZoekenKenmerkenQuary);
                
                ResultSet databaseResponse = st.executeQuery(ZoekenKenmerkenQuary);

                //
                while (databaseResponse.next()){
                    
                }    
            }
        } catch (SQLException ed) {
            System.err.println(ed);
        }
        
        return 5;
    };
    /*
    //screen
    public void start(Stage primaryStage) {
        
        //grind
        primaryStage.setTitle("Baggage found.");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Titel    
        Label scenetitle = new Label("Match screen.");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        scenetitle.setAlignment(Pos.BOTTOM_CENTER);
        grid.add(scenetitle, 0, 0);
        
        //scene
        Scene scene = new Scene(grid, 1200, 920);
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/
}