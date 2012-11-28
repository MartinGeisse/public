/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.util.ArrayList;
import com.mysema.query.support.Expressions;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Predicate;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.apidemo.Databases;
import com.mysema.commons.lang.CloseableIterator;
import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents rows from table 'phorum_settings_notes'.
 */
public class PhorumSettingsNotes implements Serializable {

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
        return "{PhorumSettingsNotes. note = " + note + ", settingName = " + settingName + "}";
    }

    /**
     * Returns the first instance with the specified settingName.
     * @param value the settingName
     * @return the instance, or null if none was found
     */
    public static PhorumSettingsNotes findBySettingName(String value) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettingsNotes table = QPhorumSettingsNotes.phorumSettingsNotes;
        query.from(table);
        query.where(table.settingName.eq(Expressions.constant(value)));
        return query.singleResult(table);
    }

    /**
     * Returns all instances with the specified settingName.
     * @param value the settingName
     * @return the instances (an empty list if none was found)
     */
    public static java.util.List<PhorumSettingsNotes> findAllBySettingName(String value) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettingsNotes table = QPhorumSettingsNotes.phorumSettingsNotes;
        query.from(table);
        query.where(table.settingName.eq(Expressions.constant(value)));
        return query.list(table);
    }

    /**
     * Returns the an instance for each of the specified settingName values that also satisfies the additional
     * conditions, mapped by settingName.
     * This method is sub-optimal if many instances exist for any of the specified values since it first fetches all those values.
     * Values for which no instance exist will be missing from the returned map.
     * @param values the settingName values
     * @param additionalConditions the additional conditions for returned instances
     * @return the instances (an empty map if none was found)
     */
    public static java.util.Map<String, PhorumSettingsNotes> mapBySettingName(java.util.Collection<String> values, Predicate... additionalConditions) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettingsNotes table = QPhorumSettingsNotes.phorumSettingsNotes;
        query.from(table);
        query.where(table.settingName.in(values));
        query.where(additionalConditions);
        CloseableIterator<PhorumSettingsNotes> it = query.iterate(table);
        java.util.Map<String, name.martingeisse.apidemo.phorum.PhorumSettingsNotes> result = new HashMap<String, name.martingeisse.apidemo.phorum.PhorumSettingsNotes>();
        while (it.hasNext()) {
        	PhorumSettingsNotes row = it.next();
        	result.put(row.getSettingName(), row);
        }
        it.close();
        return result;
    }

    /**
     * Returns all instances with any of the specified settingName values that also satisfy the additional
     * conditions, mapped by settingName.
     * @param values the settingName values
     * @param additionalConditions the additional conditions for returned instances
     * @return the instances (an empty map if none was found)
     */
    public static java.util.Map<String, java.util.List<PhorumSettingsNotes>> mapAllBySettingName(java.util.Collection<String> values, Predicate... additionalConditions) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettingsNotes table = QPhorumSettingsNotes.phorumSettingsNotes;
        query.from(table);
        query.where(table.settingName.in(values));
        query.where(additionalConditions);
        CloseableIterator<PhorumSettingsNotes> it = query.iterate(table);
        java.util.Map<String, java.util.List<name.martingeisse.apidemo.phorum.PhorumSettingsNotes>> result = new HashMap<String, java.util.List<name.martingeisse.apidemo.phorum.PhorumSettingsNotes>>();
        while (it.hasNext()) {
        	PhorumSettingsNotes row = it.next();
        	String value = row.getSettingName();
        	java.util.List<name.martingeisse.apidemo.phorum.PhorumSettingsNotes> list = result.get(value);
        	if (list == null) {
        		list = new ArrayList<name.martingeisse.apidemo.phorum.PhorumSettingsNotes>();
        		result.put(value, list);
        	}
        	list.add(row);
        }
        it.close();
        return result;
    }

}

