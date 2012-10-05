/**
 * This file was generated from the database schema.
 */
package phorum;


/**
 * This class represents rows from table 'phorum_messages'.
 */
public class PhorumMessages {

    /**
     * Constructor.
     */
    public PhorumMessages() {
    }

    /**
     * the author
     */
    private String author;

    /**
     * the body
     */
    private String body;

    /**
     * the closed
     */
    private Boolean closed;

    /**
     * the datestamp
     */
    private Integer datestamp;

    /**
     * the email
     */
    private String email;

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the ip
     */
    private String ip;

    /**
     * the messageId
     */
    private Integer messageId;

    /**
     * the meta
     */
    private String meta;

    /**
     * the moderatorPost
     */
    private Boolean moderatorPost;

    /**
     * the modifystamp
     */
    private Integer modifystamp;

    /**
     * the moved
     */
    private Boolean moved;

    /**
     * the msgid
     */
    private String msgid;

    /**
     * the parentId
     */
    private Integer parentId;

    /**
     * the recentAuthor
     */
    private String recentAuthor;

    /**
     * the recentMessageId
     */
    private Integer recentMessageId;

    /**
     * the recentUserId
     */
    private Integer recentUserId;

    /**
     * the sort
     */
    private Byte sort;

    /**
     * the status
     */
    private Byte status;

    /**
     * the subject
     */
    private String subject;

    /**
     * the thread
     */
    private Integer thread;

    /**
     * the threadCount
     */
    private Integer threadCount;

    /**
     * the threadviewcount
     */
    private Integer threadviewcount;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * the viewcount
     */
    private Integer viewcount;

    /**
     * Getter method for the author.
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter method for the author.
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter method for the body.
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Setter method for the body.
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Getter method for the closed.
     * @return the closed
     */
    public Boolean getClosed() {
        return closed;
    }

    /**
     * Setter method for the closed.
     * @param closed the closed to set
     */
    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    /**
     * Getter method for the datestamp.
     * @return the datestamp
     */
    public Integer getDatestamp() {
        return datestamp;
    }

    /**
     * Setter method for the datestamp.
     * @param datestamp the datestamp to set
     */
    public void setDatestamp(Integer datestamp) {
        this.datestamp = datestamp;
    }

    /**
     * Getter method for the email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter method for the email.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter method for the forumId.
     * @return the forumId
     */
    public Integer getForumId() {
        return forumId;
    }

    /**
     * Setter method for the forumId.
     * @param forumId the forumId to set
     */
    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    /**
     * Getter method for the ip.
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Setter method for the ip.
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
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
     * Getter method for the meta.
     * @return the meta
     */
    public String getMeta() {
        return meta;
    }

    /**
     * Setter method for the meta.
     * @param meta the meta to set
     */
    public void setMeta(String meta) {
        this.meta = meta;
    }

    /**
     * Getter method for the moderatorPost.
     * @return the moderatorPost
     */
    public Boolean getModeratorPost() {
        return moderatorPost;
    }

    /**
     * Setter method for the moderatorPost.
     * @param moderatorPost the moderatorPost to set
     */
    public void setModeratorPost(Boolean moderatorPost) {
        this.moderatorPost = moderatorPost;
    }

    /**
     * Getter method for the modifystamp.
     * @return the modifystamp
     */
    public Integer getModifystamp() {
        return modifystamp;
    }

    /**
     * Setter method for the modifystamp.
     * @param modifystamp the modifystamp to set
     */
    public void setModifystamp(Integer modifystamp) {
        this.modifystamp = modifystamp;
    }

    /**
     * Getter method for the moved.
     * @return the moved
     */
    public Boolean getMoved() {
        return moved;
    }

    /**
     * Setter method for the moved.
     * @param moved the moved to set
     */
    public void setMoved(Boolean moved) {
        this.moved = moved;
    }

    /**
     * Getter method for the msgid.
     * @return the msgid
     */
    public String getMsgid() {
        return msgid;
    }

    /**
     * Setter method for the msgid.
     * @param msgid the msgid to set
     */
    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    /**
     * Getter method for the parentId.
     * @return the parentId
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * Setter method for the parentId.
     * @param parentId the parentId to set
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Getter method for the recentAuthor.
     * @return the recentAuthor
     */
    public String getRecentAuthor() {
        return recentAuthor;
    }

    /**
     * Setter method for the recentAuthor.
     * @param recentAuthor the recentAuthor to set
     */
    public void setRecentAuthor(String recentAuthor) {
        this.recentAuthor = recentAuthor;
    }

    /**
     * Getter method for the recentMessageId.
     * @return the recentMessageId
     */
    public Integer getRecentMessageId() {
        return recentMessageId;
    }

    /**
     * Setter method for the recentMessageId.
     * @param recentMessageId the recentMessageId to set
     */
    public void setRecentMessageId(Integer recentMessageId) {
        this.recentMessageId = recentMessageId;
    }

    /**
     * Getter method for the recentUserId.
     * @return the recentUserId
     */
    public Integer getRecentUserId() {
        return recentUserId;
    }

    /**
     * Setter method for the recentUserId.
     * @param recentUserId the recentUserId to set
     */
    public void setRecentUserId(Integer recentUserId) {
        this.recentUserId = recentUserId;
    }

    /**
     * Getter method for the sort.
     * @return the sort
     */
    public Byte getSort() {
        return sort;
    }

    /**
     * Setter method for the sort.
     * @param sort the sort to set
     */
    public void setSort(Byte sort) {
        this.sort = sort;
    }

    /**
     * Getter method for the status.
     * @return the status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * Setter method for the status.
     * @param status the status to set
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * Getter method for the subject.
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Setter method for the subject.
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Getter method for the thread.
     * @return the thread
     */
    public Integer getThread() {
        return thread;
    }

    /**
     * Setter method for the thread.
     * @param thread the thread to set
     */
    public void setThread(Integer thread) {
        this.thread = thread;
    }

    /**
     * Getter method for the threadCount.
     * @return the threadCount
     */
    public Integer getThreadCount() {
        return threadCount;
    }

    /**
     * Setter method for the threadCount.
     * @param threadCount the threadCount to set
     */
    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * Getter method for the threadviewcount.
     * @return the threadviewcount
     */
    public Integer getThreadviewcount() {
        return threadviewcount;
    }

    /**
     * Setter method for the threadviewcount.
     * @param threadviewcount the threadviewcount to set
     */
    public void setThreadviewcount(Integer threadviewcount) {
        this.threadviewcount = threadviewcount;
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

    /**
     * Getter method for the viewcount.
     * @return the viewcount
     */
    public Integer getViewcount() {
        return viewcount;
    }

    /**
     * Setter method for the viewcount.
     * @param viewcount the viewcount to set
     */
    public void setViewcount(Integer viewcount) {
        this.viewcount = viewcount;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "author = " + author + ", body = " + body + ", closed = " + closed + ", datestamp = " + datestamp + ", email = " + email + ", forumId = " + forumId + ", ip = " + ip + ", messageId = " + messageId + ", meta = " + meta + ", moderatorPost = " + moderatorPost + ", modifystamp = " + modifystamp + ", moved = " + moved + ", msgid = " + msgid + ", parentId = " + parentId + ", recentAuthor = " + recentAuthor + ", recentMessageId = " + recentMessageId + ", recentUserId = " + recentUserId + ", sort = " + sort + ", status = " + status + ", subject = " + subject + ", thread = " + thread + ", threadCount = " + threadCount + ", threadviewcount = " + threadviewcount + ", userId = " + userId + ", viewcount = " + viewcount;
    }

}

