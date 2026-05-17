package db_project2026_team03.dto;

public class OrganizationDTO {
    private int orgId; //org_id (INT)
    private String orgName;//org_name (VARCHAR)
    private int orgTypeId;//org_type_id (INT)
    private int categoryId;//category_id (INT)
    private String description;//description (TEXT)
    private String short_description;//short_description (TEXT)
	private String presidentId; //president_id (VARCHAR)
    private boolean orgStatus;//orgStatus(BOOLEAN)

    public OrganizationDTO() {}

    public OrganizationDTO(int orgId, String orgName, int orgTypeId, int categoryId, 
                           String description, String short_description, String presidentId, boolean orgStatus) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgTypeId = orgTypeId;
        this.categoryId = categoryId;
        this.description = description;
        this.short_description = short_description;
        this.presidentId = presidentId;
        this.orgStatus = orgStatus;
    }
    
    public int getOrgId() { return orgId; }
    public void setOrgId(int orgId) { this.orgId = orgId; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
    public int getOrgTypeId() { return orgTypeId; }
    public void setOrgTypeId(int orgTypeId) { this.orgTypeId = orgTypeId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getShort_description() { return short_description; }
	public void setShort_description(String short_description) { this.short_description = short_description; }
    public String getPresidentId() { return presidentId; }
    public void setPresidentId(String presidentId) { this.presidentId = presidentId; }
    public boolean isOrgStatus() { return orgStatus; }
    public void setOrgStatus(boolean orgStatus) { this.orgStatus = orgStatus; }
}
