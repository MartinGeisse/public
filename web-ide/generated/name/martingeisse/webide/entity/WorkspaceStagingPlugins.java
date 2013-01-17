/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_staging_plugins'.
 */
public class WorkspaceStagingPlugins implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceStagingPlugins() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the pluginId
     */
    private Long pluginId;

    /**
     * the workspaceResourceId
     */
    private Long workspaceResourceId;

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
     * Getter method for the pluginId.
     * @return the pluginId
     */
    public Long getPluginId() {
        return pluginId;
    }

    /**
     * Setter method for the pluginId.
     * @param pluginId the pluginId to set
     */
    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * Getter method for the workspaceResourceId.
     * @return the workspaceResourceId
     */
    public Long getWorkspaceResourceId() {
        return workspaceResourceId;
    }

    /**
     * Setter method for the workspaceResourceId.
     * @param workspaceResourceId the workspaceResourceId to set
     */
    public void setWorkspaceResourceId(Long workspaceResourceId) {
        this.workspaceResourceId = workspaceResourceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{WorkspaceStagingPlugins. id = " + id + ", pluginId = " + pluginId + ", workspaceResourceId = " + workspaceResourceId + "}";
    }

}

