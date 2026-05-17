package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.BookmarkDTO;

public class BookmarkDAO {

	// [스크랩 추가] 특정 모집 공고를 스크랩에 추가
    public boolean insertBookmark(BookmarkDTO bookmark) {
        String sql = "INSERT INTO Bookmark (org_id, student_id) VALUES (?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookmark.getOrgId());
            pstmt.setString(2, bookmark.getStudentId());

            int rowsAffected = pstmt.executeUpdate();
            // 스크랩 추가 팝업
            if (rowsAffected > 0) {
            isSuccess = true;
            System.out.println("✅ 스크랩에 추가되었습니다.");
            }
            else System.out.println("xx 스크랩 추가에 실패했습니다.");
        } catch (SQLException e) {
        	// 중복 예외 처리
        	if (e.getErrorCode() == 1062) {
                System.out.println("⚠️  이미 스크랩한 공고입니다.");
            } 
        	else {
        		System.out.println("xx Bookmark 등록 실패: " + e.getMessage());
        	}
        }
        return isSuccess;
    }

    public List<BookmarkDTO> selectAllBookmarks() {
        String sql = "SELECT * FROM Bookmark";
        List<BookmarkDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BookmarkDTO dto = new BookmarkDTO();
                dto.setBookmarkId(rs.getInt("bookmark_id"));
                dto.setOrgId(rs.getInt("org_id"));
                dto.setStudentId(rs.getString("student_id"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Bookmark 조회 실패: " + e.getMessage());
        }
        return list;
    }
}
