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
        String sql = "INSERT INTO Recruitment (org_id, title, qualification, start_date, end_date, interview_required) VALUES (?, ?, ?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruitment.getOrgId());
            pstmt.setString(2, recruitment.getTitle());
            pstmt.setString(3, recruitment.getQualification());
            pstmt.setTimestamp(4, recruitment.getStartDate());
            pstmt.setTimestamp(5, recruitment.getEndDate());
            pstmt.setBoolean(6, recruitment.isInterviewRequired());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Recruitment 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

// 전체 모집 공고 데이터 조회 (DTO 반환용)
    public List<RecruitmentDTO> getAllRecruitments() {
        String sql = "SELECT * FROM vw_all_recruitments";

        List<RecruitmentDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                RecruitmentDTO dto = new RecruitmentDTO();
                dto.setRecruitmentId(rs.getInt("recruitment_id"));
                dto.setOrgId(rs.getInt("org_id"));
                dto.setTitle(rs.getString("recruitment_title")); 
                dto.setQualification(rs.getString("qualification"));
                dto.setStartDate(rs.getTimestamp("start_date"));
                dto.setEndDate(rs.getTimestamp("end_date"));
                dto.setInterviewRequired(rs.getBoolean("interview_required"));
                dto.setRecruitStatus(rs.getString("recruit_status")); 
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Recruitment 조회 실패: " + e.getMessage());
        }
        return list;
    }

    // 모집 중인 전체 공고 목록 출력
    public void printAllRecruitments() {
        String sql = "SELECT recruitment_id, org_name, short_description, recruitment_title, end_date " +
                     "FROM vw_active_recruitments " +
                     "ORDER BY end_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n=====  현재 진행 중인 모집 공고 =====");
            System.out.println("번호 | 공고 제목 | 동아리명 | 동아리 소개 | 마감일");
            System.out.println("------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("recruitment_id");
                String orgName = rs.getString("org_name");
                String orgShort = rs.getString("short_description");
                String title = rs.getString("recruitment_title");
                java.sql.Date endDate = rs.getDate("end_date");

                System.out.printf("%d | %s | %s | %s | %s\n", id, title, orgName, orgShort, endDate.toString());
            }

            if (!hasData) {
                System.out.println("현재 진행 중인 모집 공고가 없습니다.");
            }
            System.out.println("====================================\n");

        } catch (SQLException e) {
            System.out.println("xx 공고 목록 조회 실패: " + e.getMessage());
        }
    }

    // 모집 공고 키워드 검색
    public void searchRecruitmentsByKeyword(String keyword) {
        String sql = "SELECT recruitment_id, org_name, recruitment_title, end_date " +
                     "FROM vw_active_recruitments " +
                     "WHERE recruitment_title LIKE ? " +
                     "ORDER BY end_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n===== '" + keyword + "' 검색 결과 =====");
                System.out.println("번호 | 동아리명 | 공고 제목 | 마감일");
                System.out.println("------------------------------------");

                boolean hasResult = false;
                while (rs.next()) {
                    hasResult = true;
                    int id = rs.getInt("recruitment_id");
                    String orgName = rs.getString("org_name");
                    String title = rs.getString("recruitment_title"); 
                    java.sql.Date endDate = rs.getDate("end_date");

                    System.out.printf("%d | %s | %s | %s\n", id, orgName, title, endDate.toString());
                }

                if (!hasResult) {
                    System.out.println("검색 결과가 없습니다.");
                }
                System.out.println("====================================\n");
            }
        } catch (SQLException e) {
            System.out.println("xx 공고 검색 실패: " + e.getMessage());
        }
    }

     // 특정 모집 공고 상세 조회
    public void printRecruitmentDetail(int recruitmentId) {
        String sql = "SELECT *, DATEDIFF(end_date, NOW()) as d_day FROM vw_all_recruitments WHERE recruitment_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruitmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String orgName = rs.getString("org_name");
                    String title = rs.getString("recruitment_title");      
                    String qualification = rs.getString("qualification");
                    boolean interview = rs.getBoolean("interview_required");
                    java.sql.Date start = rs.getDate("start_date");
                    java.sql.Date end = rs.getDate("end_date");
                    
                    int dDay = rs.getInt("d_day");
                    String dDayText = (dDay == 0) ? "D-Day (오늘 마감)" : "D-" + dDay;

                    System.out.println("\n============================================");
                    System.out.println("  [" + dDayText + "] " + title);
                    System.out.println("============================================");
                    System.out.println("▶ 동아리명 : " + orgName);
                    System.out.println("▶ 공고제목 : " + title);
                    System.out.println("▶ 모집기간 : " + start + " ~ " + end);
                    System.out.println("▶ 면접여부 : " + (interview ? "있음 (면접 후 최종 선발)" : "없음 (서류 전형만 진행)"));
                    System.out.println("--------------------------------------------");
                    System.out.println("▶ 지원 자격 및 세부 요건 :");
                    System.out.println(qualification);
                    System.out.println("============================================\n");
                } else {
                    System.out.println("해당 번호의 공고를 찾을 수 없습니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("xx 상세 조회 실패: " + e.getMessage());
        }
    }

    public boolean updateRecruitment(RecruitmentDTO recruitment) {

        String sql = "UPDATE Recruitment "
                   + "SET title=?, qualification=?, start_date=?, end_date=?, "
                   + "interview_required=? "
                   + "WHERE recruitment_id=?";

        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, recruitment.getTitle());
            pstmt.setString(2, recruitment.getQualification());
            pstmt.setTimestamp(3, recruitment.getStartDate());
            pstmt.setTimestamp(4, recruitment.getEndDate());
            pstmt.setBoolean(5, recruitment.isInterviewRequired());
            pstmt.setInt(6, recruitment.getRecruitmentId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }

        } catch (SQLException e) {
            System.out.println("xx Recruitment 수정 실패: " + e.getMessage());
        }

        return isSuccess;
    }
    
    public boolean deleteRecruitment(int recruitmentId) {

        String sql = "DELETE FROM Recruitment WHERE recruitment_id=?";

        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruitmentId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true;
            }

        } catch (SQLException e) {
            System.out.println("xx Recruitment 삭제 실패: " + e.getMessage());
        }

        return isSuccess;
    }
}