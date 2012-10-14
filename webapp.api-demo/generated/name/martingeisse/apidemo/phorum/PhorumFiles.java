/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.io.Serializable;

/**
 * This class represents rows from table 'phorum_files'.
 */
public class PhorumFiles implements Serializable {

    /**
     * Constructor.
     */
    public PhorumFiles() {
    }

    /**
     * the addDatetime
     */
    private Integer addDatetime;

    /**
     * the fileData
     */
    private String fileData;

    /**
     * the fileId
     */
    private Integer fileId;

    /**
     * the filename
     */
    private String filename;

    /**
     * the filesize
     */
    private Integer filesize;

    /**
     * the link
     */
    private String link;

    /**
     * the messageId
     */
    private Integer messageId;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the addDatetime.
     * @return the addDatetime
     */
    public Integer getAddDatetime() {
        return addDatetime;
    }

    /**
     * Setter method for the addDatetime.
     * @param addDatetime the addDatetime to set
     */
    public void setAddDatetime(Integer addDatetime) {
        this.addDatetime = addDatetime;
    }

    /**
     * Getter method for the fileData.
     * @return the fileData
     */
    public String getFileData() {
        return fileData;
    }

    /**
     * Setter method for the fileData.
     * @param fileData the fileData to set
     */
    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    /**
     * Getter method for the fileId.
     * @return the fileId
     */
    public Integer getFileId() {
        return fileId;
    }

    /**
     * Setter method for the fileId.
     * @param fileId the fileId to set
     */
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    /**
     * Getter method for the filename.
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setter method for the filename.
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Getter method for the filesize.
     * @return the filesize
     */
    public Integer getFilesize() {
        return filesize;
    }

    /**
     * Setter method for the filesize.
     * @param filesize the filesize to set
     */
    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    /**
     * Getter method for the link.
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Setter method for the link.
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
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
        return "addDatetime = " + addDatetime + ", fileData = " + fileData + ", fileId = " + fileId + ", filename = " + filename + ", filesize = " + filesize + ", link = " + link + ", messageId = " + messageId + ", userId = " + userId;
    }

}

