/**
 * This file was generated from the database schema.
 */
package phorum;


/**
 * This class represents rows from table 'phorum_messages_edittrack'.
 */
public class PhorumMessagesEdittrack {

    /**
     * Constructor.
     */
    public PhorumMessagesEdittrack() {
    }

    /**
     * the diffBody
     */
    private String diffBody;

    /**
     * the diffSubject
     */
    private String diffSubject;

    /**
     * the messageId
     */
    private Integer messageId;

    /**
     * the time
     */
    private Integer time;

    /**
     * the trackId
     */
    private Integer trackId;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the diffBody.
     * @return the diffBody
     */
    public String getDiffBody() {
        return diffBody;
    }

    /**
     * Setter method for the diffBody.
     * @param diffBody the diffBody to set
     */
    public void setDiffBody(String diffBody) {
        this.diffBody = diffBody;
    }

    /**
     * Getter method for the diffSubject.
     * @return the diffSubject
     */
    public String getDiffSubject() {
        return diffSubject;
    }

    /**
     * Setter method for the diffSubject.
     * @param diffSubject the diffSubject to set
     */
    public void setDiffSubject(String diffSubject) {
        this.diffSubject = diffSubject;
    }

    /**
     * Getter method for the messageId.
     * @return the messageId
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * Setter method for the messageId.
     * @param messageId the messageId to set
     */
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    /**
     * Getter method for the time.
     * @return the time
     */
    public Integer getTime() {
        return time;
    }

    /**
     * Setter method for the time.
     * @param time the time to set
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * Getter method for the trackId.
     * @return the trackId
     */
    public Integer getTrackId() {
        return trackId;
    }

    /**
     * Setter method for the trackId.
     * @param trackId the trackId to set
     */
    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    /**
     * Getter method for the userId.
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Setter method for the userId.
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "diffBody = " + diffBody + ", diffSubject = " + diffSubject + ", messageId = " + messageId + ", time = " + time + ", trackId = " + trackId + ", userId = " + userId;
    }

}

