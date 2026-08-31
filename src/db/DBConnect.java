package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    // ─────────────────────────────────────────────────────────────────────────
    // SECURITY WARNING: Move these values to a config file or environment
    // variables before sharing or deploying this project.
    // Example: load from a .properties file so the password is never in source.
    // ─────────────────────────────────────────────────────────────────────────
    private static final String DB_URL  = "jdbc:oracle:thin:@localhost:1521/orcl";
    private static final String DB_USER = "C##student";
    private static final String DB_PASS = "stud123"; // TODO: move to config file

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}
