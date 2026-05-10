package db_project2026_team03;

public class BookmarkDTO {
    private int bookmarkId;
    private int orgId;
    private String studentId;

    public BookmarkDTO() {}

    public BookmarkDTO(int bookmarkId, int orgId, String studentId) {
        this.bookmarkId = bookmarkId;
        this.orgId = orgId;
        this.studentId = studentId;
    }

    public int getBookmarkId() {return bookmarkId;}
    public void setBookmarkId(int bookmarkId) {this.bookmarkId = bookmarkId;}
    public int getOrgId() {return orgId;}
    public void setOrgId(int orgId) {this.orgId = orgId;}
    public String getStudentId() {return studentId;}
    public void setStudentId(String studentId) {this.studentId = studentId;}
}