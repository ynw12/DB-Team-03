package db_project2026_team03;

import java.sql.Timestamp;

public class NoticeDTO {
    private int noticeId;
    private int orgId;
    private String title;
    private String content;
    private Timestamp createdAt;

    public NoticeDTO() {}

    public NoticeDTO(int noticeId, int orgId, String title, String content, Timestamp createdAt) {
        this.noticeId = noticeId;
        this.orgId = orgId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getNoticeId() {return noticeId;}
    public void setNoticeId(int noticeId) {this.noticeId = noticeId;}
    public int getOrgId() {return orgId;}
    public void setOrgId(int orgId) {this.orgId = orgId;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public Timestamp getCreatedAt() {return createdAt;}
    public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}
}
