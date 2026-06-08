package db_project2026_team03.dto;

import java.sql.Timestamp;

public class ScrapDTO {
	private int scrapId;
	private int recruitmentId;
	private String studentId;
	private Timestamp createdAt;
	
	public ScrapDTO() {
	}
	public ScrapDTO(int scrapId,int recruitmentId, String studentId,Timestamp createdAt) {
		this.scrapId = scrapId;
		this.recruitmentId =recruitmentId;
		this.studentId = studentId;
		this.createdAt = createdAt;
	}
	public int getScrapId() {
		return scrapId;
	}
	public int getRecruitmentId() {
		return recruitmentId;
	}
	public String getStudentId() {
		return studentId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setScrapId(int scrapId) {
		this.scrapId = scrapId;
	}
	public void setRecruitmentId(int recruitmentId) {
		this.recruitmentId = recruitmentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	

}

