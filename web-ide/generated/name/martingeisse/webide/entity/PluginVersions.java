/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'plugin_versions'.
 */
public class PluginVersions implements Serializable {

    /**
     * Constructor.
     */
    public PluginVersions() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the isActive
     */
    private Boolean isActive;

    /**
     * the isUnpacked
     */
    private Boolean isUnpacked;

    /**
     * the pluginPublicId
     */
    private String pluginPublicId;

    /**
     * the stagingWorkspaceId
     */
    private Long stagingWorkspaceId;

    /**
     * Getter method for the id.
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter method for the isActive.
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * Setter method for the isActive.
     * @param isActive the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Getter method for the isUnpacked.
     * @return the isUnpacked
     */
    public Boolean getIsUnpacked() {
        return isUnpacked;
    }

    /**
     * Setter method for the isUnpacked.
     * @param isUnpacked the isUnpacked to set
     */
    public void setIsUnpacked(Boolean isUnpacked) {
        this.isUnpacked = isUnpacked;
    }

    /**
     * Getter method for the pluginPublicId.
     * @return the pluginPublicId
     */
    public String getPluginPublicId() {
        return pluginPublicId;
    }

    /**
     * Setter method for the pluginPublicId.
     * @param pluginPublicId the pluginPublicId to set
     */
    public void setPluginPublicId(String pluginPublicId) {
        this.pluginPublicId = pluginPublicId;
    }

    /**
     * Getter method for the stagingWorkspaceId.
     * @return the stagingWorkspaceId
     */
    public Long getStagingWorkspaceId() {
        return stagingWorkspaceId;
    }

    /**
     * Setter method for the stagingWorkspaceId.
     * @param stagingWorkspaceId the stagingWorkspaceId to set
     */
    public void setStagingWorkspaceId(Long stagingWorkspaceId) {
        this.stagingWorkspaceId = stagingWorkspaceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PluginVersions. id = " + id + ", isActive = " + isActive + ", isUnpacked = " + isUnpacked + ", pluginPublicId = " + pluginPublicId + ", stagingWorkspaceId = " + stagingWorkspaceId + "}";
    }

}

