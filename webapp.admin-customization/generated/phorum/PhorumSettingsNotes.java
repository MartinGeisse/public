/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_settings_notes'.
 */
@GeneratedFromTable("phorum_settings_notes")
public class PhorumSettingsNotes extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumSettingsNotes.class);

    /**
     * Constructor.
     */
    public PhorumSettingsNotes() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the note
     */
    private String note;

    /**
     * the settingName
     */
    private String settingName;

    /**
     * Getter method for the note.
     * @return the note
     */
    @GeneratedFromColumn("note")
    public String getNote() {
        return note;
    }

    /**
     * Setter method for the note.
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Getter method for the settingName.
     * @return the settingName
     */
    @GeneratedFromColumn("setting_name")
    public String getSettingName() {
        return settingName;
    }

    /**
     * Setter method for the settingName.
     * @param settingName the settingName to set
     */
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PhorumSettingsNotes. note = " + note + ", settingName = " + settingName + "}";
    }

}

