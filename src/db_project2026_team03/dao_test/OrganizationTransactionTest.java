package db_project2026_team03.dao_test;

import db_project2026_team03.dao.OrganizationDAO;

// 🚨 본인의 실제 DAO 클래스가 있는 패키지 경로를 임포트하세요.
// 예: import db_project2026_team03.dao.OrganizationDAO;

public class OrganizationTransactionTest {

    public static void main(String[] args) {
        OrganizationDAO orgDao = new OrganizationDAO();

        System.out.println("=========================================");
        System.out.println("[트랜잭션 최종 검증 테스트 시작]");
        System.out.println("=========================================");

        int existingOrgId = 1;       // 실제 존재하는 동아리 ID
        int nonExistentOrgId = 9999; // 존재하지 않는 가짜 ID

        // -----------------------------------------------------------------
        // 케이스 ① : 존재하지 않는 동아리 테스트 (방어 로직 확인)
        // -----------------------------------------------------------------
        System.out.println("\n[케이스 1] 존재하지 않는 동아리 ID (" + nonExistentOrgId + ") 처리 시도");
        boolean result1 = orgDao.deactivateOrganization(nonExistentOrgId);
        System.out.println("➡️ 결과: " + (result1 ? "❌ 실패 (true 반환됨)" : "✅ 성공 (정상적으로 false 반환됨)"));


        // -----------------------------------------------------------------
        // 케이스 ② : 정상 작동 테스트 (핵심 기능 확인)
        // -----------------------------------------------------------------
        System.out.println("\n[케이스 2] 존재하는 동아리 ID (" + existingOrgId + ") 비활성화 및 북마크 삭제 시도");
        boolean result2 = orgDao.deactivateOrganization(existingOrgId);
        System.out.println("➡️ 결과: " + (result2 ? "✅ 성공 (true 반환됨)" : "❌ 실패 (false 반환됨)"));
        if (result2) {
            System.out.println("📌 [DB 직접 확인 필수]");
            System.out.println("   - Organization 테이블: org_id=" + existingOrgId + "의 상태가 false로 바뀌었나요?");
            System.out.println("   - Bookmark 테이블: org_id=" + existingOrgId + "인 데이터가 싹 지워졌나요?");
        }


        // -----------------------------------------------------------------
        // 케이스 ③ : 🚨 롤백 테스트 (가장 중요! 트랜잭션 안전성 확인)
        // -----------------------------------------------------------------
        System.out.println("\n[케이스 3] 롤백 테스트 (일부러 에러를 내서 확인하는 단계)");
        System.out.println("⚠️ 테스트 방법: DAO 코드에서 deleteBookmarkSql의 테이블명을 틀리게 고친 후 실행해보세요.");
        
        // 다시 다른 존재하는 동아리 ID(예: 2번)로 바꾼 뒤 에러를 유발해봅니다.
        int rollbackTestOrgId = 2; 
        boolean result3 = orgDao.deactivateOrganization(rollbackTestOrgId);
        
        System.out.println("➡️ 결과: " + (result3 ? "❌ 실패 (에러가 났는데 true가 나옴)" : "✅ 성공 (에러를 캐치하여 false 반환됨)"));
        System.out.println("📌 [DB 직접 확인 필수]");
        System.out.println("   - 에러가 터졌으므로, DB의 org_id=" + rollbackTestOrgId + "인 동아리는 여전히 'true' 상태로 남아있어야 안전한 롤백이 성공한 것입니다!");

        System.out.println("\n=========================================");
        System.out.println("[트랜잭션 테스트 종료]");
        System.out.println("=========================================");
    }
}