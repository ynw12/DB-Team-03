package db_project2026_team03.dto;

import java.sql.Timestamp;

public class BookmarkDTO {
    private int bookmarkId;
    private int orgId;
    private String studentId;
    private Timestamp createdAt;

    public BookmarkDTO() {}

    public BookmarkDTO(int bookmarkId, int orgId, String studentId, Timestamp createdAt) {
        this.bookmarkId = bookmarkId;
        this.orgId = orgId;
        this.studentId = studentId;
        this.createdAt = createdAt;
    }

    public int getBookmarkId() {return bookmarkId;}
    public void setBookmarkId(int bookmarkId) {this.bookmarkId = bookmarkId;}
    public int getOrgId() {return orgId;}
    public void setOrgId(int orgId) {this.orgId = orgId;}
    public String getStudentId() {return studentId;}
    public void setStudentId(String studentId) {this.studentId = studentId;}
	public Timestamp getCreatedAt() {return createdAt;}
	public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}
    
}