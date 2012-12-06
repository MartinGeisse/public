/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_pm_folders'.
 */
@GeneratedFromTable("phorum_pm_folders")
public class PhorumPmFolders extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumPmFolders.class);

    /**
     * Constructor.
     */
    public PhorumPmFolders() {
        super(GENERATED_CLASS_META_DATA);
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
    @GeneratedFromColumn("foldername")
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
    @GeneratedFromColumn("pm_folder_id")
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
        return "{PhorumPmFolders. foldername = " + foldername + ", pmFolderId = " + pmFolderId + ", userId = " + userId + "}";
    }

}

