package db_project2026_team03;

public class OrganizationTypeDTO {
    private int orgTypeId;
    private String typeName;


    public OrganizationTypeDTO() {}

    public OrganizationTypeDTO(int orgTypeId, String typeName) {
        this.orgTypeId = orgTypeId;
        this.typeName = typeName;
    }

    public int getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(int orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
