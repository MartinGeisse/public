/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_pm_messages'.
 */
@GeneratedFromTable("phorum_pm_messages")
public class PhorumPmMessages extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumPmMessages.class);

    /**
     * Constructor.
     */
    public PhorumPmMessages() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the author
     */
    private String author;

    /**
     * the datestamp
     */
    private Integer datestamp;

    /**
     * the message
     */
    private String message;

    /**
     * the meta
     */
    private String meta;

    /**
     * the pmMessageId
     */
    private Integer pmMessageId;

    /**
     * the subject
     */
    private String subject;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the author.
     * @return the author
     */
    @GeneratedFromColumn("author")
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
     * Getter method for the datestamp.
     * @return the datestamp
     */
    @GeneratedFromColumn("datestamp")
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
     * Getter method for the message.
     * @return the message
     */
    @GeneratedFromColumn("message")
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for the message.
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter method for the meta.
     * @return the meta
     */
    @GeneratedFromColumn("meta")
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
     * Getter method for the pmMessageId.
     * @return the pmMessageId
     */
    @GeneratedFromColumn("pm_message_id")
    public Integer getPmMessageId() {
        return pmMessageId;
    }

    /**
     * Setter method for the pmMessageId.
     * @param pmMessageId the pmMessageId to set
     */
    public void setPmMessageId(Integer pmMessageId) {
        this.pmMessageId = pmMessageId;
    }

    /**
     * Getter method for the subject.
     * @return the subject
     */
    @GeneratedFromColumn("subject")
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
     * Getter method for the userId.
     * @return the userId
     */
    @GeneratedFromColumn("user_id")
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
        return "author = " + author + ", datestamp = " + datestamp + ", message = " + message + ", meta = " + meta + ", pmMessageId = " + pmMessageId + ", subject = " + subject + ", userId = " + userId;
    }

}

