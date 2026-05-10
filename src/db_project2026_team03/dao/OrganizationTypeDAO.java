package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.OrganizationTypeDTO;

public class OrganizationTypeDAO {

    public boolean insertOrganizationType(OrganizationTypeDTO type) {
        String sql = "INSERT INTO OrganizationType (org_type_id, type_name) VALUES (?, ?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, type.getOrgTypeId());
            pstmt.setString(2, type.getTypeName());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx OrganizationType 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    public List<OrganizationTypeDTO> selectAllOrganizationTypes() {
        String sql = "SELECT * FROM OrganizationType";
        List<OrganizationTypeDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                OrganizationTypeDTO dto = new OrganizationTypeDTO();
                dto.setOrgTypeId(rs.getInt("org_type_id"));
                dto.setTypeName(rs.getString("type_name"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx OrganizationType 조회 실패: " + e.getMessage());
        }
        return list;
    }
}