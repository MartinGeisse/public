/**
 * This file was generated from the database schema.
 */
package phorum;


/**
 * This class represents rows from table 'phorum_settings_notes'.
 */
public class PhorumSettingsNotes {

    /**
     * Constructor.
     */
    public PhorumSettingsNotes() {
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
        return "note = " + note + ", settingName = " + settingName;
    }

}

