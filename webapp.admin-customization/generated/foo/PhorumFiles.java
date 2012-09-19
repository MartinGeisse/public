package foo;


/**
 * PhorumFiles is a Querydsl bean type
 */
public class PhorumFiles {

    private Integer addDatetime;

    private String fileData;

    private Integer fileId;

    private String filename;

    private Integer filesize;

    private String link;

    private Integer messageId;

    private Integer userId;

    public Integer getAddDatetime() {
        return addDatetime;
    }

    public void setAddDatetime(Integer addDatetime) {
        this.addDatetime = addDatetime;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

