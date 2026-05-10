package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.ApplicationDTO;

public class ApplicationDAO {

    public boolean insertApplication(ApplicationDTO app) {
        String sql = "INSERT INTO Application (application_id, recruitment_id, student_id, self_intro, status) VALUES (?, ?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, app.getApplicationId());
            pstmt.setInt(2, app.getRecruitmentId());
            pstmt.setString(3, app.getStudentId());
            pstmt.setString(4, app.getSelfIntro());
            pstmt.setString(5, app.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Application 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    public List<ApplicationDTO> selectAllApplications() {
        String sql = "SELECT * FROM Application";
        List<ApplicationDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ApplicationDTO dto = new ApplicationDTO();
                dto.setApplicationId(rs.getInt("application_id"));
                dto.setRecruitmentId(rs.getInt("recruitment_id"));
                dto.setStudentId(rs.getString("student_id"));
                dto.setSelfIntro(rs.getString("self_intro"));
                dto.setStatus(rs.getString("status"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Application 조회 실패: " + e.getMessage());
        }
        return list;
    }
}