package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.ScrapDTO;

public class ScrapDAO {

    public boolean insertScrap(ScrapDTO scrap) {
        String sql = "INSERT INTO Scrap (recruitment_id, student_id) VALUES (?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scrap.getRecruitmentId());
            pstmt.setString(2, scrap.getStudentId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
               String selectSql = "SELECT title FROM Recruitment WHERE recruitment_id = ?";
                try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                    selectStmt.setInt(1, scrap.getRecruitmentId());
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("✅ 스크랩에 추가되었습니다.");
                            System.out.println("▶ 공고명 : " + rs.getString("title"));
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
    
    // [기존 DAO 세팅] 전체 scrap 조회
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
    
    // [내 스크랩 조회] 학번으로 조회
    public void getScrapsByStudent(String studentId) {
        String sql =
            "SELECT recruitment_title, org_name, " +
            "       start_date, end_date, recruit_status, created_at " +
            "FROM vw_student_scrap " +
            "WHERE student_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, studentId);
 
            try (ResultSet rs = pstmt.executeQuery()) {
 
                System.out.println("============================================");
                System.out.printf("[ %s ] 님의 스크랩 내역 \n", studentId);
 
                boolean hasResult = false;
                while (rs.next()) {
                    hasResult = true;
                    String    title         = rs.getString("recruitment_title");
                    String    orgName       = rs.getString("org_name");
                    Date      startDate     = rs.getDate("start_date");
                    Date      endDate       = rs.getDate("end_date");
                    String    recruitStatus = rs.getString("recruit_status");
                    Timestamp createdAt     = rs.getTimestamp("created_at");
                    System.out.println("--------------------------------------------");
                    System.out.println("▶ 동아리명   : " + orgName);
                    System.out.println("▶ 공고 제목  : " + title);
                    System.out.println("▶ 모집 기간  : " + startDate + " ~ " + endDate);
                    System.out.println("▶ 모집 상태  : " + recruitStatus);
                    System.out.println("▶ 스크랩 일시 : " + createdAt);
                }
 
                if (!hasResult) {
                    System.out.println("--------------------------------------------");
                    System.out.println("  스크랩한 공고가 없습니다.");
                }
                System.out.println("============================================");
            }
 
        } catch (SQLException e) {
            System.out.println("xx Scrap 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // [내 스크랩 삭제] 
    public boolean deleteScrap(String studentId, int recruitmentId) {
        String sql = "DELETE FROM Scrap WHERE student_id = ? AND recruitment_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, studentId);
            pstmt.setInt(2, recruitmentId);
 
            int rows = pstmt.executeUpdate();
 
            if (rows > 0) {
                System.out.println("✅ 스크랩이 삭제되었습니다.");
                return true;
            } else {
                System.out.println("⚠️  해당 스크랩 내역이 없습니다.");
                return false;
            }
 
        } catch (SQLException e) {
            System.out.println("xx Scrap 조회 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}