/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_settings_groups'.
 */
@GeneratedFromTable("phorum_settings_groups")
public class PhorumSettingsGroups extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumSettingsGroups.class);

    /**
     * Constructor.
     */
    public PhorumSettingsGroups() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the alias
     */
    private String alias;

    /**
     * the id
     */
    private Integer id;

    /**
     * the name
     */
    private String name;

    /**
     * Getter method for the alias.
     * @return the alias
     */
    @GeneratedFromColumn("alias")
    public String getAlias() {
        return alias;
    }

    /**
     * Setter method for the alias.
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Getter method for the id.
     * @return the id
     */
    @GeneratedFromColumn("id")
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter method for the name.
     * @return the name
     */
    @GeneratedFromColumn("name")
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PhorumSettingsGroups. alias = " + alias + ", id = " + id + ", name = " + name + "}";
    }

}

