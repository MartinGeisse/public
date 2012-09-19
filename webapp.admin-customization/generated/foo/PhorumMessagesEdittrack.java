package foo;


/**
 * PhorumMessagesEdittrack is a Querydsl bean type
 */
public class PhorumMessagesEdittrack {

    private String diffBody;

    private String diffSubject;

    private Integer messageId;

    private Integer time;

    private Integer trackId;

    private Integer userId;

    public String getDiffBody() {
        return diffBody;
    }

    public void setDiffBody(String diffBody) {
        this.diffBody = diffBody;
    }

    public String getDiffSubject() {
        return diffSubject;
    }

    public void setDiffSubject(String diffSubject) {
        this.diffSubject = diffSubject;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

