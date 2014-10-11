/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.osm.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A map relation.
 */
public class OsmRelation extends OsmTaggable implements OsmRelationMember {

	/**
	 * the entries
	 */
	private final List<Entry> entries = new ArrayList<OsmRelation.Entry>();

	/**
	 * the id
	 */
	private long id;

	/**
	 * Getter method for the entries.
	 * @return the entries
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter method for the id.
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * A relation entry.
	 */
	public static final class Entry {

		/**
		 * the member
		 */
		private OsmRelationMember member;

		/**
		 * the role
		 */
		private String role;

		/**
		 * Getter method for the member.
		 * @return the member
		 */
		public OsmRelationMember getMember() {
			return member;
		}

		/**
		 * Setter method for the member.
		 * @param member the member to set
		 */
		public void setMember(OsmRelationMember member) {
			this.member = member;
		}

		/**
		 * Getter method for the role.
		 * @return the role
		 */
		public String getRole() {
			return role;
		}

		/**
		 * Setter method for the role.
		 * @param role the role to set
		 */
		public void setRole(String role) {
			this.role = role;
		}

	}
}
