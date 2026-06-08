package db_project2026_team03.dto;

public class StudentDTO {
    private String studentId;//student_id (VARCHAR)
    private String name;//name (VARCHAR)
    private String university;//university (VARCHAR)
    private String major;//major(VARCHAR)
    private String phone;//phone (VARCHAR)

    public StudentDTO() {}
    
    public StudentDTO(String studentId, String name, String university, String major, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.university = university;
        this.major = major;
        this.phone = phone;
    }

    //getter setter
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
