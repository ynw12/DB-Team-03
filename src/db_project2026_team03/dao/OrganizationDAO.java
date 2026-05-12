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
        String sql = "INSERT INTO Organization (org_id, org_name, org_type_id, category_id, description, president_id, org_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, org.getOrgId());
            pstmt.setString(2, org.getOrgName());
            pstmt.setInt(3, org.getOrgTypeId());
            pstmt.setInt(4, org.getCategoryId());
            pstmt.setString(5, org.getDescription());
            pstmt.setString(6, org.getPresidentId());
            pstmt.setBoolean(7, org.isOrgStatus());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

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
}