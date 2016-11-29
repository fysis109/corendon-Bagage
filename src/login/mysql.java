package login;

public class Mysql {

    private final String USENAME = "root";
    private final String PASSWORD = "Pulsar11";
    private final String IPADDRESS = "localhost";
    private final String POORT = "3306";
    private final String DATABASENAAM = "wsdatabase";
    private final boolean AUTORECONNECT = true;
    private final boolean SSL = false;
    
    
    String username(){
        return USENAME;
    };
        
    String password(){
        return PASSWORD;
    };
    
    String urlmysql(){
        return "jdbc:mysql://"+IPADDRESS+":"+POORT+"/"+DATABASENAAM+"?autoReconnect="+AUTORECONNECT+"&useSSL="+SSL;
    };
}