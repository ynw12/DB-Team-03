package db_project2026_team03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/DB2026Team03?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "View1236!";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("ok 데이터베이스 연결에 성공하였습니다.");
        } catch (ClassNotFoundException e) {
            System.out.println("xx JDBC 드라이버 로드 실패: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("xx 데이터베이스 연결 실패: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection testConn = getConnection();
        if (testConn != null) {
            try {
                testConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
