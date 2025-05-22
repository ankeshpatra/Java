package dao;

import model.Student;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection connection;

    public StudentDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (roll_number, name, class, section, dob, " +
                    "gender, contact_number, address, enrollment_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getRollNumber());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getClassName());
            stmt.setString(4, student.getSection());
            stmt.setDate(5, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmt.setString(6, student.getGender());
            stmt.setString(7, student.getContactNumber());
            stmt.setString(8, student.getAddress());
            stmt.setString(9, student.getEnrollmentStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("name"),
                    rs.getString("roll_number"),
                    rs.getString("class"),
                    rs.getString("section"),
                    rs.getDate("dob"),
                    rs.getString("gender"),
                    rs.getString("contact_number"),
                    rs.getString("address")
                );
                student.setEnrollmentStatus(rs.getString("enrollment_status"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name=?, class=?, section=?, dob=?, " +
                    "gender=?, contact_number=?, address=?, enrollment_status=? " +
                    "WHERE roll_number=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getClassName());
            stmt.setString(3, student.getSection());
            stmt.setDate(4, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmt.setString(5, student.getGender());
            stmt.setString(6, student.getContactNumber());
            stmt.setString(7, student.getAddress());
            stmt.setString(8, student.getEnrollmentStatus());
            stmt.setString(9, student.getRollNumber());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String rollNumber) {
        String sql = "DELETE FROM students WHERE roll_number = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rollNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> searchStudents(String searchTerm, String filterType) {
        List<Student> students = new ArrayList<>();
        String sql;
        
        // Determine the appropriate SQL query based on filter type
        if (filterType.equals("roll_number")) {
            // For roll number, we want an exact match
            sql = "SELECT * FROM students WHERE roll_number = ?";
        } else {
            // For name, we want a partial match
            sql = "SELECT * FROM students WHERE " + filterType + " LIKE ?";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (filterType.equals("roll_number")) {
                stmt.setString(1, searchTerm);
            } else {
                stmt.setString(1, "%" + searchTerm + "%");
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("name"),
                    rs.getString("roll_number"),
                    rs.getString("class"),
                    rs.getString("section"),
                    rs.getDate("dob"),
                    rs.getString("gender"),
                    rs.getString("contact_number"),
                    rs.getString("address")
                );
                student.setEnrollmentStatus(rs.getString("enrollment_status"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
} 