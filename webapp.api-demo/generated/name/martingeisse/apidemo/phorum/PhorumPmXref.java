/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.util.ArrayList;
import com.mysema.query.support.Expressions;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Predicate;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.apidemo.Databases;
import com.mysema.commons.lang.CloseableIterator;
import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents rows from table 'phorum_pm_xref'.
 */
public class PhorumPmXref implements Serializable {

    /**
     * Constructor.
     */
    public PhorumPmXref() {
    }

    /**
     * the pmFolderId
     */
    private Integer pmFolderId;

    /**
     * the pmMessageId
     */
    private Integer pmMessageId;

    /**
     * the pmXrefId
     */
    private Integer pmXrefId;

    /**
     * the readFlag
     */
    private Boolean readFlag;

    /**
     * the replyFlag
     */
    private Boolean replyFlag;

    /**
     * the specialFolder
     */
    private String specialFolder;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the pmFolderId.
     * @return the pmFolderId
     */
    public Integer getPmFolderId() {
        return pmFolderId;
    }

    /**
     * Setter method for the pmFolderId.
     * @param pmFolderId the pmFolderId to set
     */
    public void setPmFolderId(Integer pmFolderId) {
        this.pmFolderId = pmFolderId;
    }

    /**
     * Getter method for the pmMessageId.
     * @return the pmMessageId
     */
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
     * Getter method for the pmXrefId.
     * @return the pmXrefId
     */
    public Integer getPmXrefId() {
        return pmXrefId;
    }

    /**
     * Setter method for the pmXrefId.
     * @param pmXrefId the pmXrefId to set
     */
    public void setPmXrefId(Integer pmXrefId) {
        this.pmXrefId = pmXrefId;
    }

    /**
     * Getter method for the readFlag.
     * @return the readFlag
     */
    public Boolean getReadFlag() {
        return readFlag;
    }

    /**
     * Setter method for the readFlag.
     * @param readFlag the readFlag to set
     */
    public void setReadFlag(Boolean readFlag) {
        this.readFlag = readFlag;
    }

    /**
     * Getter method for the replyFlag.
     * @return the replyFlag
     */
    public Boolean getReplyFlag() {
        return replyFlag;
    }

    /**
     * Setter method for the replyFlag.
     * @param replyFlag the replyFlag to set
     */
    public void setReplyFlag(Boolean replyFlag) {
        this.replyFlag = replyFlag;
    }

    /**
     * Getter method for the specialFolder.
     * @return the specialFolder
     */
    public String getSpecialFolder() {
        return specialFolder;
    }

    /**
     * Setter method for the specialFolder.
     * @param specialFolder the specialFolder to set
     */
    public void setSpecialFolder(String specialFolder) {
        this.specialFolder = specialFolder;
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
        return "{PhorumPmXref. pmFolderId = " + pmFolderId + ", pmMessageId = " + pmMessageId + ", pmXrefId = " + pmXrefId + ", readFlag = " + readFlag + ", replyFlag = " + replyFlag + ", specialFolder = " + specialFolder + ", userId = " + userId + "}";
    }

}

