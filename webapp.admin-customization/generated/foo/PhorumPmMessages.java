package foo;


/**
 * PhorumPmMessages is a Querydsl bean type
 */
public class PhorumPmMessages {

    private String author;

    private Integer datestamp;

    private String message;

    private String meta;

    private Integer pmMessageId;

    private String subject;

    private Integer userId;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(Integer datestamp) {
        this.datestamp = datestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Integer getPmMessageId() {
        return pmMessageId;
    }

    public void setPmMessageId(Integer pmMessageId) {
        this.pmMessageId = pmMessageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

