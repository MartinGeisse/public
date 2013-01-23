/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'declared_extension_points'.
 */
public class DeclaredExtensionPoints implements Serializable {

    /**
     * Constructor.
     */
    public DeclaredExtensionPoints() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the name
     */
    private String name;

    /**
     * the onChangeClearedSection
     */
    private Integer onChangeClearedSection;

    /**
     * the pluginBundleId
     */
    private Long pluginBundleId;

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
     * Getter method for the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for the onChangeClearedSection.
     * @return the onChangeClearedSection
     */
    public Integer getOnChangeClearedSection() {
        return onChangeClearedSection;
    }

    /**
     * Setter method for the onChangeClearedSection.
     * @param onChangeClearedSection the onChangeClearedSection to set
     */
    public void setOnChangeClearedSection(Integer onChangeClearedSection) {
        this.onChangeClearedSection = onChangeClearedSection;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{DeclaredExtensionPoints. id = " + id + ", name = " + name + ", onChangeClearedSection = " + onChangeClearedSection + ", pluginBundleId = " + pluginBundleId + "}";
    }

}

