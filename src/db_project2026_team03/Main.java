package db_project2026_team03;

import java.util.Scanner;
import java.util.List;
import java.sql.Timestamp;

import db_project2026_team03.dao.*;
import db_project2026_team03.dto.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    
    //DAO 객체
    private static StudentDAO studentDao = new StudentDAO();
    private static OrganizationDAO orgDao = new OrganizationDAO();
    private static RecruitmentDAO recruitmentDao = new RecruitmentDAO();
    private static ApplicationDAO applicationDao = new ApplicationDAO();
    private static BookmarkDAO bookmarkDao = new BookmarkDAO();
    private static ScrapDAO scrapDao = new ScrapDAO();
    private static CategoryDAO categoryDao = new CategoryDAO();

    //현재 로그인한 사용자의 학번을 저장하는 전역 변수
    public static String loginedStudentId = null;

    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("   이화여대 동아리 총 관리 시스템   ");
        System.out.println("=============================================");

        while (true) {
            System.out.print("\n▶ 학번을 입력하세요 (종료하려면 'q' 입력): ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("프로그램을 종료합니다. 이용해 주셔서 감사합니다.");
                break;
            }

            if (studentDao.checkStudentExist(input)) {
                loginedStudentId = input;
                System.out.println("\n로그인 성공(접속 학번: " + loginedStudentId + ")");

                if (orgDao.checkIsAdmin(loginedStudentId)) {
                    System.out.println("[운영진] 메뉴로 진입합니다.");
                    runAdminMenu();
                } else {
                    System.out.println("[일반 학생] 메뉴로 진입합니다.");
                    runStudentMenu();
                }

                //로그아웃 후 세션 초기화
                loginedStudentId = null;
            } else {
                System.out.println("X 등록되지 않은 학번입니다. 다시 입력해 주세요.");
            }
        }
        sc.close();
    }
    
    private static void runStudentMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n================== [학생 메뉴] ==================");
            System.out.println("1. 전체 동아리 목록 및 조회(북마크)");
            System.out.println("2. 모집 공고 게시판 (검색/필터/지원/스크랩)");
            System.out.println("3. 내 활동 상세 내역 (지원서/북마크/스크랩 조회)");
            System.out.println("0. 로그아웃");
            System.out.println("=================================================");
            System.out.print("▶ 원하시는 메뉴 번호를 선택하세요: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1:
                        runOrganizationMenu(); 
                        break;
                    case 2:
                        runRecruitmentBoard(); 
                        break;
                    case 3:
                        System.out.println("\n[내 활동 상태 종합 조회]");
                        applicationDao.getApplicationsByStudent(loginedStudentId);
                        bookmarkDao.getBookmarksByStudent(loginedStudentId);
                        scrapDao.getScrapsByStudent(loginedStudentId);
                        break;
                    case 0:
                        System.out.println("학생 메뉴를 종료하고 로그아웃합니다.");
                        isRunning = false;
                        break;
                    default:
                        System.out.println("잘못된 번호 선택입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 정확히 입력해 주세요.");
            }
        }
    }
    private static void runOrganizationMenu() {
        boolean isOrgRunning = true;
        System.out.println("\n--- 교내 등록 단체 및 동아리 전체 목록 ---");
        orgDao.printAllOrganizations();

        while (isOrgRunning) {
            System.out.println("\n[동아리 정보 가이드] --------------------------------");
            System.out.println("1. 전체 동아리 목록 다시보기   2. 분야 카테고리별 인기순 필터링");
            System.out.println("3. 동아리 상세 정보 확인 및 북마크(즐겨찾기) 등록");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.println("-------------------------------------------------");
            System.out.print("▶ 작업을 선택하세요: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1:
                        orgDao.printAllOrganizations();
                        break;
                    case 2:
                        System.out.println("\n--- 카테고리 목록 ---");
                        List<CategoryDTO> categories = categoryDao.selectAllCategories();
                        for (CategoryDTO c : categories) {
                            System.out.printf("[%d] %s\n", c.getCategoryId(), c.getCategoryName());
                        }
                        System.out.print("▶ 필터링할 카테고리 번호(ID) 입력: ");
                        int catId = Integer.parseInt(sc.nextLine().trim());
                        
                        String targetCategoryName = null;
                        for (CategoryDTO c : categories) {
                            if (c.getCategoryId() == catId) {
                                targetCategoryName = c.getCategoryName();
                                break;
                            }
                        }
                        if (targetCategoryName != null) {
                            orgDao.filterOrganizationsByCategory(targetCategoryName);
                        } else {
                            System.out.println("존재하지 않는 카테고리 번호입니다.");
                        }
                        break;
                    case 3:
                        System.out.print("\n▶ 상세 정보를 확인할 동아리 번호(ID) 입력: ");
                        int orgId = Integer.parseInt(sc.nextLine().trim());
                        orgDao.printOrganizationDetail(orgId);

                        System.out.print("▶ 이 동아리를 즐겨찾기(북마크)에 추가하시겠습니까? (1: 예 / 2: 아니오): ");
                        int bookmarkChoice = Integer.parseInt(sc.nextLine().trim());
                        if (bookmarkChoice == 1) {
                            BookmarkDTO newBookmark = new BookmarkDTO();
                            newBookmark.setOrgId(orgId);
                            newBookmark.setStudentId(loginedStudentId);
                            bookmarkDao.createBookmark(newBookmark);
                        }
                        break;
                    case 0:
                        System.out.println("이전 메뉴로 돌아갑니다.");
                        isOrgRunning = false;
                        break;
                    default:
                        System.out.println("잘못된 번호 선택입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 정확히 입력해 주세요.");
            }
        }
    }

    private static void runRecruitmentBoard() {
        boolean isBoardRunning = true;
        System.out.println("\n--- 현재 접수 중인 모집 공고 목록 ---");
        recruitmentDao.printAllRecruitments();

        while (isBoardRunning) {
            System.out.println("\n[모집 공고 게시판] --------------------------------");
            System.out.println("1. 모집 공고 전체 조회    2. 공고 키워드 검색");
            System.out.println("3. 공고 세부 정보 확인 및 지원서 접수/스크랩");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.println("-------------------------------------------------");
            System.out.print("▶ 작업을 선택하세요: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1:
                        recruitmentDao.printAllRecruitments();
                        break;
                    case 2:
                        System.out.print("\n검색할 공고의 핵심 키워드를 입력하세요: ");
                        String keyword = sc.nextLine().trim();
                        recruitmentDao.searchRecruitmentsByKeyword(keyword);
                        break;
                    case 3:
                        System.out.print("\n▶ 세부 조건을 확인할 모집 공고 번호 입력: ");
                        int recruitId = Integer.parseInt(sc.nextLine().trim());
                        recruitmentDao.printRecruitmentDetail(recruitId);
                        
                        System.out.print("\n▶ 번호를 선택해주세요 (1: 즉시 지원하기 / 2: 공고 스크랩 / 3: 넘어가기): ");
                        int actionChoice = Integer.parseInt(sc.nextLine().trim());
                        
                        if (actionChoice == 1) {
                            System.out.print("제출할 자기소개서를 입력해 주세요: ");
                            String intro = sc.nextLine().trim();

                            ApplicationDTO newApp = new ApplicationDTO();
                            newApp.setRecruitmentId(recruitId);
                            newApp.setStudentId(loginedStudentId);
                            newApp.setSelfIntro(intro);

                            applicationDao.createApplication(newApp);
                        } else if (actionChoice == 2) {
                            ScrapDTO newScrap = new ScrapDTO();
                            newScrap.setRecruitmentId(recruitId);
                            newScrap.setStudentId(loginedStudentId);
                            scrapDao.createScrap(newScrap);
                        }
                        break;
                    case 0:
                        System.out.println("이전 메뉴로 돌아갑니다.");
                        isBoardRunning = false;
                        break;
                    default:
                        System.out.println("잘못된 번호 선택입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 정확히 입력해 주세요.");
            }
        }
    }

    private static void runAdminMenu() {
        //로그인한 운영진 학번으로 소속 동아리 번호 자동 조회.
        int currentOrgId = orgDao.getOrgIdByPresidentId(loginedStudentId);

        if (currentOrgId == -1) {
            System.out.println("운영진 권한이 없거나 소속된 동아리를 찾을 수 없습니다.");
            return;
        }

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n================= [운영진 메뉴] =================");
            System.out.println("1. 신규 모집 공고 등록         2. 등록된 모집 공고 수정");
            System.out.println("3. 모집 공고 삭제              4. 모집 공고 마감 처리");
            System.out.println("5. 동아리/학회 비활성화        6. 지원자 전체 조회");
            System.out.println("7. 지원서 일괄 합격/불합격 처리");                       
            System.out.println("0. 로그아웃");
            System.out.println("=================================================");
            System.out.print("▶ 원하시는 관리 번호를 선택하세요: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1:
                        System.out.println("\n--- 신규 모집 공고 등록 ---");
                        System.out.print("▶ 공고 제목 입력: ");
                        String title = sc.nextLine().trim();
                        System.out.print("▶ 지원 자격 요건 입력: ");
                        String qual = sc.nextLine().trim();
                        System.out.print("▶ 면접 필수 여부 (true/false) 입력: ");
                        boolean interview = Boolean.parseBoolean(sc.nextLine().trim());

                        RecruitmentDTO newRecruit = new RecruitmentDTO();
                        newRecruit.setOrgId(currentOrgId); //자동 조회된 본인 동아리 ID
                        newRecruit.setTitle(title);
                        newRecruit.setQualification(qual);
                        newRecruit.setStartDate(new Timestamp(System.currentTimeMillis()));
                        newRecruit.setEndDate(new Timestamp(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)));
                        newRecruit.setInterviewRequired(interview);

                        if (recruitmentDao.insertRecruitment(newRecruit)) {
                            System.out.println("성공적으로 신규 공고가 등록되었습니다.");
                        }
                        break;
                    case 2:
                        System.out.println("\n--- 모집 공고 내용 수정 ---");
                        //수정 전 본인 동아리의 공고 목록 출력
                        recruitmentDao.printRecruitmentsByOrgId(currentOrgId);
                        
                        System.out.print("▶ 수정할 공고의 번호를 입력하세요: ");
                        int updateId = Integer.parseInt(sc.nextLine().trim());
                        
                        RecruitmentDTO updateRecruit = recruitmentDao.getRecruitmentById(updateId);
                        
                        if (updateRecruit == null) {
                            System.out.println("해당 번호의 공고를 찾을 수 없습니다.");
                            break;
                        }

                        //타 동아리 공고 수정 차단
                        if (updateRecruit.getOrgId() != currentOrgId) {
                            System.out.println("!!본인 동아리의 공고만 수정할 수 있습니다.");
                            break;
                        }

                        System.out.println("[현재 제목] " + updateRecruit.getTitle());
                        System.out.print("▶ 새 제목 입력 (엔터 입력 시 기존 제목 유지.): ");
                        String newTitle = sc.nextLine().trim();
                        if (!newTitle.isEmpty()) {
                            updateRecruit.setTitle(newTitle);
                        }

                        System.out.println("[현재 자격 요건] " + updateRecruit.getQualification());
                        System.out.print("▶ 새 자격 요건 입력 (엔터 입력 시 기존 요건 유지): ");
                        String newQual = sc.nextLine().trim();
                        if (!newQual.isEmpty()) {
                            updateRecruit.setQualification(newQual);
                        }

                        System.out.println("[현재 면접 여부] " + (updateRecruit.isInterviewRequired() ? "있음" : "없음"));
                        System.out.print("▶ 면접 여부 변경 (1: 있음 / 2: 없음 / 엔터 입력 시 기존 유지): ");
                        String interviewInput = sc.nextLine().trim();
                        if (!interviewInput.isEmpty()) {
                            if (interviewInput.equals("1")) updateRecruit.setInterviewRequired(true);
                            else if (interviewInput.equals("2")) updateRecruit.setInterviewRequired(false);
                        }

                        if (recruitmentDao.updateRecruitment(updateRecruit)) {
                            System.out.println("공고 수정 작업이 성공적으로 완료되었습니다.");
                        }
                        break;

                    case 3:
                        System.out.println("\n--- 모집 공고 파기/삭제 ---");
                        recruitmentDao.printRecruitmentsByOrgId(currentOrgId);
                        
                        System.out.print("▶ 삭제할 공고 번호를 입력하세요: ");
                        int deleteId = Integer.parseInt(sc.nextLine().trim());
                        
                        if (recruitmentDao.deleteRecruitment(deleteId, currentOrgId)) {
                            System.out.println("공고 데이터 파기 완료되었습니다.");
                        }
                        break;

                    case 4:
                        System.out.println("\n--- 모집 공고 즉시 마감 처리 ---");
                        recruitmentDao.printRecruitmentsByOrgId(currentOrgId);
                        
                        System.out.print("▶ 강제 마감할 공고 번호를 입력하세요: ");
                        int closeId = Integer.parseInt(sc.nextLine().trim());
           
                        RecruitmentDTO closeRecruit = recruitmentDao.getRecruitmentById(closeId);
                        
                        if (closeRecruit == null) {
                            System.out.println("해당 번호의 공고를 찾을 수 없습니다.");
                            break;
                        }

                        if (closeRecruit.getOrgId() != currentOrgId) {
                            System.out.println("본인 동아리의 공고만 마감 처리할 수 있습니다.");
                            break;
                        }

                        closeRecruit.setEndDate(new Timestamp(System.currentTimeMillis()));

                        if (recruitmentDao.updateRecruitment(closeRecruit)) {
                            System.out.println("해당 공고의 모집이 조기 마감 처리되었습니다.");
                        }
                        break;

                    case 5:
                        System.out.println("\n--- 동아리/학회 비활성화 ---");
                        
                        List<Integer> managedOrgs = orgDao.getManagedActiveOrganizations(loginedStudentId);
                        
                        int targetDeactivateId = -1;
                        
                        if (managedOrgs.size() == 1) {
                            System.out.print("▶ 관리 중인 단체가 1개입니다. 비활성화를 원한다면 1, 이전 목록으로 돌아가시려면 0을 입력해주세요 : ");
                            String confirmInput = sc.nextLine().trim();
                            
                            if (confirmInput.equals("1")) {
                            	targetDeactivateId = managedOrgs.get(0);
                            } else if (confirmInput.equals("0")) {
                                System.out.println("[안내] 이전 목록으로 돌아갑니다.");
                                System.out.println("==================================");
                                break;
                            } else {
                                System.out.println("\n[오류] 0 또는 1만 입력 가능합니다. 처리를 취소합니다.");
                                System.out.println("==================================");
                                break;
                            }
                        } 
                        
                        else {
                            System.out.print("▶ 위 목록 중 비활성화할 단체 번호를 입력하세요: ");
                            try {
                                targetDeactivateId = Integer.parseInt(sc.nextLine().trim());
                                
                                if (!managedOrgs.contains(targetDeactivateId)) {
                                    System.out.println("\n[오류] 본인이 관리 중인 동아리 번호가 아닙니다. 다시 확인해 주세요.");
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("\n[오류] 숫자로만 입력해 주세요.");
                                break;
                            }
                        }
                        
                        boolean isDeactivate = orgDao.deactivateOrganization(targetDeactivateId, loginedStudentId);
                        
                        System.out.println("\n==================================");
                        if (isDeactivate) {
                            System.out.println("[안내] 단체가 성공적으로 비활성화되었습니다.");
                        } else {
                            System.out.println("[오류] 비활성화 처리에 실패했습니다. (DB 오류 등)");
                        }
                        System.out.println("==================================");
                        break;
  
                    case 6:
                        System.out.println("\n--- 지원자 전체 조회 ---");
                        applicationDao.getApplicationsByOrg(loginedStudentId);
                        break;

                    case 7:
                        System.out.println("\n--- 지원서 일괄 합격/불합격 처리 (트랜잭션) ---");
                        applicationDao.getApplicationsByOrg(loginedStudentId); 
                        
                        System.out.print("▶ 심사를 진행할 [공고 번호(ID)] 입력: ");
                        int recruitmentId = Integer.parseInt(sc.nextLine().trim());
                        
                        System.out.print("▶ 합격 처리할 [지원서 ID]들을 쉼표(,)로 구분하여 입력\n  (예: 1,3,5 / 전원 불합격 처리 시 그냥 엔터): ");
                        String inputIds = sc.nextLine().trim();

                        java.util.List<Integer> passedAppIds = new java.util.ArrayList<>();
                        if (!inputIds.isEmpty()) {
                            String[] tokens = inputIds.split(",");
                            for (String token : tokens) {
                                passedAppIds.add(Integer.parseInt(token.trim()));
                            }
                        }

                        applicationDao.updateBatchPassStatus(recruitmentId, passedAppIds);
                        break;

                    case 0:
                        System.out.println("운영진 메뉴를 종료하고 로그아웃합니다.");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("잘못된 번호 선택입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞게 메뉴를 다시 선택해 주세요.");
            }
        }
    }
}
