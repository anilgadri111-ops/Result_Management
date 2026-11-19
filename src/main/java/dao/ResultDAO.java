package dao;

import db.DBConnection;
import model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    // Add result
    public boolean addResult(Result result) {
        String sql = "INSERT INTO results (student_id, subject_name, marks_obtained, max_marks, grade, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, result.getStudentId());
            ps.setString(2, result.getSubjectName());
            ps.setInt(3, result.getMarksObtained());
            ps.setInt(4, result.getMaxMarks());
            ps.setString(5, Result.calculateGrade(result.getMarksObtained(), result.getMaxMarks()));
            ps.setString(6, Result.calculateStatus(result.getMarksObtained(), result.getMaxMarks()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding result: " + e.getMessage());
            return false;
        }
    }

    // Update result
    public boolean updateResult(Result result) {
        String sql = "UPDATE results SET marks_obtained=?, grade=?, status=? WHERE result_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, result.getMarksObtained());
            ps.setString(2, Result.calculateGrade(result.getMarksObtained(), result.getMaxMarks()));
            ps.setString(3, Result.calculateStatus(result.getMarksObtained(), result.getMaxMarks()));
            ps.setInt(4, result.getResultId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating result: " + e.getMessage());
            return false;
        }
    }

    // Delete result
    public boolean deleteResult(int resultId) {
        String sql = "DELETE FROM results WHERE result_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting result: " + e.getMessage());
            return false;
        }
    }

    // Get results by student ID
    public List<Result> getResultsByStudentId(int studentId) {
        List<Result> list = new ArrayList<>();
        String sql = "SELECT r.*, s.name, s.roll_no FROM results r " +
                     "JOIN students s ON r.student_id = s.student_id " +
                     "WHERE r.student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Result result = new Result();
                result.setResultId(rs.getInt("result_id"));
                result.setStudentId(rs.getInt("student_id"));
                result.setStudentName(rs.getString("name"));
                result.setRollNo(rs.getString("roll_no"));
                result.setSubjectName(rs.getString("subject_name"));
                result.setMarksObtained(rs.getInt("marks_obtained"));
                result.setMaxMarks(rs.getInt("max_marks"));
                result.setGrade(rs.getString("grade"));
                result.setStatus(rs.getString("status"));
                list.add(result);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching results: " + e.getMessage());
        }
        return list;
    }

    // Get all results
    public List<Result> getAllResults() {
        List<Result> list = new ArrayList<>();
        String sql = "SELECT r.*, s.name, s.roll_no FROM results r " +
                     "JOIN students s ON r.student_id = s.student_id ORDER BY s.name";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Result result = new Result();
                result.setResultId(rs.getInt("result_id"));
                result.setStudentId(rs.getInt("student_id"));
                result.setStudentName(rs.getString("name"));
                result.setRollNo(rs.getString("roll_no"));
                result.setSubjectName(rs.getString("subject_name"));
                result.setMarksObtained(rs.getInt("marks_obtained"));
                result.setMaxMarks(rs.getInt("max_marks"));
                result.setGrade(rs.getString("grade"));
                result.setStatus(rs.getString("status"));
                list.add(result);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all results: " + e.getMessage());
        }
        return list;
    }

    // Check if result already exists for student + subject
    public boolean resultExists(int studentId, String subjectName) {
        String sql = "SELECT COUNT(*) FROM results WHERE student_id=? AND subject_name=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, subjectName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
