/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.io.Serializable;

/**
 * This class represents rows from table 'phorum_pm_folders'.
 */
public class PhorumPmFolders implements Serializable {

    /**
     * Constructor.
     */
    public PhorumPmFolders() {
    }

    /**
     * the foldername
     */
    private String foldername;

    /**
     * the pmFolderId
     */
    private Integer pmFolderId;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the foldername.
     * @return the foldername
     */
    public String getFoldername() {
        return foldername;
    }

    /**
     * Setter method for the foldername.
     * @param foldername the foldername to set
     */
    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

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
        return "{PhorumPmFolders. foldername = " + foldername + ", pmFolderId = " + pmFolderId + ", userId = " + userId + "}";
    }

}

