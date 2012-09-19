package foo;


/**
 * PhorumPmBuddies is a Querydsl bean type
 */
public class PhorumPmBuddies {

    private Integer buddyUserId;

    private Integer pmBuddyId;

    private Integer userId;

    public Integer getBuddyUserId() {
        return buddyUserId;
    }

    public void setBuddyUserId(Integer buddyUserId) {
        this.buddyUserId = buddyUserId;
    }

    public Integer getPmBuddyId() {
        return pmBuddyId;
    }

    public void setPmBuddyId(Integer pmBuddyId) {
        this.pmBuddyId = pmBuddyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

