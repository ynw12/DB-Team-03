package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.NoticeDTO;

public class NoticeDAO {

    public boolean insertNotice(NoticeDTO notice) {
        String sql = "INSERT INTO Notice (notice_id, org_id, title, content, created_at) VALUES (?, ?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, notice.getNoticeId());
            pstmt.setInt(2, notice.getOrgId());
            pstmt.setString(3, notice.getTitle());
            pstmt.setString(4, notice.getContent());
            pstmt.setTimestamp(5, notice.getCreatedAt());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Notice 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    public List<NoticeDTO> selectAllNotices() {
        String sql = "SELECT * FROM Notice";
        List<NoticeDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                NoticeDTO dto = new NoticeDTO();
                dto.setNoticeId(rs.getInt("notice_id"));
                dto.setOrgId(rs.getInt("org_id"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Notice 조회 실패: " + e.getMessage());
        }
        return list;
    }
}