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
	
	//신규 학생 정보 데이터베이스 추가
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
    
    //등록된 전체 학생 목록 조회
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
    
  //입력된 학번이 존재하는지 확인
    public boolean checkStudentExist(String studentId) {
    	String sql = "SELECT 1 FROM Student WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();//레코드가 한 행이라도 존재하면 true,없음 false.
            }
        } catch (SQLException e) {
            System.out.println("xx 학생 확인 실패: " + e.getMessage());
        }
        return false;
	}
    
    //학번으로 학생 이름 조회
    public static String getStudentName(int studentId) {
        String nameSql = "SELECT name FROM Student WHERE student_id = ?";
        String studentName = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(nameSql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    studentName = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("xx 학생 조회 실패: " + e.getMessage());
            return null;
        }

        if (studentName == null) {
            System.out.println("xx 존재하지 않는 학생입니다.");
            return null;
        }

        return studentName;
    }
}
