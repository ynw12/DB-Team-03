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
	
	// [지원하기] create 및 안내 출력문
	   public boolean createApplication(ApplicationDTO app) {
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
	               String selectSql =
	                   "SELECT a.application_id, a.pass_status, a.self_intro, " +
	                   "       s.student_id, s.name AS student_name " +
	                   "FROM Application a " +
	                   "JOIN Student s ON a.student_id = s.student_id " +
	                   "WHERE a.student_id = ? AND a.recruitment_id = ? ";

	               try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
	                   selectStmt.setString(1, app.getStudentId());
	                   selectStmt.setInt(2, app.getRecruitmentId());

	                   try (ResultSet rs = selectStmt.executeQuery()) {
	                       if (rs.next()) {
	                           int applicationId = rs.getInt("application_id");
	                           String passStatus = rs.getString("pass_status");
	                           String studentId = rs.getString("student_id");
	                           String studentName = rs.getString("student_name");
	                           String selfIntro = rs.getString("self_intro");

	                           System.out.println();
	                           System.out.println("\n✅ 지원이 완료되었습니다.");
	                           System.out.println("============================================");
	                           System.out.printf("[ %s ] 님의 지원서%n", studentName);
	                           System.out.println("--------------------------------------------");
	                           System.out.println("▶ 지원 번호 : " + applicationId);
	                           System.out.println("▶ 공고 번호 : " + app.getRecruitmentId());
	                           System.out.println("▶ 지원자 학번 : " + studentId);
	                           System.out.println("▶ 심사 상태 : " + passStatus);
	                           System.out.println("--------------------------------------------");
	                           System.out.println("▶ 자기소개");
	                           System.out.println(selfIntro);
	                           System.out.println("============================================");
	                       } else {
	                           System.out.println("xx 지원에 실패했습니다.");
	                       }
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

    	// 학생 이름 먼저 조회
    	int id = Integer.parseInt(studentId);
        String studentName = StudentDAO.getStudentName(id);
        if (studentName == null) {
            System.out.println("xx 존재하지 않는 학생입니다.");
            return;
        }

        // 2. 지원 내역 조회
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

                System.out.println("\n============================================");
                System.out.printf("[ %s ] 님의 지원 내역 \n", studentName);

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
        }
    }
    // [지원자 조회] 운영진 학번으로 본인 동아리 지원자 목록 + 자기소개서 열람
    // Organization.president_id = 운영진 학번 조건으로 동아리 특정
    public void getApplicationsByOrg(String presidentId) {
        String sql =
            "SELECT a.application_id, " +
            "       s.student_id, s.name, s.university, s.major, " +
            "       r.title AS recruitment_title, " +
            "       a.self_intro, a.pass_status " +
            "FROM Application a " +
            "JOIN Student s      ON a.student_id     = s.student_id " +
            "JOIN Recruitment r  ON a.recruitment_id = r.recruitment_id " +
            "JOIN Organization o ON r.org_id         = o.org_id " +
            "WHERE o.president_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, presidentId);

            try (ResultSet rs = pstmt.executeQuery()) {

                System.out.println("============================================");
                System.out.println("[ 우리 동아리 지원자 목록 ]");

                boolean hasResult = false;
                while (rs.next()) {
                    hasResult = true;
                    System.out.println("--------------------------------------------");
                    System.out.println("▶ 지원서 ID : " + rs.getInt("application_id"));
                    System.out.println("▶ 학번      : " + rs.getString("student_id"));
                    System.out.println("▶ 이름      : " + rs.getString("name"));
                    System.out.println("▶ 대학교    : " + rs.getString("university"));
                    System.out.println("▶ 전공      : " + rs.getString("major"));
                    System.out.println("▶ 지원 공고 : " + rs.getString("recruitment_title"));
                    System.out.println("▶ 자기소개서: " + rs.getString("self_intro"));
                    System.out.println("▶ 심사 상태 : " + rs.getString("pass_status"));
                }

                if (!hasResult) {
                    System.out.println("--------------------------------------------");
                    System.out.println(" 지원자가 없습니다.");
                }
                System.out.println("============================================");
            }

        } catch (SQLException e) {
            System.out.println("xx 지원자 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // [합격/불합격 처리] pass_status 업데이트
    public boolean updatePassStatus(int applicationId, String status) {
        String sql = "UPDATE Application SET pass_status = ? WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, applicationId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("xx 지원서를 찾을 수 없습니다. applicationId: " + applicationId);
                return false;
            }

            System.out.println("✅ 처리 완료 | 지원서 ID: " + applicationId + " → " + status);
            return true;

        } catch (SQLException e) {
            System.out.println("xx 상태 업데이트 실패: " + e.getMessage());
            return false;
        }
        
        
    }
    
    // [지원서 일괄 심사]
    public boolean updateBatchPassStatus(int recruitmentId, List<Integer> passedAppIds) {
        String sqlAllFail = "UPDATE Application SET pass_status = '불합격' WHERE recruitment_id = ?";
        
        StringBuilder sqlPassBuilder = new StringBuilder("UPDATE Application SET pass_status = '합격' WHERE application_id IN (");
        for (int i = 0; i < passedAppIds.size(); i++) {
            sqlPassBuilder.append("?");
            if (i < passedAppIds.size() - 1) sqlPassBuilder.append(",");
        }
        sqlPassBuilder.append(")");

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlAllFail)) {
                pstmt1.setInt(1, recruitmentId);
                pstmt1.executeUpdate();
            }

            if (!passedAppIds.isEmpty()) {
                try (PreparedStatement pstmt2 = conn.prepareStatement(sqlPassBuilder.toString())) {
                    for (int i = 0; i < passedAppIds.size(); i++) {
                        pstmt2.setInt(i + 1, passedAppIds.get(i));
                    }
                    pstmt2.executeUpdate();
                }
            }

            conn.commit(); 
            System.out.println("[트랜잭션 성공] 일괄 심사 처리가 완료되었습니다. (지정 인원 합격, 그 외 불합격)");
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("xx [트랜잭션 롤백] 심사 처리 중 오류가 발생하여 모든 작업이 취소되었습니다.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("xx 에러 상세: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        
}