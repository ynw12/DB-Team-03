package db_project2026_team03.dto;

import java.sql.Timestamp;

public class RecruitmentDTO {
    private int recruitmentId;
    private int orgId;
    private String title;
    private String qualification;
    private Timestamp startDate;
    private Timestamp endDate;
    private boolean interviewRequired;
    private String recruStatus;

    public RecruitmentDTO() {}

    public RecruitmentDTO(int recruitmentId, int orgId, String title, String qualification, 
                          Timestamp startDate, Timestamp endDate, boolean interviewRequired, String recruStatus) {
        this.recruitmentId = recruitmentId;
        this.orgId = orgId;
        this.title = title;
        this.qualification = qualification;
        this.startDate = startDate;
        this.endDate = endDate;
        this.interviewRequired = interviewRequired;
        this.recruStatus = recruStatus;
    }

    public int getRecruitmentId() {return recruitmentId;}
    public void setRecruitmentId(int recruitmentId) {this.recruitmentId = recruitmentId;}
    public int getOrgId() {return orgId;}
    public void setOrgId(int orgId) {this.orgId = orgId;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getQualification() {return qualification;}
    public void setQualification(String qualification) {this.qualification = qualification;}
    public Timestamp getStartDate() {return startDate;}
    public void setStartDate(Timestamp startDate) {this.startDate = startDate;}
    public Timestamp getEndDate(){return endDate;}
    public void setEndDate(Timestamp endDate){this.endDate = endDate;}
    public boolean isInterviewRequired() {return interviewRequired;}
    public void setInterviewRequired(boolean interviewRequired){this.interviewRequired = interviewRequired;}
	public String isRecruStatus() {return recruStatus;}
	public void setRecruStatus(String recruStatus) {this.recruStatus = recruStatus;}
    
}
