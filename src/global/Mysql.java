package global;

public class Mysql {

    private final String USENAME = "root";
    private final String PASSWORD = "Pulsar11";
    private final String IPADDRESS = "localhost";
    private final String POORT = "3306";
    private final String DATABASENAAM = "corendonbagagesystem";
    private final boolean AUTORECONNECT = true;
    private final boolean SSL = false;
  
    public String getUsername(){
        return USENAME;
    };
        
    public String getPassword(){
        return PASSWORD;
    };
    
    public String getUrlmysql(){
        return "jdbc:mysql://"+IPADDRESS+":"+POORT+"/"+DATABASENAAM+"?autoReconnect="+AUTORECONNECT+"&useSSL="+SSL;
    };


}