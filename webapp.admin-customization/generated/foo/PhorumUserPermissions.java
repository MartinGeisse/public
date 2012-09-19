package foo;


/**
 * PhorumUserPermissions is a Querydsl bean type
 */
public class PhorumUserPermissions {

    private Integer forumId;

    private Integer permission;

    private Integer userId;

    public Integer getForumId() {
        return forumId;
    }

    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

