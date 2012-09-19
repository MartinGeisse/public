package foo;


/**
 * PhorumForumGroupXref is a Querydsl bean type
 */
public class PhorumForumGroupXref {

    private Integer forumId;

    private Integer groupId;

    private Integer permission;

    public Integer getForumId() {
        return forumId;
    }

    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

}

