/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.osm.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A map way.
 */
public class OsmWay extends OsmTaggable implements OsmRelationMember {

	/**
	 * the nodes
	 */
	private final List<OsmNode> nodes = new ArrayList<OsmNode>();

	/**
	 * the id
	 */
	private long id;

	/**
	 * Getter method for the nodes.
	 * @return the nodes
	 */
	public List<OsmNode> getNodes() {
		return nodes;
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

}
