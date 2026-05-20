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
        String sql = "SELECT recruitment_id, org_name, recruitment_title, end_date " +
                     "FROM vw_active_recruitments " +
                     "ORDER BY end_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n=====  현재 진행 중인 모집 공고 =====");
            System.out.println("번호 | 동아리명 | 공고 제목 | 마감일");
            System.out.println("------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("recruitment_id");
                String orgName = rs.getString("org_name");
                String title = rs.getString("recruitment_title");
                java.sql.Date endDate = rs.getDate("end_date");

                System.out.printf("%d | %s | %s | %s\n", id, orgName, title, endDate.toString());
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
        String sql = "SELECT v.*, r.qualification, DATEDIFF(v.end_date, NOW()) as d_day " +
                     "FROM vw_active_recruitments v " +
                     "JOIN Recruitment r ON v.recruitment_id = r.recruitment_id " +
                     "WHERE v.recruitment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruitmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String orgName = rs.getString("org_name");
                    String shortDesc = rs.getString("short_description");
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
                    if (shortDesc != null && !shortDesc.trim().isEmpty()) {
                        System.out.println("▶ 동아리명 : " + orgName + " (" + shortDesc + ")");
                    } else {
                        System.out.println("▶ 동아리명 : " + orgName);
                    }
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
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Recruitment 수정 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    // 특정 동아리의 모집 공고 목록 출력
    public void printRecruitmentsByOrgId(int orgId) {
        String sql = "SELECT recruitment_id, title, start_date, end_date, interview_required "
                   + "FROM Recruitment "
                   + "WHERE org_id = ? "
                   + "ORDER BY end_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orgId);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n===== 내 동아리 모집 공고 목록 =====");
                System.out.println("공고번호 | 제목 | 시작일 | 마감일 | 면접여부");
                System.out.println("----------------------------------------");

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf(
                        "%d | %s | %s | %s | %s\n",
                        rs.getInt("recruitment_id"),
                        rs.getString("title"),
                        rs.getTimestamp("start_date"),
                        rs.getTimestamp("end_date"),
                        rs.getBoolean("interview_required") ? "있음" : "없음"
                    );
                }

                if (!hasData) {
                    System.out.println("등록된 모집 공고가 없습니다.");
                }
                System.out.println("========================================\n");
            }
        } catch (SQLException e) {
            System.out.println("xx 내 모집 공고 목록 조회 실패: " + e.getMessage());
        }
    }

    // [공고 삭제] 트랜잭션으로 처리
    // Recruitment 삭제 시 Application, Scrap은 ON DELETE CASCADE로 자동 삭제
    // 트랜잭션으로 실패 시 전체 롤백 보장
    public boolean deleteRecruitment(int recruitmentId, int currentOrgId) {
        String sql = "DELETE FROM Recruitment "
                   + "WHERE recruitment_id = ? "
                   + "AND org_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, recruitmentId);
                pstmt.setInt(2, currentOrgId);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected == 0) {
                    conn.rollback();
                    System.out.println("xx 해당 공고가 존재하지 않거나 삭제 권한이 없습니다.");
                    return false;
                }

                conn.commit(); // 성공 시 커밋 — Application, Scrap CASCADE 삭제도 함께 확정
                System.out.println("✅ 공고 삭제 완료 | 관련 지원서 및 스크랩도 함께 삭제되었습니다.");
                return true;

            } catch (SQLException e) {
                conn.rollback(); // 실패 시 롤백
                System.out.println("xx 공고 삭제 실패, 롤백 처리: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("xx DB 연결 실패: " + e.getMessage());
            return false;
        }
    }

    public RecruitmentDTO getRecruitmentById(int recruitmentId) {
        String sql = "SELECT * FROM Recruitment WHERE recruitment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruitmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    RecruitmentDTO dto = new RecruitmentDTO();
                    dto.setRecruitmentId(rs.getInt("recruitment_id"));
                    dto.setOrgId(rs.getInt("org_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setQualification(rs.getString("qualification"));
                    dto.setStartDate(rs.getTimestamp("start_date"));
                    dto.setEndDate(rs.getTimestamp("end_date"));
                    dto.setInterviewRequired(rs.getBoolean("interview_required"));
                    return dto;
                }
            }
        } catch (SQLException e) {
            System.out.println("xx Recruitment 조회 실패: " + e.getMessage());
        }
        return null;
    }
}