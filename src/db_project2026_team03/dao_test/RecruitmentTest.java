package db_project2026_team03.dao_test;

import db_project2026_team03.dao.RecruitmentDAO;

public class RecruitmentTest {

    public static void main(String[] args) {
        RecruitmentTest test = new RecruitmentTest();
        test.run();
    }

    public void run() {
        RecruitmentDAO recruitmentDAO = new RecruitmentDAO();

        System.out.println("====================================");
        System.out.println(" printAllRecruitments() 메서드 테스트");
        System.out.println("====================================");

        try {
            recruitmentDAO.printAllRecruitments();
            System.out.println("[TEST SUCCESS] printAllRecruitments() 실행 완료");
        } catch (Exception e) {
            System.out.println("[TEST FAIL] printAllRecruitments() 실행 중 예외 발생");
            System.out.println("원인: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
