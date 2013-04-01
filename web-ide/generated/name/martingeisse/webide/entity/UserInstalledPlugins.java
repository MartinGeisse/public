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
     * the pluginPublicId
     */
    private String pluginPublicId;

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
        return "{UserInstalledPlugins. id = " + id + ", pluginPublicId = " + pluginPublicId + ", userId = " + userId + "}";
    }

}

