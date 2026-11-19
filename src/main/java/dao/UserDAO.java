package dao;

import db.DBConnection;

import java.sql.*;

public class UserDAO {

    public String login(String username, String password) {
        String sql = "SELECT role FROM users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); // "admin" or "student"
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null; // login failed
    }
}
