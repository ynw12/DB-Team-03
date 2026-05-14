package db_project2026_team03.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_project2026_team03.DBConnection;
import db_project2026_team03.dto.CategoryDTO;

public class CategoryDAO {

    public boolean insertCategory(CategoryDTO category) {
        String sql = "INSERT INTO Category (category_name) VALUES (?)";
        boolean isSuccess = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getCategoryName());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) isSuccess = true;

        } catch (SQLException e) {
            System.out.println("xx Category 등록 실패: " + e.getMessage());
        }
        return isSuccess;
    }

    public List<CategoryDTO> selectAllCategories() {
        String sql = "SELECT * FROM Category";
        List<CategoryDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CategoryDTO dto = new CategoryDTO();
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setCategoryName(rs.getString("category_name"));
                list.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("xx Category 조회 실패: " + e.getMessage());
        }
        return list;
    }
}
