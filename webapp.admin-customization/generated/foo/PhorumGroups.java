package foo;


/**
 * PhorumGroups is a Querydsl bean type
 */
public class PhorumGroups {

    private Integer groupId;

    private String name;

    private Boolean open;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

}

