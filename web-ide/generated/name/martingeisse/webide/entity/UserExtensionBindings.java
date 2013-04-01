/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'user_extension_bindings'.
 */
public class UserExtensionBindings implements Serializable {

    /**
     * Constructor.
     */
    public UserExtensionBindings() {
    }

    /**
     * the declaredExtensionId
     */
    private Long declaredExtensionId;

    /**
     * the declaredExtensionPointId
     */
    private Long declaredExtensionPointId;

    /**
     * the id
     */
    private Long id;

    /**
     * the userId
     */
    private Long userId;

    /**
     * Getter method for the declaredExtensionId.
     * @return the declaredExtensionId
     */
    public Long getDeclaredExtensionId() {
        return declaredExtensionId;
    }

    /**
     * Setter method for the declaredExtensionId.
     * @param declaredExtensionId the declaredExtensionId to set
     */
    public void setDeclaredExtensionId(Long declaredExtensionId) {
        this.declaredExtensionId = declaredExtensionId;
    }

    /**
     * Getter method for the declaredExtensionPointId.
     * @return the declaredExtensionPointId
     */
    public Long getDeclaredExtensionPointId() {
        return declaredExtensionPointId;
    }

    /**
     * Setter method for the declaredExtensionPointId.
     * @param declaredExtensionPointId the declaredExtensionPointId to set
     */
    public void setDeclaredExtensionPointId(Long declaredExtensionPointId) {
        this.declaredExtensionPointId = declaredExtensionPointId;
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
        return "{UserExtensionBindings. declaredExtensionId = " + declaredExtensionId + ", declaredExtensionPointId = " + declaredExtensionPointId + ", id = " + id + ", userId = " + userId + "}";
    }

}

