package dao;

import db.DBConnection;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Add a new student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (name, roll_no, department, semester) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getRollNo());
            ps.setString(3, student.getDepartment());
            ps.setInt(4, student.getSemester());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    // Update existing student
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name=?, roll_no=?, department=?, semester=? WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getRollNo());
            ps.setString(3, student.getDepartment());
            ps.setInt(4, student.getSemester());
            ps.setInt(5, student.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    // Delete student
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("roll_no"),
                    rs.getString("department"),
                    rs.getInt("semester")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
        return list;
    }

    // Search student by roll number
    public Student getStudentByRollNo(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("roll_no"),
                    rs.getString("department"),
                    rs.getInt("semester")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error searching student: " + e.getMessage());
        }
        return null;
    }

    // Get student by ID
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("roll_no"),
                    rs.getString("department"),
                    rs.getInt("semester")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student: " + e.getMessage());
        }
        return null;
    }
}
