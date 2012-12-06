/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_pm_xref'.
 */
@GeneratedFromTable("phorum_pm_xref")
public class PhorumPmXref extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumPmXref.class);

    /**
     * Constructor.
     */
    public PhorumPmXref() {
        super(GENERATED_CLASS_META_DATA);
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
     * Getter method for the pmXrefId.
     * @return the pmXrefId
     */
    @GeneratedFromColumn("pm_xref_id")
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
    @GeneratedFromColumn("read_flag")
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
    @GeneratedFromColumn("reply_flag")
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
    @GeneratedFromColumn("special_folder")
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
        return "{PhorumPmXref. pmFolderId = " + pmFolderId + ", pmMessageId = " + pmMessageId + ", pmXrefId = " + pmXrefId + ", readFlag = " + readFlag + ", replyFlag = " + replyFlag + ", specialFolder = " + specialFolder + ", userId = " + userId + "}";
    }

}

