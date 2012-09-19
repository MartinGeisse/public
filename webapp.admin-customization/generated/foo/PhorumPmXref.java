package foo;


/**
 * PhorumPmXref is a Querydsl bean type
 */
public class PhorumPmXref {

    private Integer pmFolderId;

    private Integer pmMessageId;

    private Integer pmXrefId;

    private Boolean readFlag;

    private Boolean replyFlag;

    private String specialFolder;

    private Integer userId;

    public Integer getPmFolderId() {
        return pmFolderId;
    }

    public void setPmFolderId(Integer pmFolderId) {
        this.pmFolderId = pmFolderId;
    }

    public Integer getPmMessageId() {
        return pmMessageId;
    }

    public void setPmMessageId(Integer pmMessageId) {
        this.pmMessageId = pmMessageId;
    }

    public Integer getPmXrefId() {
        return pmXrefId;
    }

    public void setPmXrefId(Integer pmXrefId) {
        this.pmXrefId = pmXrefId;
    }

    public Boolean getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Boolean readFlag) {
        this.readFlag = readFlag;
    }

    public Boolean getReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(Boolean replyFlag) {
        this.replyFlag = replyFlag;
    }

    public String getSpecialFolder() {
        return specialFolder;
    }

    public void setSpecialFolder(String specialFolder) {
        this.specialFolder = specialFolder;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

