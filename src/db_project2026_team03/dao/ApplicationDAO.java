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
	
	// [지원하기] insert 및 안내 출력문
    public boolean insertApplication(ApplicationDTO app) {
        String sql = "INSERT INTO Application (recruitment_id, student_id, self_intro) VALUES (?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

        	pstmt.setInt(1, app.getRecruitmentId());
            pstmt.setString(2, app.getStudentId());
            pstmt.setString(3, app.getSelfIntro());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                isSuccess = true;
                
                // 지원 후 팝업
                String selectSql = "SELECT application_id, pass_status FROM Application " +
                                   "WHERE student_id = ? AND recruitment_id = ?";

                try (PreparedStatement selectStmt = conn.prepareStatement(selectSql);) {
                    selectStmt.setString(1, app.getStudentId());
                    selectStmt.setInt(2, app.getRecruitmentId());
                    
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("✅ 지원이 완료되었습니다.");
                            System.out.println("▶ 지원 ID  : " + rs.getInt("application_id"));
                            System.out.println("▶ 공고 ID  : " + app.getRecruitmentId());
                            System.out.println("▶ 학번     : " + app.getStudentId());
                            System.out.println("▶ 심사 상태 : " + rs.getString("pass_status"));
            
                        } else System.out.println("xx 지원에 실패했습니다.");
                    }  
                }
            }             
        } catch (SQLException e) {
            System.out.println("xx Application 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }
    // [기존 DAO 세팅] 전체 Application 출력
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
                dto.setPassStatus(rs.getString("pass_status"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Application 조회 실패: " + e.getMessage());
        }
        return list;
    }
    // [내 지원 내역 조회] 학번으로 본인의 전체 지원 내역을 조회
    // Application → Recruitment → Organization 3중 JOIN
    // idx_application_student_recruitment 와 매칭 
    public void getApplicationsByStudent(String studentId) {
        String sql =
            "SELECT a.pass_status, " +
            "       r.title AS recruitment_title, " +
            "       o.org_name " +
            "FROM Application a " +
            "JOIN Recruitment r ON a.recruitment_id = r.recruitment_id " +
            "JOIN Organization o ON r.org_id = o.org_id " +
            "WHERE a.student_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setString(1, studentId);
 
            try (ResultSet rs = pstmt.executeQuery()) {
 
                System.out.println("============================================");
                System.out.printf("[ %s ] 님의 지원 내역 \n", studentId);

 
                boolean hasResult = false;
                while (rs.next()) {
                    hasResult = true;
                    String orgName    = rs.getString("org_name");
                    String title      = rs.getString("recruitment_title");
                    String passStatus = rs.getString("pass_status");
                    System.out.println("--------------------------------------------");
                    System.out.println("▶ 동아리명  : " + orgName);
                    System.out.println("▶ 공고 제목 : " + title);
                    System.out.println("▶ 심사 상태 : " + passStatus);
                }
 
                if (!hasResult) {
                    System.out.println("--------------------------------------------");
                    System.out.println(" 지원 내역이 없습니다.");
                }
                System.out.println("============================================");
            }
 
        } catch (SQLException e) {
            System.out.println("xx 지원 내역 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}