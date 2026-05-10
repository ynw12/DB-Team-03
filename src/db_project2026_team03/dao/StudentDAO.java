package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.StudentDTO;

public class StudentDAO {

    public boolean insertStudent(StudentDTO student) {
        String sql = "INSERT INTO Student (student_id, name, university, major, phone) VALUES (?, ?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getUniversity());
            pstmt.setString(4, student.getMajor());
            pstmt.setString(5, student.getPhone());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Student 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    public List<StudentDTO> selectAllStudents() {
        String sql = "SELECT * FROM Student";
        List<StudentDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                StudentDTO dto = new StudentDTO();
                dto.setStudentId(rs.getString("student_id"));
                dto.setName(rs.getString("name"));
                dto.setUniversity(rs.getString("university"));
                dto.setMajor(rs.getString("major"));
                dto.setPhone(rs.getString("phone"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Student 조회 실패: " + e.getMessage());
        }
        return list;
    }
}
