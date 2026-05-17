package db_project2026_team03.dao_test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import db_project2026_team03.dao.RecruitmentDAO;
import db_project2026_team03.dto.RecruitmentDTO;

public class RecruitmentDAOTest {

	public static void main(String[] args) {
		
		RecruitmentDAO dao = new RecruitmentDAO();
		
		// =====================================================
		// [5] RecruitmentDAO 테스트
		// =====================================================

		System.out.println();
		System.out.println("[5] RecruitmentDAO 테스트");

		RecruitmentDAO recruitmentDAO = new RecruitmentDAO();

		// INSERT용 DTO 생성
		RecruitmentDTO recruitment = new RecruitmentDTO();
		recruitment.setOrgId(5);
		recruitment.setTitle("백엔드 개발자 모집");
		recruitment.setQualification("Java 및 MySQL 가능자");
		recruitment.setStartDate(Timestamp.valueOf("2026-05-14 10:00:00"));
		recruitment.setEndDate(Timestamp.valueOf("2026-05-21 18:00:00"));
		recruitment.setInterviewRequired(true);

		// INSERT 테스트
		boolean insertResult = recruitmentDAO.insertRecruitment(recruitment);

		if (insertResult) {
		    System.out.println("[PASS] Recruitment INSERT");
		} else {
		    System.out.println("[FAIL] Recruitment INSERT");
		}

		// SELECT 테스트
		/*List<RecruitmentDTO> recruitmentList = recruitmentDAO.selectAllRecruitments();

		int recruitmentId = -1;

		for (RecruitmentDTO r : recruitmentList) {

		    if (r.getTitle().equals("백엔드 개발자 모집")) {

		        recruitmentId = r.getRecruitmentId();

		        break;
		    }
		}

		if (recruitmentId != -1) {

		    System.out.println("[PASS] Recruitment SELECT / ID 확인");
		    System.out.println("recruitmentId = " + recruitmentId);

		} else {

		    System.out.println("[FAIL] Recruitment SELECT / ID 확인");
		}

		// UPDATE 테스트
		RecruitmentDTO updateRecruitment = new RecruitmentDTO();

		updateRecruitment.setRecruitmentId(recruitmentId);
		updateRecruitment.setOrgId(5);
		updateRecruitment.setTitle("수정된 모집공고");
		updateRecruitment.setQualification("Spring Boot 가능자");
		updateRecruitment.setStartDate(Timestamp.valueOf("2026-05-15 09:00:00"));
		updateRecruitment.setEndDate(Timestamp.valueOf("2026-05-25 18:00:00"));
		updateRecruitment.setInterviewRequired(false);

		boolean updateResult = recruitmentDAO.updateRecruitment(updateRecruitment);

		if (updateResult) {

		    System.out.println("[PASS] Recruitment UPDATE");

		} else {

		    System.out.println("[FAIL] Recruitment UPDATE");
		}

		// DELETE 테스트
		boolean deleteResult = recruitmentDAO.deleteRecruitment(recruitmentId);

		if (deleteResult) {

		    System.out.println("[PASS] Recruitment DELETE");

		} else {

		    System.out.println("[FAIL] Recruitment DELETE");
		}*/

	}

}
