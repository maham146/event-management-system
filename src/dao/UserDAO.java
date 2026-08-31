package dao;
import model.User;
import db.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // FIX: Added con.commit() — Oracle JDBC requires explicit commit, otherwise
    //      changes are silently rolled back when the connection closes.
    public void insertUser(User u) {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement(
                "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)");
            psmt.setString(1, u.getName());
            psmt.setString(2, u.getEmail());
            psmt.setString(3, u.getPassword());
            psmt.setString(4, u.getRole());
            psmt.executeUpdate();
            con.commit(); // FIX: was missing
            psmt.close(); con.close();
        } catch (SQLException e) { System.out.println(e); }
    }

    public User login(String email, String password) {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement(
                "SELECT * FROM users WHERE email=? AND password=?");
            psmt.setString(1, email);
            psmt.setString(2, password);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                int    id   = rs.getInt("ID");
                String name = rs.getString("Name");
                String role = rs.getString("Role");
                User u = new User(id, name, email, password, role);
                rs.close(); psmt.close(); con.close();
                return u;
            }
            rs.close(); psmt.close(); con.close();
            return null;
        } catch (Exception e) { System.out.print(e); return null; }
    }

    public void viewUsers() {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                System.out.println("User ID: "    + rs.getInt("id"));
                System.out.println("User Name: "  + rs.getString("name"));
                System.out.println("User Email: " + rs.getString("email"));
                System.out.println("User Role: "  + rs.getString("role"));
            }
            rs.close(); psmt.close(); con.close();
        } catch (Exception e) { System.out.println(e); }
    }

    public List<Object[]> getAllUsers() {
        List<Object[]> list = new ArrayList<>();
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement(
                "SELECT id, name, email, role FROM users");
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role")
                });
            }
            rs.close(); psmt.close(); con.close();
        } catch (Exception e) { System.out.println(e); }
        return list;
    }

    public int getTotalUserCount() {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs = psmt.executeQuery();
            int c = rs.next() ? rs.getInt(1) : 0;
            rs.close(); psmt.close(); con.close();
            return c;
        } catch (Exception e) { System.out.println(e); return 0; }
    }

    public boolean checkrole(String role) {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement(
                "SELECT COUNT(*) FROM users WHERE LOWER(role)=LOWER(?)");
            psmt.setString(1, role);
            ResultSet rs = psmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close(); psmt.close(); con.close();
            return count >= 1;
        } catch (Exception e) { System.out.println(e); return false; }
    }

    // FIX: Added con.commit() to all three update methods below
    public void updateName(int id, String name) {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement(
                "UPDATE users SET name=? WHERE id=?");
            psmt.setString(1, name);
            psmt.setInt(2, id);
            psmt.executeUpdate();
            con.commit(); // FIX: was missing
            psmt.close(); con.close();
        } catch (Exception e) { System.out.println(e); }
    }

    public void updateEmail(int id, String email) {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement(
                "UPDATE users SET email=? WHERE id=?");
            psmt.setString(1, email);
            psmt.setInt(2, id);
            psmt.executeUpdate();
            con.commit(); // FIX: was missing
            psmt.close(); con.close();
        } catch (Exception e) { System.out.println(e); }
    }

    public void updatePassword(int id, String password) {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement psmt = con.prepareStatement(
                "UPDATE users SET password=? WHERE id=?");
            psmt.setString(1, password);
            psmt.setInt(2, id);
            psmt.executeUpdate();
            con.commit(); // FIX: was missing
            psmt.close(); con.close();
        } catch (Exception e) { System.out.println(e); }
    }
}
