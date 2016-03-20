/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.osm.data;

import java.util.HashMap;

/**
 * A whole OSM "world".
 */
public final class OsmWorld {

	/**
	 * the nodes
	 */
	private final HashMap<Long, OsmNode> nodes = new HashMap<>();

	/**
	 * the nodes
	 */
	private final HashMap<Long, OsmWay> ways = new HashMap<>();

	/**
	 * the nodes
	 */
	private final HashMap<Long, OsmRelation> relations = new HashMap<>();

	/**
	 * Getter method for the nodes.
	 * @return the nodes
	 */
	public HashMap<Long, OsmNode> getNodes() {
		return nodes;
	}

	/**
	 * Getter method for the ways.
	 * @return the ways
	 */
	public HashMap<Long, OsmWay> getWays() {
		return ways;
	}

	/**
	 * Getter method for the relations.
	 * @return the relations
	 */
	public HashMap<Long, OsmRelation> getRelations() {
		return relations;
	}

}
