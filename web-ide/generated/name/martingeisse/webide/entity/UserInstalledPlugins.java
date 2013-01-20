/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'user_installed_plugins'.
 */
public class UserInstalledPlugins implements Serializable {

    /**
     * Constructor.
     */
    public UserInstalledPlugins() {
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
     * the userId
     */
    private Long userId;

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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{UserInstalledPlugins. id = " + id + ", pluginId = " + pluginId + ", userId = " + userId + "}";
    }

}

