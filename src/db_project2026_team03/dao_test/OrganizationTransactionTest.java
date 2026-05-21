package db_project2026_team03.dao_test;

import db_project2026_team03.dao.OrganizationDAO;

// 실제 DAO 클래스가 있는 패키지 경로를 임포트하세요.
// 예: import db_project2026_team03.dao.OrganizationDAO;

public class OrganizationTransactionTest {

    public static void main(String[] args) {
        // 1. DAO 객체 생성
        OrganizationDAO orgDao = new OrganizationDAO();

        System.out.println("=========================================");
        System.out.println(" [트랜잭션 권한 검증 테스트 시작]");
        System.out.println("=========================================");

        // 테스트 데이터 설정 (실제 DB에 있는 데이터 상태에 맞춰 수정해 주세요)
        int existingOrgId = 1;         // 실제 존재하는 동아리 ID
        String realManagerId = "20260001"; // 위 동아리의 실제 대장 학번 (president_id)
        String wrongStudentId = "20231111"; // 권한이 없는 일반 학생 학번
        int nonExistentOrgId = 9999;   // 존재하지 않는 가짜 동아리 ID

        // -----------------------------------------------------------------
        // 케이스 1 : 타인의 동아리를 비활성화하려고 시도할 때 (권한 검증)
        // -----------------------------------------------------------------
        System.out.println("\n[케이스 1] 다른 사람의 동아리 ID (" + existingOrgId + ") 처리 시도");
        System.out.println(" 입력 학번: " + wrongStudentId + " (일반 학생)");
        
        boolean result1 = orgDao.deactivateOrganization(existingOrgId, wrongStudentId);
        
        if (!result1) {
            System.out.println(" ➡️ 결과: 성공 (권한이 없으므로 정상적으로 차단됨)");
        } else {
            System.out.println(" ➡️ 결과: 실패 (권한이 없는데 true가 반환됨)");
        }


        // -----------------------------------------------------------------
        // 케이스 2 : 존재하지 않는 동아리 ID를 입력했을 때 (존재 여부 검증)
        // -----------------------------------------------------------------
        System.out.println("\n[케이스 2] 존재하지 않는 동아리 ID (" + nonExistentOrgId + ") 처리 시도");
        System.out.println(" 입력 학번: " + realManagerId);
        
        boolean result2 = orgDao.deactivateOrganization(nonExistentOrgId, realManagerId);
        
        if (!result2) {
            System.out.println(" ➡️ 결과: 성공 (존재하지 않는 동아리이므로 0번 단계에서 정상적으로 false를 반환함)");
        } else {
            System.out.println(" ➡️ 결과: 실패 (존재하지 않는 동아리인데 true가 반환됨)");
        }


        // -----------------------------------------------------------------
        // 케이스 3 : 진짜 대장 학번으로 정상 비활성화 처리를 할 때 (최종 연동 성공)
        // -----------------------------------------------------------------
        System.out.println("\n[케이스 3] 본인의 동아리 ID (" + existingOrgId + ") 비활성화 시도");
        System.out.println(" 입력 학번: " + realManagerId + " (실제 운영진)");
        
        boolean result3 = orgDao.deactivateOrganization(existingOrgId, realManagerId);

        if (result3) {
            System.out.println(" ➡️ 결과: 성공 (권한 및 데이터가 확인되어 트랜잭션 정상 커밋됨)");
            System.out.println("  실행 후 확인: DB를 조회해서 아래 항목을 직접 검증해보세요!");
            System.out.println("   1. Organization 테이블에서 org_id=" + existingOrgId + "의 org_status가 false로 바뀌었는가?");
            System.out.println("   2. Bookmark 테이블에서 org_id=" + existingOrgId + "인 데이터들이 싹 지워졌는가?");
        } else {
            System.out.println(" ➡️ 결과: 실패 (정상적인 요청이나 메소드가 false를 반환함. 로그를 확인하세요.)");
        }

        System.out.println("\n=========================================");
        System.out.println(" [트랜잭션 테스트 종료]");
        System.out.println("=========================================");
    }
}