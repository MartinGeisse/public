/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_builders'.
 */
public class WorkspaceBuilders implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceBuilders() {
    }

    /**
     * the builderClass
     */
    private String builderClass;

    /**
     * the builderName
     */
    private String builderName;

    /**
     * the id
     */
    private Long id;

    /**
     * the pluginBundleId
     */
    private Long pluginBundleId;

    /**
     * the stagingPath
     */
    private String stagingPath;

    /**
     * the workspaceId
     */
    private Long workspaceId;

    /**
     * Getter method for the builderClass.
     * @return the builderClass
     */
    public String getBuilderClass() {
        return builderClass;
    }

    /**
     * Setter method for the builderClass.
     * @param builderClass the builderClass to set
     */
    public void setBuilderClass(String builderClass) {
        this.builderClass = builderClass;
    }

    /**
     * Getter method for the builderName.
     * @return the builderName
     */
    public String getBuilderName() {
        return builderName;
    }

    /**
     * Setter method for the builderName.
     * @param builderName the builderName to set
     */
    public void setBuilderName(String builderName) {
        this.builderName = builderName;
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
     * Getter method for the stagingPath.
     * @return the stagingPath
     */
    public String getStagingPath() {
        return stagingPath;
    }

    /**
     * Setter method for the stagingPath.
     * @param stagingPath the stagingPath to set
     */
    public void setStagingPath(String stagingPath) {
        this.stagingPath = stagingPath;
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
        return "{WorkspaceBuilders. builderClass = " + builderClass + ", builderName = " + builderName + ", id = " + id + ", pluginBundleId = " + pluginBundleId + ", stagingPath = " + stagingPath + ", workspaceId = " + workspaceId + "}";
    }

}

