package db_project2026_team03.dao_test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dao.ApplicationDAO;
import db_project2026_team03.dao.BookmarkDAO;
import db_project2026_team03.dao.CategoryDAO;
import db_project2026_team03.dao.OrganizationDAO;
import db_project2026_team03.dao.OrganizationTypeDAO;
import db_project2026_team03.dao.RecruitmentDAO;
import db_project2026_team03.dao.ScrapDAO;
import db_project2026_team03.dao.StudentDAO;
import db_project2026_team03.dto.ApplicationDTO;
import db_project2026_team03.dto.BookmarkDTO;
import db_project2026_team03.dto.CategoryDTO;
import db_project2026_team03.dto.OrganizationDTO;
import db_project2026_team03.dto.OrganizationTypeDTO;
import db_project2026_team03.dto.RecruitmentDTO;
import db_project2026_team03.dto.ScrapDTO;
import db_project2026_team03.dto.StudentDTO;

public class AllDAOTest {

    private static final String PREFIX = "DAO_TEST_" + System.currentTimeMillis();

    private static String studentId;
    private static int orgTypeId;
    private static int categoryId;
    private static int orgId;
    private static int recruitmentId;

    public static void main(String[] args) {
        System.out.println("========== DAO 통합 테스트 시작 ==========");
        System.out.println("테스트 Prefix: " + PREFIX);

        try {
            testStudentDAO();
            testOrganizationTypeDAO();
            testCategoryDAO();
            testOrganizationDAO();
            testRecruitmentDAO();
            testApplicationDAO();
            testBookmarkDAO();
            testScrapDAO();

            System.out.println("\n========== 전체 DAO 테스트 완료 ==========");
        } catch (Exception e) {
            System.out.println("\n========== 테스트 중 예외 발생 ==========");
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private static void testStudentDAO() {
        System.out.println("\n[1] StudentDAO 테스트");

        StudentDAO dao = new StudentDAO();
        studentId = "S" + System.currentTimeMillis();

        StudentDTO dto = new StudentDTO();
        dto.setStudentId(studentId);
        dto.setName(PREFIX + " 학생");
        dto.setUniversity("테스트대학교");
        dto.setMajor("컴퓨터공학");
        dto.setPhone("010-0000-0000");

        boolean inserted = dao.insertStudent(dto);
        printResult("Student INSERT", inserted);

        List<StudentDTO> list = dao.selectAllStudents();
        boolean found = list.stream().anyMatch(s -> studentId.equals(s.getStudentId()));
        printResult("Student SELECT", found);
    }

    private static void testOrganizationTypeDAO() {
        System.out.println("\n[2] OrganizationTypeDAO 테스트");

        OrganizationTypeDAO dao = new OrganizationTypeDAO();
        String typeName = PREFIX + " 유형";

        OrganizationTypeDTO dto = new OrganizationTypeDTO();
        dto.setTypeName(typeName);

        boolean inserted = dao.insertOrganizationType(dto);
        printResult("OrganizationType INSERT", inserted);

        orgTypeId = findInt("SELECT org_type_id FROM OrganizationType WHERE type_name = ?", typeName);
        printResult("OrganizationType SELECT / ID 확인", orgTypeId > 0);
        System.out.println("orgTypeId = " + orgTypeId);
    }

    private static void testCategoryDAO() {
        System.out.println("\n[3] CategoryDAO 테스트");

        CategoryDAO dao = new CategoryDAO();
        String categoryName = PREFIX + " 카테고리";

        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryName(categoryName);
        boolean inserted = dao.insertCategory(dto);
        printResult("Category INSERT", inserted);

        categoryId = findInt("SELECT category_id FROM Category WHERE category_name = ?", categoryName);
        printResult("Category SELECT / ID 확인", categoryId > 0);
        System.out.println("categoryId = " + categoryId);
    }

    private static void testOrganizationDAO() {
        System.out.println("\n[4] OrganizationDAO 테스트");

        OrganizationDAO dao = new OrganizationDAO();
        String orgName = PREFIX + " 조직";

        OrganizationDTO dto = new OrganizationDTO();
        dto.setOrgName(orgName);
        dto.setOrgTypeId(orgTypeId);
        dto.setCategoryId(categoryId);
        dto.setDescription("DAO 테스트용 조직 설명");
        dto.setPresidentId(studentId);

        boolean inserted = dao.insertOrganization(dto);
        printResult("Organization INSERT", inserted);

        orgId = findInt("SELECT org_id FROM Organization WHERE org_name = ?", orgName);
        printResult("Organization SELECT / ID 확인", orgId > 0);
        System.out.println("orgId = " + orgId);

        Boolean orgStatus = findBoolean("SELECT org_status FROM Organization WHERE org_id = ?", orgId);
        printResult("Organization DEFAULT org_status 확인", Boolean.TRUE.equals(orgStatus));
        System.out.println("org_status = " + orgStatus);
    }

    private static void testRecruitmentDAO() {
        System.out.println("\n[5] RecruitmentDAO 테스트");

        RecruitmentDAO dao = new RecruitmentDAO();
        String title = PREFIX + " 모집공고";

        RecruitmentDTO dto = new RecruitmentDTO();
        dto.setOrgId(orgId);
        dto.setTitle(title);
        dto.setQualification("DAO 테스트 지원 자격");
        dto.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        dto.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(7)));
        dto.setInterviewRequired(true);

        boolean inserted = dao.insertRecruitment(dto);
        printResult("Recruitment INSERT", inserted);

        recruitmentId = findInt("SELECT recruitment_id FROM Recruitment WHERE title = ?", title);
        printResult("Recruitment SELECT / ID 확인", recruitmentId > 0);
        System.out.println("recruitmentId = " + recruitmentId);

        String status = findString("SELECT recruit_status FROM Recruitment WHERE recruitment_id = ?", recruitmentId);
        printResult("Recruitment DEFAULT recruit_status 확인", "모집대기".equals(status));
        System.out.println("recruit_status = " + status);
    }

    private static void testApplicationDAO() {
        System.out.println("\n[6] ApplicationDAO 테스트");

        ApplicationDAO dao = new ApplicationDAO();

        ApplicationDTO dto = new ApplicationDTO();
        dto.setRecruitmentId(recruitmentId);
        dto.setStudentId(studentId);
        dto.setSelfIntro(PREFIX + " 자기소개서");

        boolean inserted = dao.insertApplication(dto);
        printResult("Application INSERT", inserted);

        int applicationId = findInt(
            "SELECT application_id FROM Application WHERE recruitment_id = ? AND student_id = ?",
            recruitmentId,
            studentId
        );
        printResult("Application SELECT / ID 확인", applicationId > 0);
        System.out.println("applicationId = " + applicationId);

        String passStatus = findString("SELECT pass_status FROM Application WHERE application_id = ?", applicationId);
        printResult("Application DEFAULT pass_status 확인", "대기".equals(passStatus));
        System.out.println("pass_status = " + passStatus);
    }

    private static void testBookmarkDAO() {
        System.out.println("\n[7] BookmarkDAO 테스트");

        BookmarkDAO dao = new BookmarkDAO();

        BookmarkDTO dto = new BookmarkDTO();
        dto.setOrgId(orgId);
        dto.setStudentId(studentId);

        boolean inserted = dao.insertBookmark(dto);
        printResult("Bookmark INSERT", inserted);

        int bookmarkId = findInt(
            "SELECT bookmark_id FROM Bookmark WHERE org_id = ? AND student_id = ?",
            orgId,
            studentId
        );
        printResult("Bookmark SELECT / ID 확인", bookmarkId > 0);
        System.out.println("bookmarkId = " + bookmarkId);

        Timestamp createdAt = findTimestamp("SELECT created_at FROM Bookmark WHERE bookmark_id = ?", bookmarkId);
        printResult("Bookmark DEFAULT created_at 확인", createdAt != null);
        System.out.println("created_at = " + createdAt);
    }

    private static void testScrapDAO() {
        System.out.println("\n[8] ScrapDAO 테스트");

        ScrapDAO dao = new ScrapDAO();

        ScrapDTO dto = new ScrapDTO();
        dto.setRecruitmentId(recruitmentId);
        dto.setStudentId(studentId);

        boolean inserted = dao.insertScrap(dto);
        printResult("Scrap INSERT", inserted);

        int scrapId = findInt(
            "SELECT scrap_id FROM Scrap WHERE recruitment_id = ? AND student_id = ?",
            recruitmentId,
            studentId
        );
        printResult("Scrap SELECT / ID 확인", scrapId > 0);
        System.out.println("scrapId = " + scrapId);

        Timestamp createdAt = findTimestamp("SELECT created_at FROM Scrap WHERE scrap_id = ?", scrapId);
        printResult("Scrap DEFAULT created_at 확인", createdAt != null);
        System.out.println("created_at = " + createdAt);
    }

    private static void cleanupTestData() {
        System.out.println("\n========== 테스트 데이터 정리 시작 ==========");

        // FK 때문에 자식 테이블부터 삭제합니다.
        executeUpdate("DELETE FROM Scrap WHERE student_id = ?", studentId);
        executeUpdate("DELETE FROM Bookmark WHERE student_id = ?", studentId);
        executeUpdate("DELETE FROM Application WHERE student_id = ?", studentId);
        executeUpdate("DELETE FROM Recruitment WHERE recruitment_id = ?", recruitmentId);
        executeUpdate("DELETE FROM Organization WHERE org_id = ?", orgId);
        executeUpdate("DELETE FROM Category WHERE category_id = ?", categoryId);
        executeUpdate("DELETE FROM OrganizationType WHERE org_type_id = ?", orgTypeId);
        executeUpdate("DELETE FROM Student WHERE student_id = ?", studentId);

        System.out.println("========== 테스트 데이터 정리 완료 ==========");
    }

    private static void printResult(String testName, boolean success) {
        if (success) {
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName);
        }
    }

    private static int findInt(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParams(pstmt, params);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("findInt 실패: " + e.getMessage());
        }
        return -1;
    }

    private static String findString(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParams(pstmt, params);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("findString 실패: " + e.getMessage());
        }
        return null;
    }

    private static Boolean findBoolean(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParams(pstmt, params);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("findBoolean 실패: " + e.getMessage());
        }
        return null;
    }

    private static Timestamp findTimestamp(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParams(pstmt, params);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("findTimestamp 실패: " + e.getMessage());
        }
        return null;
    }

    private static void executeUpdate(String sql, Object... params) {
        if (params.length > 0 && params[0] == null) {
            return;
        }
        if (params.length > 0 && params[0] instanceof Integer && ((Integer) params[0]) <= 0) {
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParams(pstmt, params);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("cleanup 실패: " + e.getMessage());
        }
    }

    private static void setParams(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];

            if (param instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof String) {
                pstmt.setString(i + 1, (String) param);
            } else if (param instanceof Timestamp) {
                pstmt.setTimestamp(i + 1, (Timestamp) param);
            } else if (param == null) {
                pstmt.setObject(i + 1, null);
            } else {
                pstmt.setObject(i + 1, param);
            }
        }
    }
}
