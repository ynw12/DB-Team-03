package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.RecruitmentDTO;

public class RecruitmentDAO {

    public boolean insertRecruitment(RecruitmentDTO recruitment) {
        String sql = "INSERT INTO Recruitment (recruitment_id, org_id, title, qualification, start_date, end_date, interview_required,recruit_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruitment.getRecruitmentId());
            pstmt.setInt(2, recruitment.getOrgId());
            pstmt.setString(3, recruitment.getTitle());
            pstmt.setString(4, recruitment.getQualification());
            pstmt.setTimestamp(5, recruitment.getStartDate());
            pstmt.setTimestamp(6, recruitment.getEndDate());
            pstmt.setBoolean(7, recruitment.isInterviewRequired());
            pstmt.setString(8, recruitment.getRecruStatus());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Recruitment 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    public List<RecruitmentDTO> selectAllRecruitments() {
        String sql = "SELECT * FROM Recruitment";
        List<RecruitmentDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                RecruitmentDTO dto = new RecruitmentDTO();
                dto.setRecruitmentId(rs.getInt("recruitment_id"));
                dto.setOrgId(rs.getInt("org_id"));
                dto.setTitle(rs.getString("title"));
                dto.setQualification(rs.getString("qualification"));
                dto.setStartDate(rs.getTimestamp("start_date"));
                dto.setEndDate(rs.getTimestamp("end_date"));
                dto.setInterviewRequired(rs.getBoolean("interview_required"));
                dto.setRecruStatus(rs.getString("recruit_status"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("x Recruitment 조회 실패: " + e.getMessage());
        }
        return list;
    }
}