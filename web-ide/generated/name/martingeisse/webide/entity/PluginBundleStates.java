/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'plugin_bundle_states'.
 */
public class PluginBundleStates implements Serializable {

    /**
     * Constructor.
     */
    public PluginBundleStates() {
    }

    /**
     * the data
     */
    private byte[] data;

    /**
     * the id
     */
    private Long id;

    /**
     * the pluginBundleId
     */
    private Long pluginBundleId;

    /**
     * the section
     */
    private Integer section;

    /**
     * the userId
     */
    private Long userId;

    /**
     * the workspaceId
     */
    private Long workspaceId;

    /**
     * Getter method for the data.
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Setter method for the data.
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

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
     * Getter method for the pluginBundleId.
     * @return the pluginBundleId
     */
    public Long getPluginBundleId() {
        return pluginBundleId;
    }

    /**
     * Setter method for the pluginBundleId.
     * @param pluginBundleId the pluginBundleId to set
     */
    public void setPluginBundleId(Long pluginBundleId) {
        this.pluginBundleId = pluginBundleId;
    }

    /**
     * Getter method for the section.
     * @return the section
     */
    public Integer getSection() {
        return section;
    }

    /**
     * Setter method for the section.
     * @param section the section to set
     */
    public void setSection(Integer section) {
        this.section = section;
    }

    /**
     * Getter method for the userId.
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Setter method for the userId.
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Getter method for the workspaceId.
     * @return the workspaceId
     */
    public Long getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Setter method for the workspaceId.
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PluginBundleStates. data = " + data + ", id = " + id + ", pluginBundleId = " + pluginBundleId + ", section = " + section + ", userId = " + userId + ", workspaceId = " + workspaceId + "}";
    }

}

