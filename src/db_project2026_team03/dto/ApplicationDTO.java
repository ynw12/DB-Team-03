package db_project2026_team03.dto;

public class ApplicationDTO {
    private int applicationId;
    private int recruitmentId;
    private String studentId;
    private String selfIntro;
    private String status;

    public ApplicationDTO() {}

    public ApplicationDTO(int applicationId, int recruitmentId, String studentId, 
                          String selfIntro, String status) {
        this.applicationId = applicationId;
        this.recruitmentId = recruitmentId;
        this.studentId = studentId;
        this.selfIntro = selfIntro;
        this.status = status;
    }

    public int getApplicationId() {return applicationId;}
    public void setApplicationId(int applicationId) {this.applicationId = applicationId;}
    public int getRecruitmentId() {return recruitmentId;}
    public void setRecruitmentId(int recruitmentId) {this.recruitmentId = recruitmentId;}
    public String getStudentId() {return studentId;}
    public void setStudentId(String studentId) {this.studentId = studentId;}
    public String getSelfIntro() {return selfIntro;}
    public void setSelfIntro(String selfIntro) {this.selfIntro = selfIntro;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}
