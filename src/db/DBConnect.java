package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class DBConnect {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));

            String DB_URL  = props.getProperty("db.url");
            String DB_USER = props.getProperty("db.user");
            String DB_PASS = props.getProperty("db.password");

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}