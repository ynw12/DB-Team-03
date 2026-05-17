package db_project2026_team03.dao_test;

import db_project2026_team03.dao.BookmarkDAO;
import db_project2026_team03.dao.ScrapDAO;
import db_project2026_team03.dto.BookmarkDTO;
import db_project2026_team03.dto.ScrapDTO;

public class BookmarkScrapDAOTest {

    public static void main(String[] args) {

        BookmarkDAO bookmarkDAO = new BookmarkDAO();
        ScrapDAO scrapDAO = new ScrapDAO();

        // ================================================
        // 더미 데이터 기준 (dummy_data.sql 실행 후 테스트)
        // Student : 2021000001 ~ 2021000005
        // Organization : org_id 1 (코딩 학술 동아리)
        //                org_id 2 (지역 봉사 동아리)
        //                org_id 3 (풋살 동아리)
        // Recruitment  : recruitment_id 1, 2, 3
        // ================================================

        System.out.println("\n========== [1] insertBookmark 테스트 - 정상 추가 ==========");
        String[][] bookmarkData = {
            // { studentId, orgId }
            {"2021000001", "1"},
            {"2021000001", "2"},
            {"2021000002", "1"},
            {"2021000003", "3"},
            {"2021000004", "2"},
        };

        for (String[] data : bookmarkData) {
            BookmarkDTO bookmark = new BookmarkDTO();
            bookmark.setStudentId(data[0]);
            bookmark.setOrgId(Integer.parseInt(data[1]));
            boolean result = bookmarkDAO.insertBookmark(bookmark);
            //System.out.println("▶ 학번 " + data[0] + " | 동아리 ID " + data[1] + " 결과: " + (result ? "성공" : "실패"));
        }

        System.out.println("\n========== [2] insertBookmark 테스트 - 중복 추가 ==========");
        // 동일 학번 + 동일 동아리 → 실패 예상
        BookmarkDTO dupBookmark = new BookmarkDTO();
        dupBookmark.setStudentId("2021000001");
        dupBookmark.setOrgId(1);
        boolean dupResult = bookmarkDAO.insertBookmark(dupBookmark);
        System.out.println("▶ 중복 추가 결과: " + (dupResult ? "성공" : "실패"));

        System.out.println("\n========== [3] getBookmarksByStudent 테스트 - 즐겨찾기 있는 학번 ==========");
        bookmarkDAO.getBookmarksByStudent("2021000001");  // 동아리 1, 2 즐겨찾기

        System.out.println("\n========== [4] getBookmarksByStudent 테스트 - 즐겨찾기 없는 학번 ==========");
        bookmarkDAO.getBookmarksByStudent("0000000000");  // 없는 학번 → "즐겨찾기한 동아리가 없습니다."

        System.out.println("\n========== [5] deleteBookmark 테스트 - 정상 삭제 ==========");
        boolean deleteResult = bookmarkDAO.deleteBookmark("2021000001", 2);
        System.out.println("▶ 삭제 결과: " + (deleteResult ? "성공" : "실패"));

        System.out.println("\n========== [6] deleteBookmark 테스트 - 없는 즐겨찾기 삭제 ==========");
        boolean deleteNone = bookmarkDAO.deleteBookmark("2021000001", 2);  // 이미 삭제한 것 → 실패 예상
        System.out.println("▶ 없는 즐겨찾기 삭제 결과: " + (deleteNone ? "성공" : "실패"));

        /*System.out.println("\n========== [7] selectAllBookmarks 테스트 ==========");
        bookmarkDAO.selectAllBookmarks().forEach(b ->
            System.out.println("▶ bookmark_id: " + b.getBookmarkId()
                + " | student_id: " + b.getStudentId()
                + " | org_id: " + b.getOrgId()
                + " | created_at: " + b.getCreatedAt())
        );*/

        // ================================================

        System.out.println("\n========== [8] insertScrap 테스트 - 정상 추가 ==========");
        String[][] scrapData = {
            // { studentId, recruitmentId }
            {"2021000001", "1"},
            {"2021000001", "2"},
            {"2021000002", "3"},
            {"2021000003", "1"},
            {"2021000004", "2"},
        };

        for (String[] data : scrapData) {
            ScrapDTO scrap = new ScrapDTO();
            scrap.setStudentId(data[0]);
            scrap.setRecruitmentId(Integer.parseInt(data[1]));
            boolean result = scrapDAO.insertScrap(scrap);
            //System.out.println("▶ 학번 " + data[0] + " | 공고 ID " + data[1] + " 결과: " + (result ? "성공" : "실패"));
        }

        System.out.println("\n========== [9] insertScrap 테스트 - 중복 추가 ==========");
        // 동일 학번 + 동일 공고 → 실패 예상
        ScrapDTO dupScrap = new ScrapDTO();
        dupScrap.setStudentId("2021000001");
        dupScrap.setRecruitmentId(1);
        boolean dupScrapResult = scrapDAO.insertScrap(dupScrap);
        System.out.println("▶ 중복 추가 결과: " + (dupScrapResult ? "성공" : "실패"));

        System.out.println("\n========== [10] getScrapsByStudent 테스트 - 스크랩 있는 학번 ==========");
        scrapDAO.getScrapsByStudent("2021000001");  // 공고 1, 2 스크랩

        System.out.println("\n========== [11] getScrapsByStudent 테스트 - 스크랩 없는 학번 ==========");
        scrapDAO.getScrapsByStudent("0000000000");  // 없는 학번 → "스크랩한 공고가 없습니다."

        System.out.println("\n========== [12] deleteScrap 테스트 - 정상 삭제 ==========");
        boolean deleteScrap = scrapDAO.deleteScrap("2021000001", 2);
        System.out.println("▶ 삭제 결과: " + (deleteScrap ? "성공" : "실패"));

        System.out.println("\n========== [13] deleteScrap 테스트 - 없는 스크랩 삭제 ==========");
        boolean deleteScrapNone = scrapDAO.deleteScrap("2021000001", 2);  // 이미 삭제한 것 → 실패 예상
        System.out.println("▶ 없는 스크랩 삭제 결과: " + (deleteScrapNone ? "성공" : "실패"));

        /*System.out.println("\n========== [14] selectAllScraps 테스트 ==========");
        scrapDAO.selectAllScraps().forEach(s ->
            System.out.println("▶ scrap_id: " + s.getScrapId()
                + " | student_id: " + s.getStudentId()
                + " | recruitment_id: " + s.getRecruitmentId()
                + " | created_at: " + s.getCreatedAt())
        );*/
    }
}
