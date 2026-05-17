package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.BookmarkDTO;

public class BookmarkDAO {

   // [즐겨찾기 추가] 특정 동아리를 즐겨찾기에 추가 
   public boolean insertBookmark(BookmarkDTO bookmark) {
       String sql = "INSERT INTO Bookmark (org_id, student_id) VALUES (?, ?)";
       boolean isSuccess = false;
       
       try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

               pstmt.setInt(1, bookmark.getOrgId());
               pstmt.setString(2, bookmark.getStudentId());

               int rowsAffected = pstmt.executeUpdate();

               if (rowsAffected > 0) {
                   isSuccess = true;
                  String selectSql = "SELECT title FROM Recruitment WHERE recruitment_id = ?";
                   try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                       selectStmt.setInt(1, bookmark.getOrgId());
                       try (ResultSet rs = selectStmt.executeQuery()) {
                           if (rs.next()) {
                               System.out.println("✅ 즐겨찾기에 추가되었습니다.");
                               System.out.println("▶ 동아리명 : " + rs.getString("org_name"));
                           }
                       }
                   }
               }

           } catch (SQLException e) {
               if (e.getErrorCode() == 1062) {
                   System.out.println("⚠️  이미 즐겨찾기한 동아리입니다.");
               } else {
                   System.out.println("xx Scrap 조회 실패: " + e.getMessage());
                   e.printStackTrace();
               }
           }
           return isSuccess;
       }
   // [기존 DAO 세팅] 전체 bookmark 조회
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
            System.out.println("x Bookmark 조회 실패: " + e.getMessage());
        }
        return list;
    }
    
    // [내 즐겨찾기 조회] 학번으로 조회
    public void getBookmarksByStudent(String studentId) {
        String sql =
            "SELECT org_name, org_type, category_name, created_at " +
            "FROM vw_student_bookmark " +
            "WHERE student_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, studentId);
 
            try (ResultSet rs = pstmt.executeQuery()) {
 
                System.out.println("============================================");
                System.out.printf("[ %s ] 님의 즐겨찾기 내역 \n", studentId);
 
                boolean hasResult = false;
                while (rs.next()) {
                    hasResult = true;
                    String orgName      = rs.getString("org_name");
                    String orgType      = rs.getString("org_type");
                    String categoryName = rs.getString("category_name");
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    System.out.println("--------------------------------------------");
                    System.out.println("▶ 동아리명  : " + orgName);
                    System.out.println("▶ 단체 유형 : " + orgType);
                    System.out.println("▶ 카테고리  : " + categoryName);
                    System.out.println("▶ 추가 일시 : " + createdAt);
                }
 
                if (!hasResult) {
                   
                    System.out.println("  즐겨찾기한 동아리가 없습니다.");
                }
                System.out.println("============================================");
            }
 
        } catch (SQLException e) {
            System.out.println("xx Bookmark 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
 
    // [내 즐겨찾기 삭제] 
    public boolean deleteBookmark(String studentId, int orgId) {
        String sql = "DELETE FROM Bookmark WHERE student_id = ? AND org_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, studentId);
            pstmt.setInt(2, orgId);
 
            int rows = pstmt.executeUpdate();
 
            if (rows > 0) {
                System.out.println("✅ 즐겨찾기가 삭제되었습니다.");
                return true;
            } else {
                System.out.println("⚠️  해당 즐겨찾기 내역이 없습니다.");
                return false;
            }
 
        } catch (SQLException e) {
            System.out.println("xx Bookmark 조회 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
