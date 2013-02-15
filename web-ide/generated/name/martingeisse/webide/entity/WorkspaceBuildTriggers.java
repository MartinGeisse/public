/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_build_triggers'.
 */
public class WorkspaceBuildTriggers implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceBuildTriggers() {
    }

    /**
     * the buildscriptPath
     */
    private String buildscriptPath;

    /**
     * the id
     */
    private Long id;

    /**
     * the pathPattern
     */
    private String pathPattern;

    /**
     * the triggerBasePath
     */
    private String triggerBasePath;

    /**
     * the workspaceBuilderId
     */
    private Long workspaceBuilderId;

    /**
     * the workspaceId
     */
    private Long workspaceId;

    /**
     * Getter method for the buildscriptPath.
     * @return the buildscriptPath
     */
    public String getBuildscriptPath() {
        return buildscriptPath;
    }

    /**
     * Setter method for the buildscriptPath.
     * @param buildscriptPath the buildscriptPath to set
     */
    public void setBuildscriptPath(String buildscriptPath) {
        this.buildscriptPath = buildscriptPath;
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
     * Getter method for the pathPattern.
     * @return the pathPattern
     */
    public String getPathPattern() {
        return pathPattern;
    }

    /**
     * Setter method for the pathPattern.
     * @param pathPattern the pathPattern to set
     */
    public void setPathPattern(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    /**
     * Getter method for the triggerBasePath.
     * @return the triggerBasePath
     */
    public String getTriggerBasePath() {
        return triggerBasePath;
    }

    /**
     * Setter method for the triggerBasePath.
     * @param triggerBasePath the triggerBasePath to set
     */
    public void setTriggerBasePath(String triggerBasePath) {
        this.triggerBasePath = triggerBasePath;
    }

    /**
     * Getter method for the workspaceBuilderId.
     * @return the workspaceBuilderId
     */
    public Long getWorkspaceBuilderId() {
        return workspaceBuilderId;
    }

    /**
     * Setter method for the workspaceBuilderId.
     * @param workspaceBuilderId the workspaceBuilderId to set
     */
    public void setWorkspaceBuilderId(Long workspaceBuilderId) {
        this.workspaceBuilderId = workspaceBuilderId;
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
        return "{WorkspaceBuildTriggers. buildscriptPath = " + buildscriptPath + ", id = " + id + ", pathPattern = " + pathPattern + ", triggerBasePath = " + triggerBasePath + ", workspaceBuilderId = " + workspaceBuilderId + ", workspaceId = " + workspaceId + "}";
    }

}

