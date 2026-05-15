package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.OrganizationDTO;

public class OrganizationDAO {

    public boolean insertOrganization(OrganizationDTO org) {
        String sql = "INSERT INTO Organization (org_name, org_type_id, category_id, description, president_id, org_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, org.getOrgName());
            pstmt.setInt(2, org.getOrgTypeId());
            pstmt.setInt(3, org.getCategoryId());
            pstmt.setString(4, org.getDescription());
            pstmt.setString(5, org.getPresidentId());
            pstmt.setBoolean(6, org.isOrgStatus());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0)
                isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Organization 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    public List<OrganizationDTO> selectAllOrganizations() {
        String sql = "SELECT * FROM Organization";
        List<OrganizationDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                OrganizationDTO dto = new OrganizationDTO();
                dto.setOrgId(rs.getInt("org_id"));
                dto.setOrgName(rs.getString("org_name"));
                dto.setOrgTypeId(rs.getInt("org_type_id"));
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setDescription(rs.getString("description"));
                dto.setPresidentId(rs.getString("president_id"));
                dto.setOrgStatus(rs.getBoolean("org_status"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Organization 조회 실패: " + e.getMessage());
        }
        return list;
    }

    // '활성화'된 전체 동아리 목록 조회
    public void printAllOrganizations() {
        String sql = "SELECT o.org_id, o.org_name, t.type_name, c.category_name " +
                     "FROM Organization o " +
                     "JOIN OrganizationType t ON o.org_type_id = t.org_type_id " +
                     "JOIN Category c ON o.category_id = c.category_id " +
                     "WHERE o.org_status = true " +
                     "ORDER BY o.org_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n===== 전체 동아리/학회 목록 =====");
            System.out.println("ID | 카테고리 | 소속 유형 | 단체명");
            System.out.println("-----------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("org_id");
                String category = rs.getString("category_name");
                String type = rs.getString("type_name");
                String name = rs.getString("org_name");

                System.out.printf("%d | %s | %s | %s\n", id, category, type, name);
            }

            if (!hasData) {
                System.out.println("현재 활동 중인 단체가 없습니다.");
            }
            System.out.println("=========================================\n");

        } catch (SQLException e) {
            System.out.println("xx 동아리 목록 조회 실패: " + e.getMessage());
        }
    }

    // 동아리 카테고리별 필터링 기능
    public void filterOrganizationsByCategory(String categoryName) {
        String sql = "SELECT o.org_id, o.org_name, t.type_name " +
                     "FROM Organization o " +
                     "JOIN OrganizationType t ON o.org_type_id = t.org_type_id " +
                     "JOIN Category c ON o.category_id = c.category_id " +
                     "WHERE o.org_status = true AND c.category_name = ? " +
                     "ORDER BY o.org_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoryName);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n===== '" + categoryName + "' 카테고리 단체 목록 =====");
                System.out.println("ID | 소속 유형 | 단체명");
                System.out.println("-----------------------------------------");

                boolean hasResult = false;
                while (rs.next()) {
                    hasResult = true;
                    int id = rs.getInt("org_id");
                    String type = rs.getString("type_name");
                    String name = rs.getString("org_name");

                    System.out.printf("%d | %s | %s\n", id, type, name);
                }

                if (!hasResult) {
                    System.out.println("해당 카테고리에 활동 중인 단체가 없습니다.");
                }
                System.out.println("=========================================\n");
            }
        } catch (SQLException e) {
            System.out.println("xx 카테고리 필터링 실패: " + e.getMessage());
        }
    }

    // 특정 동아리 상세 조회
    public void printOrganizationDetail(int orgId) {
        String sql = "SELECT o.org_name, t.type_name, c.category_name, o.description, s.name as president_name " +
                     "FROM Organization o " +
                     "JOIN OrganizationType t ON o.org_type_id = t.org_type_id " +
                     "JOIN Category c ON o.category_id = c.category_id " +
                     "JOIN Student s ON o.president_id = s.student_id " + 
                     "WHERE o.org_id = ? AND o.org_status = true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orgId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("org_name");
                    String type = rs.getString("type_name");
                    String category = rs.getString("category_name");
                    String description = rs.getString("description");
                    String presidentName = rs.getString("president_name");

                    System.out.println("\n============================================");
                    System.out.println("  " + name + " 상세 정보");
                    System.out.println("============================================");
                    System.out.println("▶ 소속 유형 : " + type);
                    System.out.println("▶ 활동 분야 : " + category);
                    System.out.println("▶ 운영진(대표) : " + presidentName);
                    System.out.println("--------------------------------------------");
                    System.out.println("▶ 단체 소개 :\n" + description);
                    System.out.println("============================================");
                } else {
                    System.out.println("해당 번호의 단체를 찾을 수 없습니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("xx 동아리 상세 조회 실패: " + e.getMessage());
        }
    }
}
