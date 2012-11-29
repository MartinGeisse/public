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
 * This class represents rows from table 'phorum_settings'.
 */
public class PhorumSettings implements Serializable {

    /**
     * Constructor.
     */
    public PhorumSettings() {
    }

    /**
     * the data
     */
    private String data;

    /**
     * the groupId
     */
    private Integer groupId;

    /**
     * the name
     */
    private String name;

    /**
     * the type
     */
    private String type;

    /**
     * Getter method for the data.
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Setter method for the data.
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Getter method for the groupId.
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Setter method for the groupId.
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
     * Getter method for the type.
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for the type.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PhorumSettings. data = " + data + ", groupId = " + groupId + ", name = " + name + ", type = " + type + "}";
    }

    /**
     * Returns the first instance with the specified groupId.
     * @param value the groupId
     * @return the instance, or null if none was found
     */
    public static PhorumSettings findByGroupId(Integer value) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettings table = QPhorumSettings.phorumSettings;
        query.from(table);
        query.where(table.groupId.eq(Expressions.constant(value)));
        return query.singleResult(table);
    }

    /**
     * Returns all instances with the specified groupId.
     * @param value the groupId
     * @return the instances (an empty list if none was found)
     */
    public static java.util.List<PhorumSettings> findAllByGroupId(Integer value) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettings table = QPhorumSettings.phorumSettings;
        query.from(table);
        query.where(table.groupId.eq(Expressions.constant(value)));
        return query.list(table);
    }

    /**
     * Returns the an instance for each of the specified groupId values that also satisfies the additional
     * conditions, mapped by groupId.
     * This method is sub-optimal if many instances exist for any of the specified values since it first fetches all those values.
     * Values for which no instance exist will be missing from the returned map.
     * @param values the groupId values
     * @param additionalConditions the additional conditions for returned instances
     * @return the instances (an empty map if none was found)
     */
    public static java.util.Map<Integer, PhorumSettings> mapByGroupId(java.util.Collection<Integer> values, Predicate... additionalConditions) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettings table = QPhorumSettings.phorumSettings;
        query.from(table);
        query.where(table.groupId.in(values));
        query.where(additionalConditions);
        CloseableIterator<PhorumSettings> it = query.iterate(table);
        java.util.Map<Integer, name.martingeisse.apidemo.phorum.PhorumSettings> result = new HashMap<Integer, name.martingeisse.apidemo.phorum.PhorumSettings>();
        while (it.hasNext()) {
        	PhorumSettings row = it.next();
        	result.put(row.getGroupId(), row);
        }
        it.close();
        return result;
    }

    /**
     * Returns all instances with any of the specified groupId values that also satisfy the additional
     * conditions, mapped by groupId.
     * @param values the groupId values
     * @param additionalConditions the additional conditions for returned instances
     * @return the instances (an empty map if none was found)
     */
    public static java.util.Map<Integer, java.util.List<PhorumSettings>> mapAllByGroupId(java.util.Collection<Integer> values, Predicate... additionalConditions) {
        SQLQuery query = EntityConnectionManager.getConnection(Databases.phorumDatabase).createQuery();
        QPhorumSettings table = QPhorumSettings.phorumSettings;
        query.from(table);
        query.where(table.groupId.in(values));
        query.where(additionalConditions);
        CloseableIterator<PhorumSettings> it = query.iterate(table);
        java.util.Map<Integer, java.util.List<name.martingeisse.apidemo.phorum.PhorumSettings>> result = new HashMap<Integer, java.util.List<name.martingeisse.apidemo.phorum.PhorumSettings>>();
        while (it.hasNext()) {
        	PhorumSettings row = it.next();
        	Integer value = row.getGroupId();
        	java.util.List<name.martingeisse.apidemo.phorum.PhorumSettings> list = result.get(value);
        	if (list == null) {
        		list = new ArrayList<name.martingeisse.apidemo.phorum.PhorumSettings>();
        		result.put(value, list);
        	}
        	list.add(row);
        }
        it.close();
        return result;
    }

}

