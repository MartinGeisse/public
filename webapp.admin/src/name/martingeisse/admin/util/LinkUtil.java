/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * This class contains utility methods to deal with HTML links.
 */
public class LinkUtil {

	/**
	 * Prevent instantiation.
	 */
	private LinkUtil() {
	}
	
	/**
	 * Creates a link that is disabled and has "#" as its href.
	 * @param wicketId the wicket id of the link to create
	 * @return the link
	 */
	public static AbstractLink createDisabledLink(String wicketId) {
		ExternalLink link = new ExternalLink(wicketId, "#");
		link.setEnabled(false);
		return link;
	}
	
	/**
	 * Creates a link to the single-instance entity presentation page of the specified entity instance. 
	 * @param wicketId the wicket id of the link to create
	 * @param entity the linked entity class
	 * @param entityId the id of the linked entity instance
	 * @param subpathSegments the subpath segments to walk from the entity instance navigation root
	 * to reach the node to link. The specified node must exist, otherwise this method throws an
	 * {@link IllegalArgumentException}.
	 * @return the link
	 */
	public static AbstractLink createSingleEntityLink(String wicketId, EntityDescriptor entity, Object entityId, String... subpathSegments) {
		NavigationNode node = entity.getInstanceNavigationRootNode();
		for (String segment : subpathSegments) {
			NavigationNode child = node.findChildById(segment);
			if (child == null) {
				throw new IllegalArgumentException("subpath segment '" + segment + "' not found in node " + node.getPath());
			}
			node = child;
		}
		BookmarkablePageLink<?> link = (BookmarkablePageLink<?>)node.createLink(wicketId);
		link.getPageParameters().add("id", entityId);
		return link;
	}
	
	/**
	 * Creates a link to the single-instance entity presentation page of the specified entity instance. 
	 * @param wicketId the wicket id of the link to create
	 * @param entityName the name of the linked entity class
	 * @param entityId the id of the linked entity instance
	 * @param subpathSegments the subpath segments to walk from the entity instance navigation root
	 * to reach the node to link. The specified node must exist, otherwise this method throws an
	 * {@link IllegalArgumentException}.
	 * @return the link
	 */
	public static AbstractLink createSingleEntityLink(String wicketId, String entityName, Object entityId, String... subpathSegments) {
		return createSingleEntityLink(wicketId, ApplicationSchema.instance.findEntity(entityName), entityId, subpathSegments);
	}
	
}
