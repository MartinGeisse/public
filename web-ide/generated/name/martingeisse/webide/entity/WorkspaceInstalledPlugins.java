/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_installed_plugins'.
 */
public class WorkspaceInstalledPlugins implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceInstalledPlugins() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the pluginPublicId
     */
    private String pluginPublicId;

    /**
     * the workspaceId
     */
    private Long workspaceId;

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
        return "{WorkspaceInstalledPlugins. id = " + id + ", pluginPublicId = " + pluginPublicId + ", workspaceId = " + workspaceId + "}";
    }

}

