package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.ScrapDTO;

public class ScrapDAO {

    public boolean insertScrap(ScrapDTO scrap) {
        String sql = "INSERT INTO Scrap (scrap_id, recruitment_id, student_id, created_at) VALUES (?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scrap.getScrapId());
            pstmt.setInt(2, scrap.getRecruitmentId());
            pstmt.setString(3, scrap.getStudentId());
            pstmt.setTimestamp(4, scrap.getCreatedAt());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                isSuccess = true;
            }

        } catch (SQLException e) {
            System.out.println("xx Scrap 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }
    public List<ScrapDTO> selectAllScraps() {
        String sql = "SELECT * FROM Scrap";
        List<ScrapDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ScrapDTO dto = new ScrapDTO();
                dto.setScrapId(rs.getInt("scrap_id"));
                dto.setRecruitmentId(rs.getInt("recruitment_id"));
                dto.setStudentId(rs.getString("student_id"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Scrap 조회 실패: " + e.getMessage());
        }
        return list;
    }
}